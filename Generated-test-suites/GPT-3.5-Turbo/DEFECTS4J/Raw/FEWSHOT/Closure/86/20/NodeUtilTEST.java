package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.*;

import org.junit.Test;

public class NodeUtilTest {
    
    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR, new Node(Token.NAME), new Node(Token.NAME), new Node(Token.NAME));
        assertTrue(NodeUtil.isForIn(node));
        
        node = new Node(Token.FOR, new Node(Token.NAME), new Node(Token.NAME));
        assertFalse(NodeUtil.isForIn(node));
        
        node = new Node(Token.NAME);
        assertFalse(NodeUtil.isForIn(node));
    }
    
    @Test
    public void testIsStatement() {
        Node node = new Node(Token.SCRIPT, new Node(Token.NAME));
        assertTrue(NodeUtil.isStatement(node));
        
        node = new Node(Token.BLOCK, new Node(Token.NAME));
        assertTrue(NodeUtil.isStatement(node));
        
        node = new Node(Token.LABEL, new Node(Token.NAME));
        assertTrue(NodeUtil.isStatement(node));
        
        node = new Node(Token.NAME);
        assertFalse(NodeUtil.isStatement(node));
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION, new Node(Token.NAME));
        assertTrue(NodeUtil.isFunctionDeclaration(node));
        
        node = new Node(Token.NAME);
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node scriptParent = new Node(Token.SCRIPT);
        Node functionParent = new Node(Token.FUNCTION);
        
        Node node = new Node(Token.FUNCTION, scriptParent);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
        
        node = new Node(Token.FUNCTION, functionParent);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
        
        node = new Node(Token.NAME);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}