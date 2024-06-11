package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[]{}, new int[]{}, new Node[]{}, new Node[]{}, new Node[]{}, new Node[]{}, null);
    Node nodeNotForIn = Node.newNode(Token.IF, new int[]{}, new int[]{}, new Node[]{}, new Node[]{}, new Node[]{}, new Node[]{}, null);

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newNode(Token.SCRIPT, new int[]{}, new int[]{}, new Node[]{}, new Node[]{}, new Node[]{}, new Node[]{}, null);
    Node nodeStatement = Node.newNode(Token.ASSIGN, new int[]{}, new int[]{}, new Node[]{}, new Node[]{}, new Node[]{}, new Node[]{}, null);
    Node nodeExpression = Node.newNode(Token.ADD, new int[]{}, new int[]{}, new Node[]{}, new Node[]{}, new Node[]{}, new Node[]{}, null);

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeStatement));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{}, new int[]{}, new Node[]{}, new Node[]{}, new Node[]{}, new Node[]{}, null);
    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, new int[]{}, new int[]{}, new Node[]{}, new Node[]{}, new Node[]{}, new Node[]{}, new Node[]{}, null);

    nodeFunctionExpression.setProperty("name", Node.STRING_VAL, 0);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newNode(Token.SCRIPT, new int[]{}, new int[]{}, new Node[]{}, new Node[]{}, new Node[]{}, new Node[]{}, null);
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{}, new int[]{}, new Node[]{}, new Node[]{}, new Node[]{}, new Node[]{}, null);
    Node nodeFunctionDeclarationInFunction = Node.newNode(Token.FUNCTION, new int[]{}, new int[]{}, new Node[]{}, new Node[]{}, new Node[]{}, new Node[]{}, nodeFunctionDeclaration, null);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
  }

  @Test
  public void testIsFunctionDeclarationParentIsScript() {
    Node nodeScript = Node.newNode(Token.SCRIPT, new int[]{}, new int[]{}, new Node[]{}, new Node[]{}, new Node[]{}, new Node[]{}, null);
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{}, new int[]{}, new Node[]{}, new Node[]{}, new Node[]{}, new Node[]{}, null);

    nodeScript.setFirstChild(nodeFunctionDeclaration);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
  }

  @Test
  public void testIsFunctionDeclarationParentIsFunction() {
    Node nodeFunction = Node.newNode(Token.FUNCTION, new int[]{}, new int[]{}, new Node[]{}, new Node[]{}, new Node[]{}, new Node[]{}, null);
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{}, new int[]{}, new Node[]{}, new Node[]{}, new Node[]{}, new Node[]{}, null);

    nodeFunction.setFirstChild(nodeFunctionDeclaration);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
  }

  @Test
  public void testIsFunctionDeclarationNullParent() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{}, new int[]{}, new Node[]{}, new Node[]{}, new Node[]{}, new Node[]{}, null);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
  }
}