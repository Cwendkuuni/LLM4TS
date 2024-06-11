package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node1 = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        Node node2 = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertFalse(NodeUtil.isForIn(node1));
        assertTrue(NodeUtil.isForIn(node2));
    }

    @Test
    public void testIsStatement() {
        Node node1 = new Node(Token.SCRIPT);
        Node node2 = new Node(Token.BLOCK);
        Node node3 = new Node(Token.LABEL);
        Node node4 = new Node(Token.EMPTY);
        assertTrue(NodeUtil.isStatement(node1));
        assertTrue(NodeUtil.isStatement(node2));
        assertTrue(NodeUtil.isStatement(node3));
        assertFalse(NodeUtil.isStatement(node4));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION);
        Node node2 = new Node(Token.EMPTY);
        assertTrue(NodeUtil.isFunctionDeclaration(node1));
        assertFalse(NodeUtil.isFunctionDeclaration(node2));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node parent1 = new Node(Token.SCRIPT);
        Node parent2 = new Node(Token.FUNCTION);
        Node node1 = new Node(Token.FUNCTION);
        Node node2 = new Node(Token.EMPTY);
        parent1.addChildToBack(node1);
        parent2.addChildToBack(node2);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));
    }
}