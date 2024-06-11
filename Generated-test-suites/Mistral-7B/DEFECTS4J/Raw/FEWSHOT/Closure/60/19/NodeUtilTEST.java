package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkArgument;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = new Node(Token.FOR);
    Node forNodeWithThreeChildren = new Node(Token.FOR, 3);
    Node notForNode = new Node(Token.IF);

    assertTrue(NodeUtil.isForIn(forNodeWithThreeChildren));
    assertFalse(NodeUtil.isForIn(forNode));
    assertFalse(NodeUtil.isForIn(notForNode));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = new Node(Token.SCRIPT, 10);
    Node blockNode = new Node(Token.BLOCK, 5);
    Node labelNode = new Node(Token.LABEL, "myLabel");
    Node functionNode = new Node(Token.FUNCTION, "myFunc");
    Node expressionNode = new Node(Token.ASSIGN, null, new Node(Token.NUM, 42));

    assertTrue(NodeUtil.isStatement(scriptNode));
    assertTrue(NodeUtil.isStatement(blockNode));
    assertTrue(NodeUtil.isStatement(labelNode));
    assertFalse(NodeUtil.isStatement(functionNode));
    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = new Node(Token.FUNCTION, "myFunc");
    Node functionExpressionNode = new Node(Token.ASSIGN, null, new Node(Token.FUNCTION, null, null));

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Token.SCRIPT, 10);
    Node functionDeclarationNode = new Node(Token.FUNCTION, "myFunc");
    Node functionDeclarationInFunctionNode = new Node(Token.FUNCTION, null, new Node(Token.FUNCTION, "myFunc2", null));

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationInFunctionNode));
  }

  @Test
  public void testPreconditionsCheckState() {
    Node node = null;
    checkArgument(NodeUtil.isStatement(node), "parent should not be null");
  }
}