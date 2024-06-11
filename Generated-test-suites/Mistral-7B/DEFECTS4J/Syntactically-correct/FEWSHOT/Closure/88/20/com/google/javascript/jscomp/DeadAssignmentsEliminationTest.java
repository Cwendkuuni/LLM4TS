package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;

import org.junit.Test;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis.FlowState;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraphNode;

import java.util.Collections;
import java.util.List;

public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() {
    AbstractCompiler compiler = new AbstractCompiler();
    String code = "function test() {" +
        "  var a = 1, b = 2, c = 3;" +
        "  if (a && (a = b) && (a = c)) {" +
        "    // Dead assignment to a." +
        "  }" +
        "  return a;" +
        "}";

    Node root = NodePrinter.parseJs(code, null);
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);

    ControlFlowGraph<Node> cfg = NodeTraversal.getOrCreateControlFlowGraph(root);
    for (DiGraphNode<Node, Branch> node : cfg.getDirectedGraphNodes()) {
      FlowState<DeadAssignmentsElimination.LiveVariableLattice> state =
          node.getAnnotation();
      Node n = node.getValue();
      if (n == null) {
        continue;
      }
      System.out.println("State for node: " + node + " : " + state);
      if (n.getType() == Token.IF || n.getType() == Token.WHILE) {
        continue;
      }

      if (n.getType() == Token.RETURN) {
        assertEquals(ImmutableList.of(n.getFirstChild()),
            Collections.singletonList(n.getFirstChild()));
        continue;
      }

      List<Node> assignmentsBefore = null;
      for (Node child = n; child != null; child = child.getPrevious()) {
        if (child.getType() == Token.ASSIGN ||
            child.getType() == Token.INC || child.getType() == Token.DEC) {
          if (assignmentsBefore == null) {
            assignmentsBefore = ImmutableList.of(child);
          } else {
            assignmentsBefore =
                ImmutableList.<Node>builder()
                    .addAll(assignmentsBefore)
                    .add(child)
                    .build();
          }
        }
        if (child.getType() == Token.IF || child.getType() == Token.WHILE) {
          break;
        }
      }

      if (assignmentsBefore == null) {
        continue;
      }

      String varName = "";
      for (Node child = n; child != null; child = child.getPrevious()) {
        if (child.getType() == Token.NAME) {
          varName = child.getString();
          break;
        }
      }

      assertFalse(state.getOut().isLive(new DeadAssignmentsElimination.Var(varName, null)));
      for (Node assign : assignmentsBefore) {
        assertSame(assign, n.getParent().getChild(assign.getIndex()));
      }
    }
  }
}