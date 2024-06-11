import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR, new Node(Token.BLOCK), new Node(Token.BLOCK));
        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node parent = new Node(Token.SCRIPT);
        Node node = new Node(Token.FUNCTION, parent, new Node(Token.BLOCK));
        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node parent = new Node(Token.SCRIPT);
        Node node = new Node(Token.FUNCTION, parent, new Node(Token.BLOCK));
        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node grandparent = new Node(Token.SCRIPT);
        Node parent = new Node(Token.FUNCTION, grandparent, new Node(Token.BLOCK));
        Node node = new Node(Token.FUNCTION, parent, new Node(Token.BLOCK));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}