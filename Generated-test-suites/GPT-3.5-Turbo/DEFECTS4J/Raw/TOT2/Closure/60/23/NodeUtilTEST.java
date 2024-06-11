import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node parent = new Node(Token.SCRIPT);
        Node node = new Node(Token.FUNCTION, parent, new Node(Token.EMPTY));
        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION, new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node grandParent = new Node(Token.SCRIPT);
        Node parent = new Node(Token.FUNCTION, grandParent, new Node(Token.EMPTY));
        Node node = new Node(Token.FUNCTION, parent, new Node(Token.EMPTY));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}