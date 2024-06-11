import static org.junit.Assert.*;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        // Test when node is a FOR-IN loop
        Node node1 = new Node(Token.FOR);
        Node node2 = new Node(Token.FOR);
        node2.addChildToBack(new Node(Token.NAME));
        node2.addChildToBack(new Node(Token.NAME));
        node2.addChildToBack(new Node(Token.NAME));
        
        assertTrue(NodeUtil.isForIn(node1));
        assertTrue(NodeUtil.isForIn(node2));
        
        // Test when node is not a FOR-IN loop
        Node node3 = new Node(Token.FUNCTION);
        node3.addChildToBack(new Node(Token.NAME));
        node3.addChildToBack(new Node(Token.NAME));
        node3.addChildToBack(new Node(Token.NAME));
        
        assertFalse(NodeUtil.isForIn(node3));
    }

    @Test
    public void testIsStatement() {
        // Test when node is used as a statement
        Node scriptNode1 = new Node(Token.SCRIPT);
        Node blockNode1 = new Node(Token.BLOCK);
        Node labelNode1 = new Node(Token.LABEL);
        
        Node node1 = new Node();
        node1.setParent(scriptNode1);
        
        Node node2 = new Node();
        node2.setParent(blockNode1);
        
        Node node3 = new Node();
        node3.setParent(labelNode1);
        
        assertTrue(NodeUtil.isStatement(node1));
        assertTrue(NodeUtil.isStatement(node2));
        assertTrue(NodeUtil.isStatement(node3));
        
        // Test when node is not used as a statement
        Node scriptNode2 = new Node(Token.SCRIPT);
        Node blockNode2 = new Node(Token.BLOCK);
        
        Node node4 = new Node();
        node4.setParent(scriptNode2);
        node4.getParent().setType(Token.SCRIPT);
        
        Node node5 = new Node();
        node5.setParent(blockNode2);
        node5.getParent().setType(Token.BLOCK);
        
        assertFalse(NodeUtil.isStatement(node4));
        assertFalse(NodeUtil.isStatement(node5));
    }

    @Test
    public void testIsFunctionDeclaration() {
        // Test when node is a function declaration
        Node functionNode1 = new Node(Token.FUNCTION);
        Node scriptNode1 = new Node(Token.SCRIPT);
        Node blockNode1 = new Node(Token.BLOCK);
        
        Node node1 = new Node();
        node1.setParent(functionNode1);
        
        Node node2 = new Node();
        node2.setParent(scriptNode1);
        
        Node node3 = new Node();
        node3.setParent(blockNode1);
        
        assertTrue(NodeUtil.isFunctionDeclaration(node1));
        assertTrue(NodeUtil.isFunctionDeclaration(node2));
        assertTrue(NodeUtil.isFunctionDeclaration(node3));
        
        // Test when node is not a function declaration
        Node functionNode2 = new Node(Token.FUNCTION);
        Node scriptNode2 = new Node(Token.SCRIPT);
        Node blockNode2 = new Node(Token.BLOCK);
        
        Node node4 = new Node();
        node4.setParent(functionNode2);
        node4.getParent().setType(Token.SCRIPT);
        
        Node node5 = new Node();
        node5.setParent(blockNode2);
        node5.getParent().setType(Token.BLOCK);
        
        assertFalse(NodeUtil.isFunctionDeclaration(node4));
        assertFalse(NodeUtil.isFunctionDeclaration(node5));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        // Test when node is a hoisted function declaration
        Node functionNode1 = new Node(Token.FUNCTION);
        Node scriptNode1 = new Node(Token.SCRIPT);
        Node functionNode2 = new Node(Token.FUNCTION);
        Node blockNode1 = new Node(Token.BLOCK);
        Node functionNode3 = new Node(Token.FUNCTION);
        
        Node node1 = new Node();
        node1.setParent(scriptNode1);
        node1.getParent().setType(Token.SCRIPT);
        
        Node node2 = new Node();
        node2.setParent(functionNode2);
        node2.getParent().getParent().setType(Token.FUNCTION);
        
        Node node3 = new Node();
        node3.setParent(functionNode3);
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node2));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node3));
        
        // Test when node is not a hoisted function declaration
        Node functionNode4 = new Node(Token.FUNCTION);
        Node scriptNode2 = new Node(Token.SCRIPT);
        Node blockNode2 = new Node(Token.BLOCK);
        Node functionNode5 = new Node(Token.FUNCTION);
        
        Node node4 = new Node();
        node4.setParent(functionNode4);
        
        Node node5 = new Node();
        node5.setParent(blockNode2);
        node5.getParent().setType(Token.BLOCK);
        
        Node node6 = new Node();
        node6.setParent(functionNode5);
        node6.getParent().getParent().setType(Token.SCRIPT);
        
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node4));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node5));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node6));
    }
}