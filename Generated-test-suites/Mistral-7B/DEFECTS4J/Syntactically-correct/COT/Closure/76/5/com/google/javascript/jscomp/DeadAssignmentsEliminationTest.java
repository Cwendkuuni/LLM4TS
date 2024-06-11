package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.DataFlowAnalysis.FlowState;
import com.google.javascript.jscomp.LiveVariablesAnalysis.LiveVariableLattice;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.NodeTraversal.NodeTraversal;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.graph.DiGraph.DiGraphNode;
import com.google.javascript.rhino.Token;

import org.junit.Test;

/**
 * Tests for the DeadAssignmentsElimination class.
 */
public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() {
    
    // Given
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ControlFlowGraph cfg = mock(ControlFlowGraph.class);
    LiveVariablesAnalysis liveness = mock(LiveVariablesAnalysis.class);
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    when(compiler.getControlFlowGraph()).thenReturn(cfg);
    when(compiler.reportCodeChange()).thenReturn(true);
    when(liveness.analyze()).thenReturn(true);

    Node root = Node.newProgram(1);

    // When
    deadAssignmentsElimination.process(null, root);

    // Then
    assertEquals(0, root.getChildCount());
    

    // Given
    AbstractCompiler compiler2 = mock(AbstractCompiler.class);
    ControlFlowGraph cfg2 = mock(ControlFlowGraph.class);
    LiveVariablesAnalysis liveness2 = mock(LiveVariablesAnalysis.class);
    DeadAssignmentsElimination deadAssignmentsElimination2 =
        new DeadAssignmentsElimination(compiler2);

    when(compiler2.getControlFlowGraph()).thenReturn(cfg2);
    when(compiler2.reportCodeChange()).thenReturn(true);
    when(liveness2.analyze()).thenReturn(false);

    Node root2 = Node.newProgram(1);

    // When
    deadAssignmentsElimination2.process(null, root2);

    // Then
    assertEquals(0, root2.getChildCount());

    // Given
    AbstractCompiler compiler3 = mock(AbstractCompiler.class);
    ControlFlowGraph cfg3 = mock(ControlFlowGraph.class);
    LiveVariablesAnalysis liveness3 = mock(LiveVariablesAnalysis.class);
    DeadAssignmentsElimination deadAssignmentsElimination3 =
        new DeadAssignmentsElimination(compiler3);

    when(compiler3.getControlFlowGraph()).thenReturn(cfg3);
    when(compiler3.reportCodeChange()).thenReturn(true);
    when(liveness3.analyze()).thenReturn(true);

    Node root3 = Node.newProgram(1);

    DiGraphNode<Node, Branch> node1 = new DiGraphNode<>(new Node(Token.ASSIGN, null), null);
    node1.setValue(new Node(Token.ASSIGN, null));
    when(cfg3.getDirectedGraphNodes()).thenReturn(new Node[] {node1});
    when(cfg3.getControlFlowGraphNodes()).thenReturn(new Node[] {node1.getValue()});

    // When
    deadAssignmentsElimination3.process(null, root3);

    // Then
    assertEquals(0, root3.getChildCount());

    // Given
    AbstractCompiler compiler4 = mock(AbstractCompiler.class);
    ControlFlowGraph cfg4 = mock(ControlFlowGraph.class);
    LiveVariablesAnalysis liveness4 = mock(LiveVariablesAnalysis.class);
    DeadAssignmentsElimination deadAssignmentsElimination4 =
        new DeadAssignmentsElimination(compiler4);

    when(compiler4.getControlFlowGraph()).thenReturn(cfg4);
    when(compiler4.reportCodeChange()).thenReturn(true);
    when(liveness4.analyze()).thenReturn(true);

    Node root4 = Node.newProgram(1);

    DiGraphNode<Node, Branch> node2 = new DiGraphNode<>(new Node(Token.ASSIGN, null), null);
    node2.setValue(new Node(Token.ASSIGN, null));
    DiGraphNode<Node, Branch> node3 = new DiGraphNode<>(new Node(Token.ASSIGN, null), null);
    node3.setValue(new Node(Token.ASSIGN, null));
    Branch branch = new Branch(node1, node2, null);
    when(cfg4.getDirectedGraphNodes()).thenReturn(new Node[] {node1, node2, node3});
    when(cfg4.getControlFlowGraphNodes()).thenReturn(new Node[] {node1.getValue(), node2.getValue(), node3.getValue()});
    when(cfg4.getBranch(node1.getValue(), node2.getValue())).thenReturn(branch);

    // When
    deadAssignmentsElimination4.process(null, root4);

    // Then
    assertEquals(0, root4.getChildCount());

    // Given
    AbstractCompiler compiler5 = mock(AbstractCompiler.class);
    ControlFlowGraph cfg5 = mock(ControlFlowGraph.class);
    LiveVariablesAnalysis liveness5 = mock(LiveVariablesAnalysis.class);
    DeadAssignmentsElimination deadAssignmentsElimination5 =
        new DeadAssignmentsElimination(compiler5);

    when(compiler5.getControlFlowGraph()).thenReturn(cfg5);
    when(compiler5.reportCodeChange()).thenReturn(true);
    when(liveness5.analyze()).thenReturn(true);

    Node root5 = Node.newProgram(1);

    DiGraphNode<Node, Branch> node4 = new DiGraphNode<>(new Node(Token.ASSIGN, null), null);
    node4.setValue(new Node(Token.ASSIGN, null));
    DiGraphNode<Node, Branch> node5 = new DiGraphNode<>(new Node(Token.ASSIGN, null), null);
    node5.setValue(new Node(Token.ASSIGN, null));
    Branch branch2 = new Branch(node2, node3, null);
    when(cfg5.getDirectedGraphNodes()).thenReturn(new Node[] {node1, node2, node3, node4, node5});
    when(cfg5.getControlFlowGraphNodes()).thenReturn(new Node[] {node1.getValue(), node2.getValue(), node3.getValue(), node4.getValue(), node5.getValue()});
    when(cfg5.getBranch(node3.getValue(), node4.getValue())).thenReturn(branch2);

    // When
    deadAssignmentsElimination5.process(null, root5);

    // Then
    assertEquals(0, root5.getChildCount());

    // Given
    AbstractCompiler compiler6 = mock(AbstractCompiler.class);
    ControlFlowGraph cfg6 = mock(ControlFlowGraph.class);
    LiveVariablesAnalysis liveness6 = mock(LiveVariablesAnalysis.class);
    DeadAssignmentsElimination deadAssignmentsElimination6 =
        new DeadAssignmentsElimination(compiler6);

    when(compiler6.getControlFlowGraph()).thenReturn(cfg6);
    when(compiler6.reportCodeChange()).thenReturn(true);
    when(liveness6.analyze()).thenReturn(true);

    Node root6 = Node.newProgram(1);

    DiGraphNode<Node, Branch> node6 = new DiGraphNode<>(new Node(Token.ASSIGN, null), null);
    node6.setValue(new Node(Token.ASSIGN, null));
    when(cfg6.getDirectedGraphNodes()).thenReturn(new Node[] {node6});
    when(cfg6.getControlFlowGraphNodes()).thenReturn(new Node[] {node6.getValue()});

    // When
    try {
      deadAssignmentsElimination6.process(null, root6);
    } catch (RuntimeException e) {
      // Expected exception.
    }

    // Then
    assertEquals(0, root6.getChildCount());
  }
}