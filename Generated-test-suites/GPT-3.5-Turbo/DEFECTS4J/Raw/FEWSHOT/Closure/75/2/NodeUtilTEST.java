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
        Node node1 = new Node(Token.FOR);
        node1.addChildToBack(new Node(Token.NAME));
        node1.addChildToBack(new Node(Token.NAME));
        assertTrue(NodeUtil.isForIn(node1));

        Node node2 = new Node(Token.FOR);
        node2.addChildToBack(new Node(Token.NAME));
        node2.addChildToBack(new Node(Token.NAME));
        node2.addChildToBack(new Node(Token.NAME));
        assertFalse(NodeUtil.isForIn(node2));

        Node node3 = new Node(Token.IF);
        assertFalse(NodeUtil.isForIn(node3));
    }

    @Test
    public void testIsStatement() {
        Node node1 = new Node(Token.SCRIPT);
        assertTrue(NodeUtil.isStatement(node1));

        Node node2 = new Node(Token.BLOCK);
        assertTrue(NodeUtil.isStatement(node2));

        Node node3 = new Node(Token.LABEL);
        assertTrue(NodeUtil.isStatement(node3));

        Node node4 = new Node(Token.IF);
        assertFalse(NodeUtil.isStatement(node4));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION);
        assertTrue(NodeUtil.isFunctionDeclaration(node1));

        Node node2 = new Node(Token.NAME);
        assertFalse(NodeUtil.isFunctionDeclaration(node2));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION);
        Node scriptNode = new Node(Token.SCRIPT);
        scriptNode.addChildToBack(node1);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));

        Node node2 = new Node(Token.FUNCTION);
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.addChildToBack(node2);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));
    }
}