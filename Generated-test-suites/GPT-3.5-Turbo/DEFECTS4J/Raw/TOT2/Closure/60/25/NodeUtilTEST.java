import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node n = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsNotForIn() {
        Node n = new Node(Token.WHILE, new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertFalse(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsStatement() {
        Node parent = new Node(Token.SCRIPT);
        Node n = new Node(Token.FUNCTION, parent, new Node(Token.EMPTY));
        assertTrue(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsNotStatement() {
        Node parent = new Node(Token.WHILE);
        Node n = new Node(Token.FUNCTION, parent, new Node(Token.EMPTY));
        assertFalse(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION, new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertTrue(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsNotFunctionDeclaration() {
        Node n = new Node(Token.VAR, new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertFalse(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node parent = new Node(Token.SCRIPT);
        Node grandparent = new Node(Token.FUNCTION, parent, new Node(Token.EMPTY));
        Node n = new Node(Token.FUNCTION, grandparent, new Node(Token.EMPTY));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
    }

    @Test
    public void testIsNotHoistedFunctionDeclaration() {
        Node parent = new Node(Token.SCRIPT);
        Node n = new Node(Token.FUNCTION, parent, new Node(Token.EMPTY));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
    }

}