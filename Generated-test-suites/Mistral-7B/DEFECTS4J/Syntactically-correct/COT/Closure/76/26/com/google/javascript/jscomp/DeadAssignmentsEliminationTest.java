package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeOperations;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class DeadAssignmentsEliminationTest {

  private AbstractCompiler compiler;
  private DeadAssignmentsElimination deadAssignmentsElimination;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
  }

  @Test
  public void testSimpleAssignment() {
    Node root = NodeOperations.parseJs("x = 1;");
    deadAssignmentsElimination.process(null, root);
    assertNull("Unexpected node after elimination", root.getFirstChild());
  }

  @Test
  public void testAssignmentWithDeadVariable() {
    Node root = NodeOperations.parseJs("x = 1; y = x; x = 2;");
    deadAssignmentsElimination.process(null, root);
    assertEquals("Unexpected number of nodes after elimination", 3, root.getNumChildren());
  }

  @Test
  public void testAssignmentWithDeadVariableAndConditional() {
    Node root = NodeOperations.parseJs(
        "if (x) { x = 1; } y = x; x = 2;");
    deadAssignmentsElimination.process(null, root);
    assertEquals("Unexpected number of nodes after elimination", 5, root.getNumChildren());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullExterns() {
    deadAssignmentsElimination.process(null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullRoot() {
    deadAssignmentsElimination.process(null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullCompiler() {
    new DeadAssignmentsElimination(null);
  }

  @Test(expected = NullPointerException.class)
  public void testNullControlFlowGraph() {
    NodeTraversal traversal = new NodeTraversal(null, null);
    deadAssignmentsElimination.tryRemoveDeadAssignments(traversal, null);
  }

  @Test(expected = NullPointerException.class)
  public void testNullScope() {
    NodeTraversal traversal = new NodeTraversal(null, null);
    deadAssignmentsElimination.enterScope(traversal);
  }

  @Test(expected = NullPointerException.class)
  public void testNullLiveness() {
    NodeTraversal traversal = new NodeTraversal(null, null);
    deadAssignmentsElimination.tryRemoveDeadAssignments(traversal, null);
  }

  @Test(expected = NullPointerException.class)
  public void testNullExpressionRoot() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    deadAssignmentsElimination.enterScope(new NodeTraversal(cfg, null));
  }

  @Test(expected = NullPointerException.class)
  public void testNullState() {
    NodeTraversal traversal = new NodeTraversal(null, null);
    DiGraphNode<Node, Branch> node = new DiGraphNode<>(null, null);
    deadAssignmentsElimination.tryRemoveAssignment(traversal, null, node, null);
  }

  @Test(expected = NullPointerException.class)
  public void testNullParent() {
    NodeTraversal traversal = new NodeTraversal(null, null);
    Node n = new Node(Token.ASSIGN, null, null);
    deadAssignmentsElimination.tryRemoveAssignment(traversal, n, null, null);
  }

  @Test(expected = NullPointerException.class)
  public void testNullLhs() {
    NodeTraversal traversal = new NodeTraversal(null, null);
    Node n = new Node(Token.ASSIGN, null, null);
    Node lhs = new Node(Token.NAME, null, null);
    deadAssignmentsElimination.tryRemoveAssignment(traversal, n, null, null);
  }

  @Test(expected = NullPointerException.class)
  public void testNullRhs() {
    NodeTraversal traversal = new NodeTraversal(null, null);
    Node n = new Node(Token.ASSIGN, null, null);
    Node rhs = new Node(Token.NAME, null, null);
    deadAssignmentsElimination.tryRemoveAssignment(traversal, n, null, null);
  }

  @Test(expected = NullPointerException.class)
  public void testNullScopeWhenEnteringScope() {
    NodeTraversal traversal = new NodeTraversal(null, null);
    deadAssignmentsElimination.enterScope(traversal);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testNullFirstChild() {
    Node n = new Node(Token.ASSIGN, null, null);
    deadAssignmentsElimination.tryRemoveAssignment(null, n, null, null);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testNullNext() {
    Node n = new Node(Token.ASSIGN, null, null);
    Node next = null;
    deadAssignmentsElimination.tryRemoveAssignment(null, n, null, null);
  }

  @Test(expected = IllegalStateException.class)
  public void testRemovalWhenVariableIsEscaped() {
    Node root = NodeOperations.parseJs(
        "function foo() { var x = 1; return function bar() { x = 2; } }");
    deadAssignmentsElimination.process(null, root);
  }

  @Test(expected = IllegalStateException.class)
  public void testRemovalWhenVariableIsAliveBeforeAssignment() {
    Node root = NodeOperations.parseJs("if (x) { x = 1; } x = 2;");
    deadAssignmentsElimination.process(null, root);
  }

  @Test(expected = IllegalStateException.class)
  public void testRemovalWhenVariableIsAliveAfterAssignment() {
    Node root = NodeOperations.parseJs("x = 1; x = 2;");
    deadAssignmentsElimination.process(null, root);
  }

  @Test(expected = IllegalStateException.class)
  public void testRemovalWhenVariableIsAliveWithinExpression() {
    Node root = NodeOperations.parseJs(
        "function foo(x) { if (x > 1 && x = 2) { } }");
    deadAssignmentsElimination.process(null, root);
  }

  @Test
  public void testRemovalWhenVariableIsDead() {
    Node root = NodeOperations.parseJs("x = 1; y = x; x = 2;");
    deadAssignmentsElimination.process(null, root);
    assertNull("Unexpected node after elimination", root.getLastChild().getNext());
  }

  @Test
  public void testRemovalWhenVariableIsDeadWithinConditional() {
    Node root = NodeOperations.parseJs("if (x) { x = 1; } y = x; x = 2;");
    deadAssignmentsElimination.process(null, root);
    assertNull("Unexpected node after elimination", root.getLastChild().getNext());
  }

  @Test
  public void testRemovalWhenVariableIsDeadWithinLoop() {
    Node root = NodeOperations.parseJs(
        "for (var x = 1; x < 10; x++) { x = 1; } x = 2;");
    deadAssignmentsElimination.process(null, root);
    assertNull("Unexpected node after elimination", root.getLastChild());
  }

  @Test
  public void testRemovalWhenVariableIsDeadWithinExpression() {
    Node root = NodeOperations.parseJs("x = 1; y = (x = 1) * 3; x = 2;");
    deadAssignmentsElimination.process(null, root);
    assertEquals("Unexpected number of nodes after elimination", 2, root.getNumChildren());
  }
}