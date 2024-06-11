import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node n = new Node(Token.FOR, new Node(Token.NAME), new Node(Token.NAME), new Node(Token.NAME));
        assertFalse(NodeUtil.isForIn(n));

        n = new Node(Token.FOR, new Node(Token.NAME), new Node(Token.NAME));
        assertTrue(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsStatement() {
        Node n = new Node(Token.SCRIPT);
        assertTrue(NodeUtil.isStatement(n));

        n = new Node(Token.FUNCTION);
        assertTrue(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION, new Node(Token.NAME));
        assertTrue(NodeUtil.isFunctionDeclaration(n));

        n = new Node(Token.FUNCTION, new Node(Token.NAME), new Node(Token.NAME));
        assertFalse(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION, new Node(Token.NAME));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));

        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(n);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));

        Node grandParent = new Node(Token.FUNCTION);
        grandParent.addChildToFront(parent);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
    }

}