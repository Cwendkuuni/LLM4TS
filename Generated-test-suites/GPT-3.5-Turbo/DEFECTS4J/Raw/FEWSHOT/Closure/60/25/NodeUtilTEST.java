import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node1 = new Node(Token.FOR);
        node1.addChildToBack(new Node(Token.NAME));
        node1.addChildToBack(new Node(Token.EMPTY));
        Node node2 = new Node(Token.VAR);
        node2.addChildToBack(new Node(Token.NAME));
        node2.addChildToBack(new Node(Token.EMPTY));
        Node node3 = new Node(Token.FOR);
        node3.addChildToBack(new Node(Token.NAME));
        node3.addChildToBack(new Node(Token.EMPTY));
        node3.addChildToBack(new Node(Token.BLOCK));

        assertFalse(NodeUtil.isForIn(node1));
        assertFalse(NodeUtil.isForIn(node2));
        assertTrue(NodeUtil.isForIn(node3));
    }

    @Test
    public void testIsStatement() {
        Node node1 = new Node(Token.SCRIPT);
        Node node2 = new Node(Token.FUNCTION);
        Node node3 = new Node(Token.ADD);
        Node node4 = new Node(Token.NAME);
        node4.addChildToBack(new Node(Token.EMPTY));
        node3.addChildToBack(node4);
        Node node5 = new Node(Token.IF);
        node5.addChildToBack(new Node(Token.BLOCK));
        Node node6 = new Node(Token.LABEL);
        Node node7 = new Node(Token.BLOCK);
        node7.addChildToBack(new Node(Token.EMPTY));
        node6.addChildToBack(node7);

        assertTrue(NodeUtil.isStatement(node1));
        assertTrue(NodeUtil.isStatement(node2));
        assertFalse(NodeUtil.isStatement(node3));
        assertTrue(NodeUtil.isStatement(node4));
        assertTrue(NodeUtil.isStatement(node5));
        assertTrue(NodeUtil.isStatement(node6));
        assertFalse(NodeUtil.isStatement(node7));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION);
        node1.addChildToBack(new Node(Token.EMPTY));
        Node node2 = new Node(Token.NAME);
        Node node3 = new Node(Token.FUNCTION);
        node3.addChildToBack(new Node(Token.BLOCK));
        Node node4 = new Node(Token.IF);
        node4.addChildToBack(new Node(Token.EMPTY));
        node4.addChildToBack(new Node(Token.BLOCK));
        node4.addChildToBack(new Node(Token.BLOCK));
        node4.addChildToBack(new Node(Token.EMPTY));

        assertTrue(NodeUtil.isFunctionDeclaration(node1));
        assertFalse(NodeUtil.isFunctionDeclaration(node2));
        assertFalse(NodeUtil.isFunctionDeclaration(node3));
        assertFalse(NodeUtil.isFunctionDeclaration(node4));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION);
        node1.addChildToBack(new Node(Token.EMPTY));
        Node node2 = new Node(Token.NAME);
        Node node3 = new Node(Token.FUNCTION);
        node3.addChildToBack(new Node(Token.BLOCK));
        Node node4 = new Node(Token.NAME);
        Node node5 = new Node(Token.SCRIPT);
        node5.addChildToBack(node4);
        Node node6 = new Node(Token.FUNCTION);
        node6.addChildToBack(new Node(Token.BLOCK));

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node1));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node3));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node4));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node5));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node6));
    }
}