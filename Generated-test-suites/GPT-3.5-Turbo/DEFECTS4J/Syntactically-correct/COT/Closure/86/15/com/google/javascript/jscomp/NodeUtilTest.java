
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.NAME));

        boolean result = NodeUtil.isForIn(node);

        assertFalse(result);
    }

    @Test
    public void testIsForIn_WithForInLoop() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.NAME));
        node.setIsForIn(true);

        boolean result = NodeUtil.isForIn(node);

        assertTrue(result);
    }

    @Test
    public void testIsStatement_WithScriptParent() {
        Node parent = new Node(Token.SCRIPT);
        Node node = new Node(Token.NAME);
        parent.addChildToBack(node);

        boolean result = NodeUtil.isStatement(node);

        assertTrue(result);
    }

    @Test
    public void testIsStatement_WithBlockParent() {
        Node parent = new Node(Token.BLOCK);
        Node node = new Node(Token.NAME);
        parent.addChildToBack(node);

        boolean result = NodeUtil.isStatement(node);

        assertTrue(result);
    }

    @Test
    public void testIsStatement_WithLabelParent() {
        Node parent = new Node(Token.LABEL);
        Node node = new Node(Token.NAME);
        parent.addChildToBack(node);

        boolean result = NodeUtil.isStatement(node);

        assertTrue(result);
    }

    @Test
    public void testIsStatement_WithFunctionParent() {
        Node parent = new Node(Token.FUNCTION);
        Node grandparent = new Node(Token.FUNCTION);
        Node node = new Node(Token.NAME);
        grandparent.addChildToBack(parent);
        parent.addChildToBack(node);

        boolean result = NodeUtil.isStatement(node);

        assertTrue(result);
    }

    @Test
    public void testIsStatement_WithOtherParent() {
        Node parent = new Node(Token.VAR);
        Node node = new Node(Token.NAME);
        parent.addChildToBack(node);

        boolean result = NodeUtil.isStatement(node);

        assertFalse(result);
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node grandparent = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        grandparent.addChildToBack(parent);
        parent.addChildToBack(node);

        boolean result = NodeUtil.isFunctionDeclaration(node);

        assertTrue(result);
    }

    @Test
    public void testIsFunctionDeclaration_WithNonStatementParent() {
        Node parent = new Node(Token.VAR);
        Node node = new Node(Token.FUNCTION);
        parent.addChildToBack(node);

        boolean result = NodeUtil.isFunctionDeclaration(node);

        assertFalse(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration_WithScriptParent() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);

        boolean result = NodeUtil.isHoistedFunctionDeclaration(node);

        assertTrue(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration_WithFunctionParent() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.FUNCTION);
        Node grandparent = new Node(Token.FUNCTION);
        grandparent.addChildToBack(parent);
        parent.addChildToBack(node);

        boolean result = NodeUtil.isHoistedFunctionDeclaration(node);

        assertTrue(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration_WithOtherParent() {
        Node parent = new Node(Token.SCRIPT);
        Node grandparent = new Node(Token.SCRIPT);
        grandparent.addChildToBack(parent);
        Node node = new Node(Token.FUNCTION);
        parent.addChildToBack(node);

        boolean result = NodeUtil.isHoistedFunctionDeclaration(node);

        assertFalse(result);
    }
}
