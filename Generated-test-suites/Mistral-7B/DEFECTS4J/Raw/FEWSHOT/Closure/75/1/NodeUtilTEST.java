package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn();
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeWhile = Node.newWhileStatement();
    assertFalse(NodeUtil.isForIn(nodeWhile));

    Node nodeFunction = Node.newFunction("func", Node.newBlock(Node.newExpression(Token.IDENT, "1")), Node.newBlock());
    assertFalse(NodeUtil.isForIn(nodeFunction));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newScript(Node.newExpression(Token.IDENT, "1"));
    assertTrue(NodeUtil.isStatement(nodeScript));

    Node nodeExpression = Node.newExpression(Token.IDENT, "1");
    assertFalse(NodeUtil.isStatement(nodeExpression));

    Node nodeFunction = Node.newFunction("func", Node.newBlock(Node.newExpression(Token.IDENT, "1")), Node.newBlock());
    assertTrue(NodeUtil.isStatement(nodeFunction));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = Node.newFunction("func", Node.newBlock(Node.newExpression(Token.IDENT, "1")), Node.newBlock());
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));

    Node nodeFunctionExpression = Node.newExpression(Token.FUNCTION, Node.newBlock(Node.newExpression(Token.IDENT, "1")), Node.newBlock());
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newScript(Node.newFunction("func", Node.newBlock(Node.newExpression(Token.IDENT, "1")), Node.newBlock()));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeScript.getFirstChild()));

    Node nodeFunctionInBlock = Node.newBlock(Node.newFunction("func", Node.newBlock(Node.newExpression(Token.IDENT, "1")), Node.newBlock()));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInBlock.getFirstChild()));

    Node nodeFunctionInFunction = Node.newFunction("outerFunc", Node.newBlock(Node.newFunction("func", Node.newBlock(Node.newExpression(Token.IDENT, "1")), Node.newBlock())), Node.newBlock());
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction.getFirstChild().getFirstChild()));
  }
}