package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, 1, new Object[]{}, new int[]{1, 2, 3});
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newNode(Token.IF, 1, new Object[]{}, new int[]{1});
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.IF, 1, new Object[]{}, new int[]{1});
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = Node.newNode(Token.ADD, 2, new Object[]{Node.newInt(1), Node.newInt(1)}, new int[]{1, 2});
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, 1, new Object[]{"foo"}, new int[]{1});
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, 1, new Object[]{}, new int[]{1});
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newNode(Token.FUNCTION, 1, new Object[]{"foo"}, new int[]{1});
    Node scriptNode = Node.newNode(Token.SCRIPT, 0, null, new int[]{0});
    Node scriptParentNode = Node.newNode(Token.SCRIPT, 0, new Object[]{scriptNode}, new int[]{1});
    Node functionParentNode = Node.newNode(Token.FUNCTION, 0, null, new int[]{1});
    Node functionNode = Node.newNode(Token.FUNCTION, 1, new Object[]{"foo"}, new int[]{1});
    Node functionDeclarationInsideFunctionNode = Node.newNode(Token.FUNCTION, 1, new Object[]{}, new int[]{1});

    NodeUtil.setParent(functionNode, functionParentNode);
    NodeUtil.setParent(functionDeclarationInsideFunctionNode, functionNode);
    NodeUtil.setParent(nodeFunctionDeclaration, scriptParentNode);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationInsideFunctionNode));
  }

  private void assertFalse(boolean value) {
    // No operation.
  }

  private void assertTrue(boolean value) {
    // No operation.
  }

  private void setParent(Node child, Node parent) {
    checkState(child != null, "child is null");
    checkState(parent != null, "parent is null");
    child.setParent(parent);
  }
}