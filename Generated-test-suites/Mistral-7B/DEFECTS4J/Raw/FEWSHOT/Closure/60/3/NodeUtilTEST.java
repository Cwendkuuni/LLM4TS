package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nFor = Node.newForStatement();
    assertTrue(NodeUtil.isForIn(nFor));

    Node nIf = Node.newStatement(Node.newIfStatement());
    assertFalse(NodeUtil.isForIn(nIf));

    Node nVariable = Node.newVariable("x");
    assertFalse(NodeUtil.isForIn(nVariable));
  }

  @Test
  public void testIsStatement() {
    Node nScript = Node.newScript();
    assertTrue(NodeUtil.isStatement(nScript));

    Node nVariable = Node.newVariable("x");
    assertTrue(NodeUtil.isStatement(nVariable));

    Node nIf = Node.newStatement(Node.newIfStatement());
    assertTrue(NodeUtil.isStatement(nIf));

    Node nFor = Node.newForStatement();
    assertTrue(NodeUtil.isStatement(nFor));

    Node nExpression = Node.newExpression(Node.newBinaryOperator("+", Node.newNumber(1), Node.newNumber(1)));
    assertFalse(NodeUtil.isStatement(nExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nFunction = Node.newFunction(Node.newIdentifier("foo"), null, Node.newBlock());
    assertTrue(NodeUtil.isFunctionDeclaration(nFunction));

    Node nExpression = Node.newFunction(Node.newIdentifier("foo"), null, Node.newExpression(Node.newIdentifier("x")));
    assertFalse(NodeUtil.isFunctionDeclaration(nExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nFunction = Node.newFunction(Node.newIdentifier("foo"), null, Node.newBlock());
    Node nScript = Node.newScript();
    nScript.setFirstChild(nFunction);
    Node nodeHoistedFunction = nScript;

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunction));

    Node nFunctionInFunction = Node.newFunction(Node.newIdentifier("foo"), null, Node.newBlock());
    Node nFunctionNode = Node.newFunction(Node.newIdentifier("bar"), null, Node.newBlock());
    Node nFunctionParent = Node.newFunction(Node.newIdentifier("baz"), null, Node.newBlock());
    nFunctionParent.setFirstChild(nFunctionNode);
    nFunctionNode.setFirstChild(nFunctionInFunction);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nFunctionInFunction));
  }
}