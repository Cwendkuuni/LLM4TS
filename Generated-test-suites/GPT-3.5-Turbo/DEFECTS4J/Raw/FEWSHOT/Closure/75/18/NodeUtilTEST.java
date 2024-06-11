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

        assertFalse(NodeUtil.isForIn(new Node(Token.FUNCTION)));
        assertFalse(NodeUtil.isForIn(new Node(Token.IF)));
        assertFalse(NodeUtil.isForIn(new Node(Token.FOR_OF)));
        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node blockNode = new Node(Token.BLOCK);
        Node labelNode = new Node(Token.LABEL);
        Node expressionNode = new Node(Token.EXPR_RESULT);

        assertTrue(NodeUtil.isStatement(scriptNode));
        assertTrue(NodeUtil.isStatement(blockNode));
        assertTrue(NodeUtil.isStatement(labelNode));
        assertFalse(NodeUtil.isStatement(expressionNode));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node functionNode = new Node(Token.FUNCTION);
        Node scriptNode = new Node(Token.SCRIPT);
        Node expressionNode = new Node(Token.EXPR_RESULT);

        assertFalse(NodeUtil.isFunctionDeclaration(expressionNode));
        assertFalse(NodeUtil.isFunctionDeclaration(scriptNode));
        assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node functionDeclarationNode = new Node(Token.FUNCTION);
        Node scriptNode = new Node(Token.SCRIPT);
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.addChildToFront(new Node(Token.FUNCTION));

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(scriptNode));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    }
}