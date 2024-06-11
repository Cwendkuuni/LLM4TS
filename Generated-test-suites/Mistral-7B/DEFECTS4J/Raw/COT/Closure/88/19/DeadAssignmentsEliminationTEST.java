package com.google.javascript.jscomp;

import com.google.common.base.Preconditions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static com.google.javascript.rhino.NodeUtil.has;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() {
    // Setup
    AbstractCompiler compilerMock = Mockito.mock(AbstractCompiler.class);
    LiveVariablesAnalysis livenessMock = Mockito.mock(LiveVariablesAnalysis.class);
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compilerMock);

    MockitoAnnotations.initMocks(this);

    // Test constructor
    Preconditions.checkNotNull(deadAssignmentsElimination);

    // Test process method
    Node externs = Mockito.mock(Node.class);
    Node root = Mockito.mock(Node.class);
    deadAssignmentsElimination.process(externs, root);
    Mockito.verify(compilerMock).traverse(externs, root, deadAssignmentsElimination);

    // Test enterScope method with no dead assignments
    NodeTraversal mockNodeTraversal = Mockito.mock(NodeTraversal.class);
    Node fnBlock = Mockito.mock(Node.class);
    Mockito.when(fnBlock.getLastChild()).thenReturn(null);
    Mockito.when(fnBlock.hasChildren()).thenReturn(false);
    Mockito.when(fnBlock.getParent()).thenReturn(null);
    Mockito.when(fnBlock.getType()).thenReturn(Token.BLOCK);
    Mockito.when(mockNodeTraversal.getScopeRoot()).thenReturn(null);
    Mockito.when(mockNodeTraversal.getControlFlowGraph()).thenReturn(null);
    Mockito.when(mockNodeTraversal.getScope()).thenReturn(null);
    Mockito.when(mockNodeTraversal.getScope().getVar(Mockito.anyString()))
        .thenReturn(null);
    Mockito.when(mockNodeTraversal.getScope().isDeclared(Mockito.anyString(),
        Mockito.anyBoolean())).thenReturn(false);

    deadAssignmentsElimination.enterScope(mockNodeTraversal);
    Mockito.verify(livenessMock, Mockito.never()).analyze();
    Mockito.verify(compilerMock, Mockito.never()).reportCodeChange();

    // Test enterScope method with dead assignments
    Mockito.when(fnBlock.getLastChild()).thenReturn(Mockito.mock(Node.class));
    Mockito.when(fnBlock.hasChildren()).thenReturn(true);
    Mockito.when(fnBlock.getParent()).thenReturn(null);
    Mockito.when(fnBlock.getType()).thenReturn(Token.BLOCK);
    Mockito.when(mockNodeTraversal.getScopeRoot()).thenReturn(null);
    Mockito.when(mockNodeTraversal.getControlFlowGraph()).thenReturn(null);
    Mockito.when(mockNodeTraversal.getScope()).thenReturn(null);
    Mockito.when(mockNodeTraversal.getScope().getVar(Mockito.anyString()))
        .thenReturn(Mockito.mock(Var.class));
    Mockito.when(mockNodeTraversal.getScope().isDeclared(Mockito.anyString(),
        Mockito.anyBoolean())).thenReturn(true);
    Mockito.when(mockNodeTraversal.getScope().getVar(Mockito.anyString()))
        .thenReturn(Mockito.argThat(var -> var.name.equals("x")));

    Node assignment1 = Mockito.mock(Node.class);
    Node assignment2 = Mockito.mock(Node.class);
    Node deadAssignment = Mockito.mock(Node.class);
    Mockito.when(assignment1.getType()).thenReturn(Token.ASSIGN);
    Mockito.when(assignment1.getFirstChild()).thenReturn(Mockito.mock(Node.class));
    Mockito.when(assignment1.getLastChild()).thenReturn(assignment2);
    Mockito.when(assignment2.getType()).thenReturn(Token.NAME);
    Mockito.when(assignment2.getString()).thenReturn("x");
    Mockito.when(deadAssignment.getType()).thenReturn(Token.ASSIGN);
    Mockito.when(deadAssignment.getFirstChild()).thenReturn(assignment1);
    Mockito.when(deadAssignment.getLastChild()).thenReturn(Mockito.mock(Node.class));

    Mockito.when(fnBlock.getLastChild()).thenReturn(deadAssignment);
    Mockito.when(fnBlock.getLastChild().getParent()).thenReturn(fnBlock);

    deadAssignmentsElimination.enterScope(mockNodeTraversal);
    Mockito.verify(livenessMock).analyze();
    Mockito.verify(compilerMock).reportCodeChange();
    Mockito.verify(fnBlock, Mockito.times(1)).removeChild(assignment1);
    Mockito.verify(fnBlock, Mockito.times(1)).replaceChild(deadAssignment, assignment1);

    // Test exitScope method
    deadAssignmentsElimination.exitScope(mockNodeTraversal);
    Mockito.verifyNoMoreInteractions(livenessMock, compilerMock, mockNodeTraversal, fnBlock, assignment1, assignment2, deadAssignment);

    // Test visit method
    Node invalidNode = Mockito.mock(Node.class);
    deadAssignmentsElimination.visit(mockNodeTraversal, invalidNode, null);
    Mockito.verifyNoMoreInteractions(livenessMock, compilerMock, mockNodeTraversal, invalidNode);

    // Test tryRemoveDeadAssignments method with empty graph
    ControlFlowGraph<Node> emptyGraph = Mockito.mock(ControlFlowGraph.class);
    deadAssignmentsElimination.tryRemoveDeadAssignments(mockNodeTraversal, emptyGraph);
    Mockito.verifyNoMoreInteractions(compilerMock);

    // Test tryRemoveDeadAssignments method with dead assignments
    DiGraphNode<Node, Branch> node1 = Mockito.mock(DiGraphNode.class);
    DiGraphNode<Node, Branch> node2 = Mockito.mock(DiGraphNode.class);
    ControlFlowGraph<Node> graph = Mockito.mock(ControlFlowGraph.class);
    Mockito.when(graph.getDirectedGraphNodes()).thenReturn(
        Mockito.list(node1, node2).iterator());
    Mockito.when(node1.getValue()).thenReturn(Mockito.mock(Node.class));
    Mockito.when(node1.getAnnotation()).thenReturn(null);
    Mockito.when(node2.getValue()).thenReturn(Mockito.mock(Node.class));
    Mockito.when(node2.getAnnotation()).thenReturn(
        Mockito.mock(FlowState.class));

    deadAssignmentsElimination.tryRemoveDeadAssignments(mockNodeTraversal, graph);
    Mockito.verify(compilerMock).reportCodeChange();
    Mockito.verify(node1.getValue(), Mockito.times(1)).removeChild(Mockito.anyNode());
    Mockito.verify(node1.getValue(), Mockito.times(1)).replaceChild(Mockito.anyNode(), Mockito.anyNode());
    Mockito.verify(node2.getValue(), Mockito.times(1)).removeChild(Mockito.anyNode());
  }
}