package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node nodeForIn = new Node(Token.FOR, new int[]{0, 0, 1, 0, 0, 0, 0, 0, 0, 0});
        Node nodeNotForIn = new Node(Token.IF, new int[]{0, 1, 0, 0, 0, 0, 0, 0, 0, 0});

        assertTrue(NodeUtil.isForIn(nodeForIn));
        assertFalse(NodeUtil.isForIn(nodeNotForIn));
    }

    @Test
    public void testIsStatement() {
        Node nodeScript = new Node(Token.SCRIPT, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        Node nodeFunction = new Node(Token.FUNCTION, new int[]{0, 1, 0, 0, 0, 0, 0, 0, 0, 0});
        Node nodeIf = new Node(Token.IF, new int[]{0, 1, 0, 0, 0, 0, 0, 0, 0, 0});
        Node nodeNull = null;

        checkState(nodeScript != null);
        assertTrue(NodeUtil.isStatement(nodeScript));
        checkState(nodeFunction != null);
        assertTrue(NodeUtil.isStatement(nodeFunction));
        assertFalse(NodeUtil.isStatement(nodeIf));
        assertNull(NodeUtil.isStatement(nodeNull));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{0, 1, 2, 0, 0, 0, 0, 0, 0, 0});
        Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[]{0, 1, 0, 0, 0, 0, 0, 0, 0, 0});
        Node nodeIf = new Node(Token.IF, new int[]{0, 1, 0, 0, 0, 0, 0, 0, 0, 0});

        Node parentScript = new Node(Token.SCRIPT, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        Node parentFunction = new Node(Token.FUNCTION, new int[]{0, 1, 0, 0, 0, 0, 0, 0, 0, 0});

        checkState(nodeFunctionDeclaration != null);
        assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
        checkState(nodeFunctionExpression != null);
        assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
        checkState(nodeIf != null);
        assertFalse(NodeUtil.isFunctionDeclaration(nodeIf));
        checkState(parentScript != null);
        assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
        checkState(parentFunction != null);
        assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node nodeFunctionDeclarationInScript = new Node(Token.FUNCTION, new int[]{0, 1, 2, 0, 0, 0, 0, 0, 0, 0});
        Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, new int[]{0, 1, 2, 0, 0, 0, 0, 0, 0, 0});

        Node parentScript = new Node(Token.SCRIPT, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        Node parentFunction = new Node(Token.FUNCTION, new int[]{0, 1, 0, 0, 0, 0, 0, 0, 0, 0});

        checkState(nodeFunctionDeclarationInScript != null);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
        checkState(nodeFunctionDeclarationInFunction != null);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
        checkState(parentScript != null);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
        checkState(parentFunction != null);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
    }
}