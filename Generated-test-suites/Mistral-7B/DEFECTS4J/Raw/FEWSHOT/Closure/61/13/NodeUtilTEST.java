package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.*;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn();
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpressionStatement(Node.newExpression(Node.newNumberLiteral(1)));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpressionStatement(Node.newExpression(Node.newNumberLiteral(1)));
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeNotStatement = Node.newIdentifier("myIdentifier");
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(
        Node.newIdentifier("myFunction"),
        Node.newArray(),
        Node.newBlock(Node.newExpressionStatement(Node.newExpression(Node.newNumberLiteral(1)))));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionStatement(Node.newExpression(Node.newFunction(
        Node.newIdentifier("myFunction"),
        Node.newArray(),
        Node.newBlock(Node.newExpressionStatement(Node.newExpression(Node.newNumberLiteral(1)))))));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = Node.newScript();
    Node functionDeclaration = Node.newFunction(
        Node.newIdentifier("myFunction"),
        Node.newArray(),
        Node.newBlock(Node.newExpressionStatement(Node.newExpression(Node.newNumberLiteral(1)))));
    Node hoistedFunctionDeclaration = Node.newFunction(
        Node.newIdentifier("myFunction"),
        Node.newArray(),
        Node.newBlock(Node.newExpressionStatement(Node.newExpression(Node.newNumberLiteral(1)))));
    Node functionExpression = Node.newExpressionStatement(Node.newExpression(Node.newFunction(
        Node.newIdentifier("myFunction"),
        Node.newArray(),
        Node.newBlock(Node.newExpressionStatement(Node.newExpression(Node.newNumberLiteral(1)))))));

    Node scriptWithFunctionDeclaration = Node.newBlock(scriptNode, functionDeclaration);
    Node scriptWithFunctionExpression = Node.newBlock(scriptNode, functionExpression);

    Node rootNodeScriptWithFunctionDeclaration = Node.newScript(scriptWithFunctionDeclaration);
    Node rootNodeScriptWithFunctionExpression = Node.newScript(scriptWithFunctionExpression);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclaration));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionExpression));

    checkNotNull(checkNotNull(scriptWithFunctionDeclaration.getParent()), "Script with function declaration should have a parent.");
    checkNotNull(checkNotNull(functionDeclaration.getParent()), "Function declaration should have a parent.");
    assertEquals(scriptWithFunctionDeclaration.getParent().getType(), Token.SCRIPT);
    assertEquals(functionDeclaration.getParent().getType(), Token.BLOCK);

    checkNotNull(checkNotNull(scriptWithFunctionExpression.getParent()), "Script with function expression should have a parent.");
    checkNotNull(checkNotNull(functionExpression.getParent()), "Function expression should have a parent.");
    assertEquals(scriptWithFunctionExpression.getParent().getType(), Token.SCRIPT);
    assertEquals(functionExpression.getParent().getType(), Token.BLOCK);
  }
}