package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, null, 0, 0, null);
    Node nodeFor = Node.newNode(Token.FOR, null, 0, 0, null);
    Node nodeIf = Node.newNode(Token.IF, null, 0, 0, null);

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeFor));
    assertFalse(NodeUtil.isForIn(nodeIf));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newNode(Token.SCRIPT, null, 0, 0, null);
    Node nodeBlock = Node.newNode(Token.BLOCK, null, 0, 0, null);
    Node nodeLabel = Node.newNode(Token.LABEL, "label", 0, 0, null);
    Node nodeFunction = Node.newNode(Token.FUNCTION, null, 0, 0, null);
    Node nodeExpression = Node.newNode(Token.IDENT, "expr", 0, 0, null);

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeBlock));
    assertTrue(NodeUtil.isStatement(nodeLabel));
    assertFalse(NodeUtil.isStatement(nodeFunction));
    assertFalse(NodeUtil.isStatement(nodeExpression));

    Node parentScript = Node.newNode(Token.SCRIPT, null, 0, 0, null);
    Node parentBlock = Node.newNode(Token.BLOCK, null, 0, 0, null);
    Node parentFunction = Node.newNode(Token.FUNCTION, null, 0, 0, null);
    Node parentLabel = Node.newNode(Token.LABEL, "label", 0, 0, null);

    checkState(parentScript != null);
    checkState(parentBlock != null);
    checkState(parentFunction != null);
    checkState(parentLabel != null);

    Node nodeStatementInScript = Node.newNode(Token.IF, null, 0, 0, null);
    Node nodeStatementInBlock = Node.newNode(Token.IF, null, 0, 0, null);
    Node nodeStatementInFunction = Node.newNode(Token.IF, null, 0, 0, null);
    Node nodeStatementInLabel = Node.newNode(Token.IDENT, "stmt", 0, 0, null);

    assertTrue(NodeUtil.isStatement(nodeStatementInScript));
    assertTrue(NodeUtil.isStatement(nodeStatementInBlock));
    assertFalse(NodeUtil.isStatement(nodeStatementInFunction));
    assertFalse(NodeUtil.isStatement(nodeStatementInLabel));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, "myFunc", 0, 0, null);
    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, null, 0, 0, null);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newNode(Token.SCRIPT, null, 0, 0, null);
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, "myFunc", 0, 0, null);
    Node nodeFunctionDeclarationInFunction = Node.newNode(Token.FUNCTION, "myFunc", 0, 0, null);
    Node nodeFunctionDeclarationInScript = Node.newNode(Token.FUNCTION, "myFunc", 0, 0, null);
    Node nodeFunctionInFunction = Node.newNode(Token.FUNCTION, null, 0, 0, null);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));

    Node nodeFunctionInScript = Node.newNode(Token.FUNCTION, null, 0, 0, null);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInScript));

    Node parentFunction = Node.newNode(Token.FUNCTION, null, 0, 0, null);
    Node parentFunctionWithScript = Node.newNode(Token.SCRIPT, null, 0, 0, parentFunction);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionInScriptInParentFunction = Node.newNode(Token.FUNCTION, null, 0, 0, null);
    Node nodeFunctionInScriptInParentFunctionWithScript = Node.newNode(Token.SCRIPT, null, 0, 0, Node.newNode(Token.FUNCTION, null, 0, 0, nodeFunctionInScriptInParentFunction));

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInScriptInParentFunction));
  }
}