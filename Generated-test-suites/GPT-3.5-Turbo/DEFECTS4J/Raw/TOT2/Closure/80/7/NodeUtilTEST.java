import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node n = new Node(Token.FOR, new Node(), new Node(), new Node());
        assertTrue(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsStatement() {
        Node n = new Node(Token.SCRIPT, new Node(), new Node());
        assertTrue(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION, new Node(), new Node());
        assertTrue(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node parent = new Node(Token.SCRIPT, new Node());
        Node n = new Node(Token.FUNCTION, parent, new Node());
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
    }
}