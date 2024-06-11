import org.junit.Test;

import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        // typical use case
        Node node1 = Node.newString(Token.FOR);
        Node node2 = Node.newString(Token.IN);
        Node node3 = Node.newString(Token.SOMETHING_ELSE);
        node1.addChildToFront(node2);
        node1.addChildToFront(node3);
        assertTrue(NodeUtil.isForIn(node1));
        
        // edge case
        Node node4 = Node.newString(Token.FOR);
        Node node5 = Node.newString(Token.IN);
        node4.addChildToFront(node5);
        assertFalse(NodeUtil.isForIn(node4));
        
        // error scenario
        assertFalse(NodeUtil.isForIn(null));
    }

    @Test
    public void testIsStatement() {
        // typical use case
        Node node1 = Node.newString(Token.SCRIPT);
        Node node2 = Node.newString(Token.FOR);
        node1.addChildToFront(node2);
        assertTrue(NodeUtil.isStatement(node2));

        // edge case
        Node node3 = Node.newString(Token.BLOCK);
        Node node4 = Node.newString(Token.FOR);
        node3.addChildToFront(node4);
        assertFalse(NodeUtil.isStatement(node4));

        // error scenario
        assertFalse(NodeUtil.isStatement(null));
    }

    @Test
    public void testIsFunctionDeclaration() {
        // typical use case
        Node node1 = Node.newString(Token.FUNCTION);
        node1.setType(Token.FUNCTION);
        assertTrue(NodeUtil.isFunctionDeclaration(node1));

        // edge case
        Node node2 = Node.newString(Token.ARGUMENTS);
        assertTrue(NodeUtil.isFunctionDeclaration(node2));

        // error scenario
        assertFalse(NodeUtil.isFunctionDeclaration(null));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        // typical use case
        Node node1 = Node.newString(Token.FUNCTION);
        node1.setType(Token.FUNCTION);
        Node node2 = Node.newString(Token.SCRIPT);
        node2.addChildToFront(node1);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));

        // edge case
        Node node3 = Node.newString(Token.ARGUMENTS);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node3));

        // error scenario
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(null));
    }
}