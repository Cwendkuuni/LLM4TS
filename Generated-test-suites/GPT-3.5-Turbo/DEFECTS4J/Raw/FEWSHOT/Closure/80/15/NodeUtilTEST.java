package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR, null);
        node.addChildToBack(new Node(Token.EMPTY, null));
        node.addChildToBack(new Node(Token.EMPTY, null));
        node.addChildToBack(new Node(Token.EMPTY, null));
        assertTrue(NodeUtil.isForIn(node));

        node.removeChild(node.getFirstChild());
        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.SCRIPT, null);
        assertTrue(NodeUtil.isStatement(node));

        node = new Node(Token.BLOCK, null);
        assertTrue(NodeUtil.isStatement(node));

        node = new Node(Token.LABEL, null);
        assertTrue(NodeUtil.isStatement(node));

        node = new Node(Token.FUNCTION, null);
        assertTrue(NodeUtil.isStatement(node));

        node = new Node(Token.EMPTY, null);
        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION, null);
        assertTrue(NodeUtil.isFunctionDeclaration(node));

        node = new Node(Token.SCRIPT, null);
        node.addChildToBack(new Node(Token.FUNCTION, null));
        assertFalse(NodeUtil.isFunctionDeclaration(node.getFirstChild()));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node scriptNode = new Node(Token.SCRIPT, null);
        Node functionNode = new Node(Token.FUNCTION, null);
        scriptNode.addChildToBack(functionNode);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));

        Node parentFunctionNode = new Node(Token.FUNCTION, null);
        parentFunctionNode.addChildToBack(new Node(Token.EMPTY, null));
        parentFunctionNode.addChildToBack(new Node(Token.EMPTY, null));
        parentFunctionNode.addChildToBack(new Node(Token.EMPTY, null));
        parentFunctionNode.addChildToBack(functionNode);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));

        Node grandParentNode = new Node(Token.FUNCTION, null);
        grandParentNode.addChildToBack(parantFunctionNode);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    }
}