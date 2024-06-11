package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeFor = Node.newForStatement(1, Token.IDENT, "i", new Node[0], new Node[0], new Node[0]);
    assertTrue(NodeUtil.isForIn(nodeFor));

    Node nodeIf = Node.newIfStatement(1, null, new Node[0], new Node[0]);
    assertFalse(NodeUtil.isForIn(nodeIf));

    Node nodeFunction = Node.newFunction(1, new Node[0], null, new Node[0], new Node[0]);
    assertFalse(NodeUtil.isForIn(nodeFunction));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newScript(1, new Node[0]);
    assertTrue(NodeUtil.isStatement(nodeScript));

    Node nodeIf = Node.newIfStatement(1, null, new Node[0], new Node[0]);
    assertTrue(NodeUtil.isStatement(nodeIf));

    Node nodeFunction = Node.newFunction(1, new Node[0], null, new Node[0], new Node[0]);
    assertTrue(NodeUtil.isStatement(nodeFunction));

    Node nodeExpression = Node.newCallExpression(1, null, new Node[0], new Node[0]);
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = Node.newFunction(1, new Node[0], null, new Node[0], new Node[0]);
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));

    Node nodeFunctionExpression = Node.newFunctionExpression(1, null, new Node[0], new Node[0], new Node[0]);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));

    Node nodeVariable = Node.newVariable(1, Token.IDENT, "foo");
    assertFalse(NodeUtil.isFunctionDeclaration(nodeVariable));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunction = Node.newFunction(1, new Node[0], null, new Node[0], new Node[0]);
    Node nodeScript = Node.newScript(1, new Node[0]);
    nodeScript.addChildToEnd(nodeFunction);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));

    Node nodeFunctionInFunction = Node.newFunction(1, new Node[0], null, new Node[0], new Node[0]);
    Node nodeFunctionInScript = Node.newFunction(1, new Node[0], null, new Node[0], new Node[0]);
    nodeFunctionInFunction.setParent(nodeFunctionInScript);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsFunctionDeclarationWithNull() {
    NodeUtil.isFunctionDeclaration(null);
  }

  @Test(expected = IllegalStateException.class)
  public void testIsStatementWithNull() {
    NodeUtil.isStatement(null);
  }
}