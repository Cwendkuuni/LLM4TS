package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = Node.newForStatement(1, Node.newIdentifier("i"), null, null, null);
    Node notForNode = Node.newExpressionStatement(Node.newName("x"));

    assertTrue(NodeUtil.isForIn(forNode));
    assertFalse(NodeUtil.isForIn(notForNode));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = Node.newBlock(1, Node.newStatementList());
    Node statementNode = Node.newExpressionStatement(Node.newName("x"));
    Node expressionNode = Node.newExpressionStatement(Node.newName("x") + Node.newName("+") + Node.newNumber(1));

    assertTrue(NodeUtil.isStatement(statementNode));
    assertTrue(NodeUtil.isStatement(expressionNode));
    assertTrue(NodeUtil.isStatement(scriptNode));

    Node notStatementNode = Node.newName("x");

    assertFalse(NodeUtil.isStatement(notStatementNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = Node.newFunction(1, Node.newIdentifier("foo"), null, null, null, Node.newBlock(1, Node.newStatementList()));
    Node functionExpressionNode = Node.newExpressionStatement(Node.newFunction(1, Node.newIdentifier("bar"), null, null, null, Node.newBlock(1, Node.newStatementList())));

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = Node.newBlock(1, Node.newStatementList(
        Node.newFunction(1, Node.newIdentifier("foo"), null, null, null, Node.newBlock(1, Node.newStatementList()))
    ));
    Node functionNode = Node.newFunction(1, Node.newIdentifier("bar"), null, null, null, Node.newBlock(1, Node.newStatementList()));

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(scriptNode.getChild(0)));

    Node functionInFunctionNode = Node.newFunction(1, Node.newIdentifier("qux"), null, null, null, Node.newFunction(1, Node.newIdentifier("quux"), null, null, null, Node.newBlock(1, Node.newStatementList())));

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionInFunctionNode));
  }
}