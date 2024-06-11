import org.junit.Test;

import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn_shouldReturnTrue() {
        Node node = new Node(Token.FOR);
        Node child1 = new Node(Token.EMPTY);
        Node child2 = new Node(Token.EMPTY);
        Node child3 = new Node(Token.EMPTY);
        node.addChildToFront(child1);
        node.addChildToFront(child2);
        node.addChildToFront(child3);

        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsForIn_shouldReturnFalse() {
        Node node = new Node(Token.FOR);

        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement_shouldReturnTrue() {
        Node node = new Node(Token.SCRIPT);
        Node parent = new Node(Token.BLOCK);
        parent.addChildToFront(node);

        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsStatement_shouldReturnFalse() {
        Node node = new Node(Token.EMPTY);

        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration_shouldReturnTrue() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(node);

        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsFunctionDeclaration_shouldReturnFalse() {
        Node node = new Node(Token.FUNCTION);

        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_shouldReturnTrue() {
        Node node = new Node(Token.FUNCTION);
        Node parent1 = new Node(Token.SCRIPT);
        Node parent2 = new Node(Token.BLOCK);
        parent1.addChildToFront(node);
        parent2.addChildToFront(parent1);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_shouldReturnFalse() {
        Node node = new Node(Token.FUNCTION);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }

}