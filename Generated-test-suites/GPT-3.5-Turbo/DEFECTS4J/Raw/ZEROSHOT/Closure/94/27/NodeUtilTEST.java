import org.junit.Test;

import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node1 = new Node(Token.FOR);
        Node node2 = new Node(Token.FOR);
        node2.addChildToFront(new Node(Token.ADD));
        node2.addChildToFront(new Node(Token.ADD));
        node2.addChildToFront(new Node(Token.ADD));

        assertTrue(NodeUtil.isForIn(node1));
        assertFalse(NodeUtil.isForIn(node2));
    }

    @Test
    public void testIsStatement() {
        Node node1 = new Node(Token.SCRIPT);
        Node node2 = new Node(Token.BLOCK);
        Node node3 = new Node(Token.FUNCTION);
        node3.setParent(new Node(Token.SCRIPT));
        Node node4 = new Node(Token.ADD);

        assertTrue(NodeUtil.isStatement(node1));
        assertTrue(NodeUtil.isStatement(node2));
        assertTrue(NodeUtil.isStatement(node3));
        assertFalse(NodeUtil.isStatement(node4));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION);
        node1.setParent(new Node(Token.SCRIPT));
        Node node2 = new Node(Token.FUNCTION);
        node2.setParent(new Node(Token.BLOCK));
        Node node3 = new Node(Token.TRUE);

        assertTrue(NodeUtil.isFunctionDeclaration(node1));
        assertFalse(NodeUtil.isFunctionDeclaration(node2));
        assertFalse(NodeUtil.isFunctionDeclaration(node3));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION);
        Node node1Parent = new Node(Token.SCRIPT);
        node1Parent.setParent(new Node(Token.SCRIPT));
        node1.setParent(node1Parent);
        Node node2 = new Node(Token.FUNCTION);
        Node node2Parent = new Node(Token.SCRIPT);
        node2Parent.setParent(new Node(Token.BLOCK));
        node2.setParent(node2Parent);
        Node node3 = new Node(Token.FUNCTION);
        node3.setParent(new Node(Token.TRUE));

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node3));
    }

}