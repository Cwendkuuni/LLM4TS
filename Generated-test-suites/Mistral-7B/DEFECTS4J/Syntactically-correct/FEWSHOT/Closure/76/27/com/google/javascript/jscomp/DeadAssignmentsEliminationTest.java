package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis.FlowState;
import com.google.javascript.jscomp.LiveVariablesAnalysis.LiveVariableLattice;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraphNode;
import com.google.javascript.rhino.Token;

public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() {
    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    Node root = Node.newProgram(
        Node.newText("function f() {" +
            "  var x = 1; " +
            "  x = 2; " +
            "  x = 3;" +
            "}"));

    deadAssignmentsElimination.process(null, root);

    // Check if the assignments to x are removed.
    NodeTraversal traversal = new NodeTraversal(root);
    Scope scope = traversal.getScope();
    Var x = scope.getVar("x");
    assertTrue(x != null);
    assertTrue(NodeUtil.isEmptyChildren(x.getInitializer()));
  }

  @Test
  public void testDeadAssignmentInsideIfStatement() {
    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    Node root = Node.newProgram(
        Node.newText("function f() {" +
            "  if (false) {" +
            "    var x = 1; " +
            "    x = 2;" +
            "  }" +
            "}"));

    deadAssignmentsElimination.process(null, root);

    // Check if the assignment to x is not removed because it's inside an if statement.
    NodeTraversal traversal = new NodeTraversal(root);
    Scope scope = traversal.getScope();
    Var x = scope.getVar("x");
    assertTrue(x != null);
    assertFalse(NodeUtil.isEmptyChildren(x.getInitializer()));
  }

  @Test
  public void testDeadAssignmentInsideLoop() {
    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    Node root = Node.newProgram(
        Node.newText("function f() {" +
            "  for (var i = 0; i < 10; i++) {" +
            "    var x = 1; " +
            "    x = 2;" +
            "  }" +
            "}"));

    deadAssignmentsElimination.process(null, root);

    // Check if the assignment to x is not removed because it's inside a loop.
    NodeTraversal traversal = new NodeTraversal(root);
    Scope scope = traversal.getScope();
    Var x = scope.getVar("x");
    assertTrue(x != null);
    assertFalse(NodeUtil.isEmptyChildren(x.getInitializer()));
  }

  @Test
  public void testDeadAssignmentInsideConditionExpression() {
    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    Node root = Node.newProgram(
        Node.newText("function f() {" +
            "  var x = 1; " +
            "  var y = (x = 2, x + 1);" +
            "}"));

    deadAssignmentsElimination.process(null, root);

    // Check if the assignment to x is not removed because it's inside a condition expression.
    NodeTraversal traversal = new NodeTraversal(root);
    Scope scope = traversal.getScope();
    Var x = scope.getVar("x");
    assertTrue(x != null);
    assertFalse(NodeUtil.isEmptyChildren(x.getInitializer()));
  }

  @Test
  public void testDeadAssignmentInsideFunctionCall() {
    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    Node root = Node.newProgram(
        Node.newText("function f() {" +
            "  function g() {" +
            "    var x = 1; " +
            "    x = 2;" +
            "  }" +
            "  g();" +
            "}"));

    deadAssignmentsElimination.process(null, root);

    // Check if the assignment to x is not removed because it's inside a function call.
    NodeTraversal traversal = new NodeTraversal(root);
    Scope scope = traversal.getScope();
    Var x = null;
    for (Scope innerScope = scope; innerScope != null; innerScope = innerScope.getOuterScope()) {
      for (Var variable : innerScope.getVariables()) {
        if ("x".equals(variable.name)) {
          x = variable;
          break;
        }
      }
      if (x != null) {
        break;
      }
    }
    assertTrue(x != null);
    assertFalse(NodeUtil.isEmptyChildren(x.getInitializer()));
  }

  @Test
  public void testDeadAssignmentsEliminationWithComplexControlFlow() {
    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    Node root = Node.newProgram(
        Node.newText("function f() {" +
            "  if (true) {" +
            "    if (false) {" +
            "      var x = 1; " +
            "      x = 2;" +
            "    } else {" +
            "      if (true) {" +
            "        var y = 1; " +
            "        y = 2;" +
            "      }" +
            "    }" +
            "  } else {" +
            "    var z = 1; " +
            "    z = 2;" +
            "  }" +
            "}"));

    deadAssignmentsElimination.process(null, root);

    // Check if the assignments to x, y, and z are removed if they're dead.
    NodeTraversal traversal = new NodeTraversal(root);
    Scope scope = traversal.getScope();
    Var x = scope.getVar("x");
    Var y = scope.getVar("y");
    Var z = scope.getVar("z");
    assertTrue(x != null);
    assertTrue(NodeUtil.isEmptyChildren(x.getInitializer()));
    assertTrue(y != null);
    assertTrue(NodeUtil.isEmptyChildren(y.getInitializer()));
    assertTrue(z != null);
    assertTrue(NodeUtil.isEmptyChildren(z.getInitializer()));
  }
}