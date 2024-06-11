package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[] {1, 2, 3});
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newNode(Token.IF, new int[] {1, 2});
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.ASSIGN, new int[] {1, 2, 3});
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = Node.newNode(Token.ADD, new int[] {1, 2, 3, 4});
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[] {1, 2}, null, null, null);
    Node functionChild = Node.newNode(Token.IDENT, new int[] {1, 2}, "functionName");
    nodeFunctionDeclaration.setFirstChild(functionChild);
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, new int[] {1, 2}, null, Node.newNode(Token.IDENT, new int[] {1, 2}, "functionName"), null);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newNode(Token.SCRIPT, new int[] {});
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[] {1, 2}, null, Node.newNode(Token.IDENT, new int[] {1, 2}, "functionName"), null);
    nodeScript.setFirstChild(nodeFunctionDeclaration);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = Node.newNode(Token.FUNCTION, new int[] {1, 2}, null, null, Node.newNode(Token.FUNCTION, new int[] {1, 2}, null, Node.newNode(Token.IDENT, new int[] {1, 2}, "functionName"), null));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }

  @Test(expected = AssertionError.class)
  public void testPreconditionsCheckStateFailure() {
    Node node = Node.newNode(Token.SCRIPT, new int[] {});
    NodeUtil.isStatement(node);
  }
}