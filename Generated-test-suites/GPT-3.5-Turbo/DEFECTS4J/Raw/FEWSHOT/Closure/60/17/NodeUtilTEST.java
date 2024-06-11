package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NodeUtilTest {
    
    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertEquals(true, NodeUtil.isForIn(node));
        
        node = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertEquals(false, NodeUtil.isForIn(node));
    }
    
    @Test
    public void testIsStatement() {
        Node node = new Node(Token.SCRIPT, new Node(Token.EMPTY));
        assertEquals(true, NodeUtil.isStatement(node));
        
        node = new Node(Token.BLOCK, new Node(Token.EMPTY));
        assertEquals(true, NodeUtil.isStatement(node));
        
        node = new Node(Token.LABEL, new Node(Token.EMPTY));
        assertEquals(true, NodeUtil.isStatement(node));
        
        node = new Node(Token.EMPTY);
        Node parent = new Node(Token.FUNCTION, node);
        node.setParent(parent);
        parent.setParent(new Node(Token.SCRIPT));
        assertEquals(false, NodeUtil.isStatement(node));
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        assertEquals(false, NodeUtil.isFunctionDeclaration(node));
        
        node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT, node);
        node.setParent(parent);
        assertEquals(true, NodeUtil.isFunctionDeclaration(node));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        assertEquals(false, NodeUtil.isHoistedFunctionDeclaration(node));
        
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(new Node(Token.FUNCTION));
        Node grandParent = new Node(Token.FUNCTION, parent);
        parent.setParent(grandParent);
        assertEquals(true, NodeUtil.isHoistedFunctionDeclaration(parent.getFirstChild()));
    }
}