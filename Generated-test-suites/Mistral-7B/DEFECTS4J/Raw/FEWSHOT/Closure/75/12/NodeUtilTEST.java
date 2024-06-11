package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[]{}, new int[]{1, 2, 3});
    Node nodeNotForIn = Node.newNode(Token.IF, new int[]{}, new int[]{1});

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newNode(Token.SCRIPT, new int[]{}, new int[]{1});
    Node nodeBlock = Node.newBlock(new Node[] {Node.newNode(Token.IF, new int[] {}, new int[]{1})});
    Node nodeLabel = Node.newNode(Token.LABEL, new int[]{}, new int[]{1});
    Node nodeFunction = Node.newNode(Token.FUNCTION, new int[]{}, new int[]{1, 2});
    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, new int[] {1}, new int[]{1, 2});
    Node nodeExpression = Node.newNode(Token.IDENT, new int[]{"a"}, new int[]{1});

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeBlock));
    assertTrue(NodeUtil.isStatement(nodeLabel));
    assertFalse(NodeUtil.isStatement(nodeFunction));
    assertFalse(NodeUtil.isStatement(nodeFunctionExpression));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{1, 2}, new int[]{1, 2, 3});
    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, new int[]{1}, new int[]{1, 2, 3});

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newNode(Token.SCRIPT, new int[]{}, new int[]{1});
    Node nodeFunctionInScript = Node.newNode(Token.FUNCTION, new int[]{1, 2}, new int[]{1, 2, 3});
    Node nodeFunctionInFunction = Node.newNode(Token.FUNCTION, new int[]{1}, new int[]{1, 2});
    Node nodeFunctionInFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{1, 2}, new int[]{1, 2, 3});
    Node nodeFunctionInFunctionDeclarationInScript = Node.newNode(
        Token.FUNCTION, new int[]{1, 2}, new int[]{1, 2, 3}, Node.newNode(Token.IDENT, new int[]{"a"}, new int[]{1}));

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunctionDeclarationInScript));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInScript));
  }
}