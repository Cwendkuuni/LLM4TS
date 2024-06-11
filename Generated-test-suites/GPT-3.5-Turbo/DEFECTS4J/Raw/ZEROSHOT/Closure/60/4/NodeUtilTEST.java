import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR, new Node(Token.FOR_IN), new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsForIn_withInvalidNode() {
        Node node = new Node(Token.WHILE, new Node(Token.FOR_IN), new Node(Token.EMPTY));
        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.EXPR_RESULT);
        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsStatement_withInvalidNode() {
        Node node = new Node(Token.EXPR_RESULT);
        node.detach();
        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsFunctionDeclaration_withInvalidNode() {
        Node node = new Node(Token.VAR);
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node parent = new Node(Token.SCRIPT);
        Node node = new Node(Token.FUNCTION, parent);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_withInvalidNode() {
        Node parent = new Node(Token.VAR);
        Node node = new Node(Token.FUNCTION, parent);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}