package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node1 = new Node(Token.FOR);
        Node node2 = new Node(Token.FOR);
        Node node3 = new Node(Token.WHILE);
        Node node4 = new Node(Token.FORIN);
        
        node1.addChildToBack(new Node(Token.EMPTY));
        node1.addChildToBack(new Node(Token.EMPTY));
        node1.addChildToBack(new Node(Token.EMPTY));
        
        node2.addChildToBack(new Node(Token.EMPTY));
        node2.addChildToBack(new Node(Token.EMPTY));
        
        node3.addChildToBack(new Node(Token.EMPTY));
        node3.addChildToBack(new Node(Token.EMPTY));
        
        node4.addChildToBack(new Node(Token.EMPTY));
        node4.addChildToBack(new Node(Token.EMPTY));
        node4.addChildToBack(new Node(Token.EMPTY));
        
        assertTrue(NodeUtil.isForIn(node4));
        assertFalse(NodeUtil.isForIn(node1));
        assertFalse(NodeUtil.isForIn(node2));
        assertFalse(NodeUtil.isForIn(node3));
    }

    @Test
    public void testIsStatement() {
        Node node1 = new Node(Token.SCRIPT);
        Node node2 = new Node(Token.BLOCK);
        Node node3 = new Node(Token.LABEL);
        Node node4 = new Node(Token.FOR);
        
        assertTrue(NodeUtil.isStatement(node1));
        assertTrue(NodeUtil.isStatement(node2));
        assertTrue(NodeUtil.isStatement(node3));
        assertFalse(NodeUtil.isStatement(node4));
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION);
        Node node2 = new Node(Token.FUNCTION);
        Node node3 = new Node(Token.SCRIPT);
        Node node4 = new Node(Token.FUNCTION);

        assertTrue(NodeUtil.isFunctionDeclaration(node1));
        assertTrue(NodeUtil.isFunctionDeclaration(node2));
        assertFalse(NodeUtil.isFunctionDeclaration(node3));
        assertFalse(NodeUtil.isFunctionDeclaration(node4));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION);
        Node node2 = new Node(Token.FUNCTION);
        Node node3 = new Node(Token.SCRIPT);
        Node node4 = new Node(Token.FUNCTION);
        Node parent1 = new Node(Token.SCRIPT);
        Node parent2 = new Node(Token.FUNCTION);

        node1.addChildToBack(parent1);
        node2.addChildToBack(parent2);
        
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node1));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node3));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node4));
    }
}