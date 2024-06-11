package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{0, 0, 0}, new int[]{0, 0, 0});
    Node nodeNotForIn = new Node(Token.IF, new int[]{0, 0}, new int[]{0, 0});

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF, new int[]{0, 0}, new int[]{0, 0});
    Node nodeExpression = new Node(Token.ADD, new int[]{0, 0}, new int[]{0, 0, 0});

    assertTrue(NodeUtil.isStatement(nodeStatement));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(
        Token.FUNCTION, new int[]{1, 0}, new int[]{1, 0, 0, 1});
    Node nodeFunctionExpression = new Node(
        Token.FUNCTION, new int[]{1, 0, 0}, new int[]{1, 0, 0, 1});
    Node nodeNotFunction = new Node(Token.IF, new int[]{0, 0}, new int[]{0, 0});

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeNotFunction));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = new Node(Token.SCRIPT, new int[]{0, 0}, new int[]{0, 0});
    Node nodeFunctionDeclarationInScript = new Node(
        Token.FUNCTION, new int[]{1, 0}, new int[]{1, 0, 0, 1});
    Node nodeFunctionDeclarationInFunction = new Node(
        Token.FUNCTION, new int[]{1, 0}, new int[]{1, 0, 0, 1});
    Node nodeFunctionDeclarationInBlock = new Node(
        Token.BLOCK, new int[]{0, 0}, new int[]{0, 0});
    Node nodeFunctionDeclarationInBlockWithParentFunction = new Node(
        Token.FUNCTION, new int[]{1, 0}, new int[]{1, 0, 0, 1});

    NodeUtil.NodeWithType nodeWithTypeScript = new NodeUtil.NodeWithType(
        nodeScript, Token.SCRIPT);
    NodeUtil.NodeWithType nodeWithTypeFunctionDeclarationInScript = new NodeUtil.NodeWithType(
        nodeFunctionDeclarationInScript, Token.FUNCTION);
    NodeUtil.NodeWithType nodeWithTypeFunctionDeclarationInFunction = new NodeUtil.NodeWithType(
        nodeFunctionDeclarationInFunction, Token.FUNCTION);
    NodeUtil.NodeWithType nodeWithTypeFunctionDeclarationInBlock = new NodeUtil.NodeWithType(
        nodeFunctionDeclarationInBlock, Token.BLOCK);
    NodeUtil.NodeWithType nodeWithTypeFunctionDeclarationInBlockWithParentFunction = new NodeUtil.NodeWithType(
        nodeFunctionDeclarationInBlockWithParentFunction, Token.FUNCTION);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeWithTypeFunctionDeclarationInScript.node));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeWithTypeFunctionDeclarationInFunction.node));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeWithTypeFunctionDeclarationInBlock.node));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeWithTypeFunctionDeclarationInBlockWithParentFunction.node));
  }
}