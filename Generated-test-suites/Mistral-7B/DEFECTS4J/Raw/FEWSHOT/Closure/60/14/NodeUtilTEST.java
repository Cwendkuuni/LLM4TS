package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[] {1, 2, 3}, new Node[3]);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newNode(Token.IF, new int[] {1}, new Node[0]);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newNode(Token.SCRIPT, new int[] {}, new Node[0]);
    Node nodeBlock = Node.newBlock("{}");
    Node nodeLabel = Node.newLabel("label");

    Node nodeFunction = Node.newNode(Token.FUNCTION, new int[] {1}, new Node[1]);
    Node nodeFunctionExpression = Node.newFunctionExpression("func", null, new Node[0]);

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeBlock));
    assertTrue(NodeUtil.isStatement(nodeLabel));

    assertTrue(NodeUtil.isStatement(nodeFunction));
    assertFalse(NodeUtil.isStatement(nodeFunctionExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = Node.newNode(Token.FUNCTION, new int[] {1, 1}, new Node[1]);
    nodeFunction.setName("func");
    Node nodeFunctionExpression = Node.newFunctionExpression("func", null, new Node[0]);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = Node.newNode(Token.SCRIPT, new int[] {}, new Node[0]);
    Node functionDeclaration = Node.newNode(Token.FUNCTION, new int[] {1, 1}, new Node[1]);
    functionDeclaration.setName("func");
    Node functionDeclarationInFunction = Node.newNode(Token.FUNCTION, new int[] {1}, new Node[1]);
    functionDeclarationInFunction.setName("funcInFunction");
    functionDeclarationInFunction.setParent(functionDeclaration);

    Node hoistedFunctionDeclaration = NodeUtil.isHoistedFunctionDeclaration(functionDeclaration);
    Node notHoistedFunctionDeclaration = NodeUtil.isHoistedFunctionDeclaration(functionDeclarationInFunction);

    assertTrue(hoistedFunctionDeclaration != null);
    assertFalse(notHoistedFunctionDeclaration != null);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_functionInScript() {
    Node scriptNode = Node.newNode(Token.SCRIPT, new int[] {}, new Node[0]);
    Node functionDeclaration = Node.newNode(Token.FUNCTION, new int[] {1, 1}, new Node[1]);
    functionDeclaration.setName("func");

    Node hoistedFunctionDeclaration = NodeUtil.isHoistedFunctionDeclaration(functionDeclaration);

    assertTrue(hoistedFunctionDeclaration != null);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_functionInFunction() {
    Node functionNode = Node.newNode(Token.FUNCTION, new int[] {1}, new Node[0]);
    Node functionDeclaration = Node.newNode(Token.FUNCTION, new int[] {1, 1}, new Node[1]);
    functionDeclaration.setName("func");
    functionDeclaration.setParent(functionNode);

    Node hoistedFunctionDeclaration = NodeUtil.isHoistedFunctionDeclaration(functionDeclaration);

    assertNull(hoistedFunctionDeclaration);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_functionInBlock() {
    Node blockNode = Node.newBlock("{}");
    Node functionDeclaration = Node.newNode(Token.FUNCTION, new int[] {1, 1}, new Node[1]);
    functionDeclaration.setName("func");
    functionDeclaration.setParent(blockNode);

    Node hoistedFunctionDeclaration = NodeUtil.isHoistedFunctionDeclaration(functionDeclaration);

    assertNull(hoistedFunctionDeclaration);
  }
}