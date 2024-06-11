
import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        // Test case for a valid FOR-IN loop
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.FOR_IN));
        assertTrue(NodeUtil.isForIn(node));

        // Test case for a non FOR-IN loop
        node = new Node(Token.FOR);
        assertFalse(NodeUtil.isForIn(node));

        // Test case for a FOR-IN loop with incorrect number of children
        node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.FOR_IN));
        node.addChildToBack(new Node(Token.STRING));
        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        // Test case for a node used as a statement
        Node node = new Node(Token.SCRIPT);
        node.addChildToFront(new Node(Token.VAR));
        assertTrue(NodeUtil.isStatement(node));

        // Test case for a node not used as a statement
        node = new Node(Token.EXPR_RESULT);
        assertFalse(NodeUtil.isStatement(node));

        // Test case for a node with no parent
        node = new Node(Token.CALL);
        assertThrows(Preconditions.checkState(), () -> {
            NodeUtil.isStatement(node);
        });
    }

    @Test
    public void testIsFunctionDeclaration() {
        // Test case for a valid function declaration
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(node);
        assertTrue(NodeUtil.isFunctionDeclaration(node));

        // Test case for a function expression
        node = new Node(Token.FUNCTION);
        parent = new Node(Token.SCRIPT);
        parent.addChildToBack(new Node(Token.EXPR_RESULT));
        parent.getLastChild().addChildToFront(node);
        assertFalse(NodeUtil.isFunctionDeclaration(node));

        // Test case for a function declaration as part of an expression
        node = new Node(Token.FUNCTION);
        parent = new Node(Token.EXPR_RESULT);
        parent.addChildToFront(node);
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        // Test case for a hoisted function declaration in the script scope
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(node);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

        // Test case for a hoisted function declaration in a function scope
        node = new Node(Token.FUNCTION);
        parent = new Node(Token.FUNCTION);
        parent.addChildToFront(node);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

        // Test case for a non-hoisted function declaration
        node = new Node(Token.FUNCTION);
        parent = new Node(Token.BLOCK);
        parent.addChildToFront(node);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));

        // Test case for a non-function declaration
        node = new Node(Token.FOR);
        parent = new Node(Token.SCRIPT);
        parent.addChildToFront(node);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}

