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
        Node node1 = createNode(Token.FOR, createNode(), createNode(), createNode());
        assertTrue(NodeUtil.isForIn(node1));

        Node node2 = createNode(Token.FOR, createNode(), createNode());
        assertFalse(NodeUtil.isForIn(node2));

        Node node3 = createNode(Token.WHILE, createNode(), createNode(), createNode());
        assertFalse(NodeUtil.isForIn(node3));
    }

    @Test
    public void testIsStatement() {
        Node scriptNode = createNode(Token.SCRIPT, createNode(), createNode());
        Node blockNode = createNode(Token.BLOCK, createNode(), createNode());
        Node labelNode = createNode(Token.LABEL, createNode(), createNode());
        Node functionNode = createNode(Token.FUNCTION, createNode(), createNode());

        assertTrue(NodeUtil.isStatement(scriptNode));
        assertTrue(NodeUtil.isStatement(blockNode));
        assertTrue(NodeUtil.isStatement(labelNode));
        assertFalse(NodeUtil.isStatement(functionNode));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node functionNode = createNode(Token.FUNCTION, createNode(), createNode());
        assertTrue(NodeUtil.isFunctionDeclaration(functionNode));

        Node expressionNode = createNode(Token.EXPR_RESULT, functionNode);
        assertFalse(NodeUtil.isFunctionDeclaration(expressionNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node functionNode = createNode(Token.FUNCTION, createNode(), createNode());
        Node parentScriptNode = createNode(Token.SCRIPT, functionNode);
        Node parentNode = createNode(Token.ASSIGN, parentScriptNode);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(parentScriptNode));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(parentNode));
    }

    private Node createNode(Token type, Node... children) {
        Node node = new Node(type);
        for (Node child : children) {
            node.addChildToBack(child);
        }
        return node;
    }

    private Node createNode() {
        return new Node(Token.EMPTY);
    }
}