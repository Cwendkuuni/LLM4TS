package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{0, 0, 1, 0, 1, 0, 1, 1, 1}, new int[]{2, 1, 3, 1, 4, 1, 5, 1, 6});
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, new int[]{0, 1, 0, 1, 0, 1, 0, 1}, new int[]{2, 1, 3, 1, 4, 1, 5, 1, 6});
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT, new int[]{0}, new int[]{1});
    assertTrue(NodeUtil.isStatement(nodeScript));

    Node nodeExpression = new Node(Token.ADD, new int[]{0, 1, 0}, new int[]{2, 1, 3});
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{0, 1, 0, 1, 0}, new int[]{2, 1, 3, 1, 4});
    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[]{0, 1, 0, 1, 0, 1, 0}, new int[]{2, 1, 3, 1, 4, 1, 5});
    Node nodeIfStatement = new Node(Token.IF, new int[]{0, 1, 0, 1, 0, 1, 0}, new int[]{2, 1, 3, 1, 4, 1, 5});

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeIfStatement));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = new Node(Token.SCRIPT, new int[]{0}, new int[]{1});
    Node nodeFunction = new Node(Token.FUNCTION, new int[]{0, 1, 0, 1, 0}, new int[]{2, 1, 3, 1, 4});
    Node nodeFunctionInsideFunction = new Node(Token.FUNCTION, new int[]{0, 1, 0, 1, 0, 1, 0}, new int[]{2, 1, 3, 1, 4, 1, 5});
    Node nodeFunctionInsideScript = new Node(Token.FUNCTION, new int[]{0, 1, 0, 1, 0}, new int[]{2, 1, 3, 1, 4});

    Node parentOfScript = null;
    Node parentOfFunction = new Node(Token.FUNCTION, new int[]{0, 1, 0, 1, 0}, new int[]{2, 1, 3, 1, 4});
    Node parentOfFunctionInsideFunction = new Node(Token.FUNCTION, new int[]{0, 1, 0, 1, 0}, new int[]{2, 1, 3, 1, 4});

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInsideFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInsideScript));

    checkState(parentOfScript == null);
    checkState(parentOfFunction.getParent() == parentOfScript);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));

    checkState(parentOfFunctionInsideFunction.getParent() == nodeFunction);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInsideFunction));
  }
}