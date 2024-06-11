package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node nodeForIn = new Node(Token.FOR, 1, 2, 3);
        assertTrue(NodeUtil.isForIn(nodeForIn));

        Node nodeNotForIn = new Node(Token.IF, 1, 2, 3);
        assertFalse(NodeUtil.isForIn(nodeNotForIn));
    }

    @Test
    public void testIsStatement() {
        Node nodeScript = new Node(Token.SCRIPT, 1, 2, 3);
        assertTrue(NodeUtil.isStatement(nodeScript));

        Node nodeFunction = new Node(Token.FUNCTION, 1, 2, 3);
        assertTrue(NodeUtil.isStatement(nodeFunction));

        Node nodeIf = new Node(Token.IF, 1, 2, 3);
        assertFalse(NodeUtil.isStatement(nodeIf));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node nodeFunction = new Node(Token.FUNCTION, 1, 2, 3);
        nodeFunction.setProperty("name", "myFunction");
        assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));

        Node nodeFunctionExpression = new Node(Token.FUNCTION, 1, 2, 3);
        nodeFunctionExpression.setProperty("name", null);
        assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node nodeFunctionInScript = new Node(Token.FUNCTION, 1, 2, 3);
        nodeFunctionInScript.setProperty("name", "myFunction");
        Node script = new Node(Token.SCRIPT, 1, 2, 3);
        script.setChild(nodeFunctionInScript);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInScript));

        Node nodeFunctionInFunction = new Node(Token.FUNCTION, 1, 2, 3);
        nodeFunctionInFunction.setProperty("name", "myFunction");
        Node function = new Node(Token.FUNCTION, 1, 2, 3);
        function.setChild(nodeFunctionInFunction);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));
    }

    @Test
    public void testIsStatementWithNullParent() {
        Node node = new Node(Token.IF, 1, 2, 3);
        node.setParent(null);
        Preconditions.checkArgument(NodeUtil.isStatement(node), "Node is not a statement");
    }

    @Test
    public void testIsFunctionDeclarationWithNullParent() {
        Node nodeFunction = new Node(Token.FUNCTION, 1, 2, 3);
        nodeFunction.setParent(null);
        Preconditions.checkArgument(NodeUtil.isFunctionDeclaration(nodeFunction), "Node is not a function declaration");
    }

    @Test
    public void testIsHoistedFunctionDeclarationWithNullParent() {
        Node nodeFunctionInScript = new Node(Token.FUNCTION, 1, 2, 3);
        nodeFunctionInScript.setParent(null);
        Preconditions.checkArgument(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInScript), "Node is not a hoisted function declaration");
    }

    @Test
    public void testIsForInWithNullNode() {
        Preconditions.checkArgument(NodeUtil.isForIn(null), "Node cannot be null");
    }

    @Test
    public void testIsStatementWithNullNode() {
        Preconditions.checkArgument(NodeUtil.isStatement(null), "Node cannot be null");
    }

    @Test
    public void testIsFunctionDeclarationWithNullNode() {
        Preconditions.checkArgument(NodeUtil.isFunctionDeclaration(null), "Node cannot be null");
    }

    @Test
    public void testIsHoistedFunctionDeclarationWithNullNode() {
        Preconditions.checkArgument(NodeUtil.isHoistedFunctionDeclaration(null), "Node cannot be null");
    }
}