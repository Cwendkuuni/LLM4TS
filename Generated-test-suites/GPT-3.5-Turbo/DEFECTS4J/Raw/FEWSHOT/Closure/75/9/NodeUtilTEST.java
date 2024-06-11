package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
    
    @Test
    public void testIsForIn() {
        Node n1 = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(n1));
        
        Node n2 = new Node(Token.WHILE, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertFalse(NodeUtil.isForIn(n2));
    }
    
    @Test
    public void testIsStatement() {
        Node n1 = new Node(Token.SCRIPT, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertTrue(NodeUtil.isStatement(n1));
        
        Node n2 = new Node(Token.IF, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertFalse(NodeUtil.isStatement(n2));
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        Node n1 = new Node(Token.FUNCTION, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertTrue(NodeUtil.isFunctionDeclaration(n1));
        
        Node n2 = new Node(Token.VAR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertFalse(NodeUtil.isFunctionDeclaration(n2));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node n1 = new Node(Token.FUNCTION, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        Node parent1 = new Node(Token.SCRIPT, n1, new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(parent1));
        
        Node n2 = new Node(Token.FUNCTION, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        Node parent2 = new Node(Token.BLOCK, n2, new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(parent2));
    }
}