package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node nodeForIn = Node.newNode(Token.FOR, null, null, null, null, null);
        Node nodeNotForIn = Node.newNode(Token.IF, null, null, null, null, null);

        assertTrue(NodeUtil.isForIn(nodeForIn));
        assertFalse(NodeUtil.isForIn(nodeNotForIn));
    }

    @Test
    public void testIsStatement() {
        Node nodeScript = Node.newNode(Token.SCRIPT, null, null, null, null, null);
        Node nodeBlock = Node.newNode(Token.BLOCK, null, null, null, null, null);
        Node nodeLabel = Node.newNode(Token.LABEL, "label", null, null, null, null);
        Node nodeStatement = Node.newNode(Token.ASSIGN, null, Node.newNode("x", null),
                Node.newNode(Token.NUM, 42), null, null);
        Node nodeExpression = Node.newNode(Token.ASSIGN, null, Node.newNode("x", null),
                Node.newNode(Token.PLUS, Node.newNode(Token.NUM, 2), Node.newNode(Token.NUM, 3)), null, null);
        Node nodeFunction = Node.newNode(Token.FUNCTION, "funcName", null, null, null, null);

        assertTrue(NodeUtil.isStatement(nodeScript));
        assertTrue(NodeUtil.isStatement(nodeBlock));
        assertTrue(NodeUtil.isStatement(nodeLabel));
        assertTrue(NodeUtil.isStatement(nodeStatement));
        assertFalse(NodeUtil.isStatement(nodeExpression));
        assertFalse(NodeUtil.isStatement(nodeFunction));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, "funcName", null, null, null, null);
        Node nodeFunctionExpression = Node.newNode(Token.ASSIGN, null, null, nodeFunctionDeclaration, null, null);

        assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
        assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node nodeScript = Node.newNode(Token.SCRIPT, null, null, null, null, null);
        Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, "funcName", null, null, null, null);
        Node nodeFunctionDeclarationInFunction = Node.newNode(Token.FUNCTION, null, null, null, null, nodeFunctionDeclaration);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
    }

    @Test
    public void testNullParent() {
        Node node = Node.newNode(Token.NUM, 42);

        checkState(NodeUtil.isStatement(node), "Expecting isStatement to return true for a standalone node.");
    }
}