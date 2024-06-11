package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class DeadAssignmentsEliminationTest {

  private AbstractCompiler compiler;
  private DeadAssignmentsElimination deadAssignmentsElimination;

  @Before
  public void setUp() {
    compiler = new Compiler();
    deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
  }

  @Test
  public void testProcess() {
    Node externs = new Node(Token.SCRIPT);
    Node root = new Node(Token.SCRIPT);
    deadAssignmentsElimination.process(externs, root);
    // TODO: Add assertions
  }

  @Test
  public void testEnterScope() {
    NodeTraversal t = new NodeTraversal(compiler, null);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(new Node(Token.SCRIPT));
    t.setControlFlowGraph(cfg);
    t.getScopeCreator().enterScope(new Node(Token.SCRIPT), null);
    deadAssignmentsElimination.enterScope(t);
    // TODO: Add assertions
  }

  @Test
  public void testExitScope() {
    NodeTraversal t = new NodeTraversal(compiler, null);
    deadAssignmentsElimination.exitScope(t);
    // TODO: Add assertions
  }

  @Test
  public void testVisit() {
    NodeTraversal t = new NodeTraversal(compiler, null);
    Node n = new Node(Token.SCRIPT);
    Node parent = new Node(Token.SCRIPT);
    deadAssignmentsElimination.visit(t, n, parent);
    // TODO: Add assertions
  }

  @Test
  public void testTryRemoveDeadAssignments() {
    NodeTraversal t = new NodeTraversal(compiler, null);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(new Node(Token.SCRIPT));
    deadAssignmentsElimination.tryRemoveDeadAssignments(t, cfg);
    // TODO: Add assertions
  }

  @Test
  public void testTryRemoveAssignment() {
    NodeTraversal t = new NodeTraversal(compiler, null);
    Node n = new Node(Token.SCRIPT);
    Node exprRoot = new Node(Token.SCRIPT);
    FlowState<LiveVariableLattice> state = new FlowState<>();
    deadAssignmentsElimination.tryRemoveAssignment(t, n, exprRoot, state);
    // TODO: Add assertions
  }

  @Test
  public void testIsVariableStillLiveWithinExpression() {
    Node n = new Node(Token.SCRIPT);
    Node exprRoot = new Node(Token.SCRIPT);
    String variable = "x";
    boolean result = deadAssignmentsElimination.isVariableStillLiveWithinExpression(n, exprRoot, variable);
    assertTrue(result);
  }

  @Test
  public void testIsVariableReadBeforeKill() {
    Node n = new Node(Token.SCRIPT);
    String variable = "x";
    VariableLiveness result = deadAssignmentsElimination.isVariableReadBeforeKill(n, variable);
    assertEquals(VariableLiveness.MAYBE_LIVE, result);
  }
}