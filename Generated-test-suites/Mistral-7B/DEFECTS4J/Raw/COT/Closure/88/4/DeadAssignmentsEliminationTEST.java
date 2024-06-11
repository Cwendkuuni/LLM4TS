package com.google.javascript.jscomp;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.DataFlowAnalysis.FlowState;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.Graph.DiGraphNode;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope.Var;

@RunWith(JUnit4.class)
public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() {
    // Given
    AbstractCompiler compiler = new Compiler();
    Node root = Node.newProgram(Node.newText(""));
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    // When
    deadAssignmentsElimination.process(root, root);

    // Then
    NodeTraversal nodeTraversal = new NodeTraversal(root);
    NodeTraversal.traverse(compiler, root, nodeTraversal);
    DiGraphNode<Node, Branch> startNode = nodeTraversal.getControlFlowGraph()
        .getEntryNode();
    FlowState<DeadAssignmentsElimination.LiveVariableLattice> state =
        startNode.getAnnotation();

    // Test method with no dead assignments
    testNoDeadAssignments(nodeTraversal, state);

    // Test method with dead assignments
    testDeadAssignments(nodeTraversal, state);
  }

  /**
   * Test method with no dead assignments.
   *
   * @param nodeTraversal Node traversal object
   * @param state Flow state with liveness information
   */
  private void testNoDeadAssignments(NodeTraversal nodeTraversal,
      FlowState<DeadAssignmentsElimination.LiveVariableLattice> state) {
    Node currentNode = nodeTraversal.getCurrentNode();
    // No dead assignments should not remove any assignments
    assertTrue("No dead assignments should not remove any assignments",
        currentNode.getChildCount() > 0);
  }

  /**
   * Test method with dead assignments.
   *
   * @param nodeTraversal Node traversal object
   * @param state Flow state with liveness information
   */
  private void testDeadAssignments(NodeTraversal nodeTraversal,
      FlowState<DeadAssignmentsElimination.LiveVariableLattice> state) {
    Node currentNode = nodeTraversal.getCurrentNode();

    // Dead assignments should remove dead assignments
    String varName = "deadVar";
    Node assignmentNode = new Node(Token.ASSIGN, null, new Node(Token.NAME, varName));
    Node deadAssignmentNode = new Node(Token.ASSIGN, null, new Node(Token.NAME, varName));

    if (currentNode.getChildCount() > 0) {
      Node previousNode = currentNode.getPrevious();
      previousNode.replaceChild(previousNode.getLastChild(), assignmentNode);
      previousNode.replaceChild(assignmentNode, deadAssignmentNode);
    }

    nodeTraversal.next();

    // Dead assignment should be removed
    assertTrue("Dead assignment should be removed", currentNode.getChildCount() == 0);
  }
}