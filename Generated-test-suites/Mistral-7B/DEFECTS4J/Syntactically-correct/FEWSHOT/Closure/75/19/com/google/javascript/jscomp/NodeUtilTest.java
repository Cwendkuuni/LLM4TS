package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[] {1, 2, 3});
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newNode(Token.IF, new int[] {1, 2, 3});
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.IF, new int[] {1, 2, 3});
    Node parentOfStatement = Node.newBlock(new int[] {1, 2});
    parentOfStatement.addChild(nodeStatement);

    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeNotStatement = Node.newNode(Token.ID, new int[] {1, 2, 3});
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(
        Node.newIdentifier("myFunction"), null, new int[] {1, 2, 3});
    Node parentOfFunctionDeclaration = Node.newBlock(new int[] {1, 2});
    parentOfFunctionDeclaration.addChild(nodeFunctionDeclaration);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionStatement(
        Node.newFunctionExpression(Node.newIdentifier("myFunction"), null, new int[] {1, 2, 3}));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newFunction(
        Node.newIdentifier("myFunction"), null, new int[] {1, 2, 3});
    Node parentOfFunctionDeclaration = Node.newScript(new int[] {});
    parentOfFunctionDeclaration.addChild(nodeHoistedFunctionDeclaration);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeNotHoistedFunctionDeclaration = Node.newFunction(
        Node.newIdentifier("myFunction"), null, new int[] {1, 2, 3});
    Node parentOfNotFunctionDeclaration = Node.newBlock(new int[] {1, 2});
    parentOfNotFunctionDeclaration.addChild(nodeNotHoistedFunctionDeclaration);
    Node functionNode = Node.newFunctionDeclaration(Node.newIdentifier("myFunction"), null, new int[] {1, 2, 3});
    parentOfNotFunctionDeclaration.addChild(functionNode);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNotHoistedFunctionDeclaration));
  }

  @Test
  public void testIsHoistedFunctionDeclarationWithFunctionParent() {
    Node nodeHoistedFunctionDeclaration = Node.newFunction(
        Node.newIdentifier("myFunction"), null, new int[] {1, 2, 3});
    Node parentOfFunctionDeclaration = Node.newFunction(Node.newIdentifier("myFunctionParent"), null, new int[] {});
    parentOfFunctionDeclaration.addChild(nodeHoistedFunctionDeclaration);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));
  }

  @Test
  public void testIsHoistedFunctionDeclarationWithScriptParent() {
    Node nodeHoistedFunctionDeclaration = Node.newFunction(
        Node.newIdentifier("myFunction"), null, new int[] {1, 2, 3});
    Node parentOfFunctionDeclaration = Node.newScript(new int[] {});
    parentOfFunctionDeclaration.addChild(nodeHoistedFunctionDeclaration);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));
  }

  @Test
  public void testIsHoistedFunctionDeclarationWithEmptyParent() {
    Node nodeHoistedFunctionDeclaration = Node.newFunction(
        Node.newIdentifier("myFunction"), null, new int[] {1, 2, 3});
    Node parentOfFunctionDeclaration = Node.newScript(new int[] {});

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));
  }

  @Test
  public void testIsHoistedFunctionDeclarationWithNullParent() {
    Node nodeHoistedFunctionDeclaration = Node.newFunction(
        Node.newIdentifier("myFunction"), null, new int[] {1, 2, 3});
    Node parentOfFunctionDeclaration = null;

    assertNull(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));
  }
}