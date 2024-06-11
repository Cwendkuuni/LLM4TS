package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[] {1, 2, 3});
    Node nodeForStatement = new Node(Token.FOR, new int[] {1, 2});
    Node nodeIfStatement = new Node(Token.IF, new int[] {1, 2});
    Node nodeFunction = new Node(Token.FUNCTION, new int[] {1, 2});

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeForStatement));
    assertFalse(NodeUtil.isForIn(nodeIfStatement));
    assertFalse(NodeUtil.isForIn(nodeFunction));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT, new int[0]);
    Node nodeBlock = new Node(Token.BLOCK, new int[0]);
    Node nodeLabel = new Node(Token.LABEL, new int[] {1});
    Node nodeFunction = new Node(Token.FUNCTION, new int[] {1, 2});
    Node nodeExpression = new Node(Token.IDENT, new int[] {1});

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeBlock));
    assertTrue(NodeUtil.isStatement(nodeLabel));
    assertFalse(NodeUtil.isStatement(nodeFunction));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[] {1, 2, 3});
    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[] {1, 2}, null);
    Node nodeIfStatement = new Node(Token.IF, new int[] {1, 2});

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeIfStatement));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = new Node(Token.SCRIPT, new int[0]);
    Node nodeFunctionDeclarationInScript = new Node(Token.FUNCTION, new int[] {1, 2, 3}, null);
    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, new int[] {1, 2}, new Node[]{nodeFunctionDeclarationInScript});

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
    Node nodeFunctionDeclarationOutsideFunction = new Node(Token.FUNCTION, new int[] {1, 2, 3}, null);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationOutsideFunction));
  }
}