package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node node1 = new Node(Token.FOR, new Node(Token.VAR), new Node(Token.TRUE));
        Node node2 = new Node(Token.FOR, new Node(Token.VAR), new Node(Token.FALSE));
        
        assertTrue(NodeUtil.isForIn(node1));
        assertFalse(NodeUtil.isForIn(node2));
    }

    @Test
    public void testIsStatement() {
        Node node1 = new Node(Token.SCRIPT);
        Node node2 = new Node(Token.BLOCK);
        Node node3 = new Node(Token.LABEL);
        Node node4 = new Node(Token.IF);
        
        assertTrue(NodeUtil.isStatement(node1));
        assertTrue(NodeUtil.isStatement(node2));
        assertTrue(NodeUtil.isStatement(node3));
        assertFalse(NodeUtil.isStatement(node4));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION);
        Node node2 = new Node(Token.VAR);
        
        assertTrue(NodeUtil.isFunctionDeclaration(node1));
        assertFalse(NodeUtil.isFunctionDeclaration(node2));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION);
        Node node2 = new Node(Token.IF);
        Node parent1 = new Node(Token.SCRIPT, node1);
        Node parent2 = new Node(Token.FUNCTION, node1);
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(parent1));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(parent2));
    }
}