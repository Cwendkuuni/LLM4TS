package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class UnreachableCodeEliminationTest {

  @Test
  public void testRemoveDeadExprStatementSafely_EmptyNode() {
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    Node root = new Node(Token.SCRIPT);
    elimination.process(null, root);
    Node emptyNode = new Node(Token.EMPTY);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToEnd(emptyNode);
    root.addChildToEnd(parent);

    elimination.visit(null, root, null);
    assertNull(parent.getFirstChild());
  }

  @Test
  public void testRemoveDeadExprStatementSafely_SingleNodeBlock() {
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    Node root = new Node(Token.SCRIPT);
    elimination.process(null, root);
    Node block = new Node(Token.BLOCK);
    Node singleNode = new Node(Token.NUMBER, 42);
    block.addChildToEnd(singleNode);
    root.addChildToEnd(block);

    elimination.visit(null, root, null);
    assertNull(block.getFirstChild());
  }

  @Test
  public void testRemoveDeadExprStatementSafely_BlockWithChildren() {
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    Node root = new Node(Token.SCRIPT);
    elimination.process(null, root);
    Node block = new Node(Token.BLOCK);
    Node child1 = new Node(Token.ASSIGN, null, new Node(Token.NAME, "x"), new Node(Token.NUMBER, 42));
    Node child2 = new Node(Token.ASSIGN, null, new Node(Token.NAME, "y"), new Node(Token.NUMBER, 43));
    block.addChildToEnd(child1);
    block.addChildToEnd(child2);
    root.addChildToEnd(block);

    elimination.visit(null, root, null);
    assertNull(block.getFirstChild());
  }

  @Test
  public void testRemoveDeadExprStatementSafely_BlockWithConditionalStatement() {
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    Node root = new Node(Token.SCRIPT);
    elimination.process(null, root);
    Node block = new Node(Token.BLOCK);
    Node condition = new Node(Token.IF, null, new Node(Token.ASSIGN, null, new Node(Token.NAME, "x"), new Node(Token.NUMBER, 42)), new Node(Token.BLOCK));
    Node child = new Node(Token.ASSIGN, null, new Node(Token.NAME, "y"), new Node(Token.NUMBER, 43));
    condition.getLastChild().addChildToEnd(child);
    block.addChildToEnd(condition);
    root.addChildToEnd(block);

    elimination.visit(null, root, null);
    assertNull(block.getFirstChild());
  }

  @Test
  public void testRemoveDeadExprStatementSafely_BlockWithLoopStatement() {
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    Node root = new Node(Token.SCRIPT);
    elimination.process(null, root);
    Node block = new Node(Token.BLOCK);
    Node whileStatement = new Node(Token.WHILE, null, null, new Node(Token.BLOCK));
    Node loopCondition = new Node(Token.ASSIGN, null, new Node(Token.NAME, "x"), new Node(Token.NUMBER, 42));
    whileStatement.getLastChild().addChildToEnd(loopCondition);
    Node child = new Node(Token.ASSIGN, null, new Node(Token.NAME, "y"), new Node(Token.NUMBER, 43));
    whileStatement.getLastChild().addChildToEnd(child);
    block.addChildToEnd(whileStatement);
    root.addChildToEnd(block);

    elimination.visit(null, root, null);
    assertNull(block.getFirstChild());
  }

  @Test
  public void testRemoveDeadExprStatementSafely_BlockWithConditionalBreak() {
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    Node root = new Node(Token.SCRIPT);
    elimination.process(null, root);
    Node block = new Node(Token.BLOCK);
    Node condition = new Node(Token.IF, null, new Node(Token.ASSIGN, null, new Node(Token.NAME, "x"), new Node(Token.NUMBER, 42)), new Node(Token.BLOCK));
    Node breakStatement = new Node(Token.BREAK, null, null);
    condition.getLastChild().addChildToEnd(breakStatement);
    Node child = new Node(Token.ASSIGN, null, new Node(Token.NAME, "y"), new Node(Token.NUMBER, 43));
    condition.getLastChild().addChildToEnd(child);
    block.addChildToEnd(condition);
    root.addChildToEnd(block);

    elimination.visit(null, root, null);
    assertNull(block.getFirstChild());
  }

  @Test
  public void testRemoveDeadExprStatementSafely_BlockWithConditionalContinue() {
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    Node root = new Node(Token.SCRIPT);
    elimination.process(null, root);
    Node block = new Node(Token.BLOCK);
    Node condition = new Node(Token.IF, null, new Node(Token.ASSIGN, null, new Node(Token.NAME, "x"), new Node(Token.NUMBER, 42)), new Node(Token.BLOCK));
    Node continueStatement = new Node(Token.CONTINUE, null, null);
    condition.getLastChild().addChildToEnd(continueStatement);
    Node child = new Node(Token.ASSIGN, null, new Node(Token.NAME, "y"), new Node(Token.NUMBER, 43));
    condition.getLastChild().addChildToEnd(child);
    block.addChildToEnd(condition);
    root.addChildToEnd(block);

    elimination.visit(null, root, null);
    assertNull(block.getFirstChild());
  }

  @Test
  public void testRemoveDeadExprStatementSafely_BlockWithReturnStatement() {
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    Node root = new Node(Token.SCRIPT);
    elimination.process(null, root);
    Node block = new Node(Token.BLOCK);
    Node returnStatement = new Node(Token.RETURN, null, null);
    Node child = new Node(Token.ASSIGN, null, new Node(Token.NAME, "y"), new Node(Token.NUMBER, 43));
    block.addChildToEnd(returnStatement);
    block.addChildToEnd(child);
    root.addChildToEnd(block);

    elimination.visit(null, root, null);
    assertNull(block.getFirstChild());
  }

  @Test
  public void testTryRemoveUnconditionalBranching_EmptyNode() {
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    Node root = new Node(Token.SCRIPT);
    elimination.process(null, root);
    Node emptyNode = new Node(Token.EMPTY);
    elimination.visit(null, root, null);

    Node result = elimination.tryRemoveUnconditionalBranching(emptyNode);
    assertNull(result);
  }

  @Test
  public void testTryRemoveUnconditionalBranching_ReturnNode() {
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    Node root = new Node(Token.SCRIPT);
    elimination.process(null, root);
    Node returnNode = new Node(Token.RETURN, null, null);
    elimination.visit(null, root, null);

    Node result = elimination.tryRemoveUnconditionalBranching(returnNode);
    assertNull(result);
  }

  @Test
  public void testTryRemoveUnconditionalBranching_BreakNode() {
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    Node root = new Node(Token.SCRIPT);
    elimination.process(null, root);
    Node breakNode = new Node(Token.BREAK, null, null);
    elimination.visit(null, root, null);

    Node result = elimination.tryRemoveUnconditionalBranching(breakNode);
    assertNull(result);
  }

  @Test
  public void testTryRemoveUnconditionalBranching_ContinueNode() {
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    Node root = new Node(Token.SCRIPT);
    elimination.process(null, root);
    Node continueNode = new Node(Token.CONTINUE, null, null);
    elimination.visit(null, root, null);

    Node result = elimination.tryRemoveUnconditionalBranching(continueNode);
    assertNull(result);
  }

  @Test
  public void testTryRemoveUnconditionalBranching_SimpleBranching() {
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    Node root = new Node(Token.SCRIPT);
    elimination.process(null, root);
    Node ifNode = new Node(Token.IF, null, null, new Node(Token.BLOCK));
    Node breakNode = new Node(Token.BREAK, null, null);
    Node elseNode = new Node(Token.ELSE, null, null, new Node(Token.BLOCK));
    Node returnNode = new Node(Token.RETURN, null, null);
    ifNode.addChildToEnd(breakNode);
    elseNode.addChildToEnd(returnNode);
    root.addChildToEnd(ifNode);
    root.addChildToEnd(elseNode);

    elimination.visit(null, root, null);

    Node result1 = elimination.tryRemoveUnconditionalBranching(breakNode);
    Node result2 = elimination.tryRemoveUnconditionalBranching(returnNode);

    assertTrue(result1 instanceof Node);
    assertTrue(result1.equals(breakNode));
    assertTrue(result2 instanceof Node);
    assertTrue(result2.equals(returnNode));
  }

  @Test
  public void testTryRemoveUnconditionalBranching_ComplexBranching() {
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, false);
    Node root = new Node(Token.SCRIPT);
    elimination.process(null, root);
    Node ifNode = new Node(Token.IF, null, null, new Node(Token.BLOCK));
    Node ifElseIfNode = new Node(Token.ELSE_IF, null, null, new Node(Token.BLOCK));
    Node ifElseNode = new Node(Token.ELSE, null, null, new Node(Token.BLOCK));
    Node breakNode1 = new Node(Token.BREAK, null, null);
    Node breakNode2 = new Node(Token.BREAK, null, null);
    Node returnNode1 = new Node(Token.RETURN, null, null);
    Node returnNode2 = new Node(Token.RETURN, null, null);
    ifNode.addChildToEnd(breakNode1);
    ifElseIfNode.addChildToEnd(returnNode1);
    ifElseNode.addChildToEnd(breakNode2);
    ifElseNode.addChildToEnd(returnNode2);
    root.addChildToEnd(ifNode);
    root.addChildToEnd(ifElseIfNode);
    root.addChildToEnd(ifElseNode);

    elimination.visit(null, root, null);

    Node result1 = elimination.tryRemoveUnconditionalBranching(breakNode1);
    Node result2 = elimination.tryRemoveUnconditionalBranching(returnNode1);
    Node result3 = elimination.tryRemoveUnconditionalBranching(breakNode2);
    Node result4 = elimination.tryRemoveUnconditionalBranching(returnNode2);

    assertTrue(result1 instanceof Node);
    assertTrue(result1.equals(breakNode1));
    assertTrue(result2 instanceof Node);
    assertTrue(result2.equals(returnNode1));
    assertTrue(result3 instanceof Node);
    assertTrue(result3.equals(breakNode2));
    assertTrue(result4 instanceof Node);
    assertTrue(result4.equals(returnNode2));
  }
}