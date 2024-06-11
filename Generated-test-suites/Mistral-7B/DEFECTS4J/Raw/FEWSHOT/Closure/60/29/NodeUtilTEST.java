package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node1 = new Node();
    node1.setType(Token.FOR);
    node1.setChildCount(3);

    Node node2 = new Node();
    node2.setType(Token.IF);
    node2.setChildCount(1);

    assertTrue(NodeUtil.isForIn(node1));
    assertFalse(NodeUtil.isForIn(node2));
  }

  @Test
  public void testIsStatement() {
    Node node1 = new Node();
    node1.setType(Token.IF);

    Node scriptNode = new Node();
    scriptNode.setType(Token.SCRIPT);

    Node blockNode = new Node();
    blockNode.setType(Token.BLOCK);

    Node labelNode = new Node();
    labelNode.setType(Token.LABEL);

    Node statementNode1 = new Node();
    statementNode1.setType(Token.ASSIGN);

    Node statementNode2 = new Node();
    statementNode2.setType(Token.RETURN);

    checkArgument(NodeUtil.isStatement(statementNode1), "Expected statement node to be true.");
    checkArgument(NodeUtil.isStatement(statementNode2), "Expected statement node to be true.");

    checkState(NodeUtil.isStatement(scriptNode), "Expected script node to be a statement.");
    checkState(NodeUtil.isStatement(blockNode), "Expected block node to be a statement.");
    checkState(NodeUtil.isStatement(labelNode), "Expected label node to be a statement.");

    Node expressionNode = new Node();
    expressionNode.setType(Token.IF);

    assertFalse(NodeUtil.isStatement(expressionNode), "Expected expression node to be false.");
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode1 = new Node();
    functionNode1.setType(Token.FUNCTION);
    functionNode1.setChildCount(1);

    Node functionNode2 = new Node();
    functionNode2.setType(Token.FUNCTION);
    functionNode2.setChildCount(3);

    Node statementNode = new Node();
    statementNode.setType(Token.IF);
    statementNode.setChildCount(1);

    Node hoistedFunctionNode = functionNode1;
    hoistedFunctionNode.setParent(new Node());
    hoistedFunctionNode.getParent().setType(Token.SCRIPT);

    assertTrue(NodeUtil.isFunctionDeclaration(functionNode1));
    assertFalse(NodeUtil.isFunctionDeclaration(functionNode2));
    assertFalse(NodeUtil.isFunctionDeclaration(statementNode));
    assertTrue(NodeUtil.isFunctionDeclaration(hoistedFunctionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node functionNode1 = new Node();
    functionNode1.setType(Token.FUNCTION);
    functionNode1.setChildCount(1);

    Node functionNode2 = new Node();
    functionNode2.setType(Token.FUNCTION);
    functionNode2.setChildCount(3);

    Node hoistedFunctionNode1 = functionNode1;
    hoistedFunctionNode1.setParent(new Node());
    hoistedFunctionNode1.getParent().setType(Token.SCRIPT);

    Node hoistedFunctionNode2 = new Node();
    hoistedFunctionNode2.setType(Token.FUNCTION);
    hoistedFunctionNode2.setChildCount(3);
    hoistedFunctionNode2.setParent(functionNode1);

    Node functionNodeWithinFunction = new Node();
    functionNodeWithinFunction.setType(Token.FUNCTION);
    functionNodeWithinFunction.setChildCount(1);
    hoistedFunctionNode2.setChild(0, functionNodeWithinFunction);

    Node functionInsideExpression = new Node();
    functionInsideExpression.setType(Token.FUNCTION);
    functionInsideExpression.setChildCount(1);
    statementNode.setChild(0, functionInsideExpression);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionNode1));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode2));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionNode2));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNodeWithinFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionInsideExpression));
  }
}