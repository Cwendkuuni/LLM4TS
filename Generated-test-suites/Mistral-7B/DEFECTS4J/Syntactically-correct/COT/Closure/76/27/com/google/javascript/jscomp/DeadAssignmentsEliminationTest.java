package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeOperations;

import org.junit.Before;
import org.junit.Test;

public class DeadAssignmentsEliminationTest {
  private AbstractCompiler compiler;
  private DeadAssignmentsElimination deadAssignmentsElimination;
  private NodeTraversal nodeTraversal;

  @Before
  public void setUp() {
    compiler = new Compiler();
    deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
    nodeTraversal = new NodeTraversal(compiler);
  }

  @Test
  public void testBasicFunctionality() {
    String sourceCode = "function f() { var x = 1; x = 2; }";
    Node externs = NodeOperations.parse(sourceCode, null);
    Node root = NodeOperations.parseFunctionExpression(sourceCode, null);
    deadAssignmentsElimination.process(externs, root);
    nodeTraversal.traverse(compiler, root, deadAssignmentsElimination);
    assertTrue(nodeTraversal.getScopeRoot().getLastChild().isMissing());
  }

  @Test
  public void testBasicFunctionalityWithReturnStatement() {
    String sourceCode = "function f() { var x = 1; x = 2; return x; }";
    Node externs = NodeOperations.parse(sourceCode, null);
    Node root = NodeOperations.parseFunctionExpression(sourceCode, null);
    deadAssignmentsElimination.process(externs, root);
    nodeTraversal.traverse(compiler, root, deadAssignmentsElimination);
    assertFalse(nodeTraversal.getScopeRoot().getLastChild().isMissing());
  }

  @Test
  public void testBasicFunctionalityWithConditionals() {
    String sourceCode = "function f() { var x = 1; if (true) { x = 2; } else { x = 3; } }";
    Node externs = NodeOperations.parse(sourceCode, null);
    Node root = NodeOperations.parseFunctionExpression(sourceCode, null);
    deadAssignmentsElimination.process(externs, root);
    nodeTraversal.traverse(compiler, root, deadAssignmentsElimination);
    assertTrue(nodeTraversal.getScopeRoot().getLastChild().isMissing());
  }

  @Test
  public void testBasicFunctionalityWithLoops() {
    String sourceCode = "function f() { var x = 1; for (var i = 0; i < 10; i++) { x = 2; } }";
    Node externs = NodeOperations.parse(sourceCode, null);
    Node root = NodeOperations.parseFunctionExpression(sourceCode, null);
    deadAssignmentsElimination.process(externs, root);
    nodeTraversal.traverse(compiler, root, deadAssignmentsElimination);
    assertTrue(nodeTraversal.getScopeRoot().getLastChild().isMissing());
  }

  @Test
  public void testBasicFunctionalityWithAssignmentInBranch() {
    String sourceCode = "function f() { var x = 1; if (x = 2) { x = 3; } }";
    Node externs = NodeOperations.parse(sourceCode, null);
    Node root = NodeOperations.parseFunctionExpression(sourceCode, null);
    deadAssignmentsElimination.process(externs, root);
    nodeTraversal.traverse(compiler, root, deadAssignmentsElimination);
    assertFalse(nodeTraversal.getScopeRoot().getLastChild().isMissing());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullArguments() {
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullScope() {
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    Scope scope = null;
    Node node = NodeOperations.parse("var x = 1;", null);
    deadAssignmentsElimination.enterScope(NodeTraversal.create(compiler));
    deadAssignmentsElimination.exitScope(NodeTraversal.create(compiler));
    deadAssignmentsElimination.tryRemoveDeadAssignments(
        NodeTraversal.create(compiler), null);
    deadAssignmentsElimination.tryRemoveAssignment(null, node, null);
  }
}