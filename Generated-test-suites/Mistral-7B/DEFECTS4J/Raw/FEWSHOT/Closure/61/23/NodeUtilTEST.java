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

    Node nodeNotForIn1 = Node.newExpressionStatement(Node.newName("x"));
    assertFalse(NodeUtil.isForIn(nodeNotForIn1));

    Node nodeNotForIn2 = Node.newExpressionStatement(Node.newExpression("1 + 2"));
    assertFalse(NodeUtil.isForIn(nodeNotForIn2));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newScript();
    assertTrue(NodeUtil.isStatement(nodeScript));

    Node nodeBlock = Node.newBlock(Node.newExpressionStatement(Node.newName("x")));
    assertTrue(NodeUtil.isStatement(nodeBlock));

    Node nodeLabel = Node.newLabel("label");
    assertTrue(NodeUtil.isStatement(nodeLabel));

    Node nodeFunction = Node.newFunction("func", Node.newList(), Node.newList(), Node.newBlock(Node.newExpressionStatement(Node.newName("x"))));
    assertTrue(NodeUtil.isStatement(nodeFunction));

    Node nodeExpression = Node.newExpression("1 + 2");
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction("func", Node.newList(), Node.newList(), Node.newBlock(Node.newExpressionStatement(Node.newName("x"))));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionStatement(Node.newFunction("func", Node.newList(), Node.newList(), Node.newBlock(Node.newExpressionStatement(Node.newName("x")))));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newScript();
    Node nodeFunctionDeclaration1 = Node.newFunction("func1", Node.newList(), Node.newList(), Node.newBlock(Node.newExpressionStatement(Node.newName("x"))));
    Node nodeFunctionDeclaration2 = Node.newFunction("func2", Node.newList(), Node.newList(), Node.newBlock(Node.newExpressionStatement(Node.newName("x"))));
    nodeFunctionDeclaration1.setParent(nodeScript);
    nodeFunctionDeclaration2.setParent(nodeScript);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration1));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration2));

    Node nodeFunction = Node.newFunction("func", Node.newList(), Node.newList(), Node.newBlock(Node.newExpressionStatement(Node.newName("x"))));
    Node nodeStatement = Node.newExpressionStatement(nodeFunction);
    nodeStatement.setParent(nodeScript);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_withNestedFunction() {
    Node nodeScript = Node.newScript();
    Node nodeFunctionDeclaration1 = Node.newFunction("func1", Node.newList(), Node.newList(), Node.newBlock(Node.newExpressionStatement(Node.newName("x"))));
    Node nodeFunctionDeclaration2 = Node.newFunction("func2", Node.newList(), Node.newList(), Node.newBlock(Node.newExpressionStatement(Node.newFunction("func3", Node.newList(), Node.newList(), Node.newBlock(Node.newExpressionStatement(Node.newName("x")))))));
    nodeFunctionDeclaration1.setParent(nodeScript);
    nodeFunctionDeclaration2.setParent(nodeScript);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration1));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration2));

    final int grandParentIndex = nodeScript.getParentIndex();
    checkState(grandParentIndex != -1);
    Node nodeFunction = Node.newFunction("func", Node.newList(), Node.newList(), Node.newBlock(Node.newExpressionStatement(Node.newName("x"))));
    Node nodeStatement = Node.newExpressionStatement(nodeFunction);
    nodeStatement.setParent(nodeFunctionDeclaration2);
    nodeFunctionDeclaration2.setParent(nodeScript);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));
  }
}