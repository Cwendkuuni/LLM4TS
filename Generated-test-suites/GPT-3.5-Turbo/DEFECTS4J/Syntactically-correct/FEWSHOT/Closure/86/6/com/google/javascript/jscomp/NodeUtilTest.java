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
        Node node1 = new Node(Token.FOR);
        Node node2 = new Node(Token.FOR);
        node2.addChildToBack(new Node(Token.NAME));
        node2.addChildToBack(new Node(Token.NAME));
        node2.addChildToBack(new Node(Token.NAME));
        assertTrue(NodeUtil.isForIn(node2));
        assertFalse(NodeUtil.isForIn(node1));
    }

    @Test
    public void testIsStatement() {
        Node node1 = new Node(Token.SCRIPT);
        assertTrue(NodeUtil.isStatement(node1));
        Node node2 = new Node(Token.DO);
        assertFalse(NodeUtil.isStatement(node2));
        Node node3 = new Node(Token.NAME);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node3);
        assertTrue(NodeUtil.isStatement(node3));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node1);
        assertTrue(NodeUtil.isFunctionDeclaration(node1));
        Node node2 = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isFunctionDeclaration(node2));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node1);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));
        Node node2 = new Node(Token.FUNCTION);
        Node parent2 = new Node(Token.FUNCTION);
        parent2.addChildToBack(node2);
        parent.addChildToBack(parent2);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));
    }
}