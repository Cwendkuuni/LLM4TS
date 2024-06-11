import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node node1 = new Node(Token.FOR);
        Node node2 = new Node(Token.FOR);
        node2.addChildToBack(new Node(Token.EMPTY));
        node2.addChildToBack(new Node(Token.EMPTY));
        node2.addChildToBack(new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(node2));
        assertFalse(NodeUtil.isForIn(node1));
    }

    @Test
    public void testIsStatement() {
        Node node1 = new Node(Token.SCRIPT);
        Node node2 = new Node(Token.BLOCK);
        Node node3 = new Node(Token.LABEL);
        Node node4 = new Node(Token.IF);
        Node node5 = new Node(Token.EMPTY);
        node4.addChildToBack(node5);

        assertTrue(NodeUtil.isStatement(node1));
        assertTrue(NodeUtil.isStatement(node2));
        assertTrue(NodeUtil.isStatement(node3));
        assertTrue(NodeUtil.isStatement(node4));
        assertFalse(NodeUtil.isStatement(node5));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION);
        Node node2 = new Node(Token.FUNCTION);
        Node parent1 = new Node(Token.SCRIPT);
        Node parent2 = new Node(Token.BLOCK);
        node1.setParent(parent1);
        node2.setParent(parent2);

        assertTrue(NodeUtil.isFunctionDeclaration(node1));
        assertFalse(NodeUtil.isFunctionDeclaration(node2));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION);
        Node node2 = new Node(Token.FUNCTION);
        Node node3 = new Node(Token.FUNCTION);
        Node parent1 = new Node(Token.SCRIPT);
        Node parent2 = new Node(Token.FUNCTION);
        Node parent3 = new Node(Token.BLOCK);
        node1.setParent(parent1);
        node2.setParent(parent2);
        node3.setParent(parent3);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node2));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node3));
    }
}