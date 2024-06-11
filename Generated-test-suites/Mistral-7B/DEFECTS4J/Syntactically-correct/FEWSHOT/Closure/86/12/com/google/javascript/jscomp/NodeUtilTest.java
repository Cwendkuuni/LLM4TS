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
    Node forNode = Node.newForNode();
    Node notForNode = Node.newExpressionStatement(Node.newNameNode("x"));

    assertTrue(NodeUtil.isForIn(forNode));
    assertFalse(NodeUtil.isForIn(notForNode));
  }

  @Test
  public void testIsStatement() {
    Node script = Node.newScriptNode();
    Node statement1 = Node.newExpressionStatement(Node.newNameNode("x"));
    Node statement2 = Node.newExpressionStatement(Node.newNameNode("y"));
    Node expression = Node.newExpressionStatement(Node.newNameNode("x + y"));

    assertTrue(NodeUtil.isStatement(statement1));
    assertTrue(NodeUtil.isStatement(statement2));
    assertTrue(NodeUtil.isStatement(script));

    assertFalse(NodeUtil.isStatement(expression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = Node.newFunctionNode("myFunction", new Node[0], new Node[0]);
    Node functionExpression = Node.newExpressionStatement(Node.newNameNode("x").set("value", Node.newFunctionNode("myFunction", new Node[0], new Node[0])));

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node functionDeclarationInScript = Node.newFunctionNode("myFunction", new Node[0], new Node[0]);
    Node functionDeclarationInFunction = Node.newFunctionNode("outerFunction", new Node[0], new Node[] { Node.newFunctionNode("myFunction", new Node[0], new Node[0]) });

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationInScript));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationInFunction));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_invalid() {
    Node functionDeclarationInScript = Node.newFunctionNode("myFunction", new Node[0], new Node[0]);
    Node functionDeclarationInBlock = Node.newBlock(new Node[] { Node.newFunctionNode("myFunction", new Node[0], new Node[0]) }).set("type", Token.BLOCK);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationInBlock));
  }
}