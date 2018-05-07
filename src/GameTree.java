
/**
 * Name: Melissa McMahon
 * A model for the game of 20 questions
 * 
 * @author Rick Mercer
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class GameTree {
	// Need an inner TreeNode class here . . .
	private TreeNode root;
	private TreeNode current;
	private Scanner inFile;
	private String currentFileName;

	private class TreeNode {
		private String data;
		private TreeNode left;
		private TreeNode right;

		public TreeNode(String theData) {
			data = theData;
			left = null;
			right = null;
		}

		public TreeNode(TreeNode lefTree, String theData, TreeNode righTree) {
			left = lefTree;
			data = theData;
			right = righTree;
		}
	} // end class TreeNode

	public static void main(String[] args) {
		GameTree aGame = new GameTree("t2UofATEST.txt");
		// Output from toString()
		// - - rattlesnake
		// - Is it a mammal?
		// - - tiger
		// Has feathers?
		// - - owl
		// - Barnyard?
		// - - chicken
		System.out.print(aGame.toString());
	}

	public GameTree(String fileName) {
		currentFileName = fileName;
		try {
			inFile = new Scanner(new File(currentFileName));
		} catch (FileNotFoundException e) {
			// This block would execute if currentFileNane is not found.
			// We will not have tests to construct GameTree objects with
			// non-existent files.
		}
		root = build(inFile);
		inFile.close();
		current = root;
	}

	private TreeNode build(Scanner scanner) {
		if (!scanner.hasNext())
			return null;

		String token = scanner.nextLine();
		if (token.charAt(token.length() - 1) == '?') {
			TreeNode leftTree = build(scanner);
			TreeNode rightTree = build(scanner);
			return new TreeNode(leftTree, token, rightTree);
		} else {
			return new TreeNode(token);

		}

	}

	/*
	 * Add a new question and answer to the currentNode. If the current node has
	 * the answer chicken, theGame.add("Does it swim?", "goose"); should change
	 * that node like this:
	 */
	// -----------Feathers?-----------------Feathers?------
	// -------------/----\------------------/-------\------
	// ------- chicken horse-----Does it swim?-----horse--
	// -----------------------------/------\---------------
	// --------------------------goose--chicken-----------
	/**
	 * @param newQuestion
	 *            The question to add where the old answer was.
	 * @param newAnswer
	 *            The new Yes answer for the new question.
	 */
	public void add(String newQuestion, String newAnswer) {
		String oldData = current.data;
		current.data = newQuestion;
		TreeNode temp = new TreeNode(null, newAnswer, null);
		TreeNode temp1 = new TreeNode(null, oldData, null);
		current.right = temp1;
		current.left =temp;
	}

	/**
	 * True if getCurrent() returns an answer rather than a question.
	 * 
	 * @return False if the current node is an internal node rather than an
	 *         answer at a leaf.
	 */
	public boolean foundAnswer() {
		if ((getCurrent().charAt(getCurrent().length() - 1) == '?'))
			return false;
		else
			return true;
	}

	/**
	 * Return the data for the current node, which could be a question or an
	 * answer.
	 * 
	 * @return The current question or answer.
	 */
	public String getCurrent() {
		return current.data;
	}

	/**
	 * Ask the game to update the current node by going left for Choice.yes or
	 * right for Choice.no Example code: theGame.playerSelected(Choice.Yes);
	 * 
	 * @param yesOrNo
	 */
	public void playerSelected(Choice yesOrNo) {
		if (yesOrNo == Choice.Yes)
			current = current.left;
		else
			current = current.right;
	}

	/**
	 * Begin a game at the root of the tree. getCurrent should return the
	 * question at the root of this GameTree.
	 */
	public void reStart() {
		current = root;
	}

	/**
	 * Return a textual version of this object
	 */
	@Override
	public String toString() {
		int count = 0;
		return toStringHelper(root, 0);
		
	}
	private String toStringHelper(TreeNode current, int count) {
		if (current != null) {
			String dashes = ""; 
			for (int i = 0; i < count; i++) {
				dashes += "-";
			}

			return (toStringHelper(current.right, count + 1) + dashes + current.data + "\n"
					+ toStringHelper(current.left, count + 1));
		}
		else
			return "";
	}

	/**
	 * Overwrite the old file for this gameTree with the current state that may
	 * have new questions added since the game started.
	 * 
	 */
	public void saveGame() {
		String outputFileName = currentFileName;
		FileWriter charToBytesWriter = null;
		try {
			charToBytesWriter = new FileWriter(outputFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		PrintWriter diskFile = new PrintWriter(charToBytesWriter);
		saveGameHelper(root, diskFile);
		diskFile.close();
	}

	public void saveGameHelper(TreeNode current, PrintWriter diskFile) {
		if (current != null) {
			diskFile.println(current.data);
			saveGameHelper(current.left, diskFile);
			saveGameHelper(current.right, diskFile);
		}

	}

}
