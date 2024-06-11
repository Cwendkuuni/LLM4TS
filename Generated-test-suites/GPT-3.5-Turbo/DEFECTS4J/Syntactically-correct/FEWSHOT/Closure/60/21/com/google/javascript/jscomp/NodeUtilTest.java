import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        // Test when node is a FOR-IN loop
        Node node = new Node(Token.newToken(Token.FOR), 3);
        boolean expected = true;
        boolean result = NodeUtil.isForIn(node);
        assertEquals(expected, result);

        // Test when node is not a FOR-IN loop
        node = new Node(Token.newToken(Token.EXPR_RESULT), 1);
        expected = false;
        result = NodeUtil.isForIn(node);
        assertEquals(expected, result);
    }

    @Test
    public void testIsStatement() {
        // Test when node is used as a statement
        Node node = new Node(Token.newToken(Token.SCRIPT), 2);
        boolean expected = true;
        boolean result = NodeUtil.isStatement(node);
        assertEquals(expected, result);

        // Test when node is not used as a statement
        node = new Node(Token.newToken(Token.EXPR_RESULT), 1);
        expected = false;
        result = NodeUtil.isStatement(node);
        assertEquals(expected, result);
    }

    @Test
    public void testIsFunctionDeclaration() {
        // Test when node is a function declaration
        Node node = new Node(Token.newToken(Token.FUNCTION), 1);
        Node parent = new Node(Token.newToken(Token.SCRIPT), 2);
        node.setParent(parent);
        boolean expected = true;
        boolean result = NodeUtil.isFunctionDeclaration(node);
        assertEquals(expected, result);

        // Test when node is not a function declaration
        node = new Node(Token.newToken(Token.EXPR_RESULT), 1);
        expected = false;
        result = NodeUtil.isFunctionDeclaration(node);
        assertEquals(expected, result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        // Test when node is a hoisted function declaration
        Node node = new Node(Token.newToken(Token.FUNCTION), 1);
        Node parent = new Node(Token.newToken(Token.SCRIPT), 2);
        node.setParent(parent);
        boolean expected = true;
        boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
        assertEquals(expected, result);

        // Test when node is not a hoisted function declaration
        node = new Node(Token.newToken(Token.EXPR_RESULT), 1);
        expected = false;
        result = NodeUtil.isHoistedFunctionDeclaration(node);
        assertEquals(expected, result);
    }
}