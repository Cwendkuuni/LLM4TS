package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {

  private UnreachableCodeElimination codeElimination;

  @Before
  public void setup() {
    codeElimination = new UnreachableCodeElimination(new AbstractCompiler(), true);
  }

  @Test
  public void testEnterScope() {
    // Arrange
    Node externs = new Node(Token.EXTERN);
    Node root = new Node(Token.ROOT);
    
    // Act
    codeElimination.enterScope(new NodeTraversal(), externs, root);

    // Assert
    assertEquals(externs, root.getFirstChild());
  }

  @Test
  public void testExitScope() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node externs = new Node(Token.EXTERN);
    Node root = new Node(Token.ROOT);
    
    codeElimination.enterScope(t, externs, root);

    // Act
    codeElimination.exitScope(t);

    // Assert
    assertEquals(root.getParent(), null);
  }

  @Test
  public void testProcess() {
    // Arrange
    Node externs = new Node(Token.EXTERN);
    Node root = new Node(Token.ROOT);
    
    // Act
    codeElimination.process(externs, root);

    // Assert
    assertEquals(root.getParent(), null);
  }

  @Test
  public void testVisitParentNull() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    NodeTraversal.NodeTranslator callback = codeElimination.new NodeTranslator();
    Node n = new Node(Token.VAR);
    
    // Act
    codeElimination.visit(t, n, null);

    // Assert
    assertEquals(n.getChildCount(), 0);
  }

  @Test
  public void testVisitFunctionOrScript() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    NodeTraversal.NodeTranslator callback = codeElimination.new NodeTranslator();
    Node n1 = new Node(Token.FUNCTION);
    Node n2 = new Node(Token.SCRIPT);

    // Act
    codeElimination.visit(t, n1, n1.getParent());
    codeElimination.visit(t, n2, n2.getParent());

    // Assert
    assertEquals(n1.getChildCount(), 0);
    assertEquals(n2.getChildCount(), 0);
  }

  @Test
  public void testVisitNoNodeInCfg() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    NodeTraversal.NodeTranslator callback = codeElimination.new NodeTranslator();
    Node n = new Node(Token.ADD);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();

    // Act
    codeElimination.visit(t, n, n.getParent());

    // Assert
    assertEquals(n.getChildCount(), 0);
  }

  @Test
  public void testRemoveDeadExprStatementSafely() {
    // Arrange
    Node n = new Node(Token.STRING);
    Node parent = new Node(Token.VAR);
    parent.addChildToFront(n);

    // Act
    codeElimination.removeDeadExprStatementSafely(n);

    // Assert
    assertEquals(parent.getChildCount(), 0);
  }

  @Test
  public void testRemoveDeadExprStatementSafelyWithBlock() {
    // Arrange
    Node n = new Node(Token.ADD);
    Node block = new Node(Token.BLOCK);
    block.addChildToBack(n);
    Node parent = new Node(Token.VAR);
    parent.addChildToFront(block);

    // Act
    codeElimination.removeDeadExprStatementSafely(n);

    // Assert
    assertEquals(block.getChildCount(), 0);
  }

  @Test
  public void testTryRemoveUnconditionalBranchingWithNull() {
    // Arrange
    Node n = null;

    // Act
    Node result = codeElimination.tryRemoveUnconditionalBranching(n);

    // Assert
    assertEquals(result, null);
  }

  @Test
  public void testTryRemoveUnconditionalBranchingWithBreak() {
    // Arrange
    Node n = new Node(Token.BREAK);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToFront(n);

    // Act
    Node result = codeElimination.tryRemoveUnconditionalBranching(n);

    // Assert
    assertEquals(parent.getChildCount(), 0);
    assertEquals(result, null);
  }

  @Test
  public void testTryRemoveUnconditionalBranchingWithContinue() {
    // Arrange
    Node n = new Node(Token.CONTINUE);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToFront(n);

    // Act
    Node result = codeElimination.tryRemoveUnconditionalBranching(n);

    // Assert
    assertEquals(parent.getChildCount(), 0);
    assertEquals(result, null);
  }

  @Test
  public void testTryRemoveUnconditionalBranchingWithReturn() {
    // Arrange
    Node n = new Node(Token.RETURN);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToFront(n);

    // Act
    Node result = codeElimination.tryRemoveUnconditionalBranching(n);

    // Assert
    assertEquals(parent.getChildCount(), 0);
    assertEquals(result, null);
  }

  @Test
  public void testTryRemoveUnconditionalBranchingWithMultipleOutEdges() {
    // Arrange
    Node n1 = new Node(Token.RETURN);
    Node n2 = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToFront(n1);
    parent.addChildToBack(n2);

    // Act
    Node result = codeElimination.tryRemoveUnconditionalBranching(n1);

    // Assert
    assertEquals(parent.getChildCount(), 1);
    assertEquals(result, n2);
  }

  @Test
  public void testComputeFollowingWithNull() {
    // Arrange
    Node n = null;

    // Act
    Node result = codeElimination.computeFollowing(n);

    // Assert
    assertEquals(result, null);
  }

  @Test
  public void testComputeFollowingWithBlock() {
    // Arrange
    Node block = new Node(Token.BLOCK);

    // Act
    Node result = codeElimination.computeFollowing(block);

    // Assert
    assertEquals(result, null);
  }
}