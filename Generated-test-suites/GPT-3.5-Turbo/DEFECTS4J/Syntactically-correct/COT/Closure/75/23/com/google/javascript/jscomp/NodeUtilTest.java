import org.junit.Test;

import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToFront(new Node(Token.ITER, new Node(Token.NAME)));

        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsNotForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToFront(new Node(Token.ITER, new Node(Token.NAME)));
        node.addChildToFront(new Node(Token.EMPTY));

        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.SCRIPT);

        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsNotStatement() {
        Node node = new Node(Token.NAME);

        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);

        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsNotFunctionDeclaration() {
        Node node = new Node(Token.NAME);

        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node parent = new Node(Token.SCRIPT);

        Node node = new Node(Token.FUNCTION);
        parent.addChildToFront(node);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }

    @Test
    public void testIsNotHoistedFunctionDeclaration() {
        Node parent = new Node(Token.SCRIPT);

        Node child = new Node(Token.FUNCTION);
        parent.addChildToFront(new Node(Token.ITER, child));

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(child));
    }
}