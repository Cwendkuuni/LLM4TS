package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn();
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpressionStatement(Node.newExpression(1));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newScript();
    assertTrue(NodeUtil.isStatement(nodeScript));

    Node nodeExpression = Node.newExpressionStatement(Node.newExpression(1));
    assertTrue(NodeUtil.isStatement(nodeExpression));

    Node nodeFunction = Node.newFunction(null, null, null);
    assertTrue(NodeUtil.isStatement(nodeFunction));

    Node nodeBlock = Node.newBlock(null);
    assertTrue(NodeUtil.isStatement(nodeBlock));

    Node nodeLabel = Node.newLabel("labelName");
    assertTrue(NodeUtil.isStatement(nodeLabel));

    Node nodeStatement = Node.newExpressionStatement(Node.newExpression(1));
    Node parentScript = Node.newScript();
    parentScript.appendChild(nodeStatement);
    assertTrue(NodeUtil.isStatement(nodeStatement));
    checkState(parentScript != null);

    Node nodeExpressionInExpression = Node.newExpressionStatement(
        Node.newExpression(Node.newExpression(1), Node.newExpression(2)));
    Node parentExpression = Node.newExpressionStatement(
        Node.newExpression(Node.newExpression(1), Node.newIdentifier("op")));
    parentExpression.appendChild(nodeExpressionInExpression);
    assertFalse(NodeUtil.isStatement(nodeExpressionInExpression));
    checkState(parentExpression != null);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(null, null, null);
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionStatement(
        Node.newExpression(Node.newFunction(null, null, null), null));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(null, null, null);
    Node nodeScript = Node.newScript();
    nodeScript.appendChild(nodeFunctionDeclaration);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = Node.newFunction(null, null, null);
    Node nodeFunction = Node.newFunction(null, null, null);
    nodeFunction.appendChild(nodeFunctionDeclarationInFunction);
    Node nodeParentFunction = Node.newFunction(null, null, null);
    nodeParentFunction.appendChild(nodeFunction);
    Node nodeScriptInFunction = Node.newScript();
    nodeScriptInFunction.appendChild(nodeParentFunction);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
    checkState(nodeScriptInFunction != null);
  }
}