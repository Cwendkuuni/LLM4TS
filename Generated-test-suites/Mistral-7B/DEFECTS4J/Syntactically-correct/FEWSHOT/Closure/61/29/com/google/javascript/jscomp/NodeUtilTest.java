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

    Node nodeFunction = Node.newFunction();
    assertFalse(NodeUtil.isStatement(nodeFunction));

    Node nodeAssign = Node.newAssign();
    assertTrue(NodeUtil.isStatement(nodeAssign));

    Node nodeExpression = Node.newExpression();
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = Node.newFunction();
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunction));

    Node nodeFunctionDecl = Node.newFunctionDeclaration();
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDecl));

    Node nodeAssignFunction = Node.newAssignFunction();
    assertFalse(NodeUtil.isFunctionDeclaration(nodeAssignFunction));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunction = Node.newFunction();
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));

    Node nodeFunctionDeclaration = Node.newFunctionDeclaration();
    Node parentScript = Node.newScript();
    parentScript.addChild(nodeFunctionDeclaration);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = Node.newFunctionDeclaration();
    Node parentFunction = Node.newFunction();
    parentFunction.addChild(nodeFunctionDeclarationInFunction);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }

  @Test
  public void testIsFunctionExpression() {
    Node nodeFunctionExpression = Node.newFunctionExpression();
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));

    Node nodeFunctionDeclaration = Node.newFunctionDeclaration();
    Node functionExpressionChild = Node.newExpression();
    functionExpressionChild.setType(Token.FUNCTION);
    nodeFunctionDeclaration.addChild(functionExpressionChild);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionExpression(nodeFunctionDeclaration));

    Node nodeFunctionExpressionInFunction = Node.newExpression();
    nodeFunctionExpressionInFunction.setType(Token.FUNCTION);
    Node parentFunction = Node.newFunction();
    parentFunction.addChild(nodeFunctionExpressionInFunction);
    assertTrue(NodeUtil.isFunctionExpression(nodeFunctionExpressionInFunction));
  }

  @Test
  public void testPreconditionsCheck() {
    Node nullNode = null;
    Node nonParentNode = Node.newExpression();

    try {
      NodeUtil.isStatement(nullNode);
    } catch (IllegalArgumentException e) {
      return;
    }
    throw new AssertionError("Expected IllegalArgumentException");

    try {
      NodeUtil.isStatement(nonParentNode);
    } catch (IllegalArgumentException e) {
      checkState(e.getMessage().startsWith("Parent node is null"), "Unexpected message");
    }
  }
}