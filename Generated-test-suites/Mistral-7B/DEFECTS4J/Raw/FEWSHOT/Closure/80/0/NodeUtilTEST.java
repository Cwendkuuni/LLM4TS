package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Token;

@RunWith(JUnit4.class)
public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = Node.newNode(Token.FOR, new int[] { 0, 0, 0 }, new Node[3]);
    Node notForNode = Node.newNode(Token.IF, new int[] { 0, 0 }, new Node[2]);

    assertTrue(NodeUtil.isForIn(forNode));
    assertFalse(NodeUtil.isForIn(notForNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = Node.newNode(Token.IF, new int[] { 0, 0 }, new Node[2]);
    Node expressionNode = Node.newNode(Token.IF, new int[] { 0, 1 }, new Node[3]);
    Node scriptNode = Node.newNode(Token.SCRIPT, new int[] { 0 }, new Node[0]);

    assertTrue(NodeUtil.isStatement(statementNode));
    assertFalse(NodeUtil.isStatement(expressionNode));
    assertTrue(NodeUtil.isStatement(scriptNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = Node.newNode(Token.FUNCTION, new int[] { 0, 0 }, new Node[2]);
    Node functionExpression = Node.newNode(Token.FUNCTION, new int[] { 0, 1 }, new Node[2]);
    Node ifNode = Node.newNode(Token.IF, new int[] { 0, 0 }, new Node[2]);

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpression));
    assertFalse(NodeUtil.isFunctionDeclaration(ifNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = Node.newNode(Token.SCRIPT, new int[] { 0 }, new Node[0]);
    Node functionDeclaration = Node.newNode(Token.FUNCTION, new int[] { 0, 0 }, new Node[2]);
    Node functionInFunction = Node.newNode(Token.FUNCTION, new int[] { 0, 1 }, new Node[2]);

    Node functionNodeInScript = Node.newNode(Token.FUNCTION, new int[] { 0, 0 }, new Node[2]);
    Node functionNodeInFunction = Node.newNode(Token.FUNCTION, new int[] { 0, 1 }, new Node[2]);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionExpression));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(ifNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNodeInFunction));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNodeInScript));
  }

  private void assertFalse(boolean condition) {
    org.junit.Assert.assertFalse(condition);
  }

  private void assertTrue(boolean condition) {
    org.junit.Assert.assertTrue(condition);
  }

  @Test
  public void testNullCheck() {
    assertNullCheck("Function node is null", NodeUtil.isFunctionDeclaration(null));
    assertNullCheck("Node is null", NodeUtil.isStatement(null));
  }

  private void assertNullCheck(String message, Object condition) {
    checkNotNull(condition, message);
    if (condition instanceof Node) {
      assertNull(((Node) condition).getParent());
    }
  }
}