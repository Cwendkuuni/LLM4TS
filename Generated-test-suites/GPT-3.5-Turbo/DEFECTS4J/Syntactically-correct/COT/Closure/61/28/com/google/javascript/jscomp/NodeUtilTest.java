import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.IN), new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsForIn_false() {
        Node node = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.TO), new Node(Token.EMPTY));
        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.SCRIPT, new Node(Token.EMPTY));
        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsStatement_false() {
        Node node = new Node(Token.EXPR_RESULT, new Node(Token.EMPTY));
        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION, new Node(Token.NAME), new Node(Token.PARAM_LIST), new Node(Token.BLOCK));
        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsFunctionDeclaration_false() {
        Node node = new Node(Token.FUNCTION, new Node(Token.EMPTY), new Node(Token.PARAM_LIST), new Node(Token.BLOCK));
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node parent = new Node(Token.SCRIPT);
        Node node = new Node(Token.FUNCTION, new Node(Token.NAME), new Node(Token.PARAM_LIST), new Node(Token.BLOCK), parent);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_false() {
        Node parent = new Node(Token.FUNCTION);
        Node node = new Node(Token.FUNCTION, new Node(Token.NAME), new Node(Token.PARAM_LIST), new Node(Token.BLOCK), parent);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}


