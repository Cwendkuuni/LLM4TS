package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[] {1, 2, 3});
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newNode(Token.IF);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newNode(Token.SCRIPT, null);
    Node nodeFunction = Node.newNode(Token.FUNCTION, new int[] {1, 2, 3});
    Node nodeIf = Node.newNode(Token.IF, null);
    Node nodeExpression = Node.newNode(Token.IDENT, new int[] {"x"});

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeFunction));
    assertFalse(NodeUtil.isStatement(nodeIf));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = Node.newNode(Token.FUNCTION, new int[] {1, 2, 3});
    Node nodeFunctionExpression = Node.newNode(Token.ASSIGN, new int[] {1, 2}, Node.newNode(Token.FUNCTION, new int[] {1, 2, 3}));

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newNode(Token.SCRIPT, null);
    Node nodeFunction = Node.newNode(Token.FUNCTION, new int[] {1, 2, 3});
    Node nodeFunctionInFunction = Node.newNode(Token.FUNCTION, new int[] {1, 2}, Node.newNode(Token.FUNCTION, new int[] {1, 2, 3}));

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));
  }

  @Test
  public void testIsHoistedFunctionDeclarationInsideFunction() {
    Node nodeFunction = Node.newNode(Token.FUNCTION, new int[] {1, 2}, Node.newNode(Token.SCRIPT, null));
    Node nodeFunctionInFunction = Node.newNode(Token.FUNCTION, new int[] {1, 2}, Node.newNode(Token.FUNCTION, new int[] {1, 2, 3}));

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));
  }

  @Test
  public void testIsHoistedFunctionDeclarationInsideScript() {
    Node nodeFunction = Node.newNode(Token.FUNCTION, new int[] {1, 2}, Node.newNode(Token.SCRIPT, null));
    Node nodeFunctionInFunction = Node.newNode(Token.FUNCTION, new int[] {1, 2}, Node.newNode(Token.FUNCTION, new int[] {1, 2, 3}));
    Node nodeFunctionInScript = Node.newNode(Token.FUNCTION, new int[] {1, 2}, Node.newNode(Token.SCRIPT, null));

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInScript));
  }

  private void assertFalse(boolean condition) {
    checkState(condition == false, "Expected false but got %b", condition);
  }
}