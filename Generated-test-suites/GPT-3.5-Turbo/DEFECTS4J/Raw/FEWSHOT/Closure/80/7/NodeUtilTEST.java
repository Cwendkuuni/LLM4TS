package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.NAME));
        assertTrue(NodeUtil.isForIn(node));

        Node node2 = new Node(Token.INTEGER);
        assertFalse(NodeUtil.isForIn(node2));
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.SCRIPT);
        Node child = new Node(Token.BLOCK);
        node.addChildToBack(child);
        assertTrue(NodeUtil.isStatement(child));

        Node node2 = new Node(Token.INTEGER);
        assertFalse(NodeUtil.isStatement(node2));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        node.addChildToBack(new Node(Token.NAME));
        assertTrue(NodeUtil.isFunctionDeclaration(node));

        Node node2 = new Node(Token.INTEGER);
        assertFalse(NodeUtil.isFunctionDeclaration(node2));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        node.addChildToBack(new Node(Token.NAME));
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

        Node node2 = new Node(Token.FUNCTION);
        node2.addChildToBack(new Node(Token.NAME));
        Node parent2 = new Node(Token.FUNCTION);
        parent2.addChildToBack(node2);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node2));

        Node node3 = new Node(Token.FUNCTION);
        node3.addChildToBack(new Node(Token.NAME));
        Node parent3 = new Node(Token.NAME);
        parent3.addChildToBack(node3);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node3));
    }
}