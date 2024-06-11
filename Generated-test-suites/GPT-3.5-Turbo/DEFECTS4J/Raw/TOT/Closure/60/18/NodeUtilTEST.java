import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn_WithForInLoop_ReturnsTrue() {
        Node n = new Node(Token.FOR);
        n.addChildToBack(new Node(Token.NAME));
        n.addChildToBack(new Node(Token.NAME));
        n.addChildToBack(new Node(Token.NAME));

        assertTrue(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsForIn_WithForLoop_ReturnsFalse() {
        Node n = new Node(Token.FOR);
        n.addChildToBack(new Node(Token.NAME));
        n.addChildToBack(new Node(Token.NAME));

        assertFalse(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsStatement_WithStatementParent_ReturnsTrue() {
        Node parent = new Node(Token.SCRIPT);
        Node n = new Node(Token.NAME, parent);

        assertTrue(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsStatement_WithNonStatementParent_ReturnsFalse() {
        Node parent = new Node(Token.FUNCTION);
        Node n = new Node(Token.NAME, parent);

        assertFalse(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsFunctionDeclaration_WithFunctionDeclaration_ReturnsTrue() {
        Node parent = new Node(Token.SCRIPT);
        Node n = new Node(Token.FUNCTION, parent);

        assertTrue(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsFunctionDeclaration_WithNonFunctionDeclaration_ReturnsFalse() {
        Node parent = new Node(Token.BLOCK);
        Node n = new Node(Token.FUNCTION, parent);

        assertFalse(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_WithHoistedFunctionDeclaration_ReturnsTrue() {
        Node parent = new Node(Token.SCRIPT);
        Node grandParent = new Node(Token.SCRIPT, parent);
        Node n = new Node(Token.FUNCTION, grandParent);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_WithNonHoistedFunctionDeclaration_ReturnsFalse() {
        Node parent = new Node(Token.FUNCTION);
        Node n = new Node(Token.FUNCTION, parent);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
    }
}