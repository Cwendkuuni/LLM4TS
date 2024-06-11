package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = Node.newNode(Token.FOR, null);
    assertTrue(NodeUtil.isForIn(node));

    node = Node.newNode(Token.FOR, null, Node.newNode(Token.IF, null), Node.newNode(Token.STATEMENT, null));
    assertTrue(NodeUtil.isForIn(node));

    Node ifNode = Node.newNode(Token.IF, null);
    Node statementNode = Node.newNode(Token.STATEMENT, null);
    Node forNode = Node.newNode(Token.FOR, null, ifNode, statementNode);
    assertFalse(NodeUtil.isForIn(forNode));
  }

  @Test
  public void testIsStatement() {
    Node node = Node.newNode(Token.IF, null, Node.newNode(Token.STATEMENT, null), Node.newNode(Token.STATEMENT, null));
    assertTrue(NodeUtil.isStatement(node));

    Node scriptNode = Node.newNode(Token.SCRIPT, null);
    Node blockNode = Node.newNode(Token.BLOCK, null);
    Node labelNode = Node.newNode(Token.LABEL, null, Node.newNode(Token.STATEMENT, null));

    assertTrue(NodeUtil.isStatement(scriptNode.getFirstChild()));
    assertTrue(NodeUtil.isStatement(blockNode.getFirstChild()));
    assertTrue(NodeUtil.isStatement(labelNode));

    Node expressionNode = Node.newNode(Token.EXPRESSION, null);
    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = Node.newNode(Token.FUNCTION, "funcName", Node.newNode(Token.STATEMENT, null), Node.newNode(Token.STATEMENT, null));
    assertTrue(NodeUtil.isFunctionDeclaration(functionNode));

    Node expressionFunctionNode = Node.newNode(Token.FUNCTION, null, Node.newNode(Token.STATEMENT, null), Node.newNode(Token.STATEMENT, null));
    assertFalse(NodeUtil.isFunctionDeclaration(expressionFunctionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node functionDeclarationNode = Node.newNode(Token.FUNCTION, "funcName", Node.newNode(Token.STATEMENT, null), Node.newNode(Token.STATEMENT, null));
    Node scriptNode = Node.newNode(Token.SCRIPT, null, functionDeclarationNode);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode));

    Node functionDeclarationInFunctionNode = Node.newNode(
        Token.FUNCTION,
        null,
        Node.newNode(Token.FUNCTION, null, Node.newNode(Token.STATEMENT, null), Node.newNode(Token.STATEMENT, null)),
        Node.newNode(Token.STATEMENT, null));
    Node outerFunctionNode = Node.newNode(Token.FUNCTION, null, Node.newNode(Token.STATEMENT, null), Node.newNode(functionDeclarationInFunctionNode, null));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationInFunctionNode));
  }

  @Test
  public void testNullChecks() {
    checkNull(null, "Node is null");
    Node node = Node.newNode(Token.IF, null);
    checkArgument(node != null, "Node is null");
  }
}