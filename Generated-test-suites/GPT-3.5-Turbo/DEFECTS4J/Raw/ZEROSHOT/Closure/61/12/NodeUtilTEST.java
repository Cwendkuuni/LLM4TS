import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR, new Node(), new Node(), new Node());
        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsForIn_NotForIn() {
        Node node = new Node(Token.FUNCTION, new Node(), new Node(), new Node());
        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node parent = new Node(Token.SCRIPT, new Node(), new Node(), new Node());
        Node node = new Node(Token.FUNCTION, parent, new Node(), new Node());
        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsStatement_NotStatement() {
        Node parent = new Node(Token.EXPR_RESULT, new Node(), new Node(), new Node());
        Node node = new Node(Token.FUNCTION, parent, new Node(), new Node());
        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node parent = new Node(Token.SCRIPT, new Node(), new Node(), new Node());
        Node node = new Node(Token.FUNCTION, parent, new Node(), new Node());
        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsFunctionDeclaration_NotFunctionDeclaration() {
        Node parent = new Node(Token.EXPR_RESULT, new Node(), new Node(), new Node());
        Node node = new Node(Token.FUNCTION, parent, new Node(), new Node());
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node script = new Node(Token.SCRIPT, new Node(), new Node(), new Node());
        Node function = new Node(Token.FUNCTION, script, new Node(), new Node());
        Node node = new Node(Token.FUNCTION, function, new Node(), new Node());
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_NotHoistedFunctionDeclaration() {
        Node parent = new Node(Token.SCRIPT, new Node(), new Node(), new Node());
        Node node = new Node(Token.FUNCTION, parent, new Node(), new Node());
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}