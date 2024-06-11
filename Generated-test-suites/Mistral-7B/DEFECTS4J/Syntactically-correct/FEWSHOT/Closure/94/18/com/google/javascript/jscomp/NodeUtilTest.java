package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForInStatement();
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeExpression = Node.newExpressionStatement(Node.newNumberLiteral(1));
    assertFalse(NodeUtil.isForIn(nodeExpression));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newScript();
    assertTrue(NodeUtil.isStatement(nodeScript));

    Node nodeExpression = Node.newExpressionStatement(Node.newNumberLiteral(1));
    assertTrue(NodeUtil.isStatement(nodeExpression));

    Node nodeFunction = Node.newFunction("func", Node.newFunctionParam("p"), Node.newBlock(Node.newExpressionStatement(Node.newNumberLiteral(1))));
    assertTrue(NodeUtil.isStatement(nodeFunction));

    Node nodeIf = Node.newIfStatement(Node.newExpressionStatement(Node.newBooleanLiteral(true)), Node.newBlock(Node.newExpressionStatement(Node.newNumberLiteral(1))), Node.newBlock());
    assertTrue(NodeUtil.isStatement(nodeIf));

    Node nodeLabel = Node.newLabel("label");
    assertTrue(NodeUtil.isStatement(nodeLabel));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = Node.newFunction("func", Node.newFunctionParam("p"), Node.newBlock(Node.newExpressionStatement(Node.newNumberLiteral(1))));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));

    Node nodeFunctionExpression = Node.newExpressionStatement(Node.newFunction("func", Node.newFunctionParam("p"), Node.newReturnStatement(Node.newNumberLiteral(1))));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionInScript = Node.newFunction("func", Node.newFunctionParam("p"), Node.newBlock(Node.newExpressionStatement(Node.newNumberLiteral(1))));
    Node nodeScript = Node.newScript();
    nodeScript.addChildToFront(nodeFunctionInScript);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInScript));

    Node nodeFunctionInFunction = Node.newFunction("func", Node.newFunctionParam("p"), Node.newBlock(Node.newExpressionStatement(Node.newNumberLiteral(1))));
    Node nodeFunction = Node.newFunction("outerFunc", Node.newFunctionParam("p"), Node.newBlock(nodeFunctionInFunction));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));
  }

  @Test
  public void testIsFunctionExpression() {
    Node nodeFunctionExpression = Node.newExpressionStatement(Node.newFunction("func", Node.newFunctionParam("p"), Node.newReturnStatement(Node.newNumberLiteral(1))));
    assertTrue(NodeUtil.isFunctionExpression(nodeFunctionExpression));

    Node nodeFunctionDeclaration = Node.newFunction("func", Node.newFunctionParam("p"), Node.newBlock(Node.newExpressionStatement(Node.newNumberLiteral(1))));
    assertFalse(NodeUtil.isFunctionExpression(nodeFunctionDeclaration));
  }

  @Test
  public void testIsNull() {
    Node nodeNull = Node.newNullLiteral();
    checkState(NodeUtil.isStatement(nodeNull));
    assertFalse(NodeUtil.isForIn(nodeNull));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeNull));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNull));
    assertFalse(NodeUtil.isFunctionExpression(nodeNull));
  }
}