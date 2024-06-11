import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR, new Node(), new Node(), new Node());
        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.SCRIPT, new Node());
        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION, new Node());
        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node parent = new Node(Token.SCRIPT, new Node());
        Node node = new Node(Token.FUNCTION, parent);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }

    @Test(expected = IllegalStateException.class)
    public void testIsStatementWithNullParent() {
        Node node = new Node(Token.SCRIPT, new Node());
        node.clearParent();
        NodeUtil.isStatement(node);
    }

}