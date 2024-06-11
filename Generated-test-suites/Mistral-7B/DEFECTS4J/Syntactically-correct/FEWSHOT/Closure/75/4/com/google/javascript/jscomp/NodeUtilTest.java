package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeFor = Node.newForStatement();
    Node nodeWhile = Node.newExpressionStatement(Node.newWhileStatement());
    Node nodeIf = Node.newExpressionStatement(Node.newIfStatement());

    assertTrue(NodeUtil.isForIn(nodeFor));
    assertFalse(NodeUtil.isForIn(nodeWhile));
    assertFalse(NodeUtil.isForIn(nodeIf));
  }

  @Test
  public void testIsStatement() {
    Node script = Node.newScript();
    Node block = Node.newBlockStatement();
    Node label = Node.newLabelStatement();
    Node function = Node.newFunctionStatement();
    Node expression = Node.newExpressionStatement(Node.newExpression());

    assertTrue(NodeUtil.isStatement(script));
    assertTrue(NodeUtil.isStatement(block));
    assertTrue(NodeUtil.isStatement(label));
    assertTrue(NodeUtil.isStatement(function));
    assertFalse(NodeUtil.isStatement(expression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = Node.newFunctionStatement("name", Node.newBlockStatement(), null);
    Node functionExpression = Node.newExpressionStatement(Node.newFunctionExpression(null, Node.newBlockStatement(), null));

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node script = Node.newScript();
    Node functionDeclarationInScript = Node.newFunctionStatement("name", Node.newBlockStatement(), null);
    Node functionDeclarationInFunction = Node.newFunctionStatement("name", Node.newBlockStatement(), null);
    Node functionDeclarationInBlock = Node.newFunctionStatement("name", Node.newBlockStatement(), Node.newBlockStatement());

    Node functionDeclarationInScriptAtTop = Node.newScript().addChild(functionDeclarationInScript);
    Node functionDeclarationInFunctionAtTop = Node.newFunctionStatement("anonymous", Node.newBlockStatement().addChild(functionDeclarationInFunction), null);
    Node functionDeclarationInBlockAtPosition = Node.newBlockStatement().addChild(functionDeclarationInBlock);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationInScript));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationInFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationInBlock));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationInScriptAtTop));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationInFunctionAtTop));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationInBlockAtPosition));
  }

  @Test
  public void testIsHoistedFunctionDeclarationWithParentScript() {
    Node script = Node.newScript();
    Node functionDeclaration = Node.newFunctionStatement("name", Node.newBlockStatement(), null);

    Node functionDeclarationInScript = Node.newScript().addChild(functionDeclaration);
    Node functionDeclarationInFunction = Node.newFunctionStatement("anonymous", Node.newBlockStatement().addChild(functionDeclaration), null);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationInScript));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationInFunction));
  }

  @Test
  public void testIsHoistedFunctionDeclarationWithParentBlock() {
    Node script = Node.newScript();
    Node block = Node.newBlockStatement();
    Node functionDeclaration = Node.newFunctionStatement("name", Node.newBlockStatement(), null);

    Node functionDeclarationInBlock = Node.newBlockStatement().addChild(functionDeclaration);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationInBlock));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsHoistedFunctionDeclarationWithNullParent() {
    Node functionDeclaration = Node.newFunctionStatement("name", Node.newBlockStatement(), null);

    NodeUtil.isHoistedFunctionDeclaration(functionDeclaration);
  }
}