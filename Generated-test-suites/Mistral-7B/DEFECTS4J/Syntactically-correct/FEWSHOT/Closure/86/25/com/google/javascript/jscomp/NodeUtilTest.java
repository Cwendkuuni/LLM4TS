package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node nodeForIn = Node.newNode(Token.FOR, new int[] {1, 2, 3});
        assertTrue(NodeUtil.isForIn(nodeForIn));

        Node nodeNotForIn = Node.newNode(Token.IF, new int[] {1, 2, 3});
        assertFalse(NodeUtil.isForIn(nodeNotForIn));
    }

    @Test
    public void testIsStatement() {
        Node nodeStatement = Node.newNode(Token.PRINT, new int[] {1, 2, 3});
        Node parentScript = Node.newNode(Token.SCRIPT, new int[0]);
        Node parentBlock = Node.newNode(Token.BLOCK, new int[0]);
        Node parentLabel = Node.newNode(Token.LABEL, new int[0]);

        parentScript.addChildToEnd(nodeStatement);
        checkState(parentScript != null);
        assertTrue(NodeUtil.isStatement(nodeStatement));

        parentBlock.addChildToEnd(nodeStatement);
        checkState(parentBlock != null);
        assertTrue(NodeUtil.isStatement(nodeStatement));

        parentLabel.addChildToEnd(nodeStatement);
        checkState(parentLabel != null);
        assertTrue(NodeUtil.isStatement(nodeStatement));

        Node nodeNotStatement = Node.newNode(Token.VAR, new int[] {1, 2, 3});
        assertFalse(NodeUtil.isStatement(nodeNotStatement));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[] {1, 2, 3}, "funcName");
        Node nodeIsStatementParent = Node.newNode(Token.SCRIPT, new int[0]);
        Node nodeIsStatementGrandParent = Node.newNode(Token.SCRIPT, new int[0]);

        nodeIsStatementParent.addChildToEnd(nodeFunctionDeclaration);
        nodeIsStatementGrandParent.addChildToEnd(nodeIsStatementParent);

        checkState(nodeFunctionDeclaration != null);
        assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));

        Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, new int[] {1, 2, 3});
        Node nodeIsExpressionParent = Node.newNode(Token.ASSIGN, new int[] {1, 2, 3});
        Node nodeParentStatement = Node.newNode(Token.SCRIPT, new int[0]);

        nodeParentStatement.addChildToEnd(nodeIsExpressionParent);
        nodeIsExpressionParent.addChildToEnd(nodeFunctionExpression);

        checkState(nodeFunctionExpression != null);
        assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionExpression));
    }
}