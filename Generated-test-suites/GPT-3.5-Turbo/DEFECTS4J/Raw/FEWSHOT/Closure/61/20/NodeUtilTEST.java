package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        assertFalse(NodeUtil.isForIn(node));

        Node child1 = new Node(Token.NAME);
        Node child2 = new Node(Token.NAME);
        Node child3 = new Node(Token.NAME);
        node.addChildToBack(child1);
        node.addChildToBack(child2);
        node.addChildToBack(child3);
        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.SCRIPT);
        assertTrue(NodeUtil.isStatement(node));

        node = new Node(Token.BLOCK);
        assertTrue(NodeUtil.isStatement(node));

        node = new Node(Token.LABEL);
        assertTrue(NodeUtil.isStatement(node));

        node = new Node(Token.IF);
        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(node);
        assertTrue(NodeUtil.isFunctionDeclaration(node));

        parent = new Node(Token.BLOCK);
        parent.addChildToFront(node);
        assertTrue(NodeUtil.isFunctionDeclaration(node));

        parent = new Node(Token.SCRIPT);
        parent.addChildToFront(new Node(Token.IF));
        parent.getFirstChild().addChildToFront(node);
        assertFalse(NodeUtil.isFunctionDeclaration(node));

        parent = new Node(Token.FUNCTION);
        parent.addChildToFront(new Node(Token.BLOCK));
        parent.getFirstChild().addChildToFront(node);
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(node);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

        parent = new Node(Token.BLOCK);
        parent.addChildToFront(node);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

        parent = new Node(Token.SCRIPT);
        parent.addChildToFront(new Node(Token.IF));
        parent.getFirstChild().addChildToFront(node);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));

        parent = new Node(Token.FUNCTION);
        parent.addChildToFront(new Node(Token.BLOCK));
        parent.getFirstChild().addChildToFront(node);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}