package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);

        node.addChildToBack(new Node());
        node.addChildToBack(new Node());
        node.addChildToBack(new Node());

        assertEquals(true, NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.BLOCK);

        assertEquals(true, NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);

        assertEquals(true, NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node parent = new Node(Token.SCRIPT);
        Node node = new Node(Token.FUNCTION);

        node.setParent(parent);

        assertEquals(true, NodeUtil.isHoistedFunctionDeclaration(node));
    }
}