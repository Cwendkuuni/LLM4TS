package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node n1 = new Node(Token.FOR);
        n1.addChildToBack(new Node(Token.NAME));
        n1.addChildToBack(new Node(Token.IN));
        n1.addChildToBack(new Node(Token.NAME));
        
        Node n2 = new Node(Token.FOR);
        n2.addChildToBack(new Node(Token.NAME));
        n2.addChildToBack(new Node(Token.PLUS));
        n2.addChildToBack(new Node(Token.NAME));
        
        assertEquals(true, NodeUtil.isForIn(n1));
        assertEquals(false, NodeUtil.isForIn(n2));
    }
    
    @Test
    public void testIsStatement() {
        Node n1 = new Node(Token.SCRIPT);
        Node n2 = new Node(Token.BLOCK);
        Node n3 = new Node(Token.LABEL);
        Node n4 = new Node(Token.DOT);
        
        assertEquals(true, NodeUtil.isStatement(n1));
        assertEquals(true, NodeUtil.isStatement(n2));
        assertEquals(true, NodeUtil.isStatement(n3));
        assertEquals(false, NodeUtil.isStatement(n4));
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        Node n1 = new Node(Token.FUNCTION);
        Node n2 = new Node(Token.EXPR_RESULT);
        n2.addChildToBack(new Node(Token.FUNCTION));
        
        assertEquals(true, NodeUtil.isFunctionDeclaration(n1));
        assertEquals(false, NodeUtil.isFunctionDeclaration(n2));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node n1 = new Node(Token.FUNCTION);
        Node n2 = new Node(Token.NAME);
        Node n3 = new Node(Token.FUNCTION);
        Node n4 = new Node(Token.NAME);
        Node n5 = new Node(Token.SCRIPT);
        Node n6 = new Node(Token.FUNCTION);
        Node n7 = new Node(Token.NAME);
        
        n2.setParent(n1);
        n3.setParent(n5);
        n4.setParent(n3);
        n6.setParent(n7);
        
        assertEquals(true, NodeUtil.isHoistedFunctionDeclaration(n1));
        assertEquals(true, NodeUtil.isHoistedFunctionDeclaration(n3));
        assertEquals(false, NodeUtil.isHoistedFunctionDeclaration(n4));
        assertEquals(false, NodeUtil.isHoistedFunctionDeclaration(n5));
        assertEquals(false, NodeUtil.isHoistedFunctionDeclaration(n6));
        assertEquals(false, NodeUtil.isHoistedFunctionDeclaration(n7));
    }
}