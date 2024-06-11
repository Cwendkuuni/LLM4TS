package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node node1 = new Node(Token.FOR, new Node(Token.NAME, new Node(Token.FORIN)));
        Node node2 = new Node(Token.FOR, new Node(Token.NAME), new Node(Token.NAME), new Node(Token.FORIN));
        Node node3 = new Node(Token.FOR, new Node(Token.NAME), new Node(Token.NAME));

        assertEquals(true, NodeUtil.isForIn(node1));
        assertEquals(true, NodeUtil.isForIn(node2));
        assertEquals(false, NodeUtil.isForIn(node3));
    }

    @Test
    public void testIsStatement() {
        Node node1 = new Node(Token.SCRIPT, new Node(Token.NAME));
        Node node2 = new Node(Token.BLOCK, new Node(Token.NAME));
        Node node3 = new Node(Token.LABEL, new Node(Token.NAME));
        Node node4 = new Node(Token.NAME);

        assertEquals(true, NodeUtil.isStatement(node1));
        assertEquals(true, NodeUtil.isStatement(node2));
        assertEquals(true, NodeUtil.isStatement(node3));
        assertEquals(false, NodeUtil.isStatement(node4));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION);
        Node node2 = new Node(Token.FUNCTION, new Node(Token.NAME));
        Node node3 = new Node(Token.SCRIPT, new Node(Token.FUNCTION));
        Node node4 = new Node(Token.SCRIPT, new Node(Token.NAME));

        assertEquals(false, NodeUtil.isFunctionDeclaration(node1));
        assertEquals(true, NodeUtil.isFunctionDeclaration(node2));
        assertEquals(false, NodeUtil.isFunctionDeclaration(node3));
        assertEquals(false, NodeUtil.isFunctionDeclaration(node4));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION, new Node(Token.NAME));
        Node node2 = new Node(Token.SCRIPT, new Node(Token.FUNCTION, new Node(Token.NAME)));
        Node node3 = new Node(Token.SCRIPT, new Node(Token.FUNCTION, new Node(Token.NAME)), new Node(Token.FUNCTION));

        assertEquals(false, NodeUtil.isHoistedFunctionDeclaration(node1));
        assertEquals(true, NodeUtil.isHoistedFunctionDeclaration(node2));
        assertEquals(false, NodeUtil.isHoistedFunctionDeclaration(node3));
    }
}