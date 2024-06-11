package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
  @Test
  public void testIsForIn() {
    Node forNode = Node.newNode(Token.FOR);
    Node forNodeWithThreeChildren = Node.newNode(Token.FOR, Node.newNode(Token.IDENTIFIER, "i"), Node.newNode(Token.ASSIGN, Node.newNode(Token.IDENTIFIER, "i"), Node.newNode(Token.NUMBER, 0)), Node.newNode(Token.SEMICOLON), Node.newNode(Token.BLOCK, Node.newNode(Token.STATEMENT, Node.newNode(Token.ASSIGN, Node.newNode(Token.IDENTIFIER, "j"), Node.newNode(Token.NUMBER, 1)))));
    assertTrue(NodeUtil.isForIn(forNodeWithThreeChildren));
    assertFalse(NodeUtil.isForIn(forNode));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = Node.newNode(Token.SCRIPT, null);
    Node blockNode = Node.newNode(Token.BLOCK, null);
    Node labelNode = Node.newNode(Token.LABEL, "label");
    Node functionNode = Node.newNode(Token.FUNCTION, Node.newNode(Token.IDENTIFIER, "func"), Node.newNode(Token.BLOCK, null));
    Node expressionNode = Node.newNode(Token.ASSIGN, Node.newNode(Token.IDENTIFIER, "x"), Node.newNode(Token.NUMBER, 1));

    assertTrue(NodeUtil.isStatement(scriptNode));
    assertTrue(NodeUtil.isStatement(blockNode));
    assertTrue(NodeUtil.isStatement(labelNode));
    assertTrue(NodeUtil.isStatement(functionNode));
    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = Node.newNode(Token.FUNCTION, Node.newNode(Token.IDENTIFIER, "func"), Node.newNode(Token.BLOCK, null));
    Node functionExpressionNode = Node.newNode(Token.ASSIGN, Node.newNode(Token.IDENTIFIER, "func"), Node.newNode(Token.FUNCTION, null, Node.newNode(Token.BLOCK, null)));

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = Node.newNode(Token.SCRIPT, null);
    Node hoistedFunctionDeclarationNode = Node.newNode(Token.FUNCTION, Node.newNode(Token.IDENTIFIER, "func"), Node.newNode(Token.BLOCK, null));
    Node functionDeclarationNode = Node.newNode(Token.FUNCTION, Node.newNode(Token.IDENTIFIER, "func"), Node.newNode(Token.BLOCK, null), Node.newNode(Token.RETURN, null));
    Node functionNodeInsideFunctionNode = Node.newNode(Token.FUNCTION, null, Node.newNode(Token.BLOCK, Node.newNode(Token.FUNCTION, Node.newNode(Token.IDENTIFIER, "innerFunc"), Node.newNode(Token.BLOCK, null))));

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNodeInsideFunctionNode));
  }

  @Test
  public void testIsStatementParentIsScriptOrBlock() {
    Node scriptNode = Node.newNode(Token.SCRIPT, null);
    Node blockNode = Node.newNode(Token.BLOCK, null);
    Node statementNode = Node.newNode(Token.ASSIGN, Node.newNode(Token.IDENTIFIER, "x"), Node.newNode(Token.NUMBER, 1));

    checkState(scriptNode != null, "scriptNode should not be null");
    checkState(blockNode != null, "blockNode should not be null");
    checkState(statementNode != null, "statementNode should not be null");

    Node parentOfStatementNode = Node.newNode(Token.SCRIPT, null);
    Node statementNodeAsChildOfScriptNode = Node.newNode(Token.ASSIGN, Node.newNode(Token.IDENTIFIER, "x"), Node.newNode(Token.NUMBER, 1), parentOfStatementNode);

    Node parentOfStatementNodeInsideBlockNode = Node.newNode(Token.BLOCK, null, statementNode);

    assertTrue(NodeUtil.isStatement(statementNode));
    assertTrue(NodeUtil.isStatement(statementNodeAsChildOfScriptNode));
    assertTrue(NodeUtil.isStatement(parentOfStatementNodeInsideBlockNode));
  }

  @Test
  public void testIsStatementParentIsNotScriptOrBlock() {
    Node statementNode = Node.newNode(Token.ASSIGN, Node.newNode(Token.IDENTIFIER, "x"), Node.newNode(Token.NUMBER, 1));
    Node nonScriptOrBlockNode = Node.newNode(Token.IF, Node.newNode(Token.COND, Node.newNode(Token.IDENTIFIER, "x"), Node.newNode(Token.NUMBER, 1), Node.newNode(Token.NUMBER, 2)), Node.newNode(Token.BLOCK, Node.newNode(Token.RETURN, Node.newNode(Token.NUMBER, 1))));

    assertFalse(NodeUtil.isStatement(statementNode.getParent()));
    assertFalse(NodeUtil.isStatement(nonScriptOrBlockNode));
  }
}