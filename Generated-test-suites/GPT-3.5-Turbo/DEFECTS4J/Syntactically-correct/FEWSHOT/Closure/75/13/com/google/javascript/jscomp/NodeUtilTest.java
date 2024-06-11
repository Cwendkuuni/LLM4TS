package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node node1 = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        Node node2 = new Node(Token.FUNCTION, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        
        assertTrue(NodeUtil.isForIn(node1));
        assertFalse(NodeUtil.isForIn(node2));
    }

    @Test
    public void testIsStatement() {
        Node node1 = new Node(Token.SCRIPT, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        Node node2 = new Node(Token.BLOCK, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        Node node3 = new Node(Token.LABEL, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        Node node4 = new Node(Token.FUNCTION, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));

        assertTrue(NodeUtil.isStatement(node1));
        assertTrue(NodeUtil.isStatement(node2));
        assertTrue(NodeUtil.isStatement(node3));
        assertFalse(NodeUtil.isStatement(node4));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        Node node2 = new Node(Token.FUNCTION, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        Node node3 = new Node(Token.SCRIPT, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        Node node4 = new Node(Token.LABEL, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));

        assertFalse(NodeUtil.isFunctionDeclaration(node1));
        assertFalse(NodeUtil.isFunctionDeclaration(node2));
        assertFalse(NodeUtil.isFunctionDeclaration(node3));
        assertTrue(NodeUtil.isFunctionDeclaration(node4));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        Node node2 = new Node(Token.FUNCTION, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        Node node3 = new Node(Token.SCRIPT, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        Node node4 = new Node(Token.LABEL, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node1));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node3));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node4));
    }
}