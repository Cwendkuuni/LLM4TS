package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.*;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = Node.newForStatement();
    assertTrue(NodeUtil.isForIn(forNode));

    Node ifNode = Node.newIfStatement();
    assertFalse(NodeUtil.isForIn(ifNode));

    Node variableNode = Node.newVariable("x");
    assertFalse(NodeUtil.isForIn(variableNode));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = Node.newScript(1, "function foo() {}");
    assertTrue(NodeUtil.isStatement(scriptNode.getFirstChild()));

    Node blockNode = Node.newBlock(2, null);
    assertTrue(NodeUtil.isStatement(blockNode.getFirstChild()));

    Node labelNode = Node.newLabel("foo");
    assertTrue(NodeUtil.isStatement(labelNode));

    Node ifNode = Node.newIfStatement();
    assertFalse(NodeUtil.isStatement(ifNode));

    Node expressionNode = Node.newExpressionStatement(Node.newBinaryOperator(Token.MINUS, Node.newNumber(5), Node.newNumber(2)));
    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = Node.newFunction(Node.newIdentifier("foo"), Node.newList(), Node.newBlock(2, null));
    assertTrue(NodeUtil.isFunctionDeclaration(functionNode));

    Node expressionFunctionNode = Node.newExpressionStatement(Node.newFunction(Node.newIdentifier("bar"), Node.newList(), Node.newBlock(2, null)));
    assertFalse(NodeUtil.isFunctionDeclaration(expressionFunctionNode.getFirstChild()));

    Node variableFunctionNode = Node.newVariable("baz", Node.newFunction(Node.newIdentifier("qux"), Node.newList(), Node.newBlock(2, null)));
    assertFalse(NodeUtil.isFunctionDeclaration(variableFunctionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node functionNode = Node.newFunction(Node.newIdentifier("foo"), Node.newList(), Node.newBlock(2, null));
    Node scriptNode = Node.newScript(1, Node.newList(functionNode).toJsonString());
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));

    Node functionNodeInFunction = Node.newFunction(Node.newIdentifier("bar"), Node.newList(), Node.newBlock(2, null));
    Node functionNodeInScript = Node.newFunction(Node.newIdentifier("baz"), Node.newList(), Node.newBlock(2, null));
    Node scriptNodeWithFunctions = Node.newScript(1, Node.newList(functionNodeInFunction, functionNodeInScript, functionNode).toJsonString());
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNodeInFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNodeInScript));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }

  @Test
  public void testPreconditions() {
    Node nullNode = null;
    try {
      checkState(nullNode != null);
      fail("Expected Preconditions check to throw NullPointerException");
    } catch (NullPointerException e) {
      // expected
    }
  }
}