import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node n = new Node(Token.FOR);
        n.addChildToBack(new Node(Token.EMPTY));
        n.addChildToBack(new Node(Token.EMPTY));
        n.addChildToBack(new Node(Token.EMPTY));

        assertTrue(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsStatement() {
        Node n = new Node(Token.SCRIPT);
        Node child = new Node(Token.ADD);
        n.addChildToBack(child);

        assertFalse(NodeUtil.isStatement(child));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(n);

        assertTrue(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(n);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
    }
}