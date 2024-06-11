package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeFor = Node.newFor();
    Node nodeIf = Node.newIf();
    Node nodeFunction = Node.newFunction();

    assertTrue(NodeUtil.isForIn(nodeFor));
    assertFalse(NodeUtil.isForIn(nodeIf));
    assertFalse(NodeUtil.isForIn(nodeFunction));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newScript();
    Node nodeFunction = Node.newFunction();
    Node nodeIf = Node.newIf();
    Node nodeWhile = Node.newWhile();
    Node nodeAssign = Node.newAssign();

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeFunction));
    assertTrue(NodeUtil.isStatement(nodeAssign));
    assertFalse(NodeUtil.isStatement(nodeIf));
    assertFalse(NodeUtil.isStatement(nodeWhile));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = Node.newFunction();
    Node nodeFunctionExpression = Node.newFunctionExpression();

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionInScript = Node.newFunction();
    Node nodeFunctionInFunction = Node.newFunction();
    Node nodeFunctionInFunctionExpression = Node.newFunctionExpression();

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInScript));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunctionExpression));
  }

  @Test
  public void testIsStatementParentIsScriptOrBlock() {
    Node nodeStatement = Node.newAssign();
    Node nodeScript = Node.newScript();
    Node nodeBlock = Node.newBlock();

    Node parentScript = Node.newScript();
    parentScript.addChildToEnd(nodeStatement);

    Node parentBlock = Node.newBlock();
    parentBlock.addChildToEnd(nodeStatement);

    checkState(parentScript != null, "parentScript should not be null");
    checkState(parentBlock != null, "parentBlock should not be null");

    assertTrue(NodeUtil.isStatement(nodeStatement));
    assertTrue(NodeUtil.isStatement(nodeStatement)); // actually the same statement

    assertTrue(NodeUtil.isStatement(nodeStatement), "isStatement: nodeStatement is a statement");
    assertTrue(NodeUtil.isStatement(nodeStatement), "isStatement: nodeStatement is a statement (again)");

    assertTrue(NodeUtil.isStatement(parentScript.getLastChild()), "isStatement: parentScript's last child is a statement");
    assertTrue(NodeUtil.isStatement(parentBlock.getLastChild()), "isStatement: parentBlock's last child is a statement");
  }

  @Test
  public void testIsStatementParentIsNotScriptOrBlock() {
    Node nodeStatement = Node.newAssign();
    Node nodeIf = Node.newIf();

    Node parentIf = Node.newIf();
    parentIf.addChildToEnd(nodeStatement);

    checkState(parentIf != null, "parentIf should not be null");

    assertFalse(NodeUtil.isStatement(nodeStatement), "isStatement: nodeStatement is not a statement when parent is not Script or Block");
  }
}