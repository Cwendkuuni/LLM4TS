package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(node));

        node = new Node(Token.FOR);
        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node parent = new Node(Token.SCRIPT);
        Node node = new Node(Token.EMPTY);
        node.setParent(parent);
        assertTrue(NodeUtil.isStatement(node));

        parent = new Node(Token.BLOCK);
        node = new Node(Token.EMPTY);
        node.setParent(parent);
        assertTrue(NodeUtil.isStatement(node));

        parent = new Node(Token.LABEL);
        node = new Node(Token.EMPTY);
        node.setParent(parent);
        assertTrue(NodeUtil.isStatement(node));

        parent = new Node(Token.FUNCTION);
        node = new Node(Token.EMPTY);
        node.setParent(parent);
        assertTrue(NodeUtil.isStatement(node));

        parent = new Node(Token.EMPTY);
        node = new Node(Token.EMPTY);
        node.setParent(parent);
        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        assertTrue(NodeUtil.isFunctionDeclaration(node));

        node = new Node(Token.EMPTY);
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node parent = new Node(Token.SCRIPT);
        Node grandParent = new Node(Token.FUNCTION);
        Node node = new Node(Token.FUNCTION);
        node.setParent(grandParent);
        grandParent.setParent(parent);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

        parent = new Node(Token.SCRIPT);
        grandParent = new Node(Token.SCRIPT);
        node = new Node(Token.FUNCTION);
        node.setParent(grandParent);
        grandParent.setParent(parent);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

        node = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}