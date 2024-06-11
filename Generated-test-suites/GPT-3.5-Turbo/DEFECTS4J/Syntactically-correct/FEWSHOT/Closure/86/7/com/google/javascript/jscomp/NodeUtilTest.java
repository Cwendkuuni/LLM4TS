package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        assertFalse(NodeUtil.isForIn(node));
        node.addChildToBack(new Node(Token.NAME));
        assertFalse(NodeUtil.isForIn(node));
        node.addChildToBack(new Node(Token.NAME));
        assertFalse(NodeUtil.isForIn(node));

        node.addChildToBack(new Node(Token.NAME));
        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.SCRIPT);
        Node childNode = new Node(Token.NAME);
        node.addChildToBack(childNode);
        assertTrue(NodeUtil.isStatement(childNode));

        childNode = new Node(Token.BLOCK);
        node.addChildToBack(childNode);
        assertTrue(NodeUtil.isStatement(childNode));

        childNode = new Node(Token.LABEL);
        node.addChildToBack(childNode);
        assertTrue(NodeUtil.isStatement(childNode));

        Node parentNode = new Node(Token.SCRIPT);
        parentNode.addChildToBack(parentNode);
        childNode = new Node(Token.NAME);
        parentNode.addChildToBack(childNode);
        assertFalse(NodeUtil.isStatement(childNode));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        assertTrue(NodeUtil.isFunctionDeclaration(node));

        node = new Node(Token.NAME);
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        node.addChildToBack(new Node(Token.NAME));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(new Node(Token.SCRIPT)));

        node = new Node(Token.SCRIPT);
        Node parentNode = new Node(Token.FUNCTION);
        parentNode.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(parentNode);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}