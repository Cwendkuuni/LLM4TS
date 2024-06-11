package com.google.javascript.jscomp;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.DataFlowAnalysis.FlowState;
import com.google.javascript.jscomp.LiveVariablesAnalysis.LiveVariableLattice;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.NodeTraversal.ScopedCallback;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.graph.DiGraph.DiGraphNode;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class DeadAssignmentsEliminationTest {
  private AbstractCompiler compiler;
  private ControlFlowGraph<Node> cfg;
  private LiveVariablesAnalysis liveness;
  private DeadAssignmentsElimination elimination;
  private NodeTraversal traversal;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    cfg = mock(ControlFlowGraph.class);
    liveness = mock(LiveVariablesAnalysis.class);
    elimination = new DeadAssignmentsElimination(compiler);
    traversal = new NodeTraversal(cfg);
  }

  @Test
  public void testDeadAssignmentsElimination() {
    // Test empty case
    elimination.process(null, null);

    // Test no assignment case
    Node root = Node.newBlock("root");
    elimination.process(null, root);

    // Test simple assignment case
    Node assign = new Node(Token.ASSIGN, Node.newName("x"), Node.newNumber(1));
    Node rootWithAssign = Node.newBlock("root", assign);
    elimination.process(null, rootWithAssign);

    // Test dead assignment case
    Node deadAssign = new Node(Token.ASSIGN, Node.newName("x"), Node.newName("x"));
    Node rootWithDeadAssign = Node.newBlock("root", deadAssign);
    when(liveness.getOut()).thenReturn(FlowState.live(new LiveVariableLattice()));
    when(liveness.getIn()).thenReturn(FlowState.live(new LiveVariableLattice()));
    when(liveness.getEscapedLocals()).thenReturn(new HashSet<>());
    when(cfg.getDirectedGraphNodes()).thenReturn(Collections.emptyList().iterator());
    traversal.setLivenessAnalysis(liveness);
    NodeTraversal.traverse(compiler, rootWithDeadAssign, traversal);
    elimination.tryRemoveDeadAssignments(traversal, cfg);
    assertTrue(deadAssign.isDead());

    // Test variable still live within expression case
    Node readBeforeWrite = new Node(Token.ASSIGN, Node.newName("x"), Node.newNumber(1));
    Node writeBeforeRead = new Node(Token.ASSIGN, Node.newName("x"), Node.newName("y"));
    Node rootWithReadBeforeWriteAndWriteBeforeRead =
        Node.newBlock("root", readBeforeWrite, writeBeforeRead);
    when(liveness.getOut()).thenReturn(FlowState.live(new LiveVariableLattice()));
    when(liveness.getIn()).thenReturn(FlowState.live(new LiveVariableLattice()));
    when(liveness.getEscapedLocals()).thenReturn(new HashSet<>());
    when(cfg.getDirectedGraphNodes()).thenReturn(Collections.singletonList(
        new MockDiGraphNodeWithValueAndAnnotation<>(rootWithReadBeforeWrite, null, null))
        .iterator());
    traversal.setLivenessAnalysis(liveness);
    NodeTraversal.traverse(compiler, rootWithReadBeforeWriteAndWriteBeforeRead, traversal);
    elimination.tryRemoveDeadAssignments(traversal, cfg);
    assertTrue(readBeforeWrite.isAlive());

    // Test multiple assignments case
    Node assign1 = new Node(Token.ASSIGN, Node.newName("x"), Node.newNumber(1));
    Node assign2 = new Node(Token.ASSIGN, Node.newName("x"), Node.newNumber(2));
    Node rootWithMultipleAssignments = Node.newBlock("root", assign1, assign2);
    when(liveness.getOut()).thenReturn(FlowState.live(new LiveVariableLattice()));
    when(liveness.getIn()).thenReturn(FlowState.live(new LiveVariableLattice()));
    when(liveness.getEscapedLocals()).thenReturn(new HashSet<>());
    when(cfg.getDirectedGraphNodes()).thenReturn(
        Arrays.asList(
            new MockDiGraphNodeWithValueAndAnnotation<>(assign1, null, null),
            new MockDiGraphNodeWithValueAndAnnotation<>(assign2, null, null))
            .iterator());
    traversal.setLivenessAnalysis(liveness);
    NodeTraversal.traverse(compiler, rootWithMultipleAssignments, traversal);
    elimination.tryRemoveDeadAssignments(traversal, cfg);
    assertTrue(assign2.isDead());
  }
}

class MockDiGraphNodeWithValueAndAnnotation<T> implements DiGraphNode<T, Branch> {
  private final T value;
  private final Branch annotation;

  public MockDiGraphNodeWithValueAndAnnotation(T value, Branch annotation) {
    this.value = value;
    this.annotation = annotation;
  }

  public MockDiGraphNodeWithValueAndAnnotation(T value) {
    this(value, null);
  }

  @Override
  public T getValue() {
    return value;
  }

  @Override
  public Branch getAnnotation() {
    return annotation;
  }
}