import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR, new Node(Token.BLOCK), new Node(Token.BLOCK));
        assertTrue(NodeUtil.isForIn(node));

        Node node2 = new Node(Token.IF, new Node(Token.BLOCK), new Node(Token.BLOCK));
        assertFalse(NodeUtil.isForIn(node2));
    }

    @Test
    public void testIsStatement() {
        Node parentNode = new Node(Token.SCRIPT, new Node(Token.BLOCK), new Node(Token.BLOCK));
        Node node = new Node(Token.IF, parentNode, new Node(Token.BLOCK));

        assertTrue(NodeUtil.isStatement(node));

        Node parentNode2 = new Node(Token.FUNCTION, new Node(Token.BLOCK), new Node(Token.BLOCK));
        Node node2 = new Node(Token.IF, parentNode2, new Node(Token.BLOCK));

        assertFalse(NodeUtil.isStatement(node2));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION, new Node(Token.BLOCK), new Node(Token.BLOCK));

        assertTrue(NodeUtil.isFunctionDeclaration(node));

        Node node2 = new Node(Token.WHILE, new Node(Token.BLOCK), new Node(Token.BLOCK));

        assertFalse(NodeUtil.isFunctionDeclaration(node2));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node parentNode = new Node(Token.SCRIPT, new Node(Token.BLOCK), new Node(Token.BLOCK));
        Node parentFunctionNode = new Node(Token.FUNCTION, parentNode, new Node(Token.BLOCK));
        Node node = new Node(Token.FUNCTION, parentFunctionNode, new Node(Token.BLOCK));

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

        Node parentNode2 = new Node(Token.SCRIPT, new Node(Token.BLOCK), new Node(Token.BLOCK));
        Node node2 = new Node(Token.FUNCTION, parentNode2, new Node(Token.BLOCK));

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));
    }
}