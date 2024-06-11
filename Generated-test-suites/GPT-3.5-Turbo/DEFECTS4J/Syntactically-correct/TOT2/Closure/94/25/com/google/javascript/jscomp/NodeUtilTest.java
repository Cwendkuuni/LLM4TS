import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node n = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsStatement() {
        Node n = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        assertTrue(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node parentNode = new Node(Token.SCRIPT);
        Node grandParentNode = new Node(Token.FUNCTION);
        Node n = new Node(Token.FUNCTION);
        parentNode.addChildToBack(n);
        grandParentNode.addChildToBack(parentNode);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
    }
}