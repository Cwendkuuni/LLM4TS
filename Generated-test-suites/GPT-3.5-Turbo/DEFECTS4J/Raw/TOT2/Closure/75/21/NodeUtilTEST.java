import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR, new Node(Token.OPEN_CURLY), new Node(Token.CLOSE_CURLY));
        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsNotForIn() {
        Node node = new Node(Token.IF, new Node(Token.OPEN_CURLY), new Node(Token.CLOSE_CURLY));
        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node parent = new Node(Token.SCRIPT);
        Node node = new Node(Token.IF, new Node(Token.OPEN_CURLY), new Node(Token.CLOSE_CURLY));
        node.setParent(parent);
        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsNotStatement() {
        Node parent = new Node(Token.CALL, new Node(Token.OPEN_PAREN), new Node(Token.CLOSE_PAREN));
        Node node = new Node(Token.IF, new Node(Token.OPEN_CURLY), new Node(Token.CLOSE_CURLY));
        node.setParent(parent);
        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node parent = new Node(Token.SCRIPT);
        Node node = new Node(Token.FUNCTION, new Node(Token.OPEN_PAREN), new Node(Token.CLOSE_PAREN));
        node.setParent(parent);
        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsNotFunctionDeclaration() {
        Node parent = new Node(Token.CALL, new Node(Token.OPEN_PAREN), new Node(Token.CLOSE_PAREN));
        Node node = new Node(Token.FUNCTION, new Node(Token.OPEN_PAREN), new Node(Token.CLOSE_PAREN));
        node.setParent(parent);
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node grandParent = new Node(Token.SCRIPT);
        Node parent = new Node(Token.FUNCTION);
        Node node = new Node(Token.FUNCTION, new Node(Token.OPEN_PAREN), new Node(Token.CLOSE_PAREN));
        node.setParent(parent);
        parent.setParent(grandParent);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }

    @Test
    public void testIsNotHoistedFunctionDeclaration() {
        Node grandParent = new Node(Token.SCRIPT);
        Node parent = new Node(Token.FUNCTION);
        Node node = new Node(Token.FUNCTION, new Node(Token.OPEN_PAREN), new Node(Token.CLOSE_PAREN));
        node.setParent(parent);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}