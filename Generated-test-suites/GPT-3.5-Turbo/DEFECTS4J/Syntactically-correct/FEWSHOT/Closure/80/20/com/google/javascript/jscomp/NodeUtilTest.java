package com.google.javascript.jscomp.jsonml;

import org.junit.Test;

import static org.junit.Assert.*;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        Node child1 = new Node(Token.EMPTY);
        Node child2 = new Node(Token.EMPTY);
        Node child3 = new Node(Token.EMPTY);
        
        node.addChildToBack(child1);
        node.addChildToBack(child2);
        node.addChildToBack(child3);
        
        assertTrue(NodeUtil.isForIn(node));
    }
    
    @Test
    public void testIsNotForIn() {
        Node node = new Node(Token.WHILE);
        Node child1 = new Node(Token.EMPTY);
        Node child2 = new Node(Token.EMPTY);
        
        node.addChildToBack(child1);
        node.addChildToBack(child2);
        
        assertFalse(NodeUtil.isForIn(node));
    }
    
    @Test
    public void testIsStatement() {
        Node node = new Node(Token.EMPTY);
        Node parent = new Node(Token.SCRIPT);
        
        parent.addChildToBack(node);
        
        assertTrue(NodeUtil.isStatement(node));
    }
    
    @Test
    public void testIsNotStatement() {
        Node node = new Node(Token.EMPTY);
        Node parent = new Node(Token.EXPR_RESULT);
        
        parent.addChildToBack(node);
        
        assertFalse(NodeUtil.isStatement(node));
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node script = new Node(Token.SCRIPT);
        Node parent = new Node(Token.EMPTY);
        
        parent.addChildToBack(node);
        script.addChildToFront(parent);
        
        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }
    
    @Test
    public void testIsNotFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node script = new Node(Token.SCRIPT);
        Node parent = new Node(Token.EXPR_RESULT);
        
        parent.addChildToBack(node);
        script.addChildToFront(parent);
        
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node script = new Node(Token.SCRIPT);
        Node function = new Node(Token.FUNCTION);
        
        script.addChildToFront(node);
        node.addChildToBack(function);
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }
    
    @Test
    public void testIsNotHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node script = new Node(Token.SCRIPT);
        Node function = new Node(Token.EMPTY);
        
        script.addChildToFront(node);
        node.addChildToBack(function);
        
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}