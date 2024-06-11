package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[] {1, 3});
    Node nodeNotForIn = new Node(Token.IF, new int[] {1});

    assertTrue("isForIn should return true for FOR-IN loop node", NodeUtil.isForIn(nodeForIn));
    assertFalse("isForIn should return false for IF statement node", NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT, new int[] {1});
    Node nodeFunction = new Node(Token.FUNCTION, new int[] {1, 1});
    Node nodeExpression = new Node(Token.IDENT, new int[] {1});

    assertTrue("isStatement should return true for SCRIPT node", NodeUtil.isStatement(nodeScript));
    assertTrue("isStatement should return true for FUNCTION (declaration) node", NodeUtil.isStatement(nodeFunction));
    assertFalse("isStatement should return false for EXPRESSION node", NodeUtil.isStatement(nodeExpression));

    Node parentScript = new Node(Token.SCRIPT, new int[] {1});
    parentScript.addChildToEnd(nodeFunction);
    Node parentFunction = new Node(Token.FUNCTION, new int[] {1, 1});
    parentFunction.addChildToEnd(nodeFunction);
    Node parentBlock = new Node(Token.BLOCK, new int[] {1});
    parentBlock.addChildToEnd(nodeFunction);

    assertTrue("isStatement should return true for FUNCTION node when it is a statement (in a script)", NodeUtil.isStatement(nodeFunction));
    assertTrue("isStatement should return true for FUNCTION node when it is a statement (in a function)", NodeUtil.isStatement(nodeFunction));
    assertTrue("isStatement should return true for FUNCTION node when it is a statement (in a block)", NodeUtil.isStatement(nodeFunction));
    assertFalse("isStatement should return false for FUNCTION node when it is not a statement (in an expression)", NodeUtil.isStatement(nodeFunction.getFirstChild()));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = new Node(Token.FUNCTION, new int[] {1, 1});
    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[] {1, 1, -1});

    assertTrue("isFunctionDeclaration should return true for function declaration node", NodeUtil.isFunctionDeclaration(nodeFunction));
    assertFalse("isFunctionDeclaration should return false for function expression node", NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunction = new Node(Token.FUNCTION, new int[] {1, 1});
    Node nodeScript = new Node(Token.SCRIPT, new int[] {1});
    Node nodeFunctionInScript = new Node(Token.FUNCTION, new int[] {1, 1});
    Node nodeFunctionInFunction = new Node(Token.FUNCTION, new int[] {1, 1});

    assertFalse("isHoistedFunctionDeclaration should return false for function declaration node not in the root", NodeUtil.isHoistedFunctionDeclaration(nodeFunction));
    assertTrue("isHoistedFunctionDeclaration should return true for function declaration node in the root", NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInScript));
    assertFalse("isHoistedFunctionDeclaration should return false for function declaration node in a function", NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));

    nodeFunctionInFunction.setParent(nodeScript);
    assertTrue("isHoistedFunctionDeclaration should return true for function declaration node in a function but hoisted to the root", NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));
  }

  private void assertFalse(String message, boolean condition) {
    checkState(false, message + " Expected: false, but got: " + condition);
  }

  private void assertTrue(String message, boolean condition) {
    checkState(condition, message + " Expected: true, but got: " + condition);
  }
}