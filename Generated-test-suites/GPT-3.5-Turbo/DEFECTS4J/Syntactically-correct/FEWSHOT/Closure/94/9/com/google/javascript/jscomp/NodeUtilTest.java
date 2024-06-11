package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
    // isForIn tests
    @Test
    public void testIsForIn() {
        assertTrue(NodeUtil.isForIn(new Node(Token.FOR)));
        assertFalse(NodeUtil.isForIn(new Node(Token.WHILE)));
        assertFalse(NodeUtil.isForIn(new Node(Token.IF)));
    }

    // isStatement tests
    @Test
    public void testIsStatement() {
        Node parent = new Node(Token.SCRIPT);
        Node node = new Node();
        node.setParent(parent);
        assertTrue(NodeUtil.isStatement(node));

        parent = new Node(Token.BLOCK);
        node = new Node();
        node.setParent(parent);
        assertTrue(NodeUtil.isStatement(node));

        parent = new Node(Token.LABEL);
        node = new Node();
        node.setParent(parent);
        assertTrue(NodeUtil.isStatement(node));

        parent = new Node(Token.EXPR_RESULT);
        node = new Node();
        node.setParent(parent);
        assertFalse(NodeUtil.isStatement(node));
    }

    // isFunctionDeclaration tests
    @Test
    public void testIsFunctionDeclaration() {
        Node parent = new Node(Token.FUNCTION);
        Node node = new Node();
        node.setParent(parent);
        assertTrue(NodeUtil.isFunctionDeclaration(node));

        parent = new Node(Token.WHILE);
        node = new Node();
        node.setParent(parent);
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    // isHoistedFunctionDeclaration tests
    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node parent = new Node(Token.FUNCTION);
        Node grandParent = new Node(Token.SCRIPT);
        Node node = new Node();
        node.setParent(parent);
        parent.setParent(grandParent);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

        parent = new Node(Token.FUNCTION);
        grandParent = new Node(Token.FUNCTION);
        node = new Node();
        node.setParent(parent);
        parent.setParent(grandParent);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

        parent = new Node(Token.WHILE);
        grandParent = new Node(Token.SCRIPT);
        node = new Node();
        node.setParent(parent);
        parent.setParent(grandParent);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}