package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeFor = Node.newForStatement(1, 1, 1, null, null, null, null, null, null, null, null, null);
    assertTrue(NodeUtil.isForIn(nodeFor));

    Node nodeIf = Node.newExpressionStatement(Node.newIfStatement(1, null, null, null, null, null));
    assertFalse(NodeUtil.isForIn(nodeIf));

    Node nodeFunction = Node.newFunction(1, "test", null, null, null, null, null, null);
    assertFalse(NodeUtil.isForIn(nodeFunction));
  }

  @Test
  public void testIsStatement() {
    Node nodeFunction = Node.newFunction(1, "test", null, null, null, null, null, null);
    assertTrue(NodeUtil.isStatement(nodeFunction));

    Node nodeIf = Node.newExpressionStatement(Node.newIfStatement(1, null, null, null, null, null));
    assertTrue(NodeUtil.isStatement(nodeIf));

    Node nodeFor = Node.newForStatement(1, 1, 1, null, null, null, null, null, null, null, null, null);
    assertTrue(NodeUtil.isStatement(nodeFor));

    Node nodeVariable = Node.newVar(1, "test", null, null, null);
    assertTrue(NodeUtil.isStatement(nodeVariable));

    Node nodeBreak = Node.newExpressionStatement(Node.newBreakStatement(1));
    assertTrue(NodeUtil.isStatement(nodeBreak));

    Node nodeContinue = Node.newExpressionStatement(Node.newContinueStatement(1));
    assertTrue(NodeUtil.isStatement(nodeContinue));

    Node nodeReturn = Node.newExpressionStatement(Node.newReturnStatement(1, null));
    assertTrue(NodeUtil.isStatement(nodeReturn));

    Node nodeIdentifier = Node.newIdentifier("test");
    assertFalse(NodeUtil.isStatement(nodeIdentifier));

    Node nodeNull = Node.newNull();
    assertFalse(NodeUtil.isStatement(nodeNull));

    Node nodeBoolean = Node.newBoolean(true);
    assertFalse(NodeUtil.isStatement(nodeBoolean));

    Node nodeNumber = Node.newNumber(1);
    assertFalse(NodeUtil.isStatement(nodeNumber));

    Node nodeString = Node.newString("test");
    assertFalse(NodeUtil.isStatement(nodeString));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = Node.newFunction(1, "test", null, null, null, null, null, null);
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));

    Node nodeFunctionExpression = Node.newExpressionStatement(Node.newFunctionExpression(1, "test", null, null, null, null));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));

    Node nodeVariable = Node.newVar(1, "test", null, Node.newFunction(1, "test", null, null, null, null, null, null), null);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeVariable));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunction = Node.newFunction(1, "test", null, null, null, null, null, null);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));

    Node nodeFunctionInScript = Node.newFunction(1, "test", null, null, null, null, null, null);
    Node scriptNode = Node.newScript(1, null, null, null);
    scriptNode.setChild(0, nodeFunction);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));

    Node nodeFunctionInFunction = Node.newFunction(1, "test", null, null, null, null, null, null);
    Node functionNode = Node.newFunction(1, "testFunction", null, null, null, null, null, null);
    functionNode.setChild(0, nodeFunction);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));
  }
}