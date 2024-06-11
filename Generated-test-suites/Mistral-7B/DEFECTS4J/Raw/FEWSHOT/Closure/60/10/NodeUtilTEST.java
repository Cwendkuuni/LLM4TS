package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node nodeFor = new Node(Token.FOR, new int[]{1, 2, 3});
        Node nodeIf = new Node(Token.IF, new int[]{1, 2, 3, 4});
        Node nodeFunction = new Node(Token.FUNCTION, new int[]{1, 2, 3});

        assertTrue(NodeUtil.isForIn(nodeFor));
        assertFalse(NodeUtil.isForIn(nodeIf));
        assertFalse(NodeUtil.isForIn(nodeFunction));
    }

    @Test
    public void testIsStatement() {
        Node nodeFor = new Node(Token.FOR, new int[]{1, 2, 3});
        Node nodeIf = new Node(Token.IF, new int[]{1, 2, 3, 4});
        Node nodeFunction = new Node(Token.FUNCTION, new int[]{1, 2, 3});
        Node nodeScript = new Node(Token.SCRIPT, new int[]{1});
        Node nodeBlock = new Node(Token.BLOCK, new int[]{1, 2, 3});
        Node nodeLabel = new Node(Token.LABEL, new int[]{1});
        Node nodeExpression = new Node(Token.IDENT, new int[]{1});

        assertTrue(NodeUtil.isStatement(nodeFor));
        assertTrue(NodeUtil.isStatement(nodeIf));
        assertFalse(NodeUtil.isStatement(nodeFunction));
        assertTrue(NodeUtil.isStatement(nodeScript));
        assertTrue(NodeUtil.isStatement(nodeBlock));
        assertTrue(NodeUtil.isStatement(nodeLabel));
        assertFalse(NodeUtil.isStatement(nodeExpression));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{1, 2, 3});
        Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[]{1, 2, 3, 4, 5});

        assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
        assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node nodeFunctionDeclarationInScript = new Node(Token.FUNCTION, new int[]{1, 2, 3});
        Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, new int[]{1, 2, 3}, new Node(Token.FUNCTION, new int[]{1, 2, 3}));

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
    }

    @Test
    public void testInvalidArguments() {
        Node nodeNull = null;

        checkArgument(NodeUtil.isForIn(nodeNull) == false, "isForIn() called with null");
        checkState(NodeUtil.isStatement(nodeNull) == false, "isStatement() called with null");
        checkArgument(NodeUtil.isFunctionDeclaration(nodeNull) == false, "isFunctionDeclaration() called with null");
        checkArgument(NodeUtil.isHoistedFunctionDeclaration(nodeNull) == false, "isHoistedFunctionDeclaration() called with null");
    }
}