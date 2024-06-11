import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToFront(new Node(Token.EMPTY));
        node.addChildToFront(new Node(Token.EMPTY));
        node.addChildToFront(new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsNotForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToFront(new Node(Token.EMPTY));
        node.addChildToFront(new Node(Token.EMPTY));
        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.EMPTY);
        node.setParent(new Node(Token.SCRIPT));
        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsNotStatement() {
        Node node = new Node(Token.EMPTY);
        node.setParent(new Node(Token.FUNCTION));
        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        node.setParent(new Node(Token.SCRIPT));
        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsNotFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        node.setParent(new Node(Token.BLOCK));
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclarationInScript() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        node.setParent(parent);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclarationInFunction() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.FUNCTION);
        Node grandparent = new Node(Token.SCRIPT);
        parent.setParent(grandparent);
        node.setParent(parent);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }

    @Test
    public void testIsNotHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.setParent(new Node(Token.SCRIPT));
        node.setParent(parent);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}