package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    Node nodeNotForIn = Node.newNode(Token.IF, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newNode(Token.SCRIPT, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    Node nodeBlock = Node.newBlock(Token.BLOCK, null, null);
    Node nodeLabel = Node.newNode(Token.LABEL, "label", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    Node nodeFunction = Node.newNode(Token.FUNCTION, "functionName", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, Node.newNode(Token.NAME, "functionName", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null));
    Node nodeExpression = Node.newNode(Token.IF, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeBlock));
    assertTrue(NodeUtil.isStatement(nodeLabel));
    assertTrue(NodeUtil.isStatement(nodeFunction));
    assertFalse(NodeUtil.isStatement(nodeFunctionExpression));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, "functionName", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, Node.newNode(Token.NAME, "functionName", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null));

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newNode(Token.SCRIPT, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    Node nodeFunctionDeclarationInScript = Node.newNode(Token.FUNCTION, "functionName", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    Node nodeFunctionDeclarationInFunction = Node.newNode(Token.FUNCTION, "outerFunctionName", null, null, null, null, null, null, null, null, null, null, null, null, null, Node.newNode(Node.newNode(Token.FUNCTION, "functionName", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null), null, null), null, null);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
    checkArgument(NodeUtil.isFunctionDeclaration(nodeFunctionDeclarationInFunction), "Function declaration not found");
  }

  @Test
  public void testParentNodeTypeWhenNull() {
    Node node = Node.newNode(Token.NAME, "test", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

    checkState(NodeUtil.isStatement(node), "Node is not a statement");
  }
}