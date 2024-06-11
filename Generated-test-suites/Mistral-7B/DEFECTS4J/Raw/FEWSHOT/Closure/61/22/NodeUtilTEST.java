package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn();
    Node nodeIf = Node.newLiteral(Token.IF);
    Node nodeFunction = Node.newFunction("foo", new Node[0], null);

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeIf));
    assertFalse(NodeUtil.isForIn(nodeFunction));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newText("const foo = 1;");
    Node nodeFunction = Node.newFunction("foo", new Node[0], null);
    Node nodeIf = Node.newLiteral(Token.IF);

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertFalse(NodeUtil.isStatement(nodeFunction));
    assertFalse(NodeUtil.isStatement(nodeIf));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction("foo", new Node[0], null);
    Node nodeFunctionExpression = Node.newExpressionStatement(Node.newName("foo"), new Node[0]);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = Node.newText("function foo() {}");
    Node functionNode = Node.newFunction("foo", new Node[0], null);
    Node functionDeclarationInFunctionNode = Node.newFunction("bar", new Node[0], null);
    Node functionDeclarationInScriptNode = Node.newFunction("baz", new Node[0], null);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationInScriptNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationInFunctionNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclarationNullParent() {
    Node functionNode = Node.newFunction("foo", new Node[0], null);

    Preconditions.checkNotNull(functionNode.getParent());
    assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));

    functionNode.setParent(null);
    assertNull(functionNode.getParent());
    assertFalse(NodeUtil.isFunctionDeclaration(functionNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }
}