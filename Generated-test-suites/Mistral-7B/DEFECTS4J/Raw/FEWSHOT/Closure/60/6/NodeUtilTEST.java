package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[] {1, 1, 1});
    Node nodeNotForIn = Node.newNode(Token.IF, new int[] {1, 1});

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = Node.newNode(Token.SCRIPT, new int[] {1, 1, 1});
    Node blockNode = Node.newNode(Token.BLOCK, new int[] {1, 1, 1});
    Node labelNode = Node.newNode(Token.LABEL, new int[] {1, 1, 1});
    Node functionNode = Node.newNode(Token.FUNCTION, new int[] {1, 1, 1});
    Node ifNode = Node.newNode(Token.IF, new int[] {1, 1});

    Node parentScript = Node.newNode(Token.SCRIPT, new int[] {1, 1, 1});
    Node parentBlock = Node.newNode(Token.BLOCK, new int[] {1, 1, 1});
    Node parentLabel = Node.newNode(Token.LABEL, new int[] {1, 1, 1});
    Node parentFunction = Node.newNode(Token.FUNCTION, new int[] {1, 1, 1});

    checkState(scriptNode.getParent() == null);
    checkState(blockNode.getParent() == parentBlock);
    checkState(labelNode.getParent() == parentLabel);
    checkState(functionNode.getParent() == parentFunction);
    checkState(ifNode.getParent() == parentFunction);

    assertTrue(NodeUtil.isStatement(scriptNode));
    assertTrue(NodeUtil.isStatement(blockNode));
    assertTrue(NodeUtil.isStatement(labelNode));
    assertTrue(NodeUtil.isStatement(functionNode));
    assertFalse(NodeUtil.isStatement(ifNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = Node.newNode(Token.FUNCTION, new int[] {1, 1, 1});
    Node functionExpression = Node.newNode(Token.FUNCTION, new int[] {1, 1, 1, 1});

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[] {1, 1, 1});
    Node notHoistedFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[] {1, 1, 1});

    Node scriptParent = Node.newNode(Token.SCRIPT, new int[] {1, 1, 1});
    Node functionParent = Node.newNode(Token.FUNCTION, new int[] {1, 1, 1});

    checkState(hoistedFunctionDeclaration.getParent() == scriptParent);
    checkState(notHoistedFunctionDeclaration.getParent() == functionParent);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionDeclaration));
  }
}