package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[] {1, 2, 3}, new Object[] {null, null, null});
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newNode(Token.IF, new int[] {1}, new Object[] { null });
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newNode(Token.SCRIPT, new int[] {1}, new Object[] { null });
    Node nodeBlock = Node.newBlock(new Object[] { Node.newNode(Token.RETURN, new int[] {}, new Object[] { Node.newString("foo") }) });
    Node nodeLabel = Node.newNode(Token.LABEL, new int[] {}, new Object[] { Node.newIdentifier("label") });
    Node nodeFunction = Node.newFunction(Node.newIdentifier("foo"), new int[] {}, new Object[] { null });
    Node nodeExpression = Node.newNode(Token.IF, new int[] {1}, new Object[] { Node.newBoolean(true) });

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeBlock));
    assertTrue(NodeUtil.isStatement(nodeLabel));
    assertFalse(NodeUtil.isStatement(nodeFunction));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(Node.newIdentifier("foo"), new int[] {}, new Object[] { null });
    Node nodeFunctionExpression = Node.newNode(Token.ASSIGN, new int[] {1, 2}, new Object[] { Node.newIdentifier("foo"), Node.newFunction(Node.newIdentifier("bar"), new int[] {}, new Object[] { null }) });

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclarationInRoot = Node.newFunction(Node.newIdentifier("foo"), new int[] {}, new Object[] { null });
    Node nodeFunctionDeclarationInFunction = Node.newFunction(Node.newIdentifier("foo"), new int[] {}, new Object[] { Node.newIdentifier("bar") });
    Node nodeFunctionExpressionInRoot = Node.newNode(Token.ASSIGN, new int[] {1, 2}, new Object[] { Node.newIdentifier("foo"), Node.newFunction(Node.newIdentifier("bar"), new int[] {}, new Object[] { null }) });

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInRoot));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionExpressionInRoot));
  }

  @Test
  public void testParentCheck() {
    Node nodeFunction = Node.newFunction(Node.newIdentifier("foo"), new int[] {}, new Object[] { null });
    Node nodeScript = Node.newNode(Token.SCRIPT, new int[] {}, new Object[] { null });

    checkState(nodeFunction.getParent() != null);
    checkState(nodeScript.getParent() != null);

    // Parent is null when the node is the root of the AST.
    Node rootNode = Node.newNode(Token.SCRIPT, new int[] {}, new Object[] { null });

    checkState(rootNode.getParent() == null);
  }
}