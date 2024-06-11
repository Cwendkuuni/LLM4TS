import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node node1 = new Node(Token.FOR);
        node1.addChildToFront(new Node(Token.EMPTY));
        node1.addChildToBack(new Node(Token.EMPTY));
        node1.addChildToBack(new Node(Token.EMPTY));
        
        Node node2 = new Node(Token.BLOCK);
        node2.addChildToBack(new Node(Token.EMPTY));
        node2.addChildToBack(new Node(Token.EMPTY));
        node2.addChildToBack(new Node(Token.EMPTY));
        
        Node node3 = new Node(Token.LABEL);
        node3.addChildToBack(new Node(Token.EMPTY));
        node3.addChildToBack(new Node(Token.EMPTY));
        node3.addChildToBack(new Node(Token.EMPTY));
        
        Node node4 = new Node(Token.SCRIPT);
        node4.addChildToBack(new Node(Token.EMPTY));
        node4.addChildToBack(new Node(Token.EMPTY));
        node4.addChildToBack(new Node(Token.EMPTY));
        
        assertFalse(NodeUtil.isForIn(new Node(Token.EMPTY)));
        assertFalse(NodeUtil.isForIn(node1));
        assertFalse(NodeUtil.isForIn(node2));
        assertFalse(NodeUtil.isForIn(node3));
        assertFalse(NodeUtil.isForIn(node4));
        
        Node node5 = new Node(Token.FOR);
        node5.addChildToFront(new Node(Token.EMPTY));
        node5.addChildToBack(new Node(Token.EMPTY));
        node5.addChildToBack(new Node(Token.EMPTY));
        node5.addChildToBack(new Node(Token.EMPTY));
        
        assertTrue(NodeUtil.isForIn(node5));
    }

    @Test
    public void testIsStatement() {
        Node node1 = new Node(Token.SCRIPT);
        node1.addChildToBack(new Node(Token.EMPTY));
        
        Node node2 = new Node(Token.BLOCK);
        node2.addChildToBack(new Node(Token.EMPTY));
        
        Node node3 = new Node(Token.LABEL);
        node3.addChildToBack(new Node(Token.EMPTY));
        
        Node node4 = new Node(Token.EMPTY);
        
        assertFalse(NodeUtil.isStatement(node4));
        assertTrue(NodeUtil.isStatement(node1));
        assertTrue(NodeUtil.isStatement(node2));
        assertTrue(NodeUtil.isStatement(node3));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isFunctionDeclaration(node1));
        
        Node node2 = new Node(Token.FUNCTION);
        node2.addChildToBack(new Node(Token.EMPTY));
        assertTrue(NodeUtil.isFunctionDeclaration(node2));
        
        Node node3 = new Node(Token.SCRIPT);
        node3.addChildToBack(new Node(Token.FUNCTION));
        assertFalse(NodeUtil.isFunctionDeclaration(node3));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node functionNode1 = new Node(Token.FUNCTION);
        scriptNode.addChildToBack(functionNode1);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode1));
        
        Node functionNode2 = new Node(Token.FUNCTION);
        Node functionNode3 = new Node(Token.FUNCTION);
        Node parentFunctionNode = new Node(Token.FUNCTION);
        parentFunctionNode.addChildToBack(functionNode3);
        scriptNode.addChildToBack(functionNode2);
        scriptNode.addChildToBack(parentFunctionNode);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode2));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode3));
    }
}