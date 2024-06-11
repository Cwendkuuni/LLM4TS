package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.LiveVariablesAnalysis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class DeadAssignmentsEliminationTest {

  private AbstractCompiler compiler;
  private ControlFlowGraph<Node> controlFlowGraph;
  private Node rootNode;
  private Scope scope;
  private DeadAssignmentsElimination deadAssignmentsElimination;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    controlFlowGraph = mock(ControlFlowGraph.class);
    scope = mock(Scope.class);
    deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);

    when(controlFlowGraph.getDirectedGraphNodes()).thenReturn(Mockito.mock(Iterable.class));
  }

  @Test
  public void testProcess_EmptyProgram() {
    Node emptyProgram = Node.newText("");
    deadAssignmentsElimination.process(null, emptyProgram);
    // No actions expected here
  }

  @Test
  public void testProcess_ProgramWithDeadAssignments() {
    Node root = Node.newBlock("root");
    Node xAssignment = Node.newAssignment("x", Node.newNumber(1));
    Node yAssignment = Node.newAssignment("y", Node.newNumber(1));
    Node deadAssignment = Node.newAssignment("x", Node.newNumber(2));
    root.addChild(xAssignment);
    root.addChild(yAssignment);
    root.addChild(deadAssignment);

    when(controlFlowGraph.getDirectedGraphNodes()).thenReturn(Mockito.mock(Iterable.class));
    when(controlFlowGraph.getDirectedGraphNodes()).thenAnswer(invocation -> Mockito.mock(DiGraphNode.class));

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    deadAssignmentsElimination.process(null, root);
    NodeTraversal argument = ArgumentCaptor.forClass(NodeTraversal.class);
    Mockito.verify(compiler).addPass(argument.capture());
    AbstractPostOrderCallback callback = argument.getValue();
    Mockito.verify(compiler).addCallback(DeadAssignmentsElimination.class, callback);

    // Verify that deadAssignment is removed
    Node newRoot = Node.newBlock("root");
    newRoot.addChild(xAssignment);
    newRoot.addChild(yAssignment);
    Mockito.verify(compiler).process(newRoot);
  }

  @Test
  public void testProcess_ProgramWithDeadAssignmentsButNotEliminated() {
    Node root = Node.newBlock("root");
    Node xAssignment = Node.newAssignment("x", Node.newNumber(1));
    Node yAssignment = Node.newAssignment("y", Node.newNumber(1));
    Node deadAssignment = Node.newAssignment("x", Node.newNumber(2));
    root.addChild(xAssignment);
    root.addChild(yAssignment);
    root.addChild(deadAssignment);

    when(controlFlowGraph.getDirectedGraphNodes()).thenReturn(Mockito.mock(Iterable.class));
    when(controlFlowGraph.getDirectedGraphNodes()).thenAnswer(invocation -> Mockito.mock(DiGraphNode.class));

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    DeadAssignmentsElimination deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);
    NodeTraversal argument = ArgumentCaptor.forClass(NodeTraversal.class);
    Mockito.verify(compiler).addPass(argument.capture());
    AbstractPostOrderCallback callback = argument.getValue();
    Mockito.verify(compiler).addCallback(DeadAssignmentsElimination.class, callback);

    // Verify that deadAssignment is not removed
    Node newRoot = Node.newBlock("root");
    newRoot.addChild(xAssignment);
    newRoot.addChild(yAssignment);
    newRoot.addChild(deadAssignment);
    Mockito.verify(compiler).process(newRoot);
    Mockito.verify(compiler, Mockito.never()).reportCodeChange();
  }

  @Test
  public void testTryRemoveAssignment_NoDeadAssignment() {
    Node root = Node.newBlock("root");
    Node xAssignment = Node.newAssignment("x", Node.newNumber(1));
    root.addChild(xAssignment);

    FlowState<LiveVariableLattice> state = Mockito.mock(FlowState.class);
    ControlFlowGraph<Node> cfg = mock(ControlFlowGraph.class);

    DeadAssignmentsElimination deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.tryRemoveDeadAssignments(mock(NodeTraversal.class), cfg);

    // No actions expected here
  }

  @Test
  public void testTryRemoveAssignment_DeadAssignmentButNotEliminated() {
    Node root = Node.newBlock("root");
    Node xAssignment = Node.newAssignment("x", Node.newNumber(1));
    Node deadAssignment = Node.newAssignment("x", Node.newNumber(2));
    root.addChild(xAssignment);
    root.addChild(deadAssignment);

    FlowState<LiveVariableLattice> state = Mockito.mock(FlowState.class);
    ControlFlowGraph<Node> cfg = mock(ControlFlowGraph.class);

    when(state.getOut().isLive(Mockito.any())).thenReturn(true);

    DeadAssignmentsElimination deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.tryRemoveAssignment(mock(NodeTraversal.class), Node.newNumber(1), state);

    // No actions expected here
  }

  @Test
  public void testTryRemoveAssignment_DeadAssignmentEliminated() {
    Node root = Node.newBlock("root");
    Node xAssignment = Node.newAssignment("x", Node.newNumber(1));
    Node deadAssignment = Node.newAssignment("x", Node.newNumber(2));
    root.addChild(xAssignment);
    root.addChild(deadAssignment);

    FlowState<LiveVariableLattice> state = Mockito.mock(FlowState.class);
    ControlFlowGraph<Node> cfg = mock(ControlFlowGraph.class);
    when(state.getOut().isLive(Mockito.any())).thenReturn(false);

    DeadAssignmentsElimination deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.tryRemoveAssignment(mock(NodeTraversal.class), Node.newNumber(1), state);

    Node newRoot = Node.newBlock("root");
    newRoot.addChild(xAssignment);

    Mockito.verify(compiler).process(newRoot);
    Mockito.verify(compiler).reportCodeChange();
  }
}