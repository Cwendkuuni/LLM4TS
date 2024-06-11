package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeFor = Node.newForNode();
    Node nodeIf = Node.newIfStatement();
    Node nodeFunction = Node.newFunctionNode();

    assertTrue(NodeUtil.isForIn(nodeFor));
    assertFalse(NodeUtil.isForIn(nodeIf));
    assertFalse(NodeUtil.isForIn(nodeFunction));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newScriptNode();
    Node nodeFunction = Node.newFunctionNode();
    Node nodeIf = Node.newIfStatement();
    Node nodeExpression = Node.newIdentifierNode("x");

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeFunction));
    assertTrue(NodeUtil.isStatement(nodeIf));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = Node.newFunctionNode();
    Node nodeFunctionExpression = Node.newExpressionStatement(Node.newFunctionExpression(null, null, null));

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newScriptNode();
    Node nodeFunction = Node.newFunctionNode();
    Node nodeFunctionInsideFunction = Node.newFunctionNode();
    Node nodeFunctionInsideScript = Node.newFunctionNode();

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInsideScript));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInsideFunction));

    Node scriptWithFunction = Node.newScriptNode();
    scriptWithFunction.addChild(nodeFunction);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));

    Node functionWithFunction = Node.newFunctionNode();
    functionWithFunction.addChild(nodeFunction);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));
  }

  @Test
  public void testNullCheck() {
    assertNull(NodeUtil.isForIn(null));
    assertNull(NodeUtil.isStatement(null));
    assertNull(NodeUtil.isFunctionDeclaration(null));
  }

  @Test
  public void testUnexpectedParentType() {
    Node nodeFunction = Node.newFunctionNode();
    Node nodeNumber = Node.newNumberNode(1);

    assertFalse(NodeUtil.isStatement(nodeNumber));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeNumber));

    Node parentFunction = Node.newFunctionNode();
    parentFunction.addChild(nodeFunction);
    parentFunction.addChild(nodeNumber);

    assertFalse(NodeUtil.isStatement(nodeFunction));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunction));
  }
}