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
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.NAME));

        assertTrue(NodeUtil.isForIn(node));

        node.removeChild(node.getFirstChild());

        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node blockNode = new Node(Token.BLOCK);
        Node labelNode = new Node(Token.LABEL);
        Node functionNode = new Node(Token.FUNCTION);

        Node statementNode = new Node(Token.RETURN);
        Node expressionNode = new Node(Token.REFLECT_OBJECT);

        scriptNode.addChildToBack(statementNode);
        scriptNode.addChildToBack(functionNode);
        blockNode.addChildToBack(statementNode);
        blockNode.addChildToBack(expressionNode);
        labelNode.addChildToBack(statementNode);
        labelNode.addChildToBack(expressionNode);
        functionNode.addChildToBack(statementNode);
        functionNode.addChildToBack(expressionNode);

        assertTrue(NodeUtil.isStatement(statementNode));
        assertTrue(NodeUtil.isStatement(blockNode));
        assertTrue(NodeUtil.isStatement(scriptNode));
        assertFalse(NodeUtil.isStatement(expressionNode));
        assertFalse(NodeUtil.isStatement(labelNode));
        assertFalse(NodeUtil.isStatement(functionNode));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node functionNode = new Node(Token.FUNCTION);

        assertFalse(NodeUtil.isFunctionDeclaration(functionNode));

        Node statementNode = new Node(Token.RETURN);
        Node scriptNode = new Node(Token.SCRIPT);
        Node blockNode = new Node(Token.BLOCK);

        functionNode.addChildToBack(statementNode);
        scriptNode.addChildToBack(functionNode);
        blockNode.addChildToBack(functionNode);

        assertFalse(NodeUtil.isFunctionDeclaration(functionNode));
        assertFalse(NodeUtil.isFunctionDeclaration(scriptNode));
        assertFalse(NodeUtil.isFunctionDeclaration(blockNode));

        statementNode = new Node(Token.SCRIPT);
        statementNode.addChildToBack(functionNode);

        assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node functionNode = new Node(Token.FUNCTION);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));

        Node scriptNode = new Node(Token.SCRIPT);
        Node parentFunctionNode = new Node(Token.FUNCTION);

        scriptNode.addChildToBack(functionNode);
        parentFunctionNode.addChildToBack(functionNode);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(scriptNode));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(parentFunctionNode));
    }
}