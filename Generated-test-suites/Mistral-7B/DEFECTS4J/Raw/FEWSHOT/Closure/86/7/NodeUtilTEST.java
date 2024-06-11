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

    Node nodeIf = Node.newIf();
    assertFalse(NodeUtil.isForIn(nodeIf));

    Node nodeFunction = Node.newFunction();
    assertFalse(NodeUtil.isForIn(nodeFunction));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newScript();
    assertTrue(NodeUtil.isStatement(nodeScript));

    Node nodeIf = Node.newIf();
    assertTrue(NodeUtil.isStatement(nodeIf));

    Node nodeFunctionExpression = Node.newFunctionExpression();
    assertFalse(NodeUtil.isStatement(nodeFunctionExpression));

    Node nodeVariable = Node.newIdentifier("x");
    assertFalse(NodeUtil.isStatement(nodeVariable));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = Node.newFunction();
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunction));

    Node nodeFunctionDeclaration = Node.newFunctionDeclaration("foo");
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newFunctionExpression();
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunction = Node.newFunction();
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));

    Node nodeFunctionDeclarationInScript = Node.newFunctionDeclaration("foo");
    Node parentScript = Node.newScript();
    parentScript.addChild(nodeFunctionDeclarationInScript);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));

    Node nodeFunctionDeclarationInFunction = Node.newFunction();
    Node parentFunction = Node.newFunction();
    parentFunction.addChild(nodeFunctionDeclarationInFunction);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }
}