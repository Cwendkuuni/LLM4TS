package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR, new Node(Token.IN),
            new Node(Token.NAME));

        assertTrue(NodeUtil.isForIn(node));

        node = new Node(Token.WHILE, new Node(Token.IN),
            new Node(Token.NAME));

        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.SCRIPT, new Node(Token.NAME));

        assertTrue(NodeUtil.isStatement(node));

        node = new Node(Token.VAR, new Node(Token.NAME));

        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION, new Node(Token.NAME));

        assertTrue(NodeUtil.isFunctionDeclaration(node));

        node = new Node(Token.VAR, new Node(Token.NAME));

        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION, new Node(Token.NAME));
        Node parent = new Node(Token.SCRIPT, new Node(Token.NAME));
        node.setParent(parent);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

        node = new Node(Token.FUNCTION, new Node(Token.NAME));
        parent = new Node(Token.FUNCTION, new Node(Token.NAME));
        Node grandParent = new Node(Token.SCRIPT, new Node(Token.NAME));
        parent.setParent(grandParent);
        node.setParent(parent);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

        node = new Node(Token.VAR, new Node(Token.NAME));

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}