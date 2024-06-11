package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node1 = new Node(Token.FOR);
        node1.addChildToBack(new Node(Token.NAME));
        node1.addChildToBack(new Node(Token.STRING));
        node1.addChildToBack(new Node(Token.STRING));

        assertTrue(NodeUtil.isForIn(node1));

        Node node2 = new Node(Token.FOR);
        node2.addChildToBack(new Node(Token.NAME));
        node2.addChildToBack(new Node(Token.STRING));

        assertFalse(NodeUtil.isForIn(node2));
    }

    @Test
    public void testIsStatement() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node blockNode = new Node(Token.BLOCK);
        Node labelNode = new Node(Token.LABEL);
        Node otherNode = new Node(Token.ADD);

        Node statementNode1 = new Node(Token.BLOCK);
        statementNode1.addChildToBack(scriptNode);
        statementNode1.addChildToBack(blockNode);
        statementNode1.addChildToBack(labelNode);

        Node statementNode2 = new Node(Token.BLOCK);
        statementNode2.addChildToBack(otherNode);

        assertTrue(NodeUtil.isStatement(scriptNode));
        assertTrue(NodeUtil.isStatement(blockNode));
        assertTrue(NodeUtil.isStatement(labelNode));
        assertFalse(NodeUtil.isStatement(otherNode));

        assertTrue(NodeUtil.isStatement(statementNode1));
        assertFalse(NodeUtil.isStatement(statementNode2));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node functionNode = new Node(Token.FUNCTION);
        Node nonFunctionNode = new Node(Token.ADD);

        assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
        assertFalse(NodeUtil.isFunctionDeclaration(nonFunctionNode));

        Node scriptNode = new Node(Token.SCRIPT);
        scriptNode.addChildToBack(functionNode);

        Node blockNode = new Node(Token.BLOCK);
        blockNode.addChildToBack(functionNode);

        Node labelNode = new Node(Token.LABEL);
        labelNode.addChildToBack(functionNode);

        assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
        assertFalse(NodeUtil.isFunctionDeclaration(nonFunctionNode));

        assertTrue(NodeUtil.isFunctionDeclaration(scriptNode));
        assertTrue(NodeUtil.isFunctionDeclaration(blockNode));
        assertTrue(NodeUtil.isFunctionDeclaration(labelNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node functionNode1 = new Node(Token.FUNCTION);
        Node scriptNode = new Node(Token.SCRIPT);
        scriptNode.addChildToBack(functionNode1);

        Node blockNode = new Node(Token.BLOCK);
        blockNode.addChildToBack(functionNode1);

        Node functionNode2 = new Node(Token.FUNCTION);
        Node functionExpressionNode = new Node(Token.ADD);
        functionExpressionNode.addChildToBack(functionNode2);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode1));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionExpressionNode));

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(scriptNode));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(blockNode));
    }
}