package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeFor = Node.newFor();
    assertTrue(NodeUtil.isForIn(nodeFor));

    Node nodeIf = Node.newIf();
    assertFalse(NodeUtil.isForIn(nodeIf));

    Node nodeFunction = Node.newFunction("func");
    assertFalse(NodeUtil.isForIn(nodeFunction));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newScript("func() {}");
    assertTrue(NodeUtil.isStatement(nodeScript));

    Node nodeFunctionExpression = Node.newExpression(Node.newFunction("func"));
    assertFalse(NodeUtil.isStatement(nodeFunctionExpression));

    Node nodeIf = Node.newIf();
    assertFalse(NodeUtil.isStatement(nodeIf));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = Node.newFunction("func");
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));

    Node nodeFunctionExpression = Node.newExpression(Node.newFunction("func"));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newScript("function func() {}");
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeScript));

    Node nodeFunctionInExpression = Node.newExpression(
        Node.newFunction("func"), Node.newIdentifier("expr"));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInExpression));

    Node nodeFunctionInFunction = Node.newFunction(
        "function func() {}", Node.newIdentifier("funcName"));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));
  }

  @Test
  public void testNullParent() {
    Node node = Node.newNull();
    checkState(NodeUtil.isStatement(node), "Expected statement, got null parent");
  }
}