package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node nodeForIn = Node.newForIn();
        assertTrue(NodeUtil.isForIn(nodeForIn));

        Node nodeNotForIn = Node.newExpression("1 + 2");;
        assertFalse(NodeUtil.isForIn(nodeNotForIn));
    }

    @Test
    public void testIsStatement() {
        Node nodeStatement = Node.newExpression("1 + 2");;
        Node nodeFunction = Node.newFunction("foo", new Node[0]);
        Node nodeScript = Node.newValue(Node.newText("function foo() {}"));

        assertTrue(NodeUtil.isStatement(nodeStatement.getFirstChild()));
        assertTrue(NodeUtil.isStatement(nodeFunction));
        assertTrue(NodeUtil.isStatement(nodeScript));

        Node parentScript = Node.newBlock(new Node[0]);
        parentScript.setFirstChild(nodeScript);

        assertTrue(NodeUtil.isStatement(nodeScript));
        assertTrue(NodeUtil.isStatement(nodeScript.getParent()));

        Node parentFunction = Node.newExpression("function foo() { 1 + 2; }");
        parentFunction.setFirstChild(nodeScript);

        assertTrue(NodeUtil.isStatement(nodeScript));
        assertTrue(NodeUtil.isStatement(nodeScript.getParent()));
        assertFalse(NodeUtil.isStatement(nodeFunction));

        Node nodeBlock = Node.newBlock(new Node[0]);
        nodeBlock.setFirstChild(nodeFunction);

        assertTrue(NodeUtil.isStatement(nodeFunction));
        assertTrue(NodeUtil.isStatement(nodeFunction.getParent()));
        assertFalse(NodeUtil.isStatement(nodeBlock));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node nodeFunction = Node.newFunction("foo", new Node[0]);
        Node nodeFunctionExpression = Node.newExpression("function foo() {}");

        assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));
        assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));

        Node nodeFunctionStatement = Node.newStatement(Node.newExpression("function foo() {}"));
        assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionStatement));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node nodeFunction = Node.newFunction("foo", new Node[0]);
        Node nodeFunctionInScript = Node.newValue(Node.newText("function foo() {}"));
        Node nodeFunctionInFunction = Node.newExpression("function bar() { function foo() {} }");

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInScript));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));
    }
}