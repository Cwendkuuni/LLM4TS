package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = new Node(Token.FOR);
    Node forNodeWithThreeChildren = new Node(Token.FOR);
    forNodeWithThreeChildren.addChild(new Node(Token.IDENT, "i"));
    forNodeWithThreeChildren.addChild(new Node(Token.KEYWORD, "in"));
    forNodeWithThreeChildren.addChild(new Node(Token.IDENT, "array"));

    assertTrue(NodeUtil.isForIn(forNodeWithThreeChildren));
    assertFalse(NodeUtil.isForIn(forNode));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node blockNode = new Node(Token.BLOCK);
    Node labelNode = new Node(Token.LABEL, "myLabel");
    Node functionNode = new Node(Token.FUNCTION, "myFunction");
    Node expressionFunctionNode = new Node(Token.FUNCTION, "myFunction")
        .setProperty("callee", new Node(Token.IDENT, "myFunction"));

    assertTrue(NodeUtil.isStatement(scriptNode));
    assertTrue(NodeUtil.isStatement(blockNode));
    assertTrue(NodeUtil.isStatement(labelNode));
    assertTrue(NodeUtil.isStatement(functionNode));
    assertFalse(NodeUtil.isStatement(expressionFunctionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = new Node(Token.FUNCTION, "myFunction");
    Node functionExpression = new Node(Token.FUNCTION, "myFunction")
        .setProperty("callee", new Node(Token.IDENT, "myFunction"));

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node functionDeclaration = new Node(Token.FUNCTION, "myFunction");
    Node functionDeclarationInFunction = new Node(Token.FUNCTION, "outerFunction")
        .addChild(functionDeclaration);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclaration));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationInFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclaration.getChild(0)));
  }

  @Test(expected = AssertionError.class)
  public void testCheckParentIsNotNullWhenParentIsNull() {
    Node n = new Node(Token.FUNCTION, "myFunction");
    NodeUtil.isStatement(n);
  }
}