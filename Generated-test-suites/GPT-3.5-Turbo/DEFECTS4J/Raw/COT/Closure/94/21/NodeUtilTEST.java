import org.junit.Test;

import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node n = new Node(Token.FOR, new Node[]{new Node(), new Node(), new Node()});
        assertTrue(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsForInFalse() {
        Node n = new Node(Token.FOR, new Node[]{new Node(), new Node()});
        assertFalse(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsStatement() {
        Node n = new Node(Token.SCRIPT);
        assertTrue(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsStatementFalse() {
        Node n = new Node(Token.VAR);
        assertFalse(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        assertTrue(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsFunctionDeclarationFalse() {
        Node n = new Node(Token.VAR);
        assertFalse(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node parent = new Node(Token.SCRIPT);
        Node n = new Node(Token.FUNCTION, parent);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclarationFalse() {
        Node parent = new Node(Token.VAR);
        Node n = new Node(Token.FUNCTION, parent);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclarationOutsideScript() {
        Node parent = new Node(Token.FUNCTION, new Node(Token.FUNCTION));
        Node n = new Node(Token.FUNCTION, parent);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
    }
}