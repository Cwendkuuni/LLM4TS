package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForInStatement();
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeExpression = Node.newExpressionStatement(Node.newName("x"));
    assertFalse(NodeUtil.isForIn(nodeExpression));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newScript();
    assertTrue(NodeUtil.isStatement(nodeScript));

    Node nodeExpression = Node.newExpressionStatement(Node.newName("x"));
    assertTrue(NodeUtil.isStatement(nodeExpression));

    Node nodeFunction = Node.newFunctionExpression(Node.newName("f"), new int[0], Node.newBlock(null));
    assertTrue(NodeUtil.isStatement(nodeFunction));

    Node nodeVariableDeclaration = Node.newVariableDeclarationList(Node.newVariableDeclaration("x"));
    assertFalse(NodeUtil.isStatement(nodeVariableDeclaration));

    Node nodeAssignment = Node.newAssignment(Node.newName("x"), Node.newNumber(1));
    assertTrue(NodeUtil.isStatement(nodeAssignment));

    Node nodeIfStatement = Node.newIfStatement(Node.newExpression(Node.newNumber(1)), Node.newBlock(null), Node.newBlock(null));
    assertTrue(NodeUtil.isStatement(nodeIfStatement));

    Node nodeLabel = Node.newLabel("label");
    assertTrue(NodeUtil.isStatement(nodeLabel));

    Node nodeBlock = Node.newBlock(null);
    assertTrue(NodeUtil.isStatement(nodeBlock));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunctionDeclaration("f", new int[0], Node.newBlock(null));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newFunctionExpression(Node.newName("g"), new int[0], Node.newBlock(null));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclarationInScript = Node.newFunctionDeclaration("f", new int[0], Node.newBlock(null));
    Node parentScript = Node.newScript();
    parentScript.addChildToEnd(nodeFunctionDeclarationInScript);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));

    Node nodeFunctionDeclarationInFunction = Node.newFunctionExpression(Node.newName("g"), new int[0], Node.newBlock(null));
    Node parentFunction = Node.newFunctionExpression(Node.newName("h"), new int[0], Node.newBlock(null));
    parentFunction.addChildToEnd(nodeFunctionDeclarationInFunction);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }
}