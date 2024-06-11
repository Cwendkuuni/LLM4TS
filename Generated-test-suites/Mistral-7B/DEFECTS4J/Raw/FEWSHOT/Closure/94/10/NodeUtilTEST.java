package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = Node.newNode(Token.FOR, null, null, null, null);
    Node threeChildrenForNode = Node.newNode(Token.FOR, null, Node.newNode(Token.IDENT, "i"), Node.newNode(Token.IDENT, "array"), null);

    assertTrue("Basic for loop should be considered as for in", NodeUtil.isForIn(forNode));
    assertTrue("For loop with three children should be considered as for in", NodeUtil.isForIn(threeChildrenForNode));

    Node notForNode = Node.newNode(Token.IF, null, null, null, null);
    assertTrue("Node that is not a for loop should be considered as not for in", !NodeUtil.isForIn(notForNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = Node.newNode(Token.IF, null, null, null, null);
    Node expressionNode = Node.newNode(Token.ASSIGN, null, Node.newNode(Token.IDENT, "x"), Node.newNode(Token.NUM, 10), null);

    Node scriptNode = Node.newNode(Token.SCRIPT, null, null, null, null);
    Node blockNode = Node.newNode(Token.BLOCK, null, null, null, null);
    Node labelNode = Node.newNode(Token.LABEL, "label", null, null, null);

    assertTrue("Statement node should be considered as a statement", NodeUtil.isStatement(statementNode));
    assertTrue("Script node should be considered as a statement", NodeUtil.isStatement(scriptNode));
    assertTrue("Block node should be considered as a statement", NodeUtil.isStatement(blockNode));
    assertTrue("Label node should not be considered as a statement", !NodeUtil.isStatement(labelNode));
    assertTrue("Expression node should not be considered as a statement", !NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = Node.newNode(Token.FUNCTION, "name", null, Node.newNode(Token.BLOCK, null, null, null, null), null);
    Node functionExpressionNode = Node.newNode(Token.ASSIGN, null, Node.newNode(Token.IDENT, "x"), Node.newNode(Token.FUNCTION, null, null, Node.newNode(Token.BLOCK, null, null, null, null), null), null);

    assertTrue("Function declaration node should be considered as a function declaration", NodeUtil.isFunctionDeclaration(functionDeclarationNode));
    assertTrue("Function expression node should not be considered as a function declaration", !NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = Node.newNode(Token.FUNCTION, "name", null, Node.newNode(Token.BLOCK, null, null, null, null), null);
    Node scriptNode = Node.newNode(Token.SCRIPT, null, null, null, null);
    Node functionNode = Node.newNode(Token.FUNCTION, null, null, null, null);
    Node functionInsideFunctionNode = Node.newNode(Token.FUNCTION, null, null, Node.newNode(Token.BLOCK, null, null, null, null), null);
    Node functionInsideFunctionInsideFunctionNode = Node.newNode(Token.FUNCTION, null, null, Node.newNode(Token.BLOCK, null, null, null, null), functionInsideFunctionNode);

    Node hoistedFunctionDeclarationInsideScriptNode = Node.newNode(Token.SCRIPT, null, null, Node.newNode(hoistedFunctionDeclarationNode), null);
    Node hoistedFunctionDeclarationInsideFunctionNode = Node.newNode(Token.FUNCTION, null, null, Node.newNode(hoistedFunctionDeclarationNode), null);
    Node hoistedFunctionDeclarationInsideFunctionInsideFunctionNode = Node.newNode(Token.FUNCTION, null, null, Node.newNode(hoistedFunctionDeclarationInsideFunctionNode), null);

    assertTrue("Hoisted function declaration in script should be considered as hoisted function declaration", NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));
    assertTrue("Hoisted function declaration inside function should be considered as hoisted function declaration", NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationInsideFunctionNode));
    assertTrue("Hoisted function declaration inside function inside function should be considered as hoisted function declaration", NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationInsideFunctionInsideFunctionNode));
    assertTrue("Function declaration not inside function should not be considered as hoisted function declaration", !NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode));
    assertTrue("Function declaration inside function should not be considered as hoisted function declaration", !NodeUtil.isHoistedFunctionDeclaration(functionNode));
    assertTrue("Function declaration inside function inside function should not be considered as hoisted function declaration", !NodeUtil.isHoistedFunctionDeclaration(functionInsideFunctionNode));
    assertTrue("Script node should not be considered as hoisted function declaration", !NodeUtil.isHoistedFunctionDeclaration(scriptNode));
  }
}