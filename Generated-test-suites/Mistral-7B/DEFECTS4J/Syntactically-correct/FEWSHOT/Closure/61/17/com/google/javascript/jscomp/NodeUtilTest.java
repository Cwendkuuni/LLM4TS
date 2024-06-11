package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn();
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeExpression = Node.newExpression(Node.NAME, "a");
    assertFalse(NodeUtil.isForIn(nodeExpression));

    Node nodeIfStatement = Node.newIfStatement();
    assertFalse(NodeUtil.isForIn(nodeIfStatement));

    Node nodeFunction = Node.newFunction(Node.newName("myFunction"), Node.newBlock());
    assertFalse(NodeUtil.isForIn(nodeFunction));
  }

  @Test
  public void testIsStatement() {
    Node nodeFunction = Node.newFunction(Node.newName("myFunction"), Node.newBlock());
    assertTrue(NodeUtil.isStatement(nodeFunction));

    Node nodeExpression = Node.newExpression(Node.NAME, "a");
    assertFalse(NodeUtil.isStatement(nodeExpression));

    Node nodeIfStatement = Node.newIfStatement();
    assertTrue(NodeUtil.isStatement(nodeIfStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(Node.newName("myFunction"), Node.newBlock());
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpression(Node.NAME, "myFunction", Node.newFunction(Node.newBlock()));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));

    Node nodeIfStatement = Node.newIfStatement();
    assertFalse(NodeUtil.isFunctionDeclaration(nodeIfStatement));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newScript();
    Node nodeFunctionDeclaration = Node.newFunction(Node.newName("myFunction"), Node.newBlock());
    nodeFunctionDeclaration.setParent(nodeScript);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionDeclaration2 = Node.newFunction(Node.newName("myFunction2"), Node.newBlock());
    Node nodeFunction = Node.newFunction(Node.newName("myFunction"), Node.newBlock());
    nodeFunction.setParent(nodeFunctionDeclaration2);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));

    Node nodeIfStatement = Node.newIfStatement();
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeIfStatement));
  }

  @Test
  public void testIsFunctionExpression() {
    Node nodeFunctionExpression = Node.newExpression(Node.NAME, "myFunction", Node.newFunction(Node.newBlock()));
    assertTrue(NodeUtil.isFunctionExpression(nodeFunctionExpression));

    Node nodeFunctionDeclaration = Node.newFunction(Node.newName("myFunction"), Node.newBlock());
    assertFalse(NodeUtil.isFunctionExpression(nodeFunctionDeclaration));

    Node nodeIfStatement = Node.newIfStatement();
    assertFalse(NodeUtil.isFunctionExpression(nodeIfStatement));
  }

  @Test
  public void testParentCheck() {
    Node nodeFunction = Node.newFunction(Node.newName("myFunction"), Node.newBlock());
    checkState(nodeFunction.getParent() == null, "Node has a parent: %s", nodeFunction.getParent());

    Node nodeScript = Node.newScript();
    nodeFunction.setParent(nodeScript);
    checkState(nodeFunction.getParent() == nodeScript, "Node has incorrect parent: %s", nodeFunction.getParent());
  }
}