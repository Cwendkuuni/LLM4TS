package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = Node.newNode(Token.FOR, new int[]{}, new Node[3]);
    Node notForNode = Node.newNode(Token.IF, new int[]{}, new Node[0]);

    assertTrue(NodeUtil.isForIn(forNode));
    assertFalse(NodeUtil.isForIn(notForNode));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = Node.newNode(Token.SCRIPT, new int[]{}, new Node[0]);
    Node labelNode = Node.newNode(Token.LABEL, new int[]{}, new Node[0]);
    Node functionNode = Node.newNode(Token.FUNCTION, new int[]{}, new Node[0]);
    Node expressionNode = Node.newNode(Token.EQUALS, new int[]{}, new Node[] { Node.newTextNode("1"), Node.newNumberNode(2) });

    assertTrue(NodeUtil.isStatement(scriptNode));
    assertTrue(NodeUtil.isStatement(labelNode));
    assertTrue(NodeUtil.isStatement(functionNode));
    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = Node.newNode(Token.FUNCTION, new int[] { Token.NAME, Token.LEFTPAREN, Token.RIGHTPAREN, Token.LEFTBRACE }, new Node[0]);
    Node functionExpressionNode = Node.newNode(Token.FUNCTION, new int[] { Token.NAME, Token.LEFTPAREN, Token.RIGHTPAREN, Token.LEFTBRACE }, new Node[] { Node.newTextNode("x") });

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = Node.newNode(
        Token.FUNCTION,
        new int[] { Token.NAME, Token.LEFTPAREN, Token.RIGHTPAREN, Token.LEFTBRACE },
        new Node[] { Node.newTextNode("foo") }
    );
    Node scriptNode = Node.newNode(Token.SCRIPT, new int[]{}, new Node[] { hoistedFunctionDeclarationNode });
    Node functionNode = Node.newNode(
        Token.FUNCTION,
        new int[] { Token.NAME, Token.LEFTPAREN, Token.RIGHTPAREN, Token.LEFTBRACE },
        new Node[0]
    );
    Node functionInsideFunctionNode = Node.newNode(
        Token.FUNCTION,
        new int[] { Token.NAME, Token.LEFTPAREN, Token.RIGHTPAREN, Token.LEFTBRACE },
        new Node[] { functionNode }
    );

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionInsideFunctionNode));
  }
}