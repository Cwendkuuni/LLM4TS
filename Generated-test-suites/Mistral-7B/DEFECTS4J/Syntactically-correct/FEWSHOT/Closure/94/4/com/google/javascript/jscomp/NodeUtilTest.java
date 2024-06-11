package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, null, 0, 0);
    Node nodeNotForIn = new Node(Token.ASSIGN, null, 0, 0);

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT, null, 0, 0);
    Node nodeBlock = new Node(Token.BLOCK, null, 0, 0);
    Node nodeLabel = new Node(Token.LABEL, "label", 0, 0);
    Node nodeFunction = new Node(Token.FUNCTION, null, 0, 0);
    Node nodeExpression = new Node(Token.ASSIGN, null, 0, 0);

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeBlock));
    assertTrue(NodeUtil.isStatement(nodeLabel));
    assertFalse(NodeUtil.isStatement(nodeFunction));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, "functionName", 0, 0);
    Node nodeFunctionExpression = new Node(Token.FUNCTION, null, 0, 0);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = new Node(Token.SCRIPT, null, 0, 0);
    Node nodeFunctionDeclarationInScript = new Node(Token.FUNCTION, "functionName", 0, 0);
    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, null, 0, 0);
    Node nodeFunctionDeclarationInBlock = new Node(Token.BLOCK, null, 0, 0);
    Node nodeFunctionDeclarationInLabel = new Node(Token.LABEL, "label", 0, 0);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInBlock));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInLabel));
  }

  @Test
  public void testIsFunctionDeclarationOrExpression() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, "functionName", 0, 0);
    Node nodeFunctionExpression = new Node(Token.FUNCTION, null, 0, 0);

    Node nodeParentScript = new Node(Token.SCRIPT, null, 0, 0);
    Node nodeParentFunction = new Node(Token.FUNCTION, null, 0, 0);
    Node nodeParentBlock = new Node(Token.BLOCK, null, 0, 0);

    Node nodeGrandParentScript = new Node(Token.SCRIPT, null, 0, 0);
    Node nodeGrandParentFunction = new Node(Token.FUNCTION, null, 0, 0);

    assertTrue(NodeUtil.isFunctionDeclarationOrExpression(nodeFunctionDeclaration, nodeParentScript));
    assertTrue(NodeUtil.isFunctionDeclarationOrExpression(nodeFunctionExpression, nodeParentFunction));
    assertTrue(NodeUtil.isFunctionDeclarationOrExpression(nodeFunctionDeclaration, nodeParentBlock));
    assertFalse(NodeUtil.isFunctionDeclarationOrExpression(nodeFunctionDeclaration, nodeParentFunction.getChild(0)));
    assertFalse(NodeUtil.isFunctionDeclarationOrExpression(nodeFunctionDeclaration, nodeGrandParentScript));
    assertFalse(NodeUtil.isFunctionDeclarationOrExpression(nodeFunctionDeclaration, nodeGrandParentFunction));
  }

  @Test
  public void testIsFunctionDeclarationOrExpressionWithNullParent() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, "functionName", 0, 0);

    assertFalse(NodeUtil.isFunctionDeclarationOrExpression(nodeFunctionDeclaration, null));
  }

  @Test
  public void testIsFunctionDeclarationOrExpressionWithEmptyParent() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, "functionName", 0, 0);
    Node nodeEmptyParent = new Node(Token.EMPTY, null, 0, 0);

    assertFalse(NodeUtil.isFunctionDeclarationOrExpression(nodeFunctionDeclaration, nodeEmptyParent));
  }

  @Test
  public void testIsFunctionDeclarationOrExpressionWithNonFunctionChild() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, "functionName", 0, 0);
    Node nodeChild = new Node(Token.ASSIGN, null, 0, 0);

    assertFalse(NodeUtil.isFunctionDeclarationOrExpression(nodeFunctionDeclaration, nodeChild));
  }

  @Test
  public void testIsFunctionDeclarationOrExpressionWithFunctionChild() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, "functionName", 0, 0);
    Node nodeFunctionChild = new Node(Token.FUNCTION, null, 0, 0);

    assertTrue(NodeUtil.isFunctionDeclarationOrExpression(nodeFunctionDeclaration, nodeFunctionChild));
  }

  @Test
  public void testIsFunctionDeclarationOrExpressionWithMultipleChildren() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, "functionName", 0, 0);
    Node nodeChild1 = new Node(Token.ASSIGN, null, 0, 0);
    Node nodeChild2 = new Node(Token.FUNCTION, null, 0, 0);

    assertFalse(NodeUtil.isFunctionDeclarationOrExpression(nodeFunctionDeclaration, nodeChild1));
    assertTrue(NodeUtil.isFunctionDeclarationOrExpression(nodeFunctionDeclaration, nodeChild2));
  }

  @Test
  public void testIsFunctionDeclarationOrExpressionWithMixedChildren() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, "functionName", 0, 0);
    Node nodeChild1 = new Node(Token.ASSIGN, null, 0, 0);
    Node nodeChild2 = new Node(Token.FUNCTION, null, 0, 0);

    assertFalse(NodeUtil.isFunctionDeclarationOrExpression(nodeFunctionDeclaration, nodeChild1));
    assertTrue(NodeUtil.isFunctionDeclarationOrExpression(nodeFunctionDeclaration, nodeChild2));
  }

  @Test
  public void testIsFunctionDeclarationOrExpressionWithMixedParents() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, "functionName", 0, 0);
    Node nodeParentScript = new Node(Token.SCRIPT, null, 0, 0);
    Node nodeParentFunction = new Node(Token.FUNCTION, null, 0, 0);

    assertTrue(NodeUtil.isFunctionDeclarationOrExpression(nodeFunctionDeclaration, nodeParentScript));
    assertFalse(NodeUtil.isFunctionDeclarationOrExpression(nodeFunctionDeclaration, nodeParentFunction));
  }

  @Test
  public void testIsFunctionDeclarationOrExpressionWithMixedGrandParents() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, "functionName", 0, 0);
    Node nodeGrandParentScript = new Node(Token.SCRIPT, null, 0, 0);
    Node nodeGrandParentFunction = new Node(Token.FUNCTION, null, 0, 0);

    assertTrue(NodeUtil.isFunctionDeclarationOrExpression(nodeFunctionDeclaration, nodeGrandParentScript));
    assertFalse(NodeUtil.isFunctionDeclarationOrExpression(nodeFunctionDeclaration, nodeGrandParentFunction));
  }

  @Test
  public void testIsFunctionDeclarationOrExpressionWithNullChild() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, "functionName", 0, 0);

    assertFalse(NodeUtil.isFunctionDeclarationOrExpression(nodeFunctionDeclaration, null));
  }

  @Test
  public void testIsFunctionDeclarationOrExpressionWithEmptyChild() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, "functionName", 0, 0);
    Node nodeEmptyChild = new Node(Token.EMPTY, null, 0, 0);

    assertFalse(NodeUtil.isFunctionDeclarationOrExpression(nodeFunctionDeclaration, nodeEmptyChild));
  }
}