import org.junit.Test;

import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        // Test case 1
        Node node1 = new Node(Token.FOR);
        node1.addChildToBack(new Node());
        node1.addChildToBack(new Node());
        node1.addChildToBack(new Node());
        assertTrue(NodeUtil.isForIn(node1));

        // Test case 2
        Node node2 = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isForIn(node2));
    }

    @Test
    public void testIsStatement() {
        // Test case 1
        Node node1 = new Node(Token.SCRIPT);
        assertTrue(NodeUtil.isStatement(node1));

        // Test case 2
        Node node2 = new Node(Token.STRING);
        node2.setParent(node1);
        assertTrue(NodeUtil.isStatement(node2));

        // Test case 3
        Node node3 = new Node(Token.BLOCK);
        node3.setParent(node1);
        assertTrue(NodeUtil.isStatement(node3));

        // Test case 4
        Node node4 = new Node(Token.LABEL);
        node4.setParent(node1);
        assertTrue(NodeUtil.isStatement(node4));

        // Test case 5
        Node node5 = new Node(Token.IF);
        node5.setParent(node1);
        assertFalse(NodeUtil.isStatement(node5));
    }

    @Test
    public void testIsFunctionDeclaration() {
        // Test case 1
        Node node1 = new Node(Token.FUNCTION);
        Node parent1 = new Node(Token.SCRIPT);
        node1.setParent(parent1);
        assertTrue(NodeUtil.isFunctionDeclaration(node1));

        // Test case 2
        Node node2 = new Node(Token.FUNCTION);
        Node parent2 = new Node(Token.EXPR_RESULT);
        node2.setParent(parent2);
        assertFalse(NodeUtil.isFunctionDeclaration(node2));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        // Test case 1
        Node node1 = new Node(Token.FUNCTION);
        Node parent1 = new Node(Token.SCRIPT);
        node1.setParent(parent1);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));

        // Test case 2
        Node node2 = new Node(Token.FUNCTION);
        Node parent2 = new Node(Token.FUNCTION);
        Node grandParent2 = new Node(Token.SCRIPT);
        node2.setParent(parent2);
        parent2.setParent(grandParent2);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));

        // Test case 3
        Node node3 = new Node(Token.SCRIPT);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node3));
    }
}