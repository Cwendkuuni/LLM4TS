package com.google.javascript.jscomp.jsonml.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

/**
 * Tests for the NodeUtil class.
 */
public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn();
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeAssigment = Node.newAssignment(Node.newName("x"), Node.newNumber(1));
    assertFalse(NodeUtil.isForIn(nodeAssigment));
  }

  @Test
  public void testIsStatement() {
    Node nodeZero = Node.newNumber(0);
    assertFalse(NodeUtil.isStatement(nodeZero));

    Node nodeAssign = Node.newAssignment(Node.newName("x"), Node.newNumber(1));
    assertTrue(NodeUtil.isStatement(nodeAssign));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = Node.newFunction(null, Node.newName("foo"), null, Node.newBlock(Node.newStatementList()));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));

    Node nodeFunctionExpression = Node.newExpressionStatement(Node.newAssignment(Node.newName("x"), Node.newFunction(null, null, null, Node.newBlock(Node.newStatementList()))));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newScript();
    Node nodeFunctonDeclaration = Node.newFunction(null, Node.newName("foo"), null, Node.newBlock(Node.newStatementList()));
    nodeScript.addChild(nodeFunctonDeclaration);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctonDeclaration));

    Node nodeFunctionExpression = Node.newExpressionStatement(Node.newAssignment(Node.newName("x"), Node.newFunction(null, null, null, Node.newBlock(Node.newStatementList()))));
    nodeScript.addChild(nodeFunctionExpression);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsForInvalidNodeType() {
    Node nodeNull = Node.NULL;
    assertFalse(NodeUtil.isForIn(nodeNull));
    assertFalse(NodeUtil.isStatement(nodeNull));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeNull));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNull));
  }

  @Test(expected = IllegalStateException.class)
  public void testParentIsNull() {
    Node nodeNull = Node.NULL;
    NodeUtil.isStatement(nodeNull);
  }

  @Test
  public void testIsForInvalidParentType() {
    Node nodeParent = Node.newIdentifier("parent");
    Node nodeChild = Node.newNumber(1);
    nodeParent.setChild(0, nodeChild);

    Node nodeFunction = Node.newFunction(null, Node.newName("foo"), null, Node.newBlock(Node.newStatementList()));
    nodeParent.setChild(1, nodeFunction);

    assertFalse(NodeUtil.isStatement(nodeChild));
  }
}