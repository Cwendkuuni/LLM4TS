package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkArgument;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn();
    Node nodeNotForIn = Node.newExpression(1);

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newExpression(Node.SCRIPT_TOKEN, Node.newList());
    Node nodeFunction = Node.newExpression(Node.FUNCTION, Node.newList(Node.newIdentifier("foo")), Node.newBlock(Node.newList()));
    Node nodeExpression = Node.newExpression(1);

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeFunction));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newExpression(Node.FUNCTION, Node.newList(Node.newIdentifier("foo")), Node.newBlock(Node.newList()));
    Node nodeFunctionExpression = Node.newExpression(Node.FUNCTION, Node.newList(), Node.newList());

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newExpression(Node.SCRIPT_TOKEN, Node.newList());
    Node nodeFunctionDeclarationInScript = Node.newExpression(Node.FUNCTION, Node.newList(Node.newIdentifier("foo")), Node.newBlock(Node.newList()));
    Node nodeFunctionDeclarationInFunction = Node.newExpression(Node.FUNCTION, Node.newList("bar"), Node.newList(Node.newExpression(Node.FUNCTION, Node.newList(Node.newIdentifier("foo")), Node.newBlock(Node.newList()))));

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }

  @Test
  public void testIsStatementInScript() {
    Node nodeScript = Node.newExpression(Node.SCRIPT_TOKEN, Node.newList());
    Node nodeStatement = Node.newExpression(Token.IF, Node.newList(Node.newBooleanLiteral(true), Node.newBlock(Node.newList())));

    assertTrue(NodeUtil.isStatement(nodeStatement));
    assertTrue(NodeUtil.isStatement(nodeScript));
  }

  @Test
  public void testIsStatementInBlock() {
    Node nodeBlock = Node.newBlock(Node.newList());
    Node nodeStatement = Node.newExpression(Token.IF, Node.newList(Node.newBooleanLiteral(true), Node.newBlock(Node.newList())));

    assertTrue(NodeUtil.isStatement(nodeStatement));
    assertTrue(NodeUtil.isStatement(nodeBlock));
  }

  @Test
  public void testIsStatementInLabel() {
    Node nodeLabel = Node.newExpression(Node.LABEL, Node.newIdentifier("foo"));
    Node nodeStatement = Node.newExpression(Token.IF, Node.newList(Node.newBooleanLiteral(true), Node.newBlock(Node.newList())));

    assertFalse(NodeUtil.isStatement(nodeLabel));
    assertTrue(NodeUtil.isStatement(nodeStatement));
  }

  @Test
  public void testIsStatementNull() {
    Node nodeNull = null;
    assertFalse(NodeUtil.isStatement(nodeNull));
  }
}