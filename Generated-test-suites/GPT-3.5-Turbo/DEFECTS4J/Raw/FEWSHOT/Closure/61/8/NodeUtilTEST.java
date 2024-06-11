import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        // Test cases for isForIn method
        Node node1 = new Node(Token.FOR);
        node1.addChildToBack(new Node(Token.EMPTY));
        node1.addChildToBack(new Node(Token.EMPTY));
        node1.addChildToBack(new Node(Token.EMPTY));
        assertFalse(NodeUtil.isForIn(node1));

        Node node2 = new Node(Token.FOR);
        node2.addChildToBack(new Node(Token.EMPTY));
        node2.addChildToBack(new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(node2));
    }

    @Test
    public void testIsStatement() {
        // Test cases for isStatement method
        Node scriptNode = new Node(Token.SCRIPT);
        Node blockNode = new Node(Token.BLOCK);
        Node labelNode = new Node(Token.LABEL);

        Node node1 = new Node(Token.EMPTY);
        node1.getParent().addChildToFront(scriptNode);
        assertTrue(NodeUtil.isStatement(node1));

        Node node2 = new Node(Token.EMPTY);
        node2.getParent().addChildToFront(blockNode);
        assertTrue(NodeUtil.isStatement(node2));

        Node node3 = new Node(Token.EMPTY);
        node3.getParent().addChildToFront(labelNode);
        assertTrue(NodeUtil.isStatement(node3));

        Node node4 = new Node(Token.EMPTY);
        assertFalse(NodeUtil.isStatement(node4));
    }

    @Test
    public void testIsFunctionDeclaration() {
        // Test cases for isFunctionDeclaration method
        Node functionNode = new Node(Token.FUNCTION);

        Node node1 = new Node(Token.EMPTY);
        node1.getParent().addChildToFront(functionNode);
        assertTrue(NodeUtil.isFunctionDeclaration(node1));

        Node node2 = new Node(Token.EMPTY);
        assertFalse(NodeUtil.isFunctionDeclaration(node2));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        // Test cases for isHoistedFunctionDeclaration method
        Node scriptNode = new Node(Token.SCRIPT);
        Node functionNode = new Node(Token.FUNCTION);

        Node node1 = new Node(Token.EMPTY);
        node1.getParent().addChildToFront(scriptNode);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));

        Node node2 = new Node(Token.EMPTY);
        node2.getParent().addChildToFront(functionNode);
        node2.getParent().getParent().addChildToFront(new Node(Token.EMPTY));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node2));

        Node node3 = new Node(Token.EMPTY);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node3));
    }
}