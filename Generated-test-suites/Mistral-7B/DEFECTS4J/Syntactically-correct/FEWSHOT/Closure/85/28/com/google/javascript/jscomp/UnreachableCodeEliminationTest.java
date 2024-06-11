package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class UnreachableCodeEliminationTest {

  @Test
  public void testRemoveDeadExprStatementSafely_EmptyNode() {
    UnreachableCodeElimination unreachableCodeElimination = new UnreachableCodeElimination(null, false);
    Node node = Node.EMPTY;
    unreachableCodeElimination.removeDeadExprStatementSafely(node);
    assertNull(node.getNext());
  }

  @Test
  public void testRemoveDeadExprStatementSafely_BlockNodeWithChildren() {
    UnreachableCodeElimination unreachableCodeElimination = new UnreachableCodeElimination(null, false);
    Node blockNode = Node.newBlock();
    Node childNode1 = Node.newExpressionStatement(Node.newIdentifier("x"));
    Node childNode2 = Node.newExpressionStatement(Node.newIdentifier("y"));
    blockNode.addChildToEnd(childNode1);
    blockNode.addChildToEnd(childNode2);

    unreachableCodeElimination.removeDeadExprStatementSafely(blockNode);

    assertTrue(blockNode.hasChildren());
    assertFalse(blockNode.getFirstChild().getNext() instanceof Node);
  }

  @Test
  public void testRemoveDeadExprStatementSafely_BlockNodeWithoutChildren() {
    UnreachableCodeElimination unreachableCodeElimination = new UnreachableCodeElimination(null, false);
    Node blockNode = Node.newBlock();

    unreachableCodeElimination.removeDeadExprStatementSafely(blockNode);

    assertFalse(blockNode.hasChildren());
  }

  @Test
  public void testRemoveDeadExprStatementSafely_DoNode() {
    UnreachableCodeElimination unreachableCodeElimination = new UnreachableCodeElimination(null, false);
    Node doNode = Node.newDoStatement();
    Node conditionNode = Node.newExpressionStatement(Node.newIdentifier("condition"));
    Node bodyNode = Node.newBlock();
    Node breakStatementNode = Node.newBreakStatement();
    bodyNode.addChildToEnd(breakStatementNode);
    doNode.setBody(bodyNode);
    doNode.setCondition(conditionNode);

    unreachableCodeElimination.removeDeadExprStatementSafely(doNode);

    assertTrue(doNode.hasChildren());
  }

  @Test
  public void testRemoveDeadExprStatementSafely_ReturnNode() {
    UnreachableCodeElimination unreachableCodeElimination = new UnreachableCodeElimination(null, false);
    Node returnNode = Node.newReturnStatement();

    unreachableCodeElimination.removeDeadExprStatementSafely(returnNode);

    assertNull(returnNode.getNext());
  }

  @Test
  public void testRemoveDeadExprStatementSafely_CatchNode() {
    UnreachableCodeElimination unreachableCodeElimination = new UnreachableCodeElimination(null, false);
    Node tryNode = Node.newTryStatement();
    Node catchNode = Node.newCatchStatement();
    Node finallyNode = Node.newBlock();
    Node catchBlockNode = Node.newBlock();

    tryNode.setBody(tryNode);
    tryNode.setCatchBlock(catchBlockNode);
    catchBlockNode.addChildToEnd(finallyNode);
    catchNode.setBody(catchBlockNode);

    unreachableCodeElimination.removeDeadExprStatementSafely(catchNode);

    assertNull(catchNode.getNext());
  }

  @Test
  public void testTryRemoveUnconditionalBranching_ReturnNode() {
    UnreachableCodeElimination unreachableCodeElimination = new UnreachableCodeElimination(null, true);
    Node parentNode = Node.newBlock();
    Node returnNode = Node.newReturnStatement();

    parentNode.addChildToEnd(returnNode);

    Node result = unreachableCodeElimination.tryRemoveUnconditionalBranching(returnNode);

    assertNull(result);
  }

  @Test
  public void testTryRemoveUnconditionalBranching_BreakNode() {
    UnreachableCodeElimination unreachableCodeElimination = new UnreachableCodeElimination(null, true);
    Node parentNode = Node.newBlock();
    Node breakNode = Node.newBreakStatement();
    Node nextNode = Node.newExpressionStatement(Node.newIdentifier("next"));

    parentNode.addChildToEnd(breakNode);
    parentNode.addChildToEnd(nextNode);

    Node result = unreachableCodeElimination.tryRemoveUnconditionalBranching(breakNode);

    assertNull(result);
  }

  @Test
  public void testTryRemoveUnconditionalBranching_ConditionalBranchNode() {
    UnreachableCodeElimination unreachableCodeElimination = new UnreachableCodeElimination(null, true);
    Node parentNode = Node.newBlock();
    Node ifNode = Node.newIfStatement(Node.newIdentifier("condition"), Node.newIdentical(Node.UNDEFINED, Node.NUMBER_TYPE), Node.newBlock());
    Node elseNode = Node.newBlock();
    Node lastNode = Node.newExpressionStatement(Node.newIdentifier("last"));

    parentNode.addChildToEnd(ifNode);
    parentNode.addChildToEnd(elseNode);
    ifNode.setElseBlock(elseNode);
    elseNode.addChildToEnd(lastNode);

    Node result = unreachableCodeElimination.tryRemoveUnconditionalBranching(lastNode);

    assertNull(result);
  }

  @Test
  public void testTryRemoveUnconditionalBranching_UnconditionalBranchNode() {
    UnreachableCodeElimination unreachableCodeElimination = new UnreachableCodeElimination(null, true);
    Node parentNode = Node.newBlock();
    Node ifNode = Node.newIfStatement(Node.newIdentifier("condition"), Node.newIdentical(Node.UNDEFINED, Node.NUMBER_TYPE), Node.newBlock());
    Node elseNode = Node.newBlock();
    Node breakNode = Node.newBreakStatement();

    parentNode.addChildToEnd(ifNode);
    parentNode.addChildToEnd(elseNode);
    ifNode.setElseBlock(elseNode);
    elseNode.addChildToEnd(breakNode);

    Node result = unreachableCodeElimination.tryRemoveUnconditionalBranching(breakNode);

    assertNull(result);
  }

  @Test
  public void testTryRemoveUnconditionalBranching_UnconditionalBranchNodeWithTarget() {
    UnreachableCodeElimination unreachableCodeElimination = new UnreachableCodeElimination(null, true);
    Node parentNode = Node.newBlock();
    Node ifNode = Node.newIfStatement(Node.newIdentifier("condition"), Node.newIdentical(Node.UNDEFINED, Node.NUMBER_TYPE), Node.newBlock());
    Node elseNode = Node.newBlock();
    Node breakNode = Node.newBreakStatement();
    Node targetNode = Node.newBlock();

    parentNode.addChildToEnd(ifNode);
    parentNode.addChildToEnd(elseNode);
    ifNode.setElseBlock(elseNode);
    elseNode.addChildToEnd(breakNode);
    parentNode.addChildToEnd(targetNode);

    Node result = unreachableCodeElimination.tryRemoveUnconditionalBranching(breakNode);

    assertEquals(targetNode, result);
  }
}