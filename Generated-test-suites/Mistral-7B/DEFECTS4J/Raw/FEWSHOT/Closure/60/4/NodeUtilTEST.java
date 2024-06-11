package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[] {1, 2, 3});
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, new int[] {1, 2, 3});
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF, new int[] {1, 2, 3});
    Node parentScript = new Node(Token.SCRIPT, new int[] {1});
    parentScript.addChild(nodeStatement);

    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = new Node(Token.IF, new int[] {1, 2, 3});
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[] {1, 2});
    Node parentScript = new Node(Token.SCRIPT, new int[] {1});
    parentScript.addChild(nodeFunctionDeclaration);
    nodeFunctionDeclaration.addChild(new Node(Token.IDENT, new int[] {1, 3}, "foo"));

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[] {1, 2});
    Node parentExpression = new Node(Token.ASSIGN, new int[] {1, 2, 3});
    parentExpression.addChild(nodeFunctionExpression);
    nodeFunctionExpression.addChild(new Node(Token.IDENT, new int[] {1, 4}, "foo"));

    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[] {1, 2});
    Node parentScript = new Node(Token.SCRIPT, new int[] {1});
    parentScript.addChild(nodeFunctionDeclaration);
    nodeFunctionDeclaration.addChild(new Node(Token.IDENT, new int[] {1, 3}, "foo"));

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, new int[] {1, 2, 1});
    Node functionNode = new Node(Token.FUNCTION, new int[] {1, 2});
    functionNode.addChild(nodeFunctionDeclarationInFunction);
    Node parentFunction = new Node(Token.FUNCTION, new int[] {1, 2, 1});
    parentFunction.addChild(functionNode);
    parentFunction.addChild(new Node(Token.IDENT, new int[] {1, 5}, "bar"));

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));

    Node nodeFunctionDeclarationNotInFunction = new Node(Token.FUNCTION, new int[] {1, 2, 1});
    Node parentFunctionNotFunction = new Node(Token.SCRIPT, new int[] {1});
    parentFunctionNotFunction.addChild(nodeFunctionDeclarationNotInFunction);
    parentFunctionNotFunction.addChild(new Node(Token.IDENT, new int[] {1, 5}, "bar"));

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationNotInFunction));
  }
}