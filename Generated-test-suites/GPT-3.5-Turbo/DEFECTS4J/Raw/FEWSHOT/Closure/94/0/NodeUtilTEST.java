package com.google.javascript.jscomp.jsonml;

import org.junit.Test;

import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.NAME));

        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsNotForIn() {
        Node node = new Node(Token.WHILE);
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.NAME));

        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node parent = new Node(Token.SCRIPT);
        Node node = new Node(Token.NAME);
        parent.addChildToBack(node);

        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsNotStatement() {
        Node parent = new Node(Token.EXPR_RESULT);
        Node node = new Node(Token.NAME);
        parent.addChildToBack(node);

        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);

        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsNotFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.EXPR_RESULT);
        parent.addChildToBack(node);

        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }

    @Test
    public void testIsNotHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        parent.addChildToBack(new Node(Token.FUNCTION));
        parent.addChildToBack(node);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}