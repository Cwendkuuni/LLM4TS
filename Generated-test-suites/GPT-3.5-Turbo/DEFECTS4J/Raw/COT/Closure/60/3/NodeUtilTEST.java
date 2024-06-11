
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR, new Node(), new Node(), new Node());
        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsForIn_returnsFalse() {
        Node node = new Node(Token.FOR, new Node(), new Node());
        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.SCRIPT);
        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsStatement_returnsFalse() {
        Node node = new Node(Token.EXPR_RESULT);
        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsFunctionDeclaration_returnsFalse() {
        Node node = new Node(Token.FUNCTION, new Node(Token.EXPR_RESULT));
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node functionNode = new Node(Token.FUNCTION);
        Node scriptNode = new Node(Token.SCRIPT, functionNode);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(scriptNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_returnsFalse() {
        Node functionNode = new Node(Token.FUNCTION);
        Node parentFunctionNode = new Node(Token.FUNCTION, functionNode);
        Node scriptNode = new Node(Token.SCRIPT, parentFunctionNode);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(scriptNode));
    }
}

