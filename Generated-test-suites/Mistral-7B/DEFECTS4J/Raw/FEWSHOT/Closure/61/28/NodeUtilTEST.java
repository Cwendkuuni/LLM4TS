package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = new Node(Token.FOR, new int[]{}, new int[]{0}, null);
    Node notForNode = new Node(Token.IF, new int[]{}, new int[]{0}, null);

    assertTrue(NodeUtil.isForIn(forNode));
    assertFalse(NodeUtil.isForIn(notForNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = new Node(Token.IF, new int[]{}, new int[]{0}, null);
    Node notStatementNode = new Node(Token.IDENT, new int[]{}, new int[]{0}, "x");

    Node scriptNode = new Node(Token.SCRIPT, new int[]{}, new int[]{0}, null);
    Node blockNode = new Node(Token.BLOCK, new int[]{}, new int[]{0}, null);
    Node labelNode = new Node(Token.LABEL, new int[]{}, new int[]{0}, "myLabel");

    assertTrue(NodeUtil.isStatement(statementNode));
    assertTrue(NodeUtil.isStatement(scriptNode));
    assertTrue(NodeUtil.isStatement(blockNode));
    assertTrue(NodeUtil.isStatement(labelNode));

    Node expressionNode = new Node(Token.IDENT, new int[]{}, new int[]{0}, "x");

    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = new Node(Token.FUNCTION, new int[]{}, new int[]{0}, "myFunction");
    Node functionExpressionNode = new Node(Token.FUNCTION, new int[]{}, new int[]{0}, null);

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Token.SCRIPT, new int[]{}, new int[]{0}, null);
    Node functionDeclarationNode = new Node(Token.FUNCTION, new int[]{}, new int[]{0}, "myFunction");
    Node functionInsideFunctionNode = new Node(Token.FUNCTION, new int[]{}, new int[]{0}, null);
    Node functionInsideFunctionDeclarationNode = new Node(Token.FUNCTION, new int[]{}, new int[]{0}, null);

    functionInsideFunctionNode.setParent(functionInsideFunctionDeclarationNode);
    functionInsideFunctionDeclarationNode.setParent(functionDeclarationNode);
    functionDeclarationNode.setParent(scriptNode);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionExpressionNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionInsideFunctionNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionInsideFunctionDeclarationNode));
  }
}