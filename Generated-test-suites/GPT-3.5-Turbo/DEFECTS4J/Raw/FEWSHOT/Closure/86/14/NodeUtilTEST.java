package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(node));
        
        node = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertFalse(NodeUtil.isForIn(node));
    }
    
    @Test
    public void testIsStatement() {
        Node parent = new Node(Token.SCRIPT, new Node(Token.EMPTY), new Node(Token.EMPTY));
        Node node = new Node(Token.STRING);
        node.setParent(parent);
        assertTrue(NodeUtil.isStatement(node));
        
        parent = new Node(Token.BLOCK, new Node(Token.EMPTY), new Node(Token.EMPTY));
        node = new Node(Token.STRING);
        node.setParent(parent);
        assertTrue(NodeUtil.isStatement(node));
        
        parent = new Node(Token.LABEL, new Node(Token.EMPTY), new Node(Token.EMPTY));
        node = new Node(Token.STRING);
        node.setParent(parent);
        assertTrue(NodeUtil.isStatement(node));
        
        parent = new Node(Token.FUNCTION, new Node(Token.EMPTY), new Node(Token.EMPTY));
        node = new Node(Token.STRING);
        node.setParent(parent);
        assertFalse(NodeUtil.isStatement(node));
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION, new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertTrue(NodeUtil.isFunctionDeclaration(node));
        
        node = new Node(Token.STRING);
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node parent = new Node(Token.SCRIPT, new Node(Token.EMPTY), new Node(Token.EMPTY));
        Node node = new Node(Token.FUNCTION, new Node(Token.EMPTY), new Node(Token.EMPTY));
        node.setParent(parent);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
        
        parent = new Node(Token.FUNCTION, new Node(Token.SCRIPT), new Node(Token.EMPTY));
        node = new Node(Token.FUNCTION, new Node(Token.EMPTY), new Node(Token.EMPTY));
        node.setParent(parent);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
        
        node = new Node(Token.STRING);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}