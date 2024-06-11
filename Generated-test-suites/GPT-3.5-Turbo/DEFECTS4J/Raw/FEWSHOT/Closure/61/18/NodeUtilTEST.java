package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node node1 = new Node(Token.FOR);
        Node node2 = new Node(Token.FOR);
        node2.addChildToBack(new Node(Token.NAME));
        node2.addChildToBack(new Node(Token.NAME));
        node2.addChildToBack(new Node(Token.NAME));

        assertFalse(NodeUtil.isForIn(node1));
        assertTrue(NodeUtil.isForIn(node2));
    }

    @Test
    public void testIsStatement() {
        Node node1 = new Node(Token.SCRIPT);
        Node node2 = new Node(Token.BLOCK);
        Node node3 = new Node(Token.LABEL);
        Node node4 = new Node(Token.NAME);

        assertTrue(NodeUtil.isStatement(node1));
        assertTrue(NodeUtil.isStatement(node2));
        assertTrue(NodeUtil.isStatement(node3));
        assertFalse(NodeUtil.isStatement(node4));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION);
        Node node2 = new Node(Token.SCRIPT);
        node2.setType(Token.SCRIPT);
        node2.addChildToBack(node1);
        Node node3 = new Node(Token.FUNCTION);
        Node node4 = new Node(Token.NAME);

        assertTrue(NodeUtil.isFunctionDeclaration(node1));
        assertTrue(NodeUtil.isFunctionDeclaration(node2));
        assertFalse(NodeUtil.isFunctionDeclaration(node3));
        assertFalse(NodeUtil.isFunctionDeclaration(node4));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION);
        Node node2 = new Node(Token.FUNCTION);
        node2.setType(Token.FUNCTION);
        node2.setParent(new Node(Token.SCRIPT));
        Node node3 = new Node(Token.FUNCTION);
        Node node4 = new Node(Token.NAME);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node2));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node3));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node4));
    }
}