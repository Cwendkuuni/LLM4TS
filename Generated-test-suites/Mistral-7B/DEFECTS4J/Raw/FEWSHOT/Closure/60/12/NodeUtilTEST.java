package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn();
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeWhile = Node.newWhile();
    assertFalse(NodeUtil.isForIn(nodeWhile));

    Node nodeIf = Node.newIf();
    assertFalse(NodeUtil.isForIn(nodeIf));

    Node nodeVariable = Node.newVariable("x");
    assertFalse(NodeUtil.isForIn(nodeVariable));
  }

  @Test
  public void testIsStatement() {
    Node nodeFunction = Node.newFunction("func", null);
    assertTrue(NodeUtil.isStatement(nodeFunction));

    Node nodeIf = Node.newIf();
    assertTrue(NodeUtil.isStatement(nodeIf));

    Node nodeWhile = Node.newWhile();
    assertTrue(NodeUtil.isStatement(nodeWhile));

    Node nodeVariable = Node.newVariable("x");
    assertFalse(NodeUtil.isStatement(nodeVariable));

    Node nodeExpression = Node.newName("x + y");
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = Node.newFunction("func", null);
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));

    Node nodeFunctionExpression = Node.newExpressionStatement(Node.newFunction("func", Node.newArguments(), Node.newBlock(Node.newReturnStatement(Node.newName("x")))));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunction = Node.newFunction("func", null);
    Node script = Node.newBlock(nodeFunction);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));

    Node nodeFunctionInBlock = Node.newFunction("func", null);
    Node block = Node.newBlock(nodeFunctionInBlock);
    Node scriptWithFunctionInBlock = Node.newBlock(script, block);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInBlock));

    Node nodeFunctionInFunction = Node.newFunction("func", null);
    Node function = Node.newFunction("outerFunc", null, Node.newBlock(nodeFunctionInFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));
  }
}