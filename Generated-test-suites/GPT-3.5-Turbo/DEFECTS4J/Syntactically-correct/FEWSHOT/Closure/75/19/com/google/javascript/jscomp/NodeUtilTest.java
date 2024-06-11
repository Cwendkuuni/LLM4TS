package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
    
    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR, new Node(), new Node(), new Node());
        assertTrue(NodeUtil.isForIn(node));
        
        node = new Node(Token.FOR, new Node(), new Node());
        assertFalse(NodeUtil.isForIn(node));
    }
    
    @Test
    public void testIsStatement() {
        Node parent = new Node(Token.SCRIPT);
        Node node = new Node(Token.ADD);
        node.setParent(parent);
        assertTrue(NodeUtil.isStatement(node));
        
        parent = new Node(Token.BLOCK);
        node.setParent(parent);
        assertTrue(NodeUtil.isStatement(node));
        
        parent = new Node(Token.LABEL);
        node.setParent(parent);
        assertTrue(NodeUtil.isStatement(node));
        
        parent = new Node(Token.IF);
        node.setParent(parent);
        assertFalse(NodeUtil.isStatement(node));
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        Node parent = new Node(Token.FUNCTION);
        Node node = new Node(Token.NAME);
        node.setParent(parent);
        assertTrue(NodeUtil.isFunctionDeclaration(node));
        
        parent = new Node(Token.IF);
        node.setParent(parent);
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node grandParent = new Node(Token.SCRIPT);
        Node parent = new Node(Token.FUNCTION);
        parent.setParent(grandParent);
        Node node = new Node(Token.NAME);
        node.setParent(parent);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
        
        parent = new Node(Token.IF);
        parent.setParent(grandParent);
        node.setParent(parent);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}