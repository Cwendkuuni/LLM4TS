
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR, new Node(), new Node(), new Node());
        assertFalse(NodeUtil.isForIn(new Node()));
        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.SCRIPT, new Node());
        assertTrue(NodeUtil.isStatement(node));
        assertFalse(NodeUtil.isStatement(new Node()));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION, new Node());
        assertTrue(NodeUtil.isFunctionDeclaration(node));
        assertFalse(NodeUtil.isFunctionDeclaration(new Node()));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node parent = new Node();
        Node grandparent = new Node(Token.FUNCTION, new Node(), new Node());
        parent.addChildToFront(grandparent);
        Node node = new Node(Token.FUNCTION, parent);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(new Node()));
    }
}
