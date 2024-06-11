import org.junit.Test;

import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn_typical() {
        Node node = new Node(Token.FOR);
        node.addChildToFront(new Node(Token.VAR));
        node.addChildToFront(new Node(Token.EMPTY));
        node.addChildToFront(new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsForIn_emptyBody() {
        Node node = new Node(Token.FOR);
        node.addChildToFront(new Node(Token.VAR));
        node.addChildToFront(new Node(Token.EMPTY));
        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement_parentIsScript() {
        Node parent = new Node(Token.SCRIPT);
        Node node = new Node(Token.FUNCTION);
        parent.addChildToFront(node);
        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsStatement_parentIsBlock() {
        Node parent = new Node(Token.BLOCK);
        Node node = new Node(Token.FUNCTION);
        parent.addChildToFront(node);
        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsStatement_parentIsLabel() {
        Node parent = new Node(Token.LABEL);
        Node node = new Node(Token.FUNCTION);
        parent.addChildToFront(node);
        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsStatement_parentIsOther() {
        Node parent = new Node(Token.EMPTY);
        Node node = new Node(Token.FUNCTION);
        parent.addChildToFront(node);
        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration_typical() {
        Node node = new Node(Token.FUNCTION);
        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsFunctionDeclaration_notStatement() {
        Node node = new Node(Token.FUNCTION);
        node.addChildToFront(new Node(Token.EMPTY));
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_typical() {
        Node parent = new Node(Token.SCRIPT);
        Node node = new Node(Token.FUNCTION);
        parent.addChildToFront(node);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_nestedFunction() {
        Node grandparent = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(grandparent);
        Node node = new Node(Token.FUNCTION);
        grandparent.addChildToFront(node);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_notHoisted() {
        Node parent = new Node(Token.BLOCK);
        Node node = new Node(Token.FUNCTION);
        parent.addChildToFront(node);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}