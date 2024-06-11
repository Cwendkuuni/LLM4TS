import static org.junit.Assert.*;

import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node n = new Node(Token.FOR);
        n.addChildToFront(new Node(Token.EMPTY));
        n.addChildToBack(new Node(Token.EMPTY));
        n.addChildToBack(new Node(Token.EMPTY));

        assertTrue(NodeUtil.isForIn(n));

        n.removeChild(n.getFirstChild());

        assertFalse(NodeUtil.isForIn(n));

        n = new Node(Token.WHILE);

        assertFalse(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsStatement() {
        Node n = new Node(Token.SCRIPT);
        n.addChildToFront(new Node(Token.EMPTY));

        assertTrue(NodeUtil.isStatement(n.getFirstChild()));

        n = new Node(Token.FUNCTION);
        n.addChildToFront(new Node(Token.EMPTY));

        assertFalse(NodeUtil.isStatement(n.getFirstChild()));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        n.addChildToFront(new Node(Token.EMPTY));

        n.getParent().setType(Token.SCRIPT);
        assertTrue(NodeUtil.isFunctionDeclaration(n.getFirstChild()));

        n.getParent().setType(Token.BLOCK);
        assertTrue(NodeUtil.isFunctionDeclaration(n.getFirstChild()));

        n.getParent().setType(Token.LABEL);
        assertTrue(NodeUtil.isFunctionDeclaration(n.getFirstChild()));

        n.getParent().setType(Token.EMPTY);
        assertFalse(NodeUtil.isFunctionDeclaration(n.getFirstChild()));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        n.addChildToFront(new Node(Token.EMPTY));

        n.getParent().setType(Token.SCRIPT);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n.getFirstChild()));

        n.getParent().setType(Token.BLOCK);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n.getFirstChild()));

        n.getParent().setType(Token.LABEL);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n.getFirstChild()));

        n.getParent().setType(Token.EMPTY);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n.getFirstChild()));
    }

    @Test(expected = NullPointerException.class)
    public void testIsStatementNullParent() {
        Node n = new Node(Token.EMPTY);
        NodeUtil.isStatement(n);
    }

    @Test(expected = NullPointerException.class)
    public void testIsFunctionDeclarationNullParent() {
        Node n = new Node(Token.FUNCTION);
        n.addChildToFront(new Node(Token.EMPTY));

        NodeUtil.isFunctionDeclaration(n.getFirstChild());
    }

    @Test(expected = NullPointerException.class)
    public void testIsHoistedFunctionDeclarationNullParent() {
        Node n = new Node(Token.FUNCTION);
        n.addChildToFront(new Node(Token.EMPTY));

        NodeUtil.isHoistedFunctionDeclaration(n.getFirstChild());
    }

    @Test
    public void testIsFunctionDeclarationInvalidParent() {
        Node n = new Node(Token.FUNCTION);
        n.addChildToFront(new Node(Token.EMPTY));

        NodeUtil.isFunctionDeclaration(n.getFirstChild());
        assertFalse(NodeUtil.isFunctionDeclaration(n.getFirstChild()));
    }

    @Test
    public void testIsHoistedFunctionDeclarationInvalidParent() {
        Node n = new Node(Token.FUNCTION);
        n.addChildToFront(new Node(Token.EMPTY));

        NodeUtil.isHoistedFunctionDeclaration(n.getFirstChild());
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n.getFirstChild()));
    }

}