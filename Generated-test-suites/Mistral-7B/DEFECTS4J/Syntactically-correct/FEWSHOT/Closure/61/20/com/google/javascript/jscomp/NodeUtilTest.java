package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, 1, 1, 1);
    Node nodeFor = new Node(Token.FOR, 1, 1, 1);
    Node nodeIf = new Node(Token.IF, 1, 1, 1);

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeFor));
    assertFalse(NodeUtil.isForIn(nodeIf));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT, 1, 1, 1);
    Node nodeFunction = new Node(Token.FUNCTION, 1, 1, 1);
    Node nodeExpression = new Node(Token.ASSIGN, 1, 1, 1);
    Node nodeBlock = new Node(Token.BLOCK, 1, 1, 1);
    Node nodeLabel = new Node(Token.LABEL, 1, 1, 1);

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeBlock));
    assertTrue(NodeUtil.isStatement(nodeLabel));
    assertFalse(NodeUtil.isStatement(nodeFunction));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = new Node(Token.FUNCTION, 1, 1, 1);
    Node nodeStatementFunction = new Node(Token.FUNCTION, 1, 1, 1, null, null, null, null, null);

    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunction));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeStatementFunction));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunction = new Node(Token.FUNCTION, 1, 1, 1);
    Node nodeScript = new Node(Token.SCRIPT, 1, 1, 1);
    Node nodeFunctionInScript = new Node(Token.FUNCTION, 1, 1, 1, null, null, nodeScript, null, null);
    Node nodeFunctionInFunction = new Node(Token.FUNCTION, 1, 1, 1, null, null, nodeFunction, null, null);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInScript));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));
  }

  @Test
  public void testCheckParentNotNull() {
    Node nodeFunction = new Node(Token.FUNCTION, 1, 1, 1);

    checkState(nodeFunction.getParent() != null);
  }
}