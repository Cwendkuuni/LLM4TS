package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{}, new int[]{0, 0, 0});
    Node nodeFor = new Node(Token.FOR, new int[]{}, new int[]{0, 0, 1});
    Node nodeWhile = new Node(Token.WHILE, new int[]{}, new int[]{0, 0, 0});

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeFor));
    assertFalse(NodeUtil.isForIn(nodeWhile));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT, new int[]{}, new int[]{0, 0, 0});
    Node nodeBlock = new Node(Token.BLOCK, new int[]{}, new int[]{0, 0, 0});
    Node nodeFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0});
    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 1});
    Node nodeLabel = new Node(Token.LABEL, new int[]{}, new int[]{0, 0, 0});

    Node nodeExpression = new Node(Token.ASSIGN, new int[]{}, new int[]{0, 0, 0});
    Node nodeParentOfExpression = new Node(Token.SCRIPT, new int[]{}, new int[]{0, 0, 0});
    nodeParentOfExpression.addChildToEnd(nodeExpression);

    checkNotNull(NodeUtil.isStatement(nodeScript));
    checkNotNull(NodeUtil.isStatement(nodeBlock));
    checkNotNull(NodeUtil.isStatement(nodeLabel));
    checkNotNull(NodeUtil.isStatement(nodeFunctionDeclaration));
    checkNotNull(NodeUtil.isStatement(nodeFunctionExpression));

    assertFalse(NodeUtil.isStatement(nodeFunction));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0});
    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 1});

    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = new Node(Token.SCRIPT, new int[]{}, new int[]{0, 0, 0});
    Node nodeFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0});
    Node nodeFunctionInsideFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0});
    nodeFunction.addChildToEnd(nodeFunctionInsideFunction);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInsideFunction));
  }
}