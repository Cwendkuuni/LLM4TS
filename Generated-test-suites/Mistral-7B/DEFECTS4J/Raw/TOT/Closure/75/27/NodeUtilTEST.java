package com.google.javascript.jscomp.jsonml.test;

import static org.junit.Assert.*;

import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn();
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeOther = Node.newIdentifier("otherNode");
    assertFalse(NodeUtil.isForIn(nodeOther));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpressionStatement(Node.newIdentifier("x"));
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeOther = Node.newExpression(Node.newNumberLiteral(1));
    assertFalse(NodeUtil.isStatement(nodeOther));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(
        Node.newIdentifier("myFunction"),
        new Node[]{},
        Node.newBlock(Node.newReturnStatement(Node.newIdentifier("x"))));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionStatement(
        Node.newAssignment(
            Node.newIdentifier("myFunction"),
            Node.newFunction(
                Node.newIdentifier("x"),
                new Node[]{},
                Node.newBlock(Node.newReturnStatement(Node.newIdentifier("x"))))));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node topLevelFunctionDeclaration = Node.newFunction(
        Node.newIdentifier("topLevelFunction"),
        new Node[]{},
        Node.newBlock(Node.newReturnStatement(Node.newIdentifier("x"))));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(topLevelFunctionDeclaration));

    Node functionDeclarationInFunction = Node.newFunction(
        Node.newIdentifier("functionInFunction"),
        new Node[]{},
        Node.newBlock(
            Node.newStatementList(
                Node.newExpressionStatement(Node.newAssignment(
                    Node.newIdentifier("x"),
                    Node.newNumberLiteral(1))),
                Node.newReturnStatement(Node.newIdentifier("x"))),
            Node.newLabel("label"))).getFirstChild();
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationInFunction));
  }
}