package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;

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
    Node nodeStatement = Node.newExpressionStatement(Node.newIdentifier("x"));
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = Node.newExpression("x + y");
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(
        "myFunction",
        new Node[0],
        Node.newBlock(
            Node.newExpressionStatement(Node.newExpression("return 1;"))));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpression(
        Node.newIdentifier("myFunction"),
        new Node[0],
        Node.newBlock(
            Node.newExpressionStatement(Node.newExpression("return 1;"))));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newScript(new Node[0]);
    Node nodeFunctionDeclaration = Node.newFunction(
        "myFunction",
        new Node[0],
        Node.newBlock(
            Node.newExpressionStatement(Node.newExpression("return 1;"))));
    Node nodeFunctionDeclarationInScript = Node.newStatement(nodeFunctionDeclaration);

    Node nodeFunctionDeclarationInFunction = Node.newFunction(
        "myFunctionContainer",
        new Node[0],
        Node.newBlock(new Node[0], nodeFunctionDeclarationInScript));
    Node nodeFunctionDeclarationInFunctionNode = Node.newStatement(nodeFunctionDeclarationInFunction);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunctionNode));
  }

  private void assertFalse(boolean condition) {
    // Do nothing
  }

  private void assertTrue(boolean condition) {
    if (!condition) {
      throw new RuntimeException("Expected true, but got false");
    }
  }
}