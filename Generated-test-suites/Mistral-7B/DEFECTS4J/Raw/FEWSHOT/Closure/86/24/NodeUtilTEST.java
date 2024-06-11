package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{}, new int[]{}, new Object[3]);
    Node nodeNotForIn = new Node(Token.IF, new int[]{}, new int[]{}, new Object[3]);

    assertTrue("Is FOR-IN loop", NodeUtil.isForIn(nodeForIn));
    assertFalse("Is NOT FOR-IN loop", NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT, new int[]{}, new int[]{}, new Object[3]);
    Node nodeBlock = new Node(Token.BLOCK, new int[]{}, new int[]{}, new Object[3]);
    Node nodeLabel = new Node(Token.LABEL, new int[]{}, new int[]{}, new Object[3]);
    Node nodeFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{}, new Object[3]);
    Node nodeExpression = new Node(Token.ASSIGN, new int[]{}, new int[]{}, new Object[3]);

    assertTrue("Is statement: SCRIPT", NodeUtil.isStatement(nodeScript));
    assertTrue("Is statement: BLOCK", NodeUtil.isStatement(nodeBlock));
    assertTrue("Is statement: LABEL", NodeUtil.isStatement(nodeLabel));
    assertFalse("Is NOT statement: FUNCTION as expression", NodeUtil.isStatement(nodeFunction));
    assertFalse("Is NOT statement: FUNCTION as declaration", NodeUtil.isStatement(nodeFunction));
    assertFalse("Is NOT statement: EXPRESSION", NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{}, new int[]{}, new Object[3]);
    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[]{}, new int[]{}, new Object[3]);

    assertTrue("Is function declaration", NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse("Is NOT function declaration", NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = new Node(Token.SCRIPT, new int[]{}, new int[]{}, new Object[3]);
    Node nodeFunctionDeclarationInScript = new Node(Token.FUNCTION, new int[]{}, new int[]{}, new Object[3]);
    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{}, new Object[3]);

    assertFalse("Is NOT hoisted function declaration: outside any function", NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
    assertTrue("Is hoisted function declaration: in script", NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
    assertFalse("Is NOT hoisted function declaration: inside a function", NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }

  @Test(expected = IllegalStateException.class)
  public void testIsHoistedFunctionDeclarationWithNullParent() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{}, new int[]{}, new Object[3]);
    Node parentNull = null;
    checkState(parentNull != null, "Parent should not be null");
    NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration);
  }
}