package com.google.javascript.jscomp.jsonml.test;

import static org.junit.Assert.*;

import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node forNode = Node.newForStatement(1, null, null, null, null);
    assertTrue(NodeUtil.isForIn(forNode));

    Node ifNode = Node.newExpressionStatement(Node.newIfStatement(1, null, null, null));
    assertFalse(NodeUtil.isForIn(ifNode));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node statementNode = Node.newExpressionStatement(Node.newIdentifier("x"));
    assertTrue(NodeUtil.isStatement(statementNode));

    Node expressionNode = Node.newExpression("2 + 3");
    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node functionNode = Node.newFunction(Token.VAR, "myFunction", new Node[0], null);
    assertTrue(NodeUtil.isFunctionDeclaration(functionNode));

    Node functionExpressionNode = Node.newExpressionStatement(Node.newFunction(Token.VAR, null, new Node[0], Node.newName("myFunction")));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = Node.newScript(new Node[] { Node.newFunction(Token.VAR, "myFunction", new Node[0], null) });
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(scriptNode.getFirstChild()));

    Node functionNodeInFunction = Node.newExpressionStatement(Node.newFunction(Token.VAR, "myFunction", new Node[0], null), Node.newIdentifier("myFunction2"));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNodeInFunction));
  }
}