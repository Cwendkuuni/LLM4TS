package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = new Node(Token.FOR, new int[0], new int[0], new int[0]);
    Node forNodeWithThreeChildren = new Node(Token.FOR, new int[3], new int[0], new int[0]);
    Node notForNode = new Node(Token.IF, new int[0], new int[0], new int[0]);

    assertTrue(NodeUtil.isForIn(forNodeWithThreeChildren));
    assertFalse(NodeUtil.isForIn(forNode));
    assertFalse(NodeUtil.isForIn(notForNode));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = new Node(Token.SCRIPT, new int[0], new int[0], new int[0]);
    Node blockNode = new Node(Token.BLOCK, new int[0], new int[0], new int[0]);
    Node labelNode = new Node(Token.LABEL, new int[0], new int[0], new int[0]);
    Node functionNode = new Node(Token.FUNCTION, new int[0], new int[0], new int[0]);
    Node expressionNode = new Node(Token.IDENT, new int[0], new int[0], new int[0]);

    assertTrue(NodeUtil.isStatement(scriptNode));
    assertTrue(NodeUtil.isStatement(blockNode));
    assertTrue(NodeUtil.isStatement(labelNode));
    assertFalse(NodeUtil.isStatement(expressionNode));
    assertFalse(NodeUtil.isStatement(functionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = new Node(Token.FUNCTION, new int[0], new int[0], new int[0]);
    Node functionExpression = new Node(Token.FUNCTION, new int[1], new int[0], new int[0]);
    Node ifStatement = new Node(Token.IF, new int[0], new int[0], new int[0]);
    Node scriptNode = new Node(Token.SCRIPT, new int[0], new int[0], new int[0]);

    assertFalse(NodeUtil.isFunctionDeclaration(functionExpression));
    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(ifStatement));
    assertFalse(NodeUtil.isFunctionDeclaration(scriptNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Token.SCRIPT, new int[0], new int[0], new int[0]);
    Node functionDeclaration = new Node(Token.FUNCTION, new int[0], new int[0], new int[0]);
    Node functionDeclarationInFunction = new Node(Token.FUNCTION, new int[0], new int[0], new int[0]);
    Node functionDeclarationInBlock = new Node(Token.BLOCK, new int[0], new int[0], new int[0]);

    functionDeclarationInBlock.addChild(functionDeclaration);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationInBlock));

    functionDeclarationInFunction.addChild(functionDeclaration);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclaration));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationInFunction));

    scriptNode.addChild(functionDeclaration);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclaration));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclaration));
  }
}