package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = Node.newForNode(1, 1, 2, 1, 1, 1);
    assertTrue(NodeUtil.isForIn(forNode));

    Node ifNode = Node.newIfNode(1, 1, 1, 1);
    assertFalse(NodeUtil.isForIn(ifNode));

    Node functionNode = Node.newFunctionNode(1, 1, 1, 1, 1, 1, 1, 1);
    assertFalse(NodeUtil.isForIn(functionNode));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = Node.newScriptNode("");
    assertTrue(NodeUtil.isStatement(scriptNode));

    Node ifNode = Node.newIfNode(1, 1, 1, 1);
    assertTrue(NodeUtil.isStatement(ifNode));

    Node functionNode = Node.newFunctionNode(1, 1, 1, 1, 1, 1, 1, 1);
    assertTrue(NodeUtil.isStatement(functionNode));

    Node expressionNode = Node.newNumberLiteralNode(1);
    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = Node.newFunctionNode(1, 1, 1, 1, 1, 1, 1, 1);
    Node functionDeclaration = Node.newFunctionDeclarationNode(1, 1, 2, 1, 1, "func", 1, 1, 1, 1, 1);
    assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclaration));

    Node functionExpression = Node.newExpressionStatementNode(1, 1, Node.newFunctionExpressionNode(1, 1, 2, 1, 1, "func", 1, 1, 1, 1, 1));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node functionDeclaration = Node.newFunctionDeclarationNode(1, 1, 2, 1, 1, "func", 1, 1, 1, 1, 1);
    Node scriptNode = Node.newScriptNode("");
    Node functionNode = Node.newFunctionNode(1, 1, 1, 1, 1, 1, 1, 1);
    Node functionExpression = Node.newExpressionStatementNode(1, 1, Node.newFunctionExpressionNode(1, 1, 2, 1, 1, "func", 1, 1, 1, 1, 1));

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(scriptNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionExpression));

    Node hoistedFunctionDeclaration = Node.newFunctionDeclarationNode(1, 1, 2, 1, 1, "func", 1, 1, 1, 1, 1);
    Node functionNodeInFunction = Node.newFunctionNode(1, 1, 1, 1, 1, 1, 1, 1);
    Node scriptNodeInFunction = Node.newScriptNode("");

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNodeInFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(scriptNodeInFunction));
  }

  @Test
  public void testPreconditions() {
    Node nullNode = null;
    checkArgument(NodeUtil.isStatement(Node.NEW_INSTANCE), "Expected non-null node");
    checkArgument(NodeUtil.isStatement(Node.newScriptNode("")), "Expected statement node");
    checkArgument(NodeUtil.isFunctionDeclaration(Node.newFunctionNode(1, 1, 1, 1, 1, 1, 1, 1)), "Expected function declaration node");
    checkArgument(NodeUtil.isFunctionDeclaration(Node.newFunctionDeclarationNode(1, 1, 2, 1, 1, "func", 1, 1, 1, 1, 1)), "Expected function declaration node");
    checkNotNull(NodeUtil.isFunctionDeclaration(nullNode), "Expected non-null node");
  }
}