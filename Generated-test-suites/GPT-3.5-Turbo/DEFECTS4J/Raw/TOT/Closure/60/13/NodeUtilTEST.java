import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        // Test typical use case
        Node node1 = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(node1));

        // Test empty for loop
        Node node2 = new Node(Token.FOR);
        assertFalse(NodeUtil.isForIn(node2));

        // Test for loop with only 2 children
        Node node3 = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertFalse(NodeUtil.isForIn(node3));
    }

    @Test
    public void testIsStatement() {
        // Test typical use case
        Node node1 = new Node(Token.SCRIPT);
        node1.addChildToFront(new Node(Token.VAR));
        assertTrue(NodeUtil.isStatement(node1));

        // Test node without parents
        Node node2 = new Node(Token.SCRIPT);
        assertFalse(NodeUtil.isStatement(node2));

        // Test node with parent of type LABEL
        Node node3 = new Node(Token.LABEL);
        node3.addChildToFront(new Node(Token.EMPTY));
        assertTrue(NodeUtil.isStatement(node3));

        // Test node with parent of unknown type
        Node node4 = new Node(Token.OR);
        node4.addChildToFront(new Node(Token.EMPTY));
        assertFalse(NodeUtil.isStatement(node4));
    }

    @Test
    public void testIsFunctionDeclaration() {
        // Test typical use case of function declaration
        Node node1 = new Node(Token.FUNCTION);
        node1.addChildToFront(new Node(Token.NAME));
        assertTrue(NodeUtil.isFunctionDeclaration(node1));

        // Test function declaration as part of an expression
        Node node2 = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isFunctionDeclaration(node2));

        // Test non-function node
        Node node3 = new Node(Token.NAME);
        assertFalse(NodeUtil.isFunctionDeclaration(node3));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        // Test typical use case of hoisted function declaration
        Node functionNode1 = new Node(Token.FUNCTION);
        Node scriptNode1 = new Node(Token.SCRIPT);
        scriptNode1.addChildToBack(functionNode1);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode1));

        // Test non-hoisted function declaration
        Node functionNode2 = new Node(Token.FUNCTION);
        Node parentFunctionNode = new Node(Token.FUNCTION);
        Node scriptNode2 = new Node(Token.SCRIPT);
        scriptNode2.addChildToBack(parentFunctionNode);
        parentFunctionNode.addChildToBack(functionNode2);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode2));

        // Test non-function node
        Node node3 = new Node(Token.NAME);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node3));
    }
}