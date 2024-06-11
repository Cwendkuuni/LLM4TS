package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn();
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpressionStatement(Node.newIdentifier("x"));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newScript();
    assertTrue(NodeUtil.isStatement(nodeScript));

    Node nodeExpression = Node.newExpressionStatement(Node.newNumberLiteral(1));
    assertTrue(NodeUtil.isStatement(nodeExpression));

    Node nodeFunction = Node.newFunction(Node.newIdentifier("foo"), new int[0], Node.newBlock(null));
    assertTrue(NodeUtil.isStatement(nodeFunction));

    Node nodeNotStatement = Node.newCall(Node.newIdentifier("console"), Node.newIdentifier("log"), Node.newStringLiteral("hello"));
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(Node.newIdentifier("foo"), new int[0], Node.newBlock(null));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionStatement(Node.newAssignment(Node.newIdentifier("x"), Node.newFunction(Node.newIdentifier("y"), new int[0], Node.newBlock(null))));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newScript();
    Node nodeFunctionDeclaration = Node.newFunction(Node.newIdentifier("foo"), new int[0], Node.newBlock(null));
    Node nodeFunctionDeclarationInFunction = Node.newFunction(Node.newIdentifier("bar"), new int[0], Node.newBlock(Node.newStatement(Node.newExpressionStatement(nodeFunctionDeclaration))));

    Node nodeFunctionDeclarationAtTopLevel = Node.newBlock(Node.newStatement(Node.newExpressionStatement(nodeFunctionDeclaration)));

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationAtTopLevel));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }
}