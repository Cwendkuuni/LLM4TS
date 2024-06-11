package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Predicate;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractCompiler.Pass;
import com.google.javascript.jscomp.AbstractNodeTraversal;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis.FlowState;
import com.google.javascript.jscomp.LiveVariablesAnalysis.LiveVariableLattice;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraphNode;
import com.google.javascript.rhino.Token;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Tests for DeadAssignmentsElimination class.
 */
public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ControlFlowGraph<Node> cfg = mock(ControlFlowGraph.class);
    Scope scope = mock(Scope.class);
    LiveVariablesAnalysis liveness = mock(LiveVariablesAnalysis.class);

    when(compiler.getControlFlowGraph()).thenReturn(cfg);
    when(compiler.getScope()).thenReturn(scope);
    when(compiler.reportCodeChange()).thenAnswer(new Answer<Void>() {
      @Override
      public Void answer(InvocationOnMock invocation) throws Throwable {
        Object[] args = invocation.getArguments();
        Node node = (Node) args[0];
        assertEquals("Expected code change", 1, node.getChildCount());
        return null;
      }
    });

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    Node root = new Node(Token.PROGRAM, null);
    Node externs = new Node(Token.PROGRAM, null);
    Node program = new Node(Token.BLOCK, null, root);
    Node aAssign = new Node(Token.ASSIGN, null, new Node(Token.NAME, "a"),
        new Node(Token.NAME, "b"));
    Node bAssign = new Node(Token.ASSIGN, null, new Node(Token.NAME, "b"),
        new Node(Token.NUMBER, 1));
    Node print = new Node(Token.CALL, null, new Node(Token.NAME, "print"),
        new Node(Token.NAME, "a"));
    Node ifStatement = new Node(Token.IF, null, aAssign, null, null, print);

    // Add nodes to program node
    root.addChild(externs);
    externs.addChild(program);
    program.addChild(aAssign);
    program.addChild(ifStatement);
    program.addChild(bAssign);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, root);
    deadAssignmentsElimination.process(externs, root);
    nodeTraversal.traverseAllChildren();

    // Verify that deadAssignmentsElimination removed bAssign
    assertEquals("Expected 1 child for aAssign", 1, aAssign.getChildCount());
    assertEquals("Expected 0 children for bAssign", 0, bAssign.getChildCount());
  }
}