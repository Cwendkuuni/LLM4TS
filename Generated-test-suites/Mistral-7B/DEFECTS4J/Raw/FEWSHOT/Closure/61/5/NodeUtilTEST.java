package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node nodeForIn = new Node(Token.FOR, null, 0, 0, new int[0], new int[0]);
        Node nodeIf = new Node(Token.IF, null, 0, 0, new int[0], new int[0]);
        Node nodeFunction = new Node(Token.FUNCTION, null, 0, 0, new int[0], new int[0]);

        assertTrue(NodeUtil.isForIn(nodeForIn));
        assertFalse(NodeUtil.isForIn(nodeIf));
        assertFalse(NodeUtil.isForIn(nodeFunction));
    }

    @Test
    public void testIsStatement() {
        Node nodeScript = new Node(Token.SCRIPT, null, 0, 0, new int[0], new int[0]);
        Node nodeFunction = new Node(Token.FUNCTION, null, 0, 0, new int[0], new int[0]);
        Node nodeIf = new Node(Token.IF, null, 0, 0, new int[0], new int[0]);
        Node nodeExpression = new Node(Token.ASSIGN, null, 0, 0, new int[0], new int[0]);

        Node parentExpression = new Node(Token.ASSIGN, nodeExpression, 0, 0, new int[0], new int[0]);
        Node parentFunction = new Node(Token.FUNCTION, nodeFunction, 0, 0, new int[0], new int[0]);
        Node parentBlock = new Node(Token.BLOCK, nodeScript, 0, 0, new int[0], new int[0]);
        Node parentLabel = new Node(Token.LABEL, nodeIf, 0, 0, new int[0], new int[0]);

        assertTrue(NodeUtil.isStatement(nodeScript));
        assertTrue(NodeUtil.isStatement(nodeFunction));
        assertTrue(NodeUtil.isStatement(nodeIf));
        assertTrue(NodeUtil.isStatement(parentBlock));
        assertTrue(NodeUtil.isStatement(parentLabel));
        assertFalse(NodeUtil.isStatement(nodeExpression));
        assertFalse(NodeUtil.isStatement(parentFunction));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node nodeFunctionDeclaration = new Node(Token.FUNCTION, null, 0, 0, new int[0], new int[0]);
        Node nodeFunctionExpression = new Node(Token.FUNCTION, null, 0, 0, new int[0], new int[0]);
        Node nodeVariable = new Node(Token.VAR, null, 0, 0, new int[0], new int[0]);

        assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
        assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
        assertFalse(NodeUtil.isFunctionDeclaration(nodeVariable));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node nodeFunctionDeclarationInScript = new Node(Token.FUNCTION, null, 0, 0, new int[0], new int[0]);
        Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, null, 0, 0, new int[0], new int[0]);
        Node nodeFunction = new Node(Token.FUNCTION, null, 0, 0, new int[0], new int[0]);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));
    }
}