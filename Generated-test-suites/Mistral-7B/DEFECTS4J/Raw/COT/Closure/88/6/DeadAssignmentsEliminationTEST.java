package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Predicate;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.AbstractVisitor;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.GraphTestUtils;
import com.google.javascript.jscomp.LiveVariablesAnalysis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.rhino.NodeTraversal.NodeWithSourceAndPosition;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DeadAssignmentsEliminationTest {

  private DeadAssignmentsElimination deadAssignmentsElimination;
  private AbstractCompiler compiler;
  private ControlFlowGraph<Node> controlFlowGraph;
  private LiveVariablesAnalysis liveVariablesAnalysis;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    controlFlowGraph = mock(ControlFlowGraph.class);
    liveVariablesAnalysis = mock(LiveVariablesAnalysis.class);
    deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
  }

  @Test
  public void testProcess_WhenCalled_CallsNodeTraversal() {
    Node root = createTestNode();
    deadAssignmentsElimination.process(null, root);
    NodeTraversal nodeTraversal = Mockito.capture(deadAssignmentsElimination.getTraversal());
    assertThat(nodeTraversal).isNotNull();
  }

  @Test
  public void testEnterScope_WhenCalledWithNonGlobalScope_PerformsChecks() {
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(nodeTraversal.getScope()).thenReturn(createMockScope());
    when(nodeTraversal.getControlFlowGraph()).thenReturn(controlFlowGraph);

    deadAssignmentsElimination.enterScope(nodeTraversal);

    // Check if enterScope is called only once
    Mockito.verify(nodeTraversal, Mockito.times(1)).enterScope(Mockito.any());

    // Check if necessary checks are performed
    Mockito.verify(controlFlowGraph, Mockito.times(1)).getDirectedGraphNodes();
    Mockito.verify(nodeTraversal, Mockito.times(1))
        .getScopeRoot()
        .getLastChild();
    Mockito.verify(nodeTraversal, Mockito.times(1))
        .getScopeRoot()
        .getLastChild()
        .hasChildren();
    Mockito.verify(nodeTraversal, Mockito.times(1))
        .getScopeRoot()
        .getLastChild()
        .getFirstChild();
    Mockito.verify(nodeTraversal, Mockito.times(1))
        .getScopeRoot()
        .getLastChild()
        .getFirstChild()
        .getType()
        .equals(Token.FOR);
    Mockito.verify(nodeTraversal, Mockito.times(1))
        .getScope()
        .isDeclared("x", false);
    Mockito.verify(nodeTraversal, Mockito.times(1))
        .getScope()
        .getVar("x");
  }

  @Test
  public void testEnterScope_WhenCalledWithGlobalScope_DoesNotPerformChecks() {
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(nodeTraversal.getScope()).thenReturn(createMockGlobalScope());

    deadAssignmentsElimination.enterScope(nodeTraversal);

    // Check if enterScope is called only once
    Mockito.verify(nodeTraversal, Mockito.times(1)).enterScope(Mockito.any());

    // Check if no unnecessary checks are performed
    Mockito.verify(controlFlowGraph, Mockito.never())
        .getDirectedGraphNodes();
    Mockito.verify(nodeTraversal, Mockito.never())
        .getScopeRoot()
        .getLastChild();
    Mockito.verify(nodeTraversal, Mockito.never())
        .getScopeRoot()
        .getLastChild()
        .getFirstChild();
    Mockito.verify(nodeTraversal, Mockito.never())
        .getScopeRoot()
        .getLastChild()
        .getFirstChild()
        .getType();
  }

  @Test
  public void testEnterScope_WhenCalledWithNoAssignableStatements_ExitsScope() {
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(nodeTraversal.getScope()).thenReturn(createMockScope());
    when(nodeTraversal.getControlFlowGraph()).thenReturn(controlFlowGraph);

    deadAssignmentsElimination.enterScope(nodeTraversal);
    deadAssignmentsElimination.exitScope(nodeTraversal);

    // Check if enterScope and exitScope are called once each
    Mockito.verify(nodeTraversal, Mockito.times(1)).enterScope(Mockito.any());
    Mockito.verify(nodeTraversal, Mockito.times(1)).exitScope(Mockito.any());
  }

  @Test
  public void testTryRemoveDeadAssignments_WhenEmptyGraph_DoesNotModifyGraph() {
    ControlFlowGraph<Node> emptyControlFlowGraph = mock(ControlFlowGraph.class);
    when(controlFlowGraph.getDirectedGraphNodes()).thenReturn(
        GraphTestUtils.emptyIterable());

    deadAssignmentsElimination.tryRemoveDeadAssignments(mock(NodeTraversal.class), emptyControlFlowGraph);

    Mockito.verify(emptyControlFlowGraph, Mockito.never()).removeNode(Mockito.any());
  }

  @Test
  public void testTryRemoveDeadAssignments_WhenGraphContainsDeadAssignment_RemovesAssignment() {
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    ControlFlowGraph<Node> controlFlowGraph = mock(ControlFlowGraph.class);
    when(nodeTraversal.getScope()).thenReturn(createMockScope());
    when(nodeTraversal.getControlFlowGraph()).thenReturn(controlFlowGraph);
    when(controlFlowGraph.getDirectedGraphNodes()).thenReturn(
        createTestControlFlowGraphNodes());

    deadAssignmentsElimination.enterScope(nodeTraversal);
    deadAssignmentsElimination.tryRemoveDeadAssignments(nodeTraversal, controlFlowGraph);
    deadAssignmentsElimination.exitScope(nodeTraversal);

    // Check if node was removed
    Node n = new Node(Token.ASSIGN, Node.newName("x"), Node.newNumber(0));
    Node expectedRemovedNode = new Node(Token.ASSIGN, Node.newName("x"), Node.newNumber(1));
    Node graphNode = Mockito.capture(controlFlowGraph.getNode(n));
    assertThat(graphNode).isNotNull();
    Mockito.verify(controlFlowGraph, Mockito.times(1)).removeNode(expectedRemovedNode);
  }

  @Test
  public void testIsVariableStillLiveWithinExpression_WhenVariableIsAlreadyDead_ReturnsFalse() {
    Node n = createTestNode();
    Node exprRoot = new Node(Token.ASSIGN, Node.newName("x"), Node.newNumber(0));

    boolean result = deadAssignmentsElimination.isVariableStillLiveWithinExpression(
        n, exprRoot, "x");

    assertThat(result).isFalse();
  }

  @Test
  public void testIsVariableStillLiveWithinExpression_WhenVariableIsStillLive_ReturnsTrue() {
    Node n = createTestNode();
    Node exprRoot = new Node(Token.ASSIGN, Node.newName("x"), Node.newNumber(0));
    Node readNode = new Node(Token.ASSIGN, Node.newName("y"), Node.newName("x"));

    boolean result = deadAssignmentsElimination.isVariableStillLiveWithinExpression(
        n, exprRoot, "x");

    assertThat(result).isTrue();
  }

  @Test
  public void testIsVariableStillLiveWithinExpression_WhenVariableIsDeadButHasLaterRead_ReturnsFalse() {
    Node n = createTestNode();
    Node exprRoot = new Node(Token.ASSIGN, Node.newName("x"), Node.newNumber(0));
    Node deadAssignment = new Node(Token.ASSIGN, Node.newName("x"), Node.newNumber(1));
    Node readNode = new Node(Token.ASSIGN, Node.newName("y"), Node.newName("x"));

    boolean result = deadAssignmentsElimination.isVariableStillLiveWithinExpression(
        n, exprRoot, "x");

    assertThat(result).isFalse();
  }

  private Node createTestNode() {
    return new Node(Token.ASSIGN, Node.newName("x"), Node.newNumber(0));
  }

  private Scope createMockScope() {
    Scope mockScope = mock(Scope.class);
    when(mockScope.getName()).thenReturn("testScope");
    when(mockScope.isDeclared("x", false)).thenReturn(true);
    when(mockScope.getVar("x")).thenReturn(new Var("x", Node.newName("x")));
    return mockScope;
  }

  private Scope createMockGlobalScope() {
    Scope mockGlobalScope = mock(Scope.class);
    when(mockGlobalScope.getName()).thenReturn("globalScope");
    return mockGlobalScope;
  }

  private Iterable<DiGraphNode<Node, Branch>> createTestControlFlowGraphNodes() {
    DiGraphNode<Node, Branch> node1 = createNodeWithName("EnterIfNode1");
    DiGraphNode<Node, Branch> node2 = createNodeWithName("DeadAssignmentNode");
    DiGraphNode<Node, Branch> node3 = createNodeWithName("EnterIfNode2");
    DiGraphNode<Node, Branch> node4 = createNodeWithName("ExitNode");

    when(controlFlowGraph.getNode(new Node(Token.IF, null, null))).thenReturn(node1);
    when(controlFlowGraph.getNode(new Node(Token.ASSIGN, Node.newName("x"), Node.newNumber(0)))).thenReturn(node2);
    when(controlFlowGraph.getNode(new Node(Token.IF, null, null))).thenReturn(node3);
    when(controlFlowGraph.getNode(Node.newEmpty())).thenReturn(node4);

    Iterable<DiGraphNode<Node, Branch>> nodes = createMock(Iterable.class);
    when(nodes.iterator()).thenReturn(
        new Iterator<DiGraphNode<Node, Branch>>() {
          int index = 0;

          @Override
          public boolean hasNext() {
            return index < 4;
          }

          @Override
          public DiGraphNode<Node, Branch> next() {
            return nodes.get(index++);
          }
        });
    when(controlFlowGraph.getDirectedGraphNodes()).thenReturn(nodes);

    return nodes;
  }

  private DiGraphNode<Node, Branch> createNodeWithName(String name) {
    DiGraphNode<Node, Branch> node = mock(DiGraphNode.class);
    when(node.getValue()).thenReturn(Node.newEmpty());
    when(node.getName()).thenReturn(name);
    return node;
  }
}