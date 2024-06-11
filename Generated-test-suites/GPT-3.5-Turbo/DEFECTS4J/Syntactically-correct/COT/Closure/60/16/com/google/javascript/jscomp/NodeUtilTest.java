
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(node));

        node = new Node(Token.FUNCTION, new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.SCRIPT, new Node(Token.EMPTY));
        assertTrue(NodeUtil.isStatement(node));

        node = new Node(Token.LABEL, new Node(Token.EMPTY));
        assertTrue(NodeUtil.isStatement(node));

        node = new Node(Token.FUNCTION, new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION, new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertTrue(NodeUtil.isFunctionDeclaration(node));

        node = new Node(Token.VAR, new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION, new Node(Token.EMPTY), new Node(Token.SCRIPT, new Node(Token.EMPTY)));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

        node = new Node(Token.FUNCTION, new Node(Token.EMPTY), new Node(Token.BLOCK, new Node(Token.EMPTY)));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));

        node = new Node(Token.VAR, new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}

