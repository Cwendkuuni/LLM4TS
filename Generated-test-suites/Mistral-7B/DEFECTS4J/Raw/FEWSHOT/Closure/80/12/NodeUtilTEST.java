package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkArgument;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn();
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeIf = Node.newIf();
    assertFalse(NodeUtil.isForIn(nodeIf));

    Node nodeFunction = Node.newFunction("func", new Node[0], new Node[0]);
    assertFalse(NodeUtil.isForIn(nodeFunction));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newText("function foo() {}");
    assertTrue(NodeUtil.isStatement(nodeScript));

    Node nodeIf = Node.newIf();
    assertTrue(NodeUtil.isStatement(nodeIf));

    Node nodeFunctionExpression = Node.newFunctionExpression("func", new Node[0], new Node[0]);
    assertFalse(NodeUtil.isStatement(nodeFunctionExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = Node.newFunction("foo", new Node[0], new Node[0]);
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));

    Node nodeFunctionExpression = Node.newFunctionExpression("foo", new Node[0], new Node[0]);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunction = Node.newFunction("foo", new Node[0], new Node[0]);
    Node scriptNode = Node.newText("function bar() {}");
    Node functionNode = Node.newFunction("func", new Node[0], new Node[0]);
    Node functionDeclarationNode = Node.newFunction("func", new Node[0], new Node[0]);
    Node scriptWithFunctionNode = Node.newText("function bar() {}", functionNode);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(scriptNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(scriptWithFunctionNode));
  }

  @Test (expected = AssertionError.class)
  public void testCheckStateParentIsNull() {
    Node n = Node.newText("");
    NodeUtil.isStatement(n);
  }
}