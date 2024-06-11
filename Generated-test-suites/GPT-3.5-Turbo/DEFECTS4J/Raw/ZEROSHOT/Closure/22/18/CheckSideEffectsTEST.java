import static org.junit.Assert.*;
import org.junit.Test;

public class CheckSideEffectsTest {

	@Test
	public void testVisit_NoopStatement_ThrowsError() {
		CheckSideEffects cse = new CheckSideEffects(null, null, false);
		NodeTraversal t = new NodeTraversal(null, null, null);
		Node n = new Node(Token.EMPTY);
		Node parent = new Node(Token.BLOCK);
		
		try {
			cse.visit(t, n, parent);
			fail("Expected an UnsupportedOperationException to be thrown");
		} catch (UnsupportedOperationException e) {
			assertEquals("This code lacks side-effects. Is there a bug?", e.getMessage());
		}
	}
	
	@Test
	public void testVisit_StringNode_ThrowsError() {
		CheckSideEffects cse = new CheckSideEffects(null, null, false);
		NodeTraversal t = new NodeTraversal(null, null, null);
		Node n = new Node(Token.STRING);
		Node parent = new Node(Token.EXPR_RESULT);
		
		try {
			cse.visit(t, n, parent);
			fail("Expected an UnsupportedOperationException to be thrown");
		} catch (UnsupportedOperationException e) {
			assertEquals("Is there a missing '+' on the previous line?", e.getMessage());
		}
	}
	
	@Test
	public void testVisit_OperatorNode_ThrowsError() {
		CheckSideEffects cse = new CheckSideEffects(null, null, false);
		NodeTraversal t = new NodeTraversal(null, null, null);
		Node n = new Node(Token.ADD);
		Node parent = new Node(Token.EXPR_RESULT);
		
		try {
			cse.visit(t, n, parent);
			fail("Expected an UnsupportedOperationException to be thrown");
		} catch (UnsupportedOperationException e) {
			assertEquals("The result of the 'add' operator is not being used.", e.getMessage());
		}
	}
}