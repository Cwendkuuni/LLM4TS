import org.junit.Test;

import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        // test when node represents a FOR-IN loop
        Node node = NodeUtil.isForIn(Node.newString(Token.FOR, null));
        assertTrue(node);

        // test when node does not represent a FOR-IN loop
        Node node2 = NodeUtil.isForIn(Node.newString(Token.WHILE, null));
        assertFalse(node2);
    }

    @Test
    public void testIsStatement() {
        // test when node is used as a statement
        Node node = NodeUtil.isStatement(Node.newString(Token.SCRIPT, null));
        assertTrue(node);

        // test when node is not used as a statement
        Node node2 = NodeUtil.isStatement(Node.newString(Token.VAR, null));
        assertFalse(node2);
    }

    @Test
    public void testIsFunctionDeclaration() {
        // test when node is a function declaration
        Node node = NodeUtil.isFunctionDeclaration(Node.newString(Token.FUNCTION, null));
        assertTrue(node);

        // test when node is not a function declaration
        Node node2 = NodeUtil.isFunctionDeclaration(Node.newString(Token.VAR, null));
        assertFalse(node2);
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        // test when node is a hoisted function declaration
        Node node = NodeUtil.isHoistedFunctionDeclaration(Node.newString(Token.FUNCTION, null));
        assertTrue(node);

        // test when node is not a hoisted function declaration
        Node node2 = NodeUtil.isHoistedFunctionDeclaration(Node.newString(Token.VAR, null));
        assertFalse(node2);
    }
}