package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{}, new int[]{0});
    Node nodeNotForIn = new Node(Token.IF, new int[]{}, new int[]{0});

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT, new int[]{}, new int[]{0});
    Node nodeStatement = new Node(Token.ASSIGN, new int[]{}, new int[]{0, 1});
    Node nodeExpression = new Node(Token.ADD, new int[]{}, new int[]{0, 1});

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeStatement));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{"id"}, new int[]{0, 1});
    Node nodeFunctionExpression = new Node(Token.IDENT, new int[]{1}, new int[]{0});
    Node nodeIfStatement = new Node(Token.IF, new int[]{}, new int[]{0});

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeIfStatement));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclarationInScript = new Node(Token.FUNCTION, new int[]{"id"}, new int[]{0, 1});
    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, new int[]{"id"}, new int[]{0, 1});

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }

  @Test
  public void testIsHoistedFunctionDeclarationWithParentFunction() {
    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, new int[]{"id"}, new int[]{0, 1});
    Node nodeFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{0});

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction.getParent()));
  }

  @Test
  public void testIsHoistedFunctionDeclarationWithParentScript() {
    Node nodeFunctionDeclarationInScript = new Node(Token.FUNCTION, new int[]{"id"}, new int[]{0, 1});
    Node nodeScript = new Node(Token.SCRIPT, new int[]{}, new int[]{0});

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeScript));
  }

  @Test
  public void testIsHoistedFunctionDeclarationWithParentBlock() {
    Node nodeFunctionDeclarationInBlock = new Node(Token.FUNCTION, new int[]{"id"}, new int[]{0, 1});
    Node nodeBlock = new Node(Token.BLOCK, new int[]{}, new int[]{0});

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInBlock));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeBlock));
  }

  @Test
  public void testIsHoistedFunctionDeclarationWithParentLabel() {
    Node nodeFunctionDeclarationInLabel = new Node(Token.FUNCTION, new int[]{"id"}, new int[]{0, 1});
    Node nodeLabel = new Node(Token.LABEL, new int[]{"id"}, new int[]{0});

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInLabel));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeLabel));
  }
}