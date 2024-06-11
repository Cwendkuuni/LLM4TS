package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn(1);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeIf = Node.newIf(1);
    assertFalse(NodeUtil.isForIn(nodeIf));

    Node nodeFunction = Node.newFunction(1, 1);
    assertFalse(NodeUtil.isForIn(nodeFunction));

    Node nodeVariable = Node.newVar("x", 1);
    assertFalse(NodeUtil.isForIn(nodeVariable));
  }

  @Test
  public void testIsStatement() {
    Node nodeFunction = Node.newFunction(1, 1);
    Node parentScript = Node.newText("", Token.SCRIPT);
    Node parentBlock = Node.newBlock(1);
    Node parentLabel = Node.newLabel("label1", 1);

    assertTrue(NodeUtil.isStatement(nodeFunction));
    assertTrue(NodeUtil.isStatement(parentScript.getFirstChild()));
    assertTrue(NodeUtil.isStatement(parentBlock.getFirstChild()));
    assertFalse(NodeUtil.isStatement(nodeFunction.getParent()));
    assertFalse(NodeUtil.isStatement(parentLabel));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = Node.newFunction(1, 1);
    Node nodeFunctionExpression = Node.newExpressionStatement(Node.newFunctionExpression(1, "x", 1));

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunction = Node.newFunction(1, 1);
    Node parentScript = Node.newText("", Token.SCRIPT);
    Node parentFunction = Node.newFunction(1, 1, Node.newName("f", 1));

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(parentFunction.getFirstChild()));
  }
}