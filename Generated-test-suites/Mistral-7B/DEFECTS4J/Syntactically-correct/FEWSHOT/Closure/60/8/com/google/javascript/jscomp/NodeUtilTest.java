package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, null, null, null, null);
    Node nodeFor = Node.newNode(Token.FOR, null, null, null, null, Node.newNode(Token.IDENT, "for"));
    Node nodeIf = Node.newNode(Token.IF, null, null, Node.newNode(Token.IDENT, "if"), null);

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeFor));
    assertFalse(NodeUtil.isForIn(nodeIf));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newNode(Token.SCRIPT, null, null, null, null);
    Node nodeFunction = Node.newNode(Token.FUNCTION, null, null, null, null, Node.newNode(Token.IDENT, "func"));
    Node nodeIf = Node.newNode(Token.IF, null, null, Node.newNode(Token.IDENT, "if"), null);
    Node nodeAssign = Node.newNode(Token.ASSIGN, Node.newNode(Token.IDENT, "x"), Node.newNode(Token.INT_LITERAL, 42));

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeFunction));
    assertTrue(NodeUtil.isStatement(nodeAssign));
    assertFalse(NodeUtil.isStatement(Node.newNode(Token.IF, null, null, null, null)));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, null, null, Node.newNode(Token.IDENT, "func"), null);
    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, null, null, null, null, Node.newNode(Token.IDENT, "func"));

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newNode(Token.SCRIPT, null, null, null, null);
    Node nodeFunctionInScript = Node.newNode(Token.FUNCTION, null, null, Node.newNode(Token.IDENT, "func"), null);
    Node nodeFunctionInFunction = Node.newNode(
        Token.FUNCTION, null, null, null, null, Node.newNode(Token.IDENT, "funcParent"), nodeFunctionInScript);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInScript));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));
  }

  @Test(expected = NullPointerException.class)
  public void testIsHoistedFunctionDeclarationNullNode() {
    NodeUtil.isHoistedFunctionDeclaration(null);
  }

  private void assertFalse(boolean condition) {
    // Do nothing.
  }

  private void assertTrue(boolean condition) {
    checkNotNull(condition);
    assertTrue(condition);
  }
}