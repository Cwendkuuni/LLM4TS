package com.google.javascript.jscomp.jsonml;

import org.junit.Test;

import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node n1 = new Node(Token.FOR);
        Node n2 = new Node(Token.FOR);
        n2.addChildToBack(new Node(Token.EMPTY));
        n2.addChildToBack(new Node(Token.EMPTY));
        n2.addChildToBack(new Node(Token.EMPTY));

        assertTrue(NodeUtil.isForIn(n2));
        assertFalse(NodeUtil.isForIn(n1));
    }

    @Test
    public void testIsStatement() {
        Node n1 = new Node(Token.SCRIPT);
        Node n2 = new Node(Token.BLOCK);
        Node n3 = new Node(Token.LABEL);
        Node n4 = new Node(Token.NAME);

        assertTrue(NodeUtil.isStatement(n1));
        assertTrue(NodeUtil.isStatement(n2));
        assertTrue(NodeUtil.isStatement(n3));
        assertFalse(NodeUtil.isStatement(n4));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node n1 = new Node(Token.FUNCTION);
        Node n2 = new Node(Token.SCRIPT);
        Node n3 = new Node(Token.BLOCK);

        assertTrue(NodeUtil.isFunctionDeclaration(n1));
        assertFalse(NodeUtil.isFunctionDeclaration(n2));
        assertFalse(NodeUtil.isFunctionDeclaration(n3));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node n1 = new Node(Token.FUNCTION);
        n1.setParent(new Node(Token.SCRIPT));
        Node n2 = new Node(Token.FUNCTION);
        n2.setParent(new Node(Token.FUNCTION));
        Node n3 = new Node(Token.FUNCTION);
        n3.setParent(new Node(Token.BLOCK));
        n3.getParent().setParent(new Node(Token.SCRIPT));

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n1));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n2));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n3));
    }
}