package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NodeUtilTest {
    
    @Test
    public void testIsForIn_True() {
        Node node = new Node(Token.FOR, null);
        node.addChildToFront(new Node(Token.EMPTY)); // Child 1
        node.addChildToFront(new Node(Token.EMPTY)); // Child 2
        node.addChildToFront(new Node(Token.EMPTY)); // Child 3
        
        assertEquals(true, NodeUtil.isForIn(node));
    }
    
    @Test
    public void testIsForIn_False() {
        Node node = new Node(Token.FOR, null);
        node.addChildToFront(new Node(Token.EMPTY)); // Child 1
        node.addChildToFront(new Node(Token.EMPTY)); // Child 2
        
        assertEquals(false, NodeUtil.isForIn(node));
    }
    
    @Test
    public void testIsStatement_True() {
        Node parent = new Node(Token.SCRIPT, null);
        Node node = new Node(Token.EMPTY, null);
        parent.addChildToFront(node);
        
        assertEquals(true, NodeUtil.isStatement(node));
    }
    
    @Test
    public void testIsStatement_False() {
        Node parent = new Node(Token.FUNCTION, null);
        Node node = new Node(Token.EMPTY, null);
        parent.addChildToFront(node);
        
        assertEquals(false, NodeUtil.isStatement(node));
    }
    
    @Test
    public void testIsFunctionDeclaration_True() {
        Node node = new Node(Token.FUNCTION, null);
        Node parent = new Node(Token.SCRIPT, null);
        parent.addChildToFront(node);
        
        assertEquals(true, NodeUtil.isFunctionDeclaration(node));
    }
    
    @Test
    public void testIsFunctionDeclaration_False() {
        Node node = new Node(Token.FUNCTION, null);
        Node parent = new Node(Token.EXPR_RESULT, null);
        parent.addChildToFront(node);
        
        assertEquals(false, NodeUtil.isFunctionDeclaration(node));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration_True() {
        Node function = new Node(Token.FUNCTION, null);
        Node script = new Node(Token.SCRIPT, null);
        Node parent = new Node(Token.SCRIPT, null);
        parent.addChildToFront(script);
        script.addChildToFront(function);
        
        assertEquals(true, NodeUtil.isHoistedFunctionDeclaration(function));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration_False() {
        Node function = new Node(Token.FUNCTION, null);
        Node parent = new Node(Token.SCRIPT, null);
        parent.addChildToFront(function);
        
        assertEquals(false, NodeUtil.isHoistedFunctionDeclaration(function));
    }
    
}