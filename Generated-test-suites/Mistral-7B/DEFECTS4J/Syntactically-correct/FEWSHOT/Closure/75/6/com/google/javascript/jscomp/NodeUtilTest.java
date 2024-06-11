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

    Node nodeIf = Node.newIf();
    assertFalse(NodeUtil.isForIn(nodeIf));

    Node nodeWhile = Node.newWhile();
    assertFalse(NodeUtil.isForIn(nodeWhile));

    Node nodeFunction = Node.newFunction();
    assertFalse(NodeUtil.isForIn(nodeFunction));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newScript();
    assertTrue(NodeUtil.isStatement(nodeScript));

    Node nodeExpression = Node.newIdentifier("x");
    assertFalse(NodeUtil.isStatement(nodeExpression));

    Node nodeIf = Node.newIf();
    assertTrue(NodeUtil.isStatement(nodeIf));

    Node nodeLabel = Node.newLabel();
    assertTrue(NodeUtil.isStatement(nodeLabel));

    Node nodeWhile = Node.newWhile();
    assertTrue(NodeUtil.isStatement(nodeWhile));

    Node nodeFunction = Node.newFunction();
    assertTrue(NodeUtil.isStatement(nodeFunction));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunctionDeclaration("func");
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newFunctionExpression("func", Node.newList(), Node.newBlock(Node.newList()));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newScript();
    Node functionDeclaration = Node.newFunctionDeclaration("func");
    Node childFunctionDeclaration = Node.newFunctionDeclaration("childFunc");
    Node childFunctionDeclaration2 = Node.newFunctionDeclaration("childFunc2");

    nodeScript.setBody(Node.newList(functionDeclaration, childFunctionDeclaration, childFunctionDeclaration2));

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(childFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(childFunctionDeclaration2));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_functionInFunction() {
    Node nodeFunction = Node.newFunctionDeclaration("func");
    Node childFunction = Node.newFunctionDeclaration("childFunc");
    Node childFunctionBody = Node.newBlock(Node.newList(childFunction));

    nodeFunction.setBody(childFunctionBody);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(childFunction));
  }

  @Test(expected = IllegalStateException.class)
  public void testIsHoistedFunctionDeclaration_parentIsNull() {
    Node nodeFunction = Node.newFunctionDeclaration("func");
    checkState(nodeFunction.getParent() != null, "Parent of node is null");
  }
}