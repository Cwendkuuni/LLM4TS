package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn("for (var i in obj) { }");
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeForStatement = Node.newFor("for (var i = 0; i < arr.length; i++) { }");
    assertFalse(NodeUtil.isForIn(nodeForStatement));

    Node nodeWhile = Node.newStatement("while (condition) { }");
    assertFalse(NodeUtil.isForIn(nodeWhile));

    Node nodeIf = Node.newStatement("if (condition) { }");
    assertFalse(NodeUtil.isForIn(nodeIf));

    Node nodeVariable = Node.newVariable("var x;");
    assertFalse(NodeUtil.isForIn(nodeVariable));

    Node nodeFunction = Node.newFunction("function foo() {}");
    assertFalse(NodeUtil.isForIn(nodeFunction));
  }

  @Test
  public void testIsStatement() {
    Node nodeForIn = Node.newForIn("for (var i in obj) { }");
    assertTrue(NodeUtil.isStatement(nodeForIn.getFirstChild()));

    Node nodeForStatement = Node.newFor("for (var i = 0; i < arr.length; i++) { }");
    assertTrue(NodeUtil.isStatement(nodeForStatement.getFirstChild()));

    Node nodeWhile = Node.newStatement("while (condition) { }");
    assertTrue(NodeUtil.isStatement(nodeWhile));

    Node nodeIf = Node.newStatement("if (condition) { }");
    assertTrue(NodeUtil.isStatement(nodeIf));

    Node nodeVariable = Node.newVariable("var x;");
    assertFalse(NodeUtil.isStatement(nodeVariable));

    Node nodeFunction = Node.newFunction("function foo() {}");
    assertFalse(NodeUtil.isStatement(nodeFunction));

    Node nodeExpression = Node.newExpression("1 + 1");
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction("function foo() {}");
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpression("function foo() {}");
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));

    Node nodeVariable = Node.newVariable("function foo() {}");
    assertFalse(NodeUtil.isFunctionDeclaration(nodeVariable));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionInRootScope = Node.newFunction("function foo() {}");
    Node nodeScript = Node.newScript("function bar() {}");
    Node nodeFunctionInFunctionScope = Node.newFunction("function foo() {}", null, nodeScript);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInRootScope));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunctionScope));
  }
}