import org.junit.Test;

import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node n1 = new Node(Token.FOR);
        n1.addChildToFront(new Node(Token.LP));
        n1.addChildAfter(new Node(Token.RP), n1.getFirstChild());
        n1.addChildToBack(new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(n1));

        Node n2 = new Node(Token.FOR);
        n2.addChildToFront(new Node(Token.LP));
        n2.addChildAfter(new Node(Token.RP), n2.getFirstChild());
        n2.addChildToBack(new Node(Token.EMPTY));
        n2.addChildToBack(new Node(Token.EMPTY));
        assertFalse(NodeUtil.isForIn(n2));

        Node n3 = new Node(Token.EMPTY);
        assertFalse(NodeUtil.isForIn(n3));
    }

    @Test
    public void testIsStatement() {
        Node n1 = new Node(Token.SCRIPT);
        Node n2 = new Node(Token.BLOCK);
        Node n3 = new Node(Token.LABEL);
        Node n4 = new Node(Token.FUNCTION);

        assertTrue(NodeUtil.isStatement(n1));
        assertTrue(NodeUtil.isStatement(n2));
        assertTrue(NodeUtil.isStatement(n3));
        assertFalse(NodeUtil.isStatement(n4));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node n1 = new Node(Token.FUNCTION);
        n1.addChildToFront(new Node(Token.NAME));

        assertTrue(NodeUtil.isFunctionDeclaration(n1));

        Node n2 = new Node(Token.FUNCTION);
        n2.addChildToFront(new Node(Token.NAME));
        n2.getParent().addChildToBack(new Node(Token.EXPR_RESULT));

        assertFalse(NodeUtil.isFunctionDeclaration(n2));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node n1 = new Node(Token.FUNCTION);
        n1.addChildToFront(new Node(Token.NAME));
        n1.getParent().addChildToFront(new Node(Token.SCRIPT));

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n1));

        Node n2 = new Node(Token.FUNCTION);
        n2.addChildToFront(new Node(Token.NAME));
        n2.getParent().getParent().addChildToFront(new Node(Token.FUNCTION));

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n2));

        Node n3 = new Node(Token.FUNCTION);
        n3.addChildToFront(new Node(Token.NAME));
        n3.getParent().getParent().addChildToFront(new Node(Token.EXPR_RESULT));

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n3));
    }
}