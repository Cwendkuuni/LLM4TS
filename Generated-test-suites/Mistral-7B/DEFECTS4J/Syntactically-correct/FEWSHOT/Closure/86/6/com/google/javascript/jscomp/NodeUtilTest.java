package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, 1);
    Node nodeFor = new Node(Token.FOR, 1);
    Node nodeIf = new Node(Token.IF, 1);

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeFor));
    assertFalse(NodeUtil.isForIn(nodeIf));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT, 1);
    Node nodeBlock = new Node(Token.BLOCK, 1);
    Node nodeStatement = new Node(Token.IDENT, 1);
    Node nodeExpression = new Node(Token.IDENT, 1);
    Node nodeFunction = new Node(Token.FUNCTION, 1);
    Node nodeIf = new Node(Token.IF, 1);
    Node nodeLabel = new Node(Token.LABEL, 1);

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeBlock));
    assertTrue(NodeUtil.isStatement(nodeStatement));
    assertFalse(NodeUtil.isStatement(nodeExpression));
    assertFalse(NodeUtil.isStatement(nodeFunction));
    assertFalse(NodeUtil.isStatement(nodeIf));
    assertFalse(NodeUtil.isStatement(nodeLabel));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, 1);
    Node nodeFunctionExpression = new Node(Token.FUNCTION, 1);
    Node nodeVariable = new Node(Token.IDENT, 1);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeVariable));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = new Node(Token.SCRIPT, 1);
    Node nodeBlock = new Node(Token.BLOCK, 1);
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, 1);
    Node nodeFunction = new Node(Token.FUNCTION, 1);
    Node nodeFunctionInScript = new Node(Token.FUNCTION, 1);
    Node nodeFunctionInBlock = new Node(Token.FUNCTION, 1);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInScript));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInBlock));

    checkState(nodeFunctionDeclaration.getParent() != null);
    checkState(nodeFunctionInScript.getParent() != null);
    checkState(nodeFunctionInBlock.getParent() != null);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInScript));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInBlock));
  }
}