package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = new Node(Token.FOR);
    forNode.setChildCount(3);
    Node init = new Node(Token.VAR);
    Node condition = new Node(Token.NUMBER, 1);
    Node increment = new Node(Token.ASSIGN);
    increment.setRightChild(new Node(Token.PLUS, new Node(Token.IDENT, "i"), new Node(Token.NUMBER, 1)));
    forNode.setChild(0, init);
    forNode.setChild(1, condition);
    forNode.setChild(2, increment);

    assertTrue(NodeUtil.isForIn(forNode));

    Node notForNode = new Node(Token.IF);
    assertFalse(NodeUtil.isForIn(notForNode));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node statementNode = new Node(Token.PRINT, new Node(Token.STRING, "Hello, World!"));
    scriptNode.setChild(0, statementNode);

    assertTrue(NodeUtil.isStatement(statementNode));

    Node expressionNode = new Node(Token.ASSIGN, new Node(Token.IDENT, "x"), new Node(Token.NUMBER, 42));
    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION, new Node(Token.IDENT, "foo"));
    Node statementFunctionNode = new Node(Token.FUNCTION, new Node(Token.IDENT, "bar"), new Node(Token.BLOCK, null));
    Node functionExpressionNode = new Node(Token.ASSIGN, new Node(Token.IDENT, "baz"), new Node(Token.FUNCTION, null, new Node(Token.BLOCK, null)));

    assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
    assertFalse(NodeUtil.isFunctionDeclaration(statementFunctionNode));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Token.SCRIPT);

    Node functionDeclarationNode = new Node(Token.FUNCTION, new Node(Token.IDENT, "foo"));
    Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION, new Node(Token.IDENT, "bar"), new Node(Token.BLOCK, null));
    hoistedFunctionDeclarationNode.setParent(scriptNode);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));

    Node functionExpressionNode = new Node(Token.ASSIGN, new Node(Token.IDENT, "baz"), new Node(Token.FUNCTION, null, new Node(Token.BLOCK, null)));
    Node hoistedFunctionExpressionNode = new Node(Token.ASSIGN, new Node(Token.IDENT, "qux"), hoistedFunctionExpressionNode);
    hoistedFunctionExpressionNode.setParent(scriptNode);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionExpressionNode));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_nestedFunction() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node outerFunction = new Node(Token.FUNCTION, new Node(Token.IDENT, "outer"), new Node(Token.BLOCK, null));
    outerFunction.setParent(scriptNode);

    Node innerFunctionDeclarationNode = new Node(Token.FUNCTION, new Node(Token.IDENT, "inner"));
    Node innerFunctionExpressionNode = new Node(Token.ASSIGN, new Node(Token.IDENT, "inner"), innerFunctionDeclarationNode);
    innerFunctionDeclarationNode.setParent(outerFunction);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(innerFunctionDeclarationNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(innerFunctionExpressionNode));
  }
}