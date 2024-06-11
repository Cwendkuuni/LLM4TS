package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Predicate;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis.FlowState;
import com.google.javascript.jscomp.LiveVariablesAnalysis.LiveVariableLattice;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.jscomp.graph.DiGraphNode;
import com.google.javascript.rhino.NodeTraversal.ScopedCallback;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;

public class DeadAssignmentsEliminationTest {

  private AbstractCompiler compiler;
  private ControlFlowGraph<Node> controlFlowGraph;
  private LiveVariablesAnalysis liveVariablesAnalysis;
  private DeadAssignmentsElimination deadAssignmentsElimination;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class, Answers.RETURNS_DEEP_STUBS);
    controlFlowGraph = mock(ControlFlowGraph.class);
    liveVariablesAnalysis = mock(LiveVariablesAnalysis.class);
    when(compiler.getControlFlowGraph()).thenReturn(controlFlowGraph);
    deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
  }

  @Test
  public void testDeadAssignmentsElimination_SimpleAssignment() {
    Node root = Node.newText("x = 1;");
    NodeTraversal nodeTraversal = new NodeTraversal(root, null, null, null);

    deadAssignmentsElimination.enterScope(nodeTraversal);
    deadAssignmentsElimination.tryRemoveDeadAssignments(nodeTraversal, controlFlowGraph);
    deadAssignmentsElimination.exitScope(nodeTraversal);

    Node expectedResult = Node.newText("1;");
    Node actualResult = nodeTraversal.getScopeRoot();

    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @Test
  public void testDeadAssignmentsElimination_AssignmentsInsideIfStatement() {
    Node root = Node.newIsOr(
        Node.newText("if (x > 5)"), Node.newText("x = 1;"));
    Node condition = Node.newText("x > 5");
    Node assign = Node.newAssign(Node.newName("x"), new Node(Token.NUMBER, 1));

    NodeTraversal nodeTraversal = new NodeTraversal(root, null, null, null);

    DiGraph<Node, Branch> controlFlowGraph = new DiGraph<>();
    DiGraphNode<Node, Branch> startNode = controlFlowGraph.addNode(root);
    DiGraphNode<Node, Branch> ifNode = controlFlowGraph.addNode(condition);
    DiGraphNode<Node, Branch> thenNode = controlFlowGraph.addNode(assign);
    DiGraphNode<Node, Branch> endNode = controlFlowGraph.addNode(Node.newText(""));

    when(controlFlowGraph.getDirectedGraphNodes()).thenReturn(
        controlFlowGraph.getNodes().values());

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    deadAssignmentsElimination.enterScope(nodeTraversal);
    deadAssignmentsElimination.process(null, root);
    deadAssignmentsElimination.exitScope(nodeTraversal);

    FlowState<LiveVariableLattice> state = new FlowState<>();
    state.getIn().setValue(new LiveVariableLattice(null, null, null));
    state.getOut().setValue(new LiveVariableLattice(null, null, null));

    deadAssignmentsElimination.tryRemoveDeadAssignments(nodeTraversal, controlFlowGraph);

    Node expectedResult = Node.newText("if (x > 5)");
    Node actualResult = nodeTraversal.getScopeRoot();

    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @Test(expected = AssertionError.class)
  public void testDeadAssignmentsElimination_AssignmentInsideForStatement() {
    Node root = Node.newText("for (var i = 0; i < 10; i++) { i = 1; }");
    NodeTraversal nodeTraversal = new NodeTraversal(root, null, null, null);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    deadAssignmentsElimination.enterScope(nodeTraversal);
    deadAssignmentsElimination.process(null, root);
    deadAssignmentsElimination.exitScope(nodeTraversal);
  }

  @Test
  public void testDeadAssignmentsElimination_AssignmentInsideFunction() {
    Node root = Node.newText("function test() { var x = 1; x = 2; }");
    NodeTraversal nodeTraversal = new NodeTraversal(root, null, null, null);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    deadAssignmentsElimination.enterScope(nodeTraversal);
    deadAssignmentsElimination.process(null, root);
    deadAssignmentsElimination.exitScope(nodeTraversal);

    Node expectedResult = Node.newText("function test() { }");
    Node actualResult = nodeTraversal.getScopeRoot();

    assertThat(actualResult).isEqualTo(expectedResult);
  }
}