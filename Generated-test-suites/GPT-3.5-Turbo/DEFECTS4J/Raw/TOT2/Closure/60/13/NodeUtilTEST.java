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
        Node node = new Node(Token.SCRIPT);
        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node parentNode = new Node(Token.SCRIPT);
        Node grandParentNode = new Node(Token.FUNCTION, new Node(Token.EMPTY));
        grandParentNode.addChildToBack(parentNode);
        Node node = new Node(Token.FUNCTION);
        parentNode.addChildToBack(node);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }

    @Test(expected = NullPointerException.class)
    public void testIsForIn_NullNode() {
        Node node = null;
        NodeUtil.isForIn(node);
    }

    @Test
    public void testIsStatement_InvalidParent() {
        Node node = new Node(Token.LABEL);
        assertFalse(NodeUtil.isStatement(node));
    }

}