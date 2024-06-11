
import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn_Positive() {
        // Given
        Node n = new Node(Token.FOR);
        n.addChildToBack(new Node(Token.SCRIPT));
        n.addChildToBack(new Node(Token.SCRIPT));
        n.addChildToBack(new Node(Token.SCRIPT));

        // Then
        assertTrue(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsForIn_Negative() {
        // Given
        Node n = new Node(Token.IF);
        n.addChildToBack(new Node(Token.SCRIPT));
        n.addChildToBack(new Node(Token.SCRIPT));

        // Then
        assertFalse(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsStatement() {
        // Given
        Node n = new Node(Token.SCRIPT);
        Node parent = new Node(Token.EXPR_RESULT);
        n.addChildToBack(parent);

        // Then
        assertTrue(NodeUtil.isStatement(parent));
    }

    @Test
    public void testIsFunctionDeclaration() {
        // Given
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        n.addChildToBack(parent);

        // Then
        assertTrue(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        // Given
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        Node grandParent = new Node(Token.FUNCTION);
        grandParent.addChildToBack(n);
        parent.addChildToBack(grandParent);
        n.addChildToBack(parent);

        // Then
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
    }
}
