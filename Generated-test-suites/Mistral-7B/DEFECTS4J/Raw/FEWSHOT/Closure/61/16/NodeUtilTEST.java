package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR);
    nodeForIn.setChildCount(3);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT);
    Node nodeBlock = new Node(Token.BLOCK);
    Node nodeLabel = new Node(Token.LABEL);
    Node nodeFunction = new Node(Token.FUNCTION);
    Node nodeExpression = new Node(Token.NUMBER, 42);

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeBlock));
    assertTrue(NodeUtil.isStatement(nodeLabel));
    assertFalse(NodeUtil.isStatement(nodeFunction));
    assertFalse(NodeUtil.isStatement(nodeExpression));

    Node nodeStatementInExpression = new Node(Token.ASSIGN, null, nodeExpression);
    assertTrue(NodeUtil.isStatement(nodeStatementInExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, "funcName");
    nodeFunctionDeclaration.setType(Token.FUNCTION);
    nodeFunctionDeclaration.setProperty("name", "funcName");
    nodeFunctionDeclaration.setChildCount(1);

    Node nodeFunctionExpression = new Node(Token.FUNCTION, null);
    nodeFunctionExpression.setType(Token.FUNCTION);
    nodeFunctionExpression.setProperty("expression", true);
    nodeFunctionExpression.setChildCount(1);

    Node nodeScript = new Node(Token.SCRIPT);
    Node nodeBlock = new Node(Token.BLOCK);
    Node nodeLabel = new Node(Token.LABEL);
    Node nodeFunctionInScript = new Node(Token.FUNCTION, "funcName");
    nodeFunctionInScript.setType(Token.FUNCTION);
    nodeFunctionInScript.setProperty("name", "funcName");
    nodeFunctionInScript.setParent(nodeScript);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionInScript));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclarationInScript = new Node(Token.FUNCTION, "funcName");
    nodeFunctionDeclarationInScript.setType(Token.FUNCTION);
    nodeFunctionDeclarationInScript.setProperty("name", "funcName");
    nodeFunctionDeclarationInScript.setParent(new Node(Token.SCRIPT));

    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, "funcName");
    nodeFunctionDeclarationInFunction.setType(Token.FUNCTION);
    nodeFunctionDeclarationInFunction.setProperty("name", "funcName");
    nodeFunctionDeclarationInFunction.setParent(new Node(Token.FUNCTION, "parentFuncName"));

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }
}