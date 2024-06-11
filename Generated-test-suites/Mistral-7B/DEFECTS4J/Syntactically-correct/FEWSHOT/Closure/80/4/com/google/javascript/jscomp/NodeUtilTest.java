package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn();
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newCall(Token.VAR, "foo");
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpressionStatement(Node.newCall(Token.PRINT, "foo"));
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeNotStatement = Node.newCall(Token.PRINT, "foo");
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction("foo", new Node[]{});
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionStatement(Node.newFunction("foo", new Node[]{Node.newName("bar")}));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeRootFunction = Node.newFunction("rootFunction", new Node[]{});
    Node nodeFunctionDeclaration = Node.newFunction("foo", new Node[]{});
    Node nodeFunctionDeclarationInRootFunction = Node.newFunction("foo", new Node[]{});
    Node nodeFunctionExpression = Node.newExpressionStatement(Node.newFunction("foo", new Node[]{Node.newName("bar")}));

    Node scriptNode = Node.newScript(nodeFunctionDeclaration, nodeRootFunction);
    checkState(scriptNode != null);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInRootFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionExpression));
  }
}