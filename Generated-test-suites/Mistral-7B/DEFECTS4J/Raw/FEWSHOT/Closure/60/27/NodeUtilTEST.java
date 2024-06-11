package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeFor = new Node(Token.FOR, new int[]{}, new int[]{0});
    Node nodeIf = new Node(Token.IF, new int[]{}, new int[]{0, 1});

    assertTrue(NodeUtil.isForIn(nodeFor));
    assertFalse(NodeUtil.isForIn(nodeIf));
  }

  @Test
  public void testIsStatement() {
    Node nodeFor = new Node(Token.FOR, new int[]{}, new int[]{0});
    Node nodeIf = new Node(Token.IF, new int[]{}, new int[]{0, 1});
    Node nodeFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{0});
    Node nodeExpression = new Node(Token.ASSIGN, new int[]{}, new int[]{0, 1});

    Node script = new Node(Token.SCRIPT, new int[]{}, new int[]{0});
    Node block = new Node(Token.BLOCK, new int[]{}, new int[]{0});
    Node label = new Node(Token.LABEL, new int[]{"label"}, new int[]{0});

    assertTrue(NodeUtil.isStatement(nodeFor));
    assertTrue(NodeUtil.isStatement(script));
    assertTrue(NodeUtil.isStatement(block));
    assertTrue(NodeUtil.isStatement(label));
    assertFalse(NodeUtil.isStatement(nodeIf));
    assertFalse(NodeUtil.isStatement(nodeFunction));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = new Node(Token.FUNCTION, new int[]{"name"}, new int[]{0, 1, 2});
    Node nodeFunctionExpression = new Node(Token.EXPR_STATEMENT, new int[]{}, new int[]{0, 1});
    Node nodeFunctionWithBody = new Node(Token.FUNCTION, new int[]{"name"}, new int[]{0, 1, 2, 3});

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionWithBody));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunction = new Node(Token.FUNCTION, new int[]{"name"}, new int[]{0, 1, 2});
    Node nodeScript = new Node(Token.SCRIPT, new int[]{}, new int[]{0});
    Node nodeFunctionInScript = new Node(Token.FUNCTION, new int[]{"name"}, new int[]{0, 1, 2});
    Node nodeFunctionInFunction = new Node(Token.FUNCTION, new int[]{"func"}, new int[]{0, 1, 2});

    Node functionNode = new Node(Token.FUNCTION, new int[]{"func"}, new int[]{0, 1, 2, 3});

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInScript));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }

  @Test
  public void testNullCheck() {
    checkState(NodeUtil.isForIn(null) == false);
    checkState(NodeUtil.isStatement(null) == false);
    checkState(NodeUtil.isFunctionDeclaration(null) == false);
    checkState(NodeUtil.isHoistedFunctionDeclaration(null) == false);
  }
}