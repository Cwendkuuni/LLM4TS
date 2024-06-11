package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.*;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForInStatement(1, 1, 3, null, null, null, null, null);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpressionStatement(1, Node.newIdentifier("x"), Node.newNumberLiteral(5));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpressionStatement(1, Node.newIdentifier("x"), Node.newNumberLiteral(5));
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeNotStatement = Node.newIdentifier("x");
    assertFalse(NodeUtil.isStatement(nodeNotStatement));

    Node nodeFunction = Node.newFunction(1, Node.newIdentifier("foo"), null, Node.newBlock(1, null));
    assertTrue(NodeUtil.isStatement(nodeFunction));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(1, Node.newIdentifier("foo"), null, Node.newBlock(1, null));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionStatement(1, Node.newIdentifier("x"), Node.newFunction(1, null, null, Node.newBlock(1, null)));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newScript(1, null);
    Node nodeFunctionDeclarationInScript = Node.newFunction(1, Node.newIdentifier("foo"), null, Node.newBlock(1, null));
    nodeScript.setFirstChild(nodeFunctionDeclarationInScript);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));

    Node nodeFunctionDeclarationInFunction = Node.newFunction(1, Node.newIdentifier("bar"), null, Node.newBlock(1, null));
    Node nodeFunction = Node.newFunction(1, null, null, Node.newBlock(1, null));
    nodeFunction.setFirstChild(nodeFunctionDeclarationInFunction);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }
}