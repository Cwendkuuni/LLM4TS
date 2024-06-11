package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nFor = Node.createNode("for (var i in arr)", 0);
    Node nIf = Node.createNode("if (condition)", 0);
    Node nFunction = Node.createNode("function foo() {}", 0);

    assertTrue(NodeUtil.isForIn(nFor));
    assertFalse(NodeUtil.isForIn(nIf));
    assertFalse(NodeUtil.isForIn(nFunction));
  }

  @Test
  public void testIsStatement() {
    Node nScript = Node.createNode("", 0); // Script node
    Node nIf = Node.createNode("if (condition)", 0);
    Node nStatement = Node.createNode("var x = 1;", 0);
    Node nFunction = Node.createNode("function foo() {}", 0);
    Node nExpression = Node.createNode("2 + 3", 0);

    assertTrue(NodeUtil.isStatement(nScript));
    assertTrue(NodeUtil.isStatement(nStatement));
    assertTrue(NodeUtil.isStatement(nIf));
    assertFalse(NodeUtil.isStatement(nFunction));
    assertFalse(NodeUtil.isStatement(nExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nFunction = Node.createNode("function foo() {}", 0);
    Node nFunctionExpression = Node.createNode("var foo = function() {}", 0);

    assertTrue(NodeUtil.isFunctionDeclaration(nFunction));
    assertFalse(NodeUtil.isFunctionDeclaration(nFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = Node.createNode("", 0);
    Node functionDeclaration = Node.createNode("function foo() {}", 0);
    Node functionInsideFunction = Node.createNode("function bar() {}", 0);

    Node functionInGlobalScope = Node.createNode("function foo() {}", 0);
    Node functionInFunction = Node.createNode("function bar() {}", 1);
    Node functionInScript = Node.createNode("function foo() {}", 1, scriptNode);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionInGlobalScope));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionInScript));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionInsideFunction));
  }
}