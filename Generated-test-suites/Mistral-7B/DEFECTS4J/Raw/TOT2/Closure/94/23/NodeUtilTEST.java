package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.*;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forInNode = new Node(Token.FOR);
    forInNode.setChildCount(3);
    assertTrue(NodeUtil.isForIn(forInNode));

    Node notForInNode = new Node(Token.IF);
    assertFalse(NodeUtil.isForIn(notForInNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = new Node(Token.IF);
    assertTrue(NodeUtil.isStatement(statementNode));

    Node expressionNode = new Node(Token.ADD);
    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = new Node(Token.FUNCTION);
    functionDeclaration.setProperty("id", "testFunction");
    functionDeclaration.setChildCount(1);
    functionDeclaration.getChild(0).setProperty("body", new Node(Token.BLOCK));
    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclaration));

    Node functionExpression = new Node(Token.FUNCTION);
    functionExpression.setProperty("id", null);
    functionExpression.setChildCount(1);
    functionExpression.getChild(0).setProperty("body", new Node(Token.BLOCK));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclaration = new Node(Token.FUNCTION);
    hoistedFunctionDeclaration.setProperty("id", "testFunction");
    hoistedFunctionDeclaration.setChildCount(1);
    hoistedFunctionDeclaration.getChild(0).setProperty("body", new Node(Token.BLOCK));

    Node scriptNode = new Node(Token.SCRIPT);
    scriptNode.setChildCount(1);
    scriptNode.getChild(0).set(hoistedFunctionDeclaration);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration));

    Node functionNodeInFunction = new Node(Token.FUNCTION);
    functionNodeInFunction.setProperty("id", "testFunction2");
    functionNodeInFunction.setChildCount(1);
    functionNodeInFunction.getChild(0).setProperty("body", new Node(Token.BLOCK));

    Node functionNode = new Node(Token.FUNCTION);
    functionNode.setProperty("id", "testFunction3");
    functionNode.setChildCount(1);
    functionNode.getChild(0).setProperty("body", new Node(Token.BLOCK));

    Node parentFunctionNode = new Node(Token.FUNCTION);
    parentFunctionNode.setProperty("id", "parentFunction");
    parentFunctionNode.setChildCount(1);
    parentFunctionNode.getChild(0).set(functionNode);

    Node scriptNodeWithFunctionNode = new Node(Token.SCRIPT);
    scriptNodeWithFunctionNode.setChildCount(1);
    scriptNodeWithFunctionNode.getChild(0).set(parentFunctionNode);

    Node hoistedFunctionDeclarationInFunction = new Node(Token.FUNCTION);
    hoistedFunctionDeclarationInFunction.setProperty("id", "testFunction4");
    hoistedFunctionDeclarationInFunction.setChildCount(1);
    hoistedFunctionDeclarationInFunction.getChild(0).setProperty("body", new Node(Token.BLOCK));

    Node parentFunctionNodeWithHoistedFunction = new Node(Token.FUNCTION);
    parentFunctionNodeWithHoistedFunction.setProperty("id", "parentFunction2");
    parentFunctionNodeWithHoistedFunction.setChildCount(1);
    parentFunctionNodeWithHoistedFunction.getChild(0).set(hoistedFunctionDeclarationInFunction);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationInFunction));

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }
}