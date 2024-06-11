import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        // Typical use case
        assertFalse(NodeUtil.isForIn(new Node(Token.FOR, new Node(), new Node(), new Node())));

        // Edge case
        assertTrue(NodeUtil.isForIn(new Node(Token.FOR, new Node(), new Node(), new Node(), new Node())));
    }

    @Test
    public void testIsStatement() {
        // Typical use case
        assertTrue(NodeUtil.isStatement(new Node(Token.SCRIPT)));

        // Edge case
        assertFalse(NodeUtil.isStatement(new Node(Token.VAR)));

        // Error scenario
        assertThrows(NullPointerException.class, () -> NodeUtil.isStatement(null));
    }

    @Test
    public void testIsFunctionDeclaration() {
        // Typical use case
        assertTrue(NodeUtil.isFunctionDeclaration(new Node(Token.FUNCTION)));

        // Edge case
        assertFalse(NodeUtil.isFunctionDeclaration(new Node(Token.VAR)));

        // Error scenario
        assertThrows(NullPointerException.class, () -> NodeUtil.isFunctionDeclaration(null));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        // Typical use case
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(new Node(Token.FUNCTION)));

        // Edge case
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(new Node(Token.FUNCTION, new Node(), new Node())));

        // Error scenario
        assertThrows(NullPointerException.class, () -> NodeUtil.isHoistedFunctionDeclaration(null));
    }
}