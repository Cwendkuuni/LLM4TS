package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node node1 = new Node(Token.FOR, new Node(Token.NAME));
        Node node2 = new Node(Token.FOR, new Node(Token.NAME), new Node(Token.NAME));
        Node node3 = new Node(Token.WHILE, new Node(Token.NAME));

        assertTrue(NodeUtil.isForIn(node1));
        assertFalse(NodeUtil.isForIn(node2));
        assertFalse(NodeUtil.isForIn(node3));
    }

    @Test
    public void testIsStatement() {
        Node node1 = new Node(Token.SCRIPT, new Node(Token.NAME));
        Node node2 = new Node(Token.BLOCK, new Node(Token.NAME));
        Node node3 = new Node(Token.LABEL, new Node(Token.NAME));
        Node node4 = new Node(Token.WHILE, new Node(Token.NAME));

        assertTrue(NodeUtil.isStatement(node1));
        assertTrue(NodeUtil.isStatement(node2));
        assertTrue(NodeUtil.isStatement(node3));
        assertFalse(NodeUtil.isStatement(node4));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION, new Node(Token.NAME));
        Node node2 = new Node(Token.FUNCTION, new Node(Token.NAME));
        Node node3 = new Node(Token.FUNCTION, new Node(Token.NAME));
        Node node4 = new Node(Token.NAME);

        node1.setParent(new Node(Token.SCRIPT));
        node2.setParent(new Node(Token.BLOCK));
        node3.setParent(new Node(Token.LABEL));
        node4.setParent(new Node(Token.SCRIPT));

        assertTrue(NodeUtil.isFunctionDeclaration(node1));
        assertFalse(NodeUtil.isFunctionDeclaration(node2));
        assertFalse(NodeUtil.isFunctionDeclaration(node3));
        assertFalse(NodeUtil.isFunctionDeclaration(node4));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION, new Node(Token.NAME));
        Node node2 = new Node(Token.FUNCTION, new Node(Token.NAME));
        Node node3 = new Node(Token.FUNCTION, new Node(Token.NAME));
        Node node4 = new Node(Token.FUNCTION, new Node(Token.NAME));

        node1.setParent(new Node(Token.SCRIPT));
        node2.setParent(new Node(Token.SCRIPT));
        node3.setParent(new Node(Token.BLOCK));
        node4.setParent(new Node(Token.NAME));

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node3));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node4));
    }
}