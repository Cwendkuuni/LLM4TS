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

    Node nodeWhile = Node.newStatement(Token.WHILE);
    assertFalse(NodeUtil.isForIn(nodeWhile));

    Node nodeIf = Node.newStatement(Token.IF);
    assertFalse(NodeUtil.isForIn(nodeIf));

    Node nodeFunction = Node.newFunction("func", new int[0]);
    assertFalse(NodeUtil.isForIn(nodeFunction));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newScript(new int[0]);
    assertTrue(NodeUtil.isStatement(nodeScript));

    Node nodeFunction = Node.newFunction("func", new int[0]);
    assertTrue(NodeUtil.isStatement(nodeFunction));

    Node nodeExpression = Node.newExpression("1 + 1");
    assertFalse(NodeUtil.isStatement(nodeExpression));

    Node nodeNull = Node.nullLiteral();
    assertFalse(NodeUtil.isStatement(nodeNull));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = Node.newFunction("func", new int[0]);
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));

    Node nodeFunctionExpression = Node.newExpression(
        Node.newFunctionExpression(Node.newIdentifier("func"), new int[0],
            Node.newBlock(new int[0])));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunction = Node.newFunction("func", new int[0]);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));

    Node nodeRoot = Node.newScript(new int[0]);
    nodeRoot.addChild(Node.newStatement(nodeFunction));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));

    Node nodeFunctionInsideFunction = Node.newFunction("func2", new int[0]);
    Node nodeFunctionParent = Node.newFunction("func1", new int[0]);
    nodeFunctionParent.addChild(Node.newStatement(nodeFunctionInsideFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInsideFunction));
  }
}