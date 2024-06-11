package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[]{}, new int[]{0, 0, 0});
    Node nodeNotForIn = Node.newNode(Token.IF, new int[]{}, new int[]{0, 0, 0});

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newNode(Token.SCRIPT, new int[]{}, new int[]{0, 0, 0});
    Node nodeBlock = Node.newNode(Token.BLOCK, new int[]{}, new int[]{0, 0, 0});
    Node nodeLabel = Node.newNode(Token.LABEL, new int[]{}, new int[]{0, 0, 0});
    Node nodeFunction = Node.newNode(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0});
    Node nodeIf = Node.newNode(Token.IF, new int[]{}, new int[]{0, 0, 0});
    Node nodeExpression = Node.newNode(Token.ASSIGN, new int[]{}, new int[]{0, 0, 0});

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeBlock));
    assertTrue(NodeUtil.isStatement(nodeLabel));
    assertFalse(NodeUtil.isStatement(nodeFunction)); // Part of an expression
    assertFalse(NodeUtil.isStatement(nodeIf));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{1}, new int[]{0, 0, 0});
    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, new int[]{0}, new int[]{0, 0, 0});
    Node nodeIfStatement = Node.newNode(Token.IF, new int[]{1}, new int[]{0, 0, 0});

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeIfStatement));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newNode(Token.SCRIPT, new int[]{1, 0}, new int[]{0, 0, 0});
    Node nodeFunctionDeclarationInScript = Node.newNode(Token.FUNCTION, new int[]{1}, new int[]{0, 0, 0});
    Node nodeFunctionDeclarationInFunction = Node.newNode(
         Token.FUNCTION, new int[]{1, 0}, new int[]{0, 0, 0});

    Node nodeFunctionDeclarationNotInScript = Node.newNode(
        Token.FUNCTION, new int[]{1}, new int[]{1, 0, 0});

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationNotInScript));
  }

  @Test
  public void testIsHoistedFunctionDeclarationWithNullParent() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{1}, new int[]{0, 0, 0});

    assertNull(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
  }
}