package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{}, new Object[]{}, new int[0]);
    Node nodeNotForIn = new Node(Token.IF, new int[]{}, new Object[]{}, new int[0]);

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT, new int[]{}, new Object[]{}, new int[0]);
    Node nodeStatement = new Node(Token.ASSIGN, new int[]{}, new Object[]{}, new int[0]);
    Node nodeExpression = new Node(Token.PLUS, new int[]{}, new Object[]{}, new int[0]);

    Node parentScript = new Node(Token.SCRIPT, new int[]{}, new Object[]{}, new int[0]);
    Node parentBlock = new Node(Token.BLOCK, new int[]{}, new Object[]{}, new int[0]);
    Node parentLabel = new Node(Token.LABEL, new int[]{}, new Object[]{}, new int[0]);

    Node nodeInScript = new Node(Token.ASSIGN, new int[]{}, new Object[]{}, new int[0]);
    nodeInScript.setParent(parentScript);

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeStatement));
    assertFalse(NodeUtil.isStatement(nodeExpression));

    assertTrue(NodeUtil.isStatement(nodeInScript));
    assertTrue(NodeUtil.isStatement(parentScript));
    assertTrue(NodeUtil.isStatement(parentBlock));
    assertTrue(NodeUtil.isStatement(parentLabel));

    checkState(null == nodeStatement.getParent());
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = new Node(Token.FUNCTION, new int[]{}, new Object[]{}, new int[0]);
    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[]{}, new Object[]{}, new int[1]);
    Node nodeIf = new Node(Token.IF, new int[]{}, new Object[]{}, new int[0]);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeIf));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunction = new Node(Token.FUNCTION, new int[]{}, new Object[]{}, new int[0]);
    Node nodeFunctionInFunction = new Node(Token.FUNCTION, new int[]{}, new Object[]{}, new int[1]);
    Node nodeFunctionInScript = new Node(Token.FUNCTION, new int[]{}, new Object[]{}, new int[0]);

    Node parentFunction = new Node(Token.FUNCTION, new int[]{}, new Object[]{}, new int[0]);
    Node parentScript = new Node(Token.SCRIPT, new int[]{}, new Object[]{}, new int[0]);

    Node nodeInFunction = new Node(Token.FUNCTION, new int[]{}, new Object[]{}, new int[1]);
    nodeFunctionInFunction.setParent(parentFunction);

    Node nodeInScript = new Node(Token.FUNCTION, new int[]{}, new Object[]{}, new int[0]);
    nodeFunctionInScript.setParent(parentScript);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInScript));
  }
}