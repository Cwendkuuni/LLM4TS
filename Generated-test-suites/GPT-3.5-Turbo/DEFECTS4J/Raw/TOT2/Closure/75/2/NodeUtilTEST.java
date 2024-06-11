import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR, new Node(Token.BLOCK), new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(node));
        
        Node node2 = new Node(Token.IF, new Node(Token.EMPTY), new Node(Token.BLOCK));
        assertFalse(NodeUtil.isForIn(node2));
    }

    @Test
    public void testIsStatement() {
        Node parentNode = new Node(Token.SCRIPT);
        Node node = new Node(Token.VAR, parentNode);
        assertTrue(NodeUtil.isStatement(node));

        Node parentNode2 = new Node(Token.FUNCTION);
        Node node2 = new Node(Token.VAR, parentNode2);
        assertFalse(NodeUtil.isStatement(node2));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        assertTrue(NodeUtil.isFunctionDeclaration(node));

        Node node2 = new Node(Token.IF);
        assertFalse(NodeUtil.isFunctionDeclaration(node2));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node functionNode = new Node(Token.FUNCTION, scriptNode);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));

        Node functionNode2 = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode2));
    }
}