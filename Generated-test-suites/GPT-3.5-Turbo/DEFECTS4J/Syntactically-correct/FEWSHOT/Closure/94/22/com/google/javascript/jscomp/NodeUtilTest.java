package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        assertFalse(NodeUtil.isForIn(null));
        Node node = new Node(Token.FOR, new Node(Token.IN));
        assertFalse(NodeUtil.isForIn(node));
        node.addChildToFront(new Node(Token.EMPTY));
        assertFalse(NodeUtil.isForIn(node));
        node.addChildToFront(new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        assertFalse(NodeUtil.isStatement(null));
        assertFalse(NodeUtil.isStatement(new Node(0)));
        Node node = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isStatement(node));
        node.getParent().setType(Token.SCRIPT);
        assertTrue(NodeUtil.isStatement(node));
        node.getParent().setType(Token.BLOCK);
        assertTrue(NodeUtil.isStatement(node));
        node.getParent().setType(Token.LABEL);
        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        assertFalse(NodeUtil.isFunctionDeclaration(null));
        assertFalse(NodeUtil.isFunctionDeclaration(new Node(0)));
        Node node = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isFunctionDeclaration(node));
        node.getParent().setType(Token.SCRIPT);
        assertFalse(NodeUtil.isFunctionDeclaration(node));
        node.setParent(new Node(Token.SCRIPT));
        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(null));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(new Node(0)));
        Node node = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
        Node parent = new Node(Token.SCRIPT);
        node.setParent(parent);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
        parent.setParent(new Node(Token.FUNCTION));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}