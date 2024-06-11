import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(node));

        Node invalidNode = new Node(Token.IF);
        assertFalse(NodeUtil.isForIn(invalidNode));
    }

    @Test
    public void testIsStatement() {
        Node parent = new Node(Token.SCRIPT);
        Node node = new Node(Token.EMPTY);
        parent.addChildToBack(node);
        assertTrue(NodeUtil.isStatement(node));

        Node invalidNode = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isStatement(invalidNode));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        assertTrue(NodeUtil.isFunctionDeclaration(node));

        Node invalidNode = new Node(Token.EMPTY);
        assertFalse(NodeUtil.isFunctionDeclaration(invalidNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node scriptParent = new Node(Token.SCRIPT);
        Node functionParent = new Node(Token.FUNCTION);
        functionParent.addChildToBack(new Node(Token.FUNCTION));

        Node node = new Node(Token.FUNCTION);
        scriptParent.addChildToBack(node);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

        Node invalidNode = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(invalidNode));
    }
}