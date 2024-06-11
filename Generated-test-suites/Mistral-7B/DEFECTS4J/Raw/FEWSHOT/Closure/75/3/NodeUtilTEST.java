package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = Node.newFor();
    Node notForInNode = Node.newExpression("1 + 1");

    assertTrue(NodeUtil.isForIn(forNode));
    assertFalse(NodeUtil.isForIn(notForInNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = Node.newExpression("1 + 1");
    Node notStatementNode = Node.newIdentifier("x");

    Node parentStatementNode = Node.newBlock();
    parentStatementNode.addChild(statementNode);
    parentStatementNode.addChild(Node.newExpression("1 + 1"));

    Node scriptNode = Node.newScript();
    scriptNode.addChild(parentStatementNode);
    scriptNode.addChild(Node.newExpression("1 + 1"));

    Node functionNode = Node.newFunction("foo", null, null);
    Node functionParentNode = Node.newBlock();
    functionParentNode.addChild(functionNode);
    functionParentNode.addChild(Node.newExpression("1 + 1"));

    Node labelNode = Node.newLabel("label");

    // It is a statement
    assertTrue(NodeUtil.isStatement(statementNode));
    assertTrue(NodeUtil.isStatement(parentStatementNode));
    assertTrue(NodeUtil.isStatement(scriptNode));
    assertFalse(NodeUtil.isStatement(notStatementNode));
    assertFalse(NodeUtil.isStatement(functionNode));
    assertFalse(NodeUtil.isStatement(labelNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = Node.newFunction("foo", null, null);
    Node functionExpressionNode = Node.newExpression("function(){}");

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = Node.newFunction("foo", null, null);
    Node notHoistedFunctionDeclarationNode = Node.newFunction("foo", null, null);

    Node scriptNode = Node.newScript();
    scriptNode.addChild(hoistedFunctionDeclarationNode);

    Node functionNode = Node.newFunction("foo", null, null);
    Node functionParentNode = Node.newBlock();
    functionParentNode.addChild(functionNode);
    scriptNode.addChild(functionParentNode);

    Node functionDeclarationInFunctionNode = Node.newFunction("foo", null, null);
    Node functionDeclarationInFunctionParentNode = Node.newBlock();
    functionDeclarationInFunctionParentNode.addChild(functionDeclarationInFunctionNode);
    functionParentNode.addChild(functionDeclarationInFunctionParentNode);

    Node hoistedFunctionDeclarationInFunctionNode = Node.newFunction("foo", null, null);
    Node hoistedFunctionDeclarationInFunctionParentNode = Node.newBlock();
    hoistedFunctionDeclarationInFunctionParentNode.addChild(hoistedFunctionDeclarationInFunctionNode);
    functionDeclarationInFunctionParentNode.addChild(hoistedFunctionDeclarationInFunctionParentNode);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionDeclarationNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationInFunctionNode));
  }

  private void assertFalse(boolean condition) {
    checkState(condition == false, "Expected false but got true.");
  }

  private void assertNull(Object obj) {
    checkState(obj == null, "Expected null but got %s.", obj);
  }
}