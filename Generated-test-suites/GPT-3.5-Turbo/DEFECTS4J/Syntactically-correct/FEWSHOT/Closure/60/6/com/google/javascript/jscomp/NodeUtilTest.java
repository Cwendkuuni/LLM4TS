package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
    
    @Test
    public void testIsForIn() {
        Node node1 = new Node(Token.FOR);
        Node node2 = new Node(Token.FOR);
        node2.addChildToBack(new Node(Token.EMPTY));
        node2.addChildToBack(new Node(Token.EMPTY));
        node2.addChildToBack(new Node(Token.EMPTY));
        
        assertFalse(NodeUtil.isForIn(node1));
        assertTrue(NodeUtil.isForIn(node2));
    }
    
    @Test
    public void testIsStatement() {
        Node parent1 = new Node(Token.SCRIPT);
        Node parent2 = new Node(Token.BLOCK);
        Node parent3 = new Node(Token.LABEL);
        Node parent4 = new Node(Token.IF);

        Node node1 = new Node(Token.EMPTY);
        node1.setParent(parent1);
        Node node2 = new Node(Token.EMPTY);
        node2.setParent(parent2);
        Node node3 = new Node(Token.EMPTY);
        node3.setParent(parent3);
        Node node4 = new Node(Token.EMPTY);
        node4.setParent(parent4);

        assertTrue(NodeUtil.isStatement(node1));
        assertTrue(NodeUtil.isStatement(node2));
        assertTrue(NodeUtil.isStatement(node3));
        assertFalse(NodeUtil.isStatement(node4));
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        Node parent1 = new Node(Token.SCRIPT);
        Node parent2 = new Node(Token.BLOCK);
        Node parent3 = new Node(Token.IF);
        
        Node node1 = new Node(Token.FUNCTION);
        node1.setParent(parent1);
        Node node2 = new Node(Token.FUNCTION);
        node2.setParent(parent2);
        Node node3 = new Node(Token.FUNCTION);
        node3.setParent(parent3);
        
        assertTrue(NodeUtil.isFunctionDeclaration(node1));
        assertTrue(NodeUtil.isFunctionDeclaration(node2));
        assertFalse(NodeUtil.isFunctionDeclaration(node3));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node parent1 = new Node(Token.SCRIPT);
        Node parent2 = new Node(Token.FUNCTION);
        Node parent3 = new Node(Token.IF);
        
        Node node1 = new Node(Token.FUNCTION);
        node1.setParent(parent1);
        Node node2 = new Node(Token.FUNCTION);
        node2.setParent(parent2);
        Node node3 = new Node(Token.FUNCTION);
        node3.setParent(parent3);
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node3));
    }
}