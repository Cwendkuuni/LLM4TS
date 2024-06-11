package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.STATEMENT_BLOCK));
        assertTrue(NodeUtil.isForIn(node));

        node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.STATEMENT_BLOCK));
        node.addChildToBack(new Node(Token.NAME));
        assertTrue(NodeUtil.isForIn(node));

        node = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isForIn(node));

        node = new Node(Token.FOR);
        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.SCRIPT);
        assertTrue(NodeUtil.isStatement(node));

        node = new Node(Token.BLOCK);
        assertTrue(NodeUtil.isStatement(node));

        node = new Node(Token.LABEL);
        assertTrue(NodeUtil.isStatement(node));

        node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        parent.addChildToBack(node);
        assertFalse(NodeUtil.isStatement(node));

        node = new Node(Token.NAME);
        assertNotNull(node.getParent());
        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        assertTrue(NodeUtil.isFunctionDeclaration(node));

        node = new Node(Token.NAME);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        assertFalse(NodeUtil.isFunctionDeclaration(node));

        node = new Node(Token.FUNCTION);
        parent = new Node(Token.STATEMENT_BLOCK);
        parent.addChildToBack(node);
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

        node = new Node(Token.FUNCTION);
        parent = new Node(Token.STATEMENT_BLOCK);
        parent.addChildToBack(new Node(Token.FUNCTION));
        parent.addChildToBack(new Node(Token.FUNCTION));
        parent.addChildToBack(node);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

        node = new Node(Token.FUNCTION);
        parent = new Node(Token.FUNCTION);
        parent.addChildToBack(node);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));

        node = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));

        node = new Node(Token.STATEMENT_BLOCK);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}