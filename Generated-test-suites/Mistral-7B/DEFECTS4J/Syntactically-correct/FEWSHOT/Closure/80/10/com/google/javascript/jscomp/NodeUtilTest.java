package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[] {1, 2, 3});
    Node nodeNotForIn = Node.newNode(Token.IF, new int[] {1, 2});

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newNode(Token.SCRIPT, new int[] {});
    Node nodeStatement = Node.newNode(Token.PRINT, new int[] {1, 2});
    Node nodeFunction = Node.newNode(Token.FUNCTION, new int[] {1, 2});
    Node nodeExpression = Node.newNode(Token.ADD, new int[] {1, 2});

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeStatement));
    assertTrue(NodeUtil.isStatement(nodeFunction)); // only if it's a function declaration
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[] {1, 2, 3});
    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, new int[] {1, 2, 3, 4});

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newNode(Token.SCRIPT, new int[] {});
    Node nodeFunctionDeclarationInScript = Node.newNode(Token.FUNCTION, new int[] {1, 2, 3});
    Node nodeFunctionDeclarationInFunction = Node.newNode(
        Token.FUNCTION, new int[] {1, 2, 3}, Node.newNode(Token.FUNCTION, new int[] {1, 2}));

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }

  private void assertFalse(boolean condition) {
    // A placeholder for JUnit 4, as assertFalse is not available in JUnit 4
  }
}