package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeFor = new Node(Token.FOR);
    nodeFor.setChildCount(3);
    assertTrue(NodeUtil.isForIn(nodeFor));

    Node nodeNotFor = new Node(Token.IF);
    assertFalse(NodeUtil.isForIn(nodeNotFor));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT);
    Node nodeStatement = new Node(Token.ASSIGN);
    Node nodeNotStatement = new Node(Token.IF);
    Node nodeFunction = new Node(Token.FUNCTION);

    checkState(nodeScript.getParent() == null);
    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeStatement));
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
    assertFalse(NodeUtil.isStatement(nodeFunction));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION);
    nodeFunctionDeclaration.setChildCount(1);
    nodeFunctionDeclaration.getChild(0).setName("testFunction");
    Node nodeFunctionExpression = new Node(Token.FUNCTION);
    nodeFunctionExpression.setChildCount(1);
    nodeFunctionExpression.getChild(0).setName("testFunction");

    Node parentScript = new Node(Token.SCRIPT);
    Node parentFunction = new Node(Token.FUNCTION);

    checkState(parentScript.getParent() == null);
    checkState(parentFunction.getParent() != null);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = new Node(Token.SCRIPT);
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION);
    nodeFunctionDeclaration.setChildCount(1);
    nodeFunctionDeclaration.getChild(0).setName("testFunction");
    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION);
    nodeFunctionDeclarationInFunction.setChildCount(1);
    nodeFunctionDeclarationInFunction.getChild(0).setName("testFunction");

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));

    Node nodeFunction = new Node(Token.FUNCTION);
    nodeFunction.setChildCount(1);
    nodeFunction.getChild(0).setName("testFunction");
    Node nodeFunctionInScript = new Node(Token.FUNCTION);
    nodeFunctionInScript.setChildCount(1);
    nodeFunctionInScript.getChild(0).setName("testFunction");
    nodeFunctionInScript.setParent(nodeScript);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction.getParent()));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));
  }
}