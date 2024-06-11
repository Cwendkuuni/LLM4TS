import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

	@Test
	public void testIsForIn() {
		// Test typical use case
		Node n1 = new Node(Token.FOR);
		Node n2 = new Node(Token.FOR);
		n2.addChildToBack(new Node(Token.EXPR_RESULT));
		n2.addChildToBack(new Node(Token.ASSIGN));
		n2.addChildToBack(new Node(Token.STRING));
		assertTrue(NodeUtil.isForIn(n1));
		
		// Test edge case: node is not a FOR loop
		assertFalse(NodeUtil.isForIn(n2));
		
		// Test error scenario: node has incorrect child count
		Node n3 = new Node(Token.FOR);
		n3.addChildToBack(new Node(Token.EXPR_RESULT));
		n3.addChildToBack(new Node(Token.ASSIGN));
		assertTrue(NodeUtil.isForIn(n3));
	}

	@Test
	public void testIsStatement() {
		// Test typical use case
		Node n1 = new Node(Token.FUNCTION);
		Node parent1 = new Node(Token.SCRIPT);
		parent1.addChildToFront(n1);
		n1.setParent(parent1);
		assertTrue(NodeUtil.isStatement(n1));
		
		// Test edge case: node is not used as a statement
		Node n2 = new Node(Token.BLOCK);
		Node parent2 = new Node(Token.SCRIPT);
		parent2.addChildToFront(n2);
		n2.setParent(parent2);
		assertFalse(NodeUtil.isStatement(n2));
		
		// Test error scenario: node has no parent
		Node n3 = new Node(Token.STRING);
		assertFalse(NodeUtil.isStatement(n3));
	}

	@Test
	public void testIsFunctionDeclaration() {
		// Test typical use case
		Node n1 = new Node(Token.FUNCTION);
		Node parent1 = new Node(Token.SCRIPT);
		parent1.addChildToFront(n1);
		n1.setParent(parent1);
		assertTrue(NodeUtil.isFunctionDeclaration(n1));
		
		// Test edge case: node is not a function declaration
		Node n2 = new Node(Token.STRING);
		Node parent2 = new Node(Token.SCRIPT);
		parent2.addChildToFront(n2);
		n2.setParent(parent2);
		assertFalse(NodeUtil.isFunctionDeclaration(n2));
		
		// Test error scenario: node has no parent
		Node n3 = new Node(Token.BLOCK);
		assertFalse(NodeUtil.isFunctionDeclaration(n3));
	}

	@Test
	public void testIsHoistedFunctionDeclaration() {
		// Test typical use case
		Node n1 = new Node(Token.FUNCTION);
		Node parent1 = new Node(Token.SCRIPT);
		parent1.addChildToFront(n1);
		n1.setParent(parent1);
		assertTrue(NodeUtil.isHoistedFunctionDeclaration(n1));
		
		// Test edge case: node is not a hoisted function declaration
		Node n2 = new Node(Token.FUNCTION);
		Node parent2 = new Node(Token.BLOCK);
		Node grandparent2 = new Node(Token.SCRIPT);
		grandparent2.addChildToFront(parent2);
		parent2.addChildToFront(n2);
		n2.setParent(parent2);
		parent2.setParent(grandparent2);
		assertFalse(NodeUtil.isHoistedFunctionDeclaration(n2));
		
		// Test error scenario: node has no parent
		Node n3 = new Node(Token.STRING);
		assertFalse(NodeUtil.isHoistedFunctionDeclaration(n3));
	}

}