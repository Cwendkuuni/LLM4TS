package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeFor = new Node(Token.FOR, 0, 0, 3);
    Node nodeIf = new Node(Token.IF, 0, 0, 2);

    assertTrue(NodeUtil.isForIn(nodeFor));
    assertFalse(NodeUtil.isForIn(nodeIf));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT, 0, 0, 0);
    Node nodeFunction = new Node(Token.FUNCTION, 0, 0, 0);
    Node nodeIf = new Node(Token.IF, 0, 0, 2);

    checkArgument(NodeUtil.isStatement(nodeScript), "A script node should be a statement.");
    checkArgument(NodeUtil.isStatement(nodeFunction), "A function declaration node should be a statement.");
    assertFalse(NodeUtil.isStatement(nodeIf), "An IF statement node should not be a statement in this context.");
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, 0, 0, 0);
    Node nodeFunctionExpression = new Node(Token.FUNCTION, 0, 0, 1);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Token.SCRIPT, 0, 0, 0);
    Node functionDeclarationNode = new Node(Token.FUNCTION, 0, 0, 0);
    Node functionDeclarationInsideFunctionNode = new Node(Token.FUNCTION, 0, 0, 0);
    Node functionDeclarationInsideFunctionChildNode = new Node(Token.FUNCTION, 0, 0, 0);

    Node functionNode = new Node(Token.FUNCTION, 0, 0, 1);
    Node functionInsideFunctionNode = new Node(Token.FUNCTION, 0, 0, 1);

    Node root = new Node(Token.SCRIPT, 0, 0, 0);
    root.addChild(scriptNode);
    root.addChild(functionDeclarationNode);
    root.addChild(functionNode);

    checkState(functionDeclarationNode.getParent() == root, "Function declaration parent is not the root.");

    Node functionScope = new Node(Token.FUNCTION, 0, 0, 0);
    functionScope.addChild(functionDeclarationInsideFunctionNode);
    functionScope.addChild(functionInsideFunctionNode);

    functionDeclarationInsideFunctionNode.setParent(functionScope);
    functionNode.setParent(functionScope);

    root.setChild(1, functionScope);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationInsideFunctionNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionInsideFunctionNode));
  }
}