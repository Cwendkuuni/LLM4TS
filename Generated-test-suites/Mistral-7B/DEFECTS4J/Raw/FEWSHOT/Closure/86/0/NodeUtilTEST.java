package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeFor = Node.newForNode();
    Node nodeIf = Node.newIfStatementNode();

    assertTrue(NodeUtil.isForIn(nodeFor));
    assertFalse(NodeUtil.isForIn(nodeIf));
  }

  @Test
  public void testIsStatement() {
    Node nodeFor = Node.newForNode();
    Node nodeIf = Node.newIfStatementNode();
    Node nodeFunction = Node.newFunctionNode("func", null);
    Node nodeExpression = Node.newExpressionStatement(Node.newNameNode("x"));

    assertTrue(NodeUtil.isStatement(nodeFor));
    assertTrue(NodeUtil.isStatement(nodeFunction));
    assertTrue(NodeUtil.isStatement(nodeExpression));
    assertFalse(NodeUtil.isStatement(Node.newNumberNode(1)));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = Node.newFunctionNode("func", null);
    Node nodeFunctionExpression = Node.newExpressionStatement(nodeFunction);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionInScript = Node.newFunctionNode("func", null);
    Node nodeFunctionInFunction = Node.newFunctionNode("func", Node.newNameNode("parentFunc"));

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInScript));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));
  }

  @Test
  public void testCheckState() {
    Node nodeFor = Node.newForNode();
    Node nodeIf = Node.newIfStatementNode();
    Node nodeParent = Node.newBlockNode();
    Node nodeScript = Node.newScriptNode();

    Node nodeNull = null;

    checkState(nodeFor != null, "nodeFor should not be null");
    checkState(nodeIf.getParent() != null, "nodeIf parent should not be null");
    checkState(nodeParent != null, "nodeParent should not be null");
    checkState(nodeScript != null, "nodeScript should not be null");
    checkState(nodeNull == null, "nodeNull should be null");
  }
}