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

    Node nodeIf = Node.newIfStatement();
    assertFalse(NodeUtil.isForIn(nodeIf));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newScript();
    Node nodeStatement = Node.newExpressionStatement(Node.newName("x"));
    Node nodeIf = Node.newIfStatement();

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeStatement));
    assertFalse(NodeUtil.isStatement(nodeIf));

    Node nodeFunction = Node.newFunction();
    assertTrue(NodeUtil.isStatement(nodeFunction));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunctionDeclaration("foo", Node.newList());
    Node nodeFunctionExpression = Node.newFunctionExpression(Node.newName("bar"), Node.newList());

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunctionDeclaration("foo", Node.newList());
    Node nodeFunctionDeclarationInFunction = Node.newFunction(
        "bar", Node.newList(), Node.newBlock(Node.newList(nodeFunctionDeclaration)));

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));

    Node nodeFunctionDeclarationInBlock = Node.newBlock(Node.newList(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInBlock));
  }
}