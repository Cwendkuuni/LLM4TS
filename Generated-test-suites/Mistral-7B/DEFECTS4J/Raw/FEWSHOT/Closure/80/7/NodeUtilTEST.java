package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn();
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpression("1 + 2"); // A simple expression
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newScript("1 + 2;"); // A script node
    assertTrue(NodeUtil.isStatement(nodeScript));

    Node nodeExpression = Node.newExpression("1 + 2");; // A simple expression
    assertFalse(NodeUtil.isStatement(nodeExpression));

    Node nodeFunction = Node.newFunction("f", new Node[0], Node.newBlock(new Node[0])); // A function declaration
    assertTrue(NodeUtil.isStatement(nodeFunction));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = Node.newFunction("f", new Node[0], Node.newBlock(new Node[0])); // A function declaration
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));

    Node nodeFunctionExpression = Node.newExpression("function f() {}"); // A function expression
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionScript = Node.newScript("function f() {}"); // A function declaration at the script level
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionScript));

    Node nodeFunctionBlock = Node.newBlock(new Node[] { Node.newFunction("f", new Node[0], Node.newBlock(new Node[0])) }); // A function declaration inside a block
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionBlock.getFirstChild()));

    Node nodeFunctionFunction = Node.newFunction("f", new Node[0], Node.newBlock(new Node[0])); // A function declaration inside a function
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionFunction));
  }
}