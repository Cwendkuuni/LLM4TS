import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node n = new Node(Token.FOR, new Node(), new Node(), new Node());
        assertTrue(NodeUtil.isForIn(n));

        Node m = new Node(Token.IF, new Node(), new Node());
        assertFalse(NodeUtil.isForIn(m));
    }

    @Test
    public void testIsStatement() {
        Node n = new Node(Token.SCRIPT, new Node());
        assertTrue(NodeUtil.isStatement(n));

        Node m = new Node(Token.FUNCTION, new Node());
        m.addChildToFront(new Node());
        assertFalse(NodeUtil.isStatement(m));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION, new Node());
        assertTrue(NodeUtil.isFunctionDeclaration(n));

        Node m = new Node(Token.ADD, new Node(), new Node());
        assertFalse(NodeUtil.isFunctionDeclaration(m));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION, new Node());
        Node parent = new Node(Token.SCRIPT, n);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));

        Node m = new Node(Token.FUNCTION, new Node());
        Node grandparent = new Node(Token.FUNCTION, m);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(m));
    }
}