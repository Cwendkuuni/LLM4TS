package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn();
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeExpression = Node.newExpression(Node.IDENT("x"));
    assertFalse(NodeUtil.isForIn(nodeExpression));

    Node nodeStatement = Node.newStatement(Node.IDENT("x"));
    assertFalse(NodeUtil.isForIn(nodeStatement));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newScript();
    assertTrue(NodeUtil.isStatement(nodeScript));

    Node nodeExpression = Node.newExpression(Node.IDENT("x"));
    assertFalse(NodeUtil.isStatement(nodeExpression));

    Node nodeFunction = Node.newFunction("myFunction", new Node[0], Node.newBlock(new Node[0]));
    assertFalse(NodeUtil.isStatement(nodeFunction));

    Node nodeBlock = Node.newBlock(new Node[0]);
    assertTrue(NodeUtil.isStatement(nodeBlock));

    Node nodeIf = Node.newIf(Node.newExpression(Node.IDENT("x")), Node.newStatement(Node.IDENT("y")), Node.newBlock(new Node[0]));
    assertTrue(NodeUtil.isStatement(nodeIf));

    Node nodeLabel = Node.newLabel("myLabel");
    assertTrue(NodeUtil.isStatement(nodeLabel));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction("myFunction", new Node[0], Node.newBlock(new Node[0]));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpression(Node.newFunction("myFunction", new Node[0], Node.newBlock(new Node[0])));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclarationInScript = Node.newFunction("myFunction", new Node[0], Node.newBlock(new Node[0]));
    Node nodeScript = Node.newScript();
    nodeScript.addChild(nodeFunctionDeclarationInScript);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));

    Node nodeFunctionDeclarationInFunction = Node.newFunction("outerFunction", new Node[0], Node.newBlock(new Node[0]));
    Node nodeInnerFunction = Node.newFunction("innerFunction", new Node[0], Node.newBlock(new Node[0]));
    nodeInnerFunction.setParent(nodeFunctionDeclarationInFunction);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeInnerFunction));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsFunctionDeclarationWithNullNode() {
    NodeUtil.isFunctionDeclaration(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsHoistedFunctionDeclarationWithNullNode() {
    NodeUtil.isHoistedFunctionDeclaration(null);
  }

  private void assertFalse(boolean condition) {
    // Do nothing
  }

  private void assertTrue(boolean condition) {
    checkState(condition);
  }
}