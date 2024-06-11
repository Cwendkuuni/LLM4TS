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

        node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.NAME));
        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.NAME);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        node.getParents().add(parent);
        assertTrue(NodeUtil.isStatement(node));

        node = new Node(Token.NAME);
        parent = new Node(Token.BLOCK);
        parent.addChildToBack(node);
        node.getParents().add(parent);
        assertTrue(NodeUtil.isStatement(node));

        node = new Node(Token.NAME);
        parent = new Node(Token.LABEL);
        parent.addChildToBack(node);
        node.getParents().add(parent);
        assertTrue(NodeUtil.isStatement(node));

        node = new Node(Token.NAME);
        parent = new Node(Token.TYPEOF);
        parent.addChildToBack(node);
        node.getParents().add(parent);
        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        node.getParents().add(parent);
        assertTrue(NodeUtil.isFunctionDeclaration(node));

        node = new Node(Token.FUNCTION);
        parent = new Node(Token.BLOCK);
        parent.addChildToBack(node);
        node.getParents().add(parent);
        assertTrue(NodeUtil.isFunctionDeclaration(node));

        node = new Node(Token.FUNCTION);
        parent = new Node(Token.NAME);
        parent.addChildToBack(node);
        node.getParents().add(parent);
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        node.getParents().add(parent);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

        node = new Node(Token.FUNCTION);
        parent = new Node(Token.FUNCTION);
        parent.addChildToBack(new Node(Token.NAME));
        parent.addChildToBack(node);
        node.getParents().add(parent);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

        node = new Node(Token.FUNCTION);
        parent = new Node(Token.BLOCK);
        parent.addChildToBack(node);
        node.getParents().add(parent);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}