import org.junit.Test;

import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node n = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsStatement() {
        Node n = new Node(Token.EXPR_RESULT);
        assertTrue(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        assertTrue(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node parent = new Node(Token.SCRIPT);
        Node grandParent = new Node(Token.FUNCTION);
        Node n = new Node(Token.FUNCTION, parent, grandParent);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
    }

    @Test(expected = NullPointerException.class)
    public void testIsForIn_NullNode() {
        Node n = null;
        NodeUtil.isForIn(n);
    }

    @Test
    public void testIsStatement_ParentNull() {
        Node n = new Node(Token.EXPR_RESULT);
        n.setParent(null);
        assertFalse(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsStatement_OtherParentType() {
        Node n = new Node(Token.EXPR_RESULT);
        Node parent = new Node(Token.IF);
        n.setParent(parent);
        assertFalse(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsFunctionDeclaration_NonFunctionNode() {
        Node n = new Node(Token.CONST);
        assertFalse(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_ParentNotScriptOrFunction() {
        Node parent = new Node(Token.IF);
        Node grandParent = new Node(Token.SWITCH);
        Node n = new Node(Token.FUNCTION, parent, grandParent);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
    }
}