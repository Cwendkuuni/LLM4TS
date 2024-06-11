
import org.junit.Test;

import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        // Testing when node represents a FOR-IN loop
        Node node = new Node(Token.FOR);
        node.addChildToFront(new Node(Token.EMPTY));
        node.addChildToFront(new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(node));

        // Testing when node does not represent a FOR-IN loop
        node = new Node(Token.EMPTY);
        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        // Testing when node is used as a statement
        Node node = new Node(Token.SCRIPT);
        assertTrue(NodeUtil.isStatement(node));
        node = new Node(Token.BLOCK);
        assertTrue(NodeUtil.isStatement(node));
        node = new Node(Token.LABEL);
        assertTrue(NodeUtil.isStatement(node));

        // Testing when node is not used as a statement
        node = new Node(Token.SCRIPT);
        Node child = new Node(Token.EMPTY);
        node.addChildToFront(child);
        assertFalse(NodeUtil.isStatement(child));
    }

    @Test
    public void testIsFunctionDeclaration() {
        // Testing when node is a function declaration
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(node);
        assertTrue(NodeUtil.isFunctionDeclaration(node));

        // Testing when node is a function expression
        node = new Node(Token.FUNCTION);
        parent = new Node(Token.EMPTY);
        parent.addChildToFront(node);
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        // Testing when node is a hoisted function declaration
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(node);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

        // Testing when node is not a hoisted function declaration
        node = new Node(Token.FUNCTION);
        parent = new Node(Token.BLOCK);
        parent.addChildToFront(node);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}
