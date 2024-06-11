package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));
        
        assertTrue(NodeUtil.isForIn(node));
        
        node.removeChild(node.getFirstChild());
        assertFalse(NodeUtil.isForIn(node));
    }
    
    @Test
    public void testIsStatement() {
        Node node = new Node(Token.SCRIPT);
        assertTrue(NodeUtil.isStatement(node));
        
        node = new Node(Token.BLOCK);
        assertTrue(NodeUtil.isStatement(node));
        
        node = new Node(Token.LABEL);
        assertTrue(NodeUtil.isStatement(node));
        
        node = new Node(Token.NAME);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        node.setParent(parent);
        assertFalse(NodeUtil.isStatement(node));
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        node.setParent(parent);
        
        assertTrue(NodeUtil.isFunctionDeclaration(node));
        
        parent = new Node(Token.BLOCK);
        parent.addChildToBack(node);
        node.setParent(parent);
        
        assertTrue(NodeUtil.isFunctionDeclaration(node));
        
        parent = new Node(Token.NAME);
        parent.addChildToBack(node);
        node.setParent(parent);
        
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        node.setParent(parent);
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
        
        parent = new Node(Token.FUNCTION);
        parent.addChildToBack(new Node(Token.SCRIPT));
        parent.getFirstChild().addChildToBack(node);
        node.setParent(parent);
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
        
        parent = new Node(Token.BLOCK);
        parent.addChildToBack(new Node(Token.SCRIPT));
        parent.getFirstChild().addChildToBack(node);
        node.setParent(parent);
        
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}