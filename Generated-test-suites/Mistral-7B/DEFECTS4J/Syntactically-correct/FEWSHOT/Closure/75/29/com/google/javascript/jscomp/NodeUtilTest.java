package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, null, null, null, null);
    Node nodeFor = Node.newNode(Token.FOR, null, null, null, null, Token.FORIN, null);
    Node nodeIf = Node.newNode(Token.IF, null, null, null, null);

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeFor));
    assertFalse(NodeUtil.isForIn(nodeIf));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = Node.newNode(Token.SCRIPT, null, null, null, null);
    Node blockNode = Node.newNode(Token.BLOCK, null, null, null, null);
    Node labelNode = Node.newNode(Token.LABEL, null, null, null, null, "labelName");
    Node functionNode = Node.newNode(Token.FUNCTION, null, null, null, null);
    Node ifNode = Node.newNode(Token.IF, null, null, null, null);
    Node expressionNode = Node.newNode(Token.ASSIGN, null, null, null, null, Token.IDENT, "x");

    assertTrue(NodeUtil.isStatement(scriptNode));
    assertTrue(NodeUtil.isStatement(blockNode));
    assertTrue(NodeUtil.isStatement(labelNode));
    assertTrue(NodeUtil.isStatement(functionNode));
    assertFalse(NodeUtil.isStatement(ifNode));
    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = Node.newNode(Token.FUNCTION, null, null, null, null);
    Node functionExpression = Node.newNode(Token.FUNCTION, null, null, null, null, Token.ASSIGN, Node.newNode(Token.IDENT, null, null, null, null, "f"));
    Node ifStatement = Node.newNode(Token.IF, null, null, null, null);

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpression));
    assertFalse(NodeUtil.isFunctionDeclaration(ifStatement));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = Node.newNode(Token.SCRIPT, null, null, null, null);
    Node functionDeclaration = Node.newNode(Token.FUNCTION, null, null, null, null);
    Node functionDeclarationInFunction = Node.newNode(Token.FUNCTION, null, null, null, null, Token.FUNCTION, null, Node.newNode(Token.FUNCTION, null, null, null, null), null);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclaration));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationInFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(Node.newNode(Token.FUNCTION, null, null, null, null, Token.ASSIGN, Node.newNode(Token.IDENT, null, null, null, null, "f"))));
  }

  // Add more test cases as needed

  private static boolean assertFalse(boolean condition) {
    if (condition) {
      throw new AssertionError("Expected false, but got true.");
    }
  }

  private static void assertFunctionDeclaration(Node node, boolean expected) {
    checkArgument(expected != node.getType(), "Expected node to be a function declaration, but it is type %s", node.getType());
    assertTrue(expected == NodeUtil.isFunctionDeclaration(node));
  }

  private static void assertHoistedFunctionDeclaration(Node node, boolean expected) {
    checkArgument(expected != node.getType(), "Expected node to be a hoisted function declaration, but it is type %s", node.getType());
    assertTrue(expected == NodeUtil.isHoistedFunctionDeclaration(node));
  }

  private static void assertFunction(Node node, int type, int childrenCount) {
    checkState(node.getType() == type, "Expected node to be type %s, but it is type %s", type, node.getType());
    checkArgument(node.getChildCount() == childrenCount, "Expected node to have %d children, but it has %d", childrenCount, node.getChildCount());
  }
}