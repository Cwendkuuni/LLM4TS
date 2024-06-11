import org.junit.Test;

import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node1 = new Node(Token.FOR);
        Node node2 = new Node(Token.WHILE);
        Node node3 = new Node(Token.FOR);
        node3.addChildToBack(new Node(Token.EXPR_RESULT));
        node3.addChildToBack(new Node(Token.EXPR_RESULT));
        
        assertTrue(NodeUtil.isForIn(node1));
        assertFalse(NodeUtil.isForIn(node2));
        assertFalse(NodeUtil.isForIn(node3));
    }

    @Test
    public void testIsStatement() {
        Node node1 = new Node(Token.SCRIPT);
        Node node2 = new Node(Token.BLOCK);
        Node node3 = new Node(Token.FUNCTION);
        node2.addChildToBack(new Node(Token.EXPR_RESULT));

        assertTrue(NodeUtil.isStatement(node1));
        assertTrue(NodeUtil.isStatement(node2));
        assertTrue(NodeUtil.isStatement(node3));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION);
        Node node2 = new Node(Token.EXPR_RESULT);
        Node node3 = new Node(Token.FUNCTION);
        node2.addChildToBack(new Node(Token.FUNCTION));

        assertTrue(NodeUtil.isFunctionDeclaration(node1));
        assertFalse(NodeUtil.isFunctionDeclaration(node2));
        assertFalse(NodeUtil.isFunctionDeclaration(node3));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION);
        Node node2 = new Node(Token.FUNCTION);
        Node node3 = new Node(Token.SCRIPT);
        Node node4 = new Node(Token.FUNCTION);
        node4.addChildToBack(new Node(Token.FUNCTION));

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node1));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node2));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node3));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node4));
    }
}