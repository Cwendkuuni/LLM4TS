package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn();
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node node = Node.newExpressionStatement(Node.newAssign(Node.newIdent("x"), Node.newNumber(42)));
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newScript(Node.newText("function foo() {}"));
    assertTrue(NodeUtil.isStatement(nodeScript));

    Node nodeFunctionExpression = Node.newExpressionStatement(Node.newExpression(Node.newFunction("", Node.newBlock(Node.newText("")))));
    assertFalse(NodeUtil.isStatement(nodeFunctionExpression));

    Node nodeBlock = Node.newBlock(Node.newText(""));
    assertTrue(NodeUtil.isStatement(nodeBlock));

    Node nodeLabel = Node.newLabel("foo");
    assertTrue(NodeUtil.isStatement(nodeLabel));

    Node nodeVariableDeclaration = Node.newVariable("x", Node.newNumber(42), Node.newNoInit());
    assertFalse(NodeUtil.isStatement(nodeVariableDeclaration));

    Node nodeExpressionStatement = Node.newExpressionStatement(Node.newAssign(Node.newIdent("x"), Node.newNumber(42)));
    assertTrue(NodeUtil.isStatement(nodeExpressionStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction("foo", Node.newBlock(Node.newText("")));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionStatement(Node.newExpression(Node.newFunction("", Node.newBlock(Node.newText("")))));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclarationInScript = Node.newFunction("foo", Node.newBlock(Node.newText("")));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));

    Node nodeFunctionDeclarationInFunction = Node.newFunctionDeclaration("foo", Node.newBlock(Node.newText("")));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }

  @Test
  public void testNullParent() {
    Node node = Node.newExpressionStatement(Node.newAssign(Node.newIdent("x"), Node.newNumber(42)));
    checkState(node.getParent() != null);
  }
}