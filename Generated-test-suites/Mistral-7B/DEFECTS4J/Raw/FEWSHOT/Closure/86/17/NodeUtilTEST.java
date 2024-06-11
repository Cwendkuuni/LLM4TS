package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeFor = Node.newForStatement(null, null, null, null, null, null, null, null, null);
    Node nodeIf = Node.newIfStatement(null, null, null, null, null, null, null);

    assertTrue(NodeUtil.isForIn(nodeFor));
    assertFalse(NodeUtil.isForIn(nodeIf));
  }

  @Test
  public void testIsStatement() {
    Node nodeFunction = Node.newFunction(
        null, "funcName", null, null, Node.newBlock(null), null, null);
    Node nodeExpression = Node.newExpressionStatement(Node.newIdentifier("x"), Node.newNumber(5));

    assertTrue(NodeUtil.isStatement(nodeFunction));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = Node.newFunction(
        null, "funcName", null, null, Node.newBlock(null), null, null);
    Node nodeFunctionExpression = Node.newExpressionStatement(Node.newFunction(null, null, null, null), null);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newScript(null);
    Node nodeFunction = Node.newFunction(
        null, "funcName", null, null, Node.newBlock(null), null, null);
    Node nodeFunctionInFunction = Node.newFunction(
        null, null, null, null, Node.newBlock(Node.newExpressionStatement(Node.newIdentifier("x"), Node.newNumber(5))), null, null);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));
  }

  @Test
  public void testIsHoistedFunctionDeclarationInScript() {
    Node nodeScript = Node.newScript(Node.newFunction(null, "funcName", null, null, Node.newBlock(null), null, null));

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeScript.getFirstChild()));
  }
}