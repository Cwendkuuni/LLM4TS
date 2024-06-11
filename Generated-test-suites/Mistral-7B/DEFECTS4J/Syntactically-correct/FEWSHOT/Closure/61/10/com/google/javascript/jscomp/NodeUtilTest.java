package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn("for (var i in array) { }");
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeFor = Node.newFor("for (var i = 0; i < 10; i++) { }");
    assertFalse(NodeUtil.isForIn(nodeFor));

    Node nodeWhile = Node.newStatement("while (condition) { }");
    assertFalse(NodeUtil.isForIn(nodeWhile));

    Node nodeIf = Node.newStatement("if (condition) { }");
    assertFalse(NodeUtil.isForIn(nodeIf));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newScript("function foo() {}");
    assertTrue(NodeUtil.isStatement(nodeScript));

    Node nodeExpression = Node.newExpression("1 + 1");
    assertFalse(NodeUtil.isStatement(nodeExpression));

    Node nodeBlock = Node.newBlock("{ }");
    assertTrue(NodeUtil.isStatement(nodeBlock));

    Node nodeLabel = Node.newLabel("label");
    assertTrue(NodeUtil.isStatement(nodeLabel));

    Node nodeFunction = Node.newFunction("function foo() { }");
    assertTrue(NodeUtil.isStatement(nodeFunction));

    Node nodeStatement = Node.newStatement("{}");
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpressionStatement = Node.newExpressionStatement(Node.newName("x").setProperty("y", Node.newNumber(42)));
    assertTrue(NodeUtil.isStatement(nodeExpressionStatement));

    Node nodeIf = Node.newIfStatement(Node.newCondition(Node.newBinaryOperator(Node.newName("x"), Token.OP_EQ, Node.newNumber(42))), Node.newBlock("{}"), Node.newBlock("{}"));
    assertTrue(NodeUtil.isStatement(nodeIf));

    Node nodeWhile = Node.newWhileStatement(Node.newCondition(Node.newName("x")), Node.newBlock("{}"));
    assertTrue(NodeUtil.isStatement(nodeWhile));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction("function foo() {}");
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionStatement(Node.newFunction("function foo() {}"));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newScript("function foo() {}");
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeScript));

    Node nodeFunctionInFunction = Node.newFunction("function bar() { function foo() {} }");
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction.getFirstChild()));

    Node nodeFunctionInBlock = Node.newBlock("function foo() {}");
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInBlock));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsHoistedFunctionDeclarationNullParent() {
    Node nodeFunction = Node.newFunction("function foo() {}");
    checkState(nodeFunction.getParent() != null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsHoistedFunctionDeclarationNullNode() {
    Node node = null;
    checkState(node != null);
  }
}