package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeFor = Node.newForStatement(1, null, null, null, null);
    assertTrue(NodeUtil.isForIn(nodeFor));

    Node nodeIf = Node.newIfStatement(1, null, null, null);
    assertFalse(NodeUtil.isForIn(nodeIf));
  }

  @Test
  public void testIsStatement() {
    Node nodeFunction = Node.newFunction(1, null, null, null);
    assertTrue(NodeUtil.isStatement(nodeFunction));

    Node nodeIf = Node.newIfStatement(1, null, null, null);
    assertTrue(NodeUtil.isStatement(nodeIf));

    Node nodeExpression = Node.newExpressionStatement(Node.newName("x"), null);
    assertTrue(NodeUtil.isStatement(nodeExpression));

    Node nodeScript = Node.newScript(1);
    assertTrue(NodeUtil.isStatement(nodeScript));

    Node nodeBlock = Node.newBlock(1);
    assertTrue(NodeUtil.isStatement(nodeBlock));

    Node nodeLabel = Node.newLabel(1, "label");
    assertTrue(NodeUtil.isStatement(nodeLabel));

    Node nodeVariable = Node.newVariable(1, "a");
    assertFalse(NodeUtil.isStatement(nodeVariable));

    Node nodeAssignment = Node.newAssignment(1, Node.newName("x"), Node.newName("y"));
    assertFalse(NodeUtil.isStatement(nodeAssignment));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = Node.newFunction(1, null, null, null);
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));

    Node nodeFunctionExpression = Node.newExpressionStatement(nodeFunction, null);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));

    Node nodeFunctionInExpression = Node.newFunctionExpression(1, null, null, null);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionInExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionInScript = Node.newFunction(1, null, null, null);
    Node script = Node.newScript(1, nodeFunctionInScript);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInScript));

    Node nodeFunctionInFunction = Node.newFunction(1, null, null, null);
    Node function = Node.newFunction(1, null, null, null, nodeFunctionInFunction);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));

    Node nodeFunctionInBlock = Node.newFunction(1, null, null, null);
    Node block = Node.newBlock(1, null, nodeFunctionInBlock);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInBlock));

    Node nodeFunctionInIf = Node.newIfStatement(1, null, null, null, nodeFunctionInBlock);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInBlock));

    checkArgument(NodeUtil.isFunctionDeclaration(nodeFunctionInBlock));
    checkState(nodeFunctionInBlock.getParent() != null);
    Node parent = nodeFunctionInBlock.getParent();
    checkArgument(parent.getType() == Token.IF);
    Node ifNode = (Node) parent;
    checkArgument(NodeUtil.isStatement(ifNode));
    checkState(ifNode.getParent() != null);
    parent = ifNode.getParent();
    checkArgument(parent.getType() == Token.SCRIPT || parent.getType() == Token.FUNCTION);
  }
}