package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node nodeForIn = new Node(Token.FOR, 1, 1, 1, new Node[3]);
        assertTrue(NodeUtil.isForIn(nodeForIn));

        Node nodeNotForIn = new Node(Token.IF, 1, 1, 1, new Node[0]);
        assertFalse(NodeUtil.isForIn(nodeNotForIn));

        Node nodeEmpty = new Node(Token.NUL, 1, 1, 1, new Node[0]);
        assertFalse(NodeUtil.isForIn(nodeEmpty));
    }

    @Test
    public void testIsStatement() {
        Node nodeScript = new Node(Token.SCRIPT, 1, 1, 1, new Node[0]);
        assertTrue(NodeUtil.isStatement(nodeScript));

        Node nodeFunction = new Node(Token.FUNCTION, 1, 1, 1, new Node[0]);
        assertFalse(NodeUtil.isStatement(nodeFunction));

        Node nodeExpression = new Node(Token.IF, 1, 1, 1, new Node[1]);
        assertFalse(NodeUtil.isStatement(nodeExpression));

        Node nodeStatement = new Node(Token.ASSIGN, 1, 1, 1, new Node[1]);
        assertTrue(NodeUtil.isStatement(nodeStatement));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node nodeFunctionDeclaration = new Node(Token.FUNCTION, 1, 1, 1, new Node[1]);
        new Node("id", 1, 1, 1, new Node[0]); // function name
        Node nodeBody = new Node(Token.BLOCK, 1, 1, 1, new Node[0]);
        nodeFunctionDeclaration.setChildren(new Node[] { new Node("id"), nodeBody });

        Node nodeFunctionExpression = new Node(Token.FUNCTION, 1, 1, 1, new Node[1]);
        new Node("id", 1, 1, 1, new Node[0]); // function name
        Node nodeExpression = new Node(Token.ASSIGN, 1, 1, 1, new Node[1]);
        nodeExpression.setChildren(new Node[] { nodeFunctionExpression });
        Node nodeStatement = new Node(Token.STATEMENT, 1, 1, 1, new Node[1]);
        nodeStatement.setChildren(new Node[] { nodeExpression });
        Node nodeBlock = new Node(Token.BLOCK, 1, 1, 1, new Node[1]);
        nodeBlock.setChildren(new Node[] { nodeStatement });
        nodeFunctionExpression.setParent(nodeBlock);

        assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
        assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node nodeScript = new Node(Token.SCRIPT, 1, 1, 1, new Node[0]);
        Node nodeFunctionDeclaration = new Node(Token.FUNCTION, 1, 1, 1, new Node[1]);
        new Node("id", 1, 1, 1, new Node[0]); // function name
        Node nodeBody = new Node(Token.BLOCK, 1, 1, 1, new Node[0]);
        nodeFunctionDeclaration.setChildren(new Node[] { new Node("id"), nodeBody });
        nodeScript.setChildren(new Node[] { nodeFunctionDeclaration });

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));

        Node nodeFunction = new Node(Token.FUNCTION, 1, 1, 1, new Node[0]);
        new Node("id", 1, 1, 1, new Node[0]); // function name
        Node nodeBody2 = new Node(Token.BLOCK, 1, 1, 1, new Node[0]);
        nodeFunction.setChildren(new Node[] { new Node("id"), nodeBody2 });
        Node nodeStatement = new Node(Token.STATEMENT, 1, 1, 1, new Node[1]);
        nodeStatement.setChildren(new Node[] { nodeFunction });
        Node nodeBlock = new Node(Token.BLOCK, 1, 1, 1, new Node[1]);
        nodeBlock.setChildren(new Node[] { nodeStatement });
        Node nodeExpression = new Node(Token.ASSIGN, 1, 1, 1, new Node[1]);
        nodeExpression.setChildren(new Node[] { nodeBlock });
        Node nodeFunction2 = new Node(Token.FUNCTION, 1, 1, 1, new Node[0]);
        new Node("id2", 1, 1, 1, new Node[0]); // function name
        nodeFunction2.setParent(nodeExpression);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));
    }
}