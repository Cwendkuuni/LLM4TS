package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR, Token.FOR);
        Node child1 = new Node(Token.NAME, Token.NAME);
        Node child2 = new Node(Token.EMPTY, Token.EMPTY);
        Node child3 = new Node(Token.EMPTY, Token.EMPTY);
        node.addChildToBack(child1);
        node.addChildToBack(child2);
        node.addChildToBack(child3);
        
        assertEquals(true, NodeUtil.isForIn(node));
    }
    
    @Test
    public void testIsStatement() {
        Node node = new Node(Token.SCRIPT, Token.SCRIPT);
        
        assertEquals(true, NodeUtil.isStatement(node));
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION, Token.FUNCTION);
        
        assertEquals(true, NodeUtil.isFunctionDeclaration(node));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION, Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT, Token.SCRIPT);
        node.setParent(parent);
        
        assertEquals(true, NodeUtil.isHoistedFunctionDeclaration(node));
    }
}