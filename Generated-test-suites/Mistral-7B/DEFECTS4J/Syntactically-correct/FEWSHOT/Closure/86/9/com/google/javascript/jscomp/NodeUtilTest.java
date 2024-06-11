package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[]{}, new int[]{3}, new Node[]{});
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newNode(Token.IF, new int[]{}, new int[]{1}, new Node[]{});
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.PRINT, new int[]{}, new int[]{1}, new Node[]{Node.newTextNode("hello")});
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = Node.newNode(Token.ASSIGN, new int[]{}, new int[]{1}, new Node[]{Node.newNumberLiteral(1), Node.newNumberLiteral(2)});
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{}, new int[]{1}, new Node[]{});
    Node nodeChildFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{}, new int[]{1}, new Node[]{Node.newIdentifier("childFunction")});
    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, new int[]{}, new int[]{1}, new Node[]{Node.newIdentifier("functionName")});

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeChildFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{}, new int[]{1}, new Node[]{Node.newIdentifier("hoistedFunction")});
    Node nodeFunctionDeclarationInBlock = Node.newNode(Token.BLOCK, new int[]{}, new int[]{1}, new Node[]{nodeFunctionDeclaration});
    Node nodeFunctionDeclarationInFunction = Node.newNode(Token.FUNCTION, new int[]{}, new int[]{1}, new Node[]{Node.newIdentifier("functionName")});

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInBlock));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }
}