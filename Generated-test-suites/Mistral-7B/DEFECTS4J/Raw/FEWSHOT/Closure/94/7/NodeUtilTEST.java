package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn(0, null, null, null, null, null, null, null);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpressionStatement(Node.newIdentifier("x"), null);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newScript(null);
    Node nodeBlock = Node.newBlock(null);
    Node nodeLabel = Node.newLabel("label", null);
    Node nodeStatement = Node.newExpressionStatement(Node.newIdentifier("x"), null);
    Node nodeExpression = Node.newExpressionStatement(Node.newBinaryOperator(Node.newIdentifier("x"), Token.PLUS, Node.newNumber(1)), null);

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeBlock));
    assertTrue(NodeUtil.isStatement(nodeLabel));
    assertTrue(NodeUtil.isStatement(nodeStatement));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(
        Node.newIdentifier("myFunction"),
        null,
        Node.newBlock(null),
        null,
        null,
        null,
        null,
        null);
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionStatement(Node.newFunction(Node.newIdentifier("myFunction"), null, null), null);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclarationInRootScope = Node.newFunction(
        Node.newIdentifier("myFunction"),
        null,
        Node.newBlock(null),
        null,
        null,
        null,
        null,
        null);
    Node nodeFunctionDeclarationInFunctionScope = Node.newFunction(
        Node.newIdentifier("myFunction"),
        null,
        Node.newBlock(null),
        null,
        null,
        null,
        Node.newFunction(Node.newIdentifier("outerFunction"), null, Node.newBlock(null), null, null, null, null, null),
        null);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInRootScope));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunctionScope));
  }

  @Test
  public void testIsHoistedFunctionDeclarationWithNullParent() {
    Node nodeFunctionDeclaration = Node.newFunction(
        Node.newIdentifier("myFunction"),
        null,
        Node.newBlock(null),
        null,
        null,
        null,
        null,
        null);

    checkState(nodeFunctionDeclaration.getParent() == null, "Unexpected parent node");
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
  }
}