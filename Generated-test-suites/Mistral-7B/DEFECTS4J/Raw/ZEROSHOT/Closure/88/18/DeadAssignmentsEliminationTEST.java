package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertTrue;

import com.google.common.base.Predicates;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractNodeTraversal;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.DataFlowAnalysis.FlowState;
import com.google.javascript.jscomp.LiveVariablesAnalysis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() {
    AbstractCompiler compiler = new AbstractCompiler();
    String sourceCode = "function foo() {" +
        "  var a = 1;" +
        "  var b = 2;" +
        "  var c = 3;" +
        "  a = b;" +
        "  c = a;" +
        "  return a; " +
        "}";

    Node root = AbstractNodeTraversal.parseSingleFile(compiler, sourceCode, "foo");
    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(compiler.getExterns(), root);

    // Check that the dead assignments have been removed.
    Node foo = root.getFirstChild();
    Node aAssignment = NodeUtil.getFirstAssignee(foo, "a");
    assertTrue(aAssignment == null);
    Node cAssignment = NodeUtil.getFirstAssignee(foo, "c");
    assertTrue(cAssignment == null);

    // Check that the variable assignments have not affected the control flow.
    ControlFlowGraph<Node> controlFlowGraph = foo.getAnnotation();
    DiGraph<Node, Branch> graph = controlFlowGraph.getDirectedGraph();
    assertTrue(graph.getNodes().size() == 4);
    for (Node node : graph.getNodes()) {
      FlowState<LiveVariablesAnalysis.LiveVariableLattice> state = node.getAnnotation();
      if (state != null) {
        assertTrue(state.getIn().getLiveVariables().size() == 3);
        assertTrue(state.getOut().getLiveVariables().size() == 3);
      }
    }

    // Test with a function that does not contain any dead assignments.
    sourceCode = "function foo() {" +
        "  var a = 1;" +
        "  var b = 2;" +
        "  var c = 3;" +
        "  var d = 4;" +
        "  a = b;" +
        "  c = a;" +
        "  d = a;" +
        "  return a; " +
        "}";

    root = AbstractNodeTraversal.parseSingleFile(compiler, sourceCode, "foo");
    eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(compiler.getExterns(), root);

    // Check that no dead assignments have been removed.
    foo = root.getFirstChild();
    aAssignment = NodeUtil.getFirstAssignee(foo, "a");
    assertTrue(aAssignment != null);
    cAssignment = NodeUtil.getFirstAssignee(foo, "c");
    assertTrue(cAssignment != null);
    Node dAssignment = NodeUtil.getFirstAssignee(foo, "d");
    assertTrue(dAssignment != null);
  }
}