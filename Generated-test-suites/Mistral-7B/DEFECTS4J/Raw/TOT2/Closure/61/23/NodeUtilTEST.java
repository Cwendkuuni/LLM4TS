
package com.google.javascript.jscomp.jsonml;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeFactory;
import org.junit.Assert;
import org.junit.Test;

public class NodeUtilTest {
  private static NodeFactory nodeFactory = new NodeFactory();

  @Test
  public void testIsForIn() {
    Node forNode = NodeFactory.createToken(Token.FOR, "for (i in array) {...}");
    assertTrue(NodeUtil.isForIn(forNode));

    Node notForNode = NodeFactory.createToken(Token.IF, "if (condition) {...}");
    assertFalse(NodeUtil.isForIn(notForNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = NodeFactory.createToken(Token.IF, "if (condition) {...}");
    assertTrue(NodeUtil.isStatement(statementNode));

    Node expressionNode = NodeFactory.createToken(Token.IDENT, "function foo() {...}");
    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = NodeFactory.createNode(
        "function foo() {...}", "FunctionDeclaration", null);
    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));

    Node functionExpressionNode = NodeFactory.createNode(
        "function foo() {...}", "FunctionExpression", null);
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = NodeFactory.createNode(
        "function foo() {...}", "FunctionDeclaration", NodeFactory.createToken(Token.SCRIPT, ""));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));

    Node notHoistedFunctionDeclarationNode = NodeFactory.createNode(
        "function foo() {...}", "FunctionDeclaration", NodeFactory.createToken(Token.BLOCK, "..."));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionDeclarationNode));
  }

  @Test(expected = NullPointerException.class)
  public void testIsForInWithNullNode() {
    Node nullNode = null;
    assertFalse(NodeUtil.isForIn(nullNode));
  }

  @Test
  public void testIsForInWithEmptyNode() {
    Node emptyNode = NodeFactory.createNode(Token.EOF);
    assertFalse(NodeUtil.isForIn(emptyNode));
  }

  @Test
  public void testIsForInWithNonFORNode() {
    Node nonFORNode = NodeFactory.createToken(Token.IDENT, "foo");
    assertFalse(NodeUtil.isForIn(nonFORNode));
  }

  @Test(expected = NullPointerException.class)
  public void testIsStatementWithNullNode() {
    Node nullNode = null;
    assertFalse(NodeUtil.isStatement(nullNode));
  }

  @Test
  public void testIsStatementWithEmptyNode() {
    Node emptyNode = NodeFactory.createNode(Token.EOF);
    assertFalse(NodeUtil.isStatement(emptyNode));
  }

  @Test
  public void testIsStatementWithNonStatementNode() {
    Node nonStatementNode = NodeFactory.createToken(Token.IDENT, "foo");
    assertFalse(NodeUtil.isStatement(nonStatementNode));
  }

  @Test(expected = NullPointerException.class)
  public void testIsFunctionDeclarationWithNullNode() {
    Node nullNode = null;
    assertFalse(NodeUtil.isFunctionDeclaration(nullNode));
  }

  @Test
  public void testIsFunctionDeclarationWithEmptyNode() {
    Node emptyNode = NodeFactory.createNode(Token.EOF);
    assertFalse(NodeUtil.isFunctionDeclaration(emptyNode));
  }

  @Test
  public void testIsFunctionDeclarationWithNonFunctionNode() {
    Node nonFunctionNode = NodeFactory.createToken(Token.IF, "if (condition) {...}");
    assertFalse(NodeUtil.isFunctionDeclaration(nonFunctionNode));
  }

  @Test(expected = NullPointerException.class)
  public void testIsHoistedFunctionDeclarationWithNullNode() {
    Node nullNode = null;
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nullNode));
  }

  @Test
  public void testIsHoistedFunctionDeclarationWithEmptyNode() {
    Node emptyNode = NodeFactory.createNode(Token.EOF);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(emptyNode));
  }

  @Test
  public void testIsHoistedFunctionDeclarationWithNonFunctionDeclarationNode() {
    Node nonFunctionDeclarationNode = NodeFactory.createNode(
        "var foo = function() {...}", "VariableDeclaration", null);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nonFunctionDeclarationNode));
  }

  @Test
  public void testIsFunctionExpression() {
    Node functionExpressionNode = NodeFactory.createNode(
        "function foo() {...}", "FunctionExpression", null);
    assertFalse(NodeUtil.isFunctionExpression(functionExpressionNode));

    Node expressionFunctionNode = NodeFactory.createNode(
        "function foo() {...}", "ExpressionStatement", functionExpressionNode);
    assertTrue(NodeUtil.isFunctionExpression(expressionFunctionNode));
  }
}

