package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR, new Node(), new Node(), new Node());
        assertEquals(true, NodeUtil.isForIn(node));

        node = new Node(Token.FOR, new Node(), new Node());
        assertEquals(false, NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.SCRIPT);
        Node parent = new Node();
        parent.addChildToFront(node);
        assertEquals(true, NodeUtil.isStatement(node));

        node = new Node(Token.BLOCK);
        parent = new Node();
        parent.addChildToFront(node);
        assertEquals(true, NodeUtil.isStatement(node));

        node = new Node(Token.LABEL);
        parent = new Node();
        parent.addChildToFront(node);
        assertEquals(true, NodeUtil.isStatement(node));

        node = new Node();
        parent = new Node();
        parent.addChildToFront(node);
        assertEquals(false, NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        node.setParent(parent);
        assertEquals(true, NodeUtil.isFunctionDeclaration(node));

        node = new Node(Token.FUNCTION);
        parent = new Node(Token.BLOCK);
        node.setParent(parent);
        assertEquals(true, NodeUtil.isFunctionDeclaration(node));

        node = new Node(Token.FUNCTION);
        parent = new Node(Token.LABEL);
        node.setParent(parent);
        assertEquals(true, NodeUtil.isFunctionDeclaration(node));

        node = new Node(Token.FUNCTION);
        parent = new Node(Token.SCRIPT);
        Node grandparent = new Node(Token.FUNCTION);
        parent.setParent(grandparent);
        node.setParent(parent);
        assertEquals(false, NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        node.setParent(parent);
        assertEquals(true, NodeUtil.isHoistedFunctionDeclaration(node));

        node = new Node(Token.FUNCTION);
        parent = new Node(Token.SCRIPT);
        Node grandparent = new Node(Token.LABEL);
        parent.setParent(grandparent);
        node.setParent(parent);
        assertEquals(true, NodeUtil.isHoistedFunctionDeclaration(node));

        node = new Node(Token.FUNCTION);
        parent = new Node(Token.BLOCK);
        node.setParent(parent);
        assertEquals(false, NodeUtil.isHoistedFunctionDeclaration(node));
    }
}