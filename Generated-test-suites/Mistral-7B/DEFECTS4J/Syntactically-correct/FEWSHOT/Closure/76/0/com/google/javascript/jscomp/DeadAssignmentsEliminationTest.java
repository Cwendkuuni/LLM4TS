package org.apache.commons.cli.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Predicate;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.AbstractVisitor;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.FlowState;
import com.google.javascript.jscomp.GraphTraversal;
import com.google.javascript.jscomp.LiveVariablesAnalysis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.ScopedCallback;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeUtil;

import org.junit.Test;

public class DeadAssignmentsEliminationTest {
  @Test
  public void testDeadAssignmentsElimination() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ControlFlowGraph<Node> cfg = mock(ControlFlowGraph.class);
    LiveVariablesAnalysis liveness = mock(LiveVariablesAnalysis.class);
    when(compiler.getControlFlowGraph()).thenReturn(cfg);
    when(liveness.analyze()).thenReturn(true);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    Node node = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), null);
    Scope scope = mock(Scope.class);
    when(scope.isDeclared("x", false)).thenReturn(true);

    NodeTraversal nodeTraversal = new NodeTraversal(null, null, null, null, null, null, null);
    nodeTraversal.setGraph(cfg);
    nodeTraversal.setScope(scope);

    deadAssignmentsElimination.process(null, null);
    nodeTraversal.traverse(compiler, node, deadAssignmentsElimination);

    assertTrue(node.getFirstChild() == null);
  }

  @Test
  public void testDeadAssignmentsEliminationWithCondition() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ControlFlowGraph<Node> cfg = mock(ControlFlowGraph.class);
    LiveVariablesAnalysis liveness = mock(LiveVariablesAnalysis.class);
    when(compiler.getControlFlowGraph()).thenReturn(cfg);
    when(liveness.analyze()).thenReturn(true);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    Node condition = new Node(Token.IF, null, null);
    Node assign = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), null);

    Node node = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), null);
    node.addChild(condition);
    node.addChild(assign);
    Scope scope = mock(Scope.class);
    when(scope.isDeclared("x", false)).thenReturn(true);

    NodeTraversal nodeTraversal = new NodeTraversal(null, null, null, null, null, null, null);
    nodeTraversal.setGraph(cfg);
    nodeTraversal.setScope(scope);

    deadAssignmentsElimination.process(null, null);
    nodeTraversal.traverse(compiler, node, deadAssignmentsElimination);

    assertTrue(assign.getParent() == null);
  }

  @Test
  public void testDeadAssignmentsEliminationWithReturnStatement() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ControlFlowGraph<Node> cfg = mock(ControlFlowGraph.class);
    LiveVariablesAnalysis liveness = mock(LiveVariablesAnalysis.class);
    when(compiler.getControlFlowGraph()).thenReturn(cfg);
    when(liveness.analyze()).thenReturn(true);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    Node returnStatement = new Node(Token.RETURN, null, null);
    Node assign = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), null);

    Node node = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), null);
    node.addChild(returnStatement);
    node.addChild(assign);
    Scope scope = mock(Scope.class);
    when(scope.isDeclared("x", false)).thenReturn(true);

    NodeTraversal nodeTraversal = new NodeTraversal(null, null, null, null, null, null, null);
    nodeTraversal.setGraph(cfg);
    nodeTraversal.setScope(scope);

    deadAssignmentsElimination.process(null, null);
    nodeTraversal.traverse(compiler, node, deadAssignmentsElimination);

    assertTrue(assign.getParent() == null);
  }

  @Test
  public void testDeadAssignmentsEliminationWithVariableStillLive() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ControlFlowGraph<Node> cfg = mock(ControlFlowGraph.class);
    LiveVariablesAnalysis liveness = mock(LiveVariablesAnalysis.class);
    when(compiler.getControlFlowGraph()).thenReturn(cfg);
    when(liveness.analyze()).thenReturn(true);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    Node assign1 = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), null);
    Node assign2 = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), null);

    Node node = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), null);
    node.addChild(assign1);
    node.addChild(assign2);
    Scope scope = mock(Scope.class);
    when(scope.isDeclared("x", false)).thenReturn(true);

    NodeTraversal nodeTraversal = new NodeTraversal(null, null, null, null, null, null, null);
    nodeTraversal.setGraph(cfg);
    nodeTraversal.setScope(scope);

    deadAssignmentsElimination.process(null, null);
    nodeTraversal.traverse(compiler, node, deadAssignmentsElimination);

    assertFalse(assign1.getParent() == null);
  }

  @Test
  public void testDeadAssignmentsEliminationWithConditionAndVariableStillLive() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ControlFlowGraph<Node> cfg = mock(ControlFlowGraph.class);
    LiveVariablesAnalysis liveness = mock(LiveVariablesAnalysis.class);
    when(compiler.getControlFlowGraph()).thenReturn(cfg);
    when(liveness.analyze()).thenReturn(true);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    Node condition = new Node(Token.IF, null, null);
    Node assign1 = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), null);
    Node assign2 = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), null);

    Node node = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), null);
    node.addChild(condition);
    node.addChild(assign1);
    node.addChild(assign2);
    Scope scope = mock(Scope.class);
    when(scope.isDeclared("x", false)).thenReturn(true);

    NodeTraversal nodeTraversal = new NodeTraversal(null, null, null, null, null, null, null);
    nodeTraversal.setGraph(cfg);
    nodeTraversal.setScope(scope);

    deadAssignmentsElimination.process(null, null);
    nodeTraversal.traverse(compiler, node, deadAssignmentsElimination);

    assertFalse(assign1.getParent() == null);
  }

  @Test
  public void testDeadAssignmentsEliminationWithIncrement() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ControlFlowGraph<Node> cfg = mock(ControlFlowGraph.class);
    LiveVariablesAnalysis liveness = mock(LiveVariablesAnalysis.class);
    when(compiler.getControlFlowGraph()).thenReturn(cfg);
    when(liveness.analyze()).thenReturn(true);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    Node increment = new Node(Token.INC, new Node(Token.NAME, "x"), null);

    Node node = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), null);
    node.addChild(increment);
    Scope scope = mock(Scope.class);
    when(scope.isDeclared("x", false)).thenReturn(true);

    NodeTraversal nodeTraversal = new NodeTraversal(null, null, null, null, null, null, null);
    nodeTraversal.setGraph(cfg);
    nodeTraversal.setScope(scope);

    deadAssignmentsElimination.process(null, null);
    nodeTraversal.traverse(compiler, node, deadAssignmentsElimination);

    assertTrue(increment.getParent() == null);
  }

  @Test
  public void testDeadAssignmentsEliminationWithDecrement() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ControlFlowGraph<Node> cfg = mock(ControlFlowGraph.class);
    LiveVariablesAnalysis liveness = mock(LiveVariablesAnalysis.class);
    when(compiler.getControlFlowGraph()).thenReturn(cfg);
    when(liveness.analyze()).thenReturn(true);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    Node decrement = new Node(Token.DEC, new Node(Token.NAME, "x"), null);

    Node node = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), null);
    node.addChild(decrement);
    Scope scope = mock(Scope.class);
    when(scope.isDeclared("x", false)).thenReturn(true);

    NodeTraversal nodeTraversal = new NodeTraversal(null, null, null, null, null, null, null);
    nodeTraversal.setGraph(cfg);
    nodeTraversal.setScope(scope);

    deadAssignmentsElimination.process(null, null);
    nodeTraversal.traverse(compiler, node, deadAssignmentsElimination);

    assertTrue(decrement.getParent() == null);
  }

  @Test
  public void testDeadAssignmentsEliminationWithForLoop() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ControlFlowGraph<Node> cfg = mock(ControlFlowGraph.class);
    LiveVariablesAnalysis liveness = mock(LiveVariablesAnalysis.class);
    when(compiler.getControlFlowGraph()).thenReturn(cfg);
    when(liveness.analyze()).thenReturn(true);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    Node init = new Node(Token.ASSIGN, new Node(Token.NAME, "i"), null);
    Node condition = new Node(Token.ASSIGN, new Node(Token.NUM, 0), null);
    Node increment = new Node(Token.ASSIGN, new Node(Token.NAME, "i"), null);
    Node forLoop = new Node(Token.FOR, null, init, condition, null, null, null);
    Node assign = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), null);

    Node node = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), null);
    node.addChild(forLoop);
    node.addChild(assign);
    Scope scope = mock(Scope.class);
    when(scope.isDeclared("i", false)).thenReturn(true);
    when(scope.isDeclared("x", false)).thenReturn(true);

    NodeTraversal nodeTraversal = new NodeTraversal(null, null, null, null, null, null, null);
    nodeTraversal.setGraph(cfg);
    nodeTraversal.setScope(scope);

    deadAssignmentsElimination.process(null, null);
    nodeTraversal.traverse(compiler, node, deadAssignmentsElimination);

    assertTrue(assign.getParent() == null);
  }

  @Test
  public void testDeadAssignmentsEliminationWithForInLoop() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ControlFlowGraph<Node> cfg = mock(ControlFlowGraph.class);
    LiveVariablesAnalysis liveness = mock(LiveVariablesAnalysis.class);
    when(compiler.getControlFlowGraph()).thenReturn(cfg);
    when(liveness.analyze()).thenReturn(true);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    Node init = new Node(Token.ASSIGN, new Node(Token.NAME, "i"), null);
    Node condition = new Node(Token.ASSIGN, new Node(Token.NAME, "i"), null);
    Node increment = new Node(Token.ASSIGN, new Node(Token.NAME, "i"), null);
    Node forLoop = new Node(Token.FORIN, null, null, null, null, null, null);
    Node assign = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), null);

    Node node = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), null);
    node.addChild(forLoop);
    node.addChild(assign);
    Scope scope = mock(Scope.class);
    when(scope.isDeclared("i", false)).thenReturn(true);
    when(scope.isDeclared("x", false)).thenReturn(true);

    NodeTraversal nodeTraversal = new NodeTraversal(null, null, null, null, null, null, null);
    nodeTraversal.setGraph(cfg);
    nodeTraversal.setScope(scope);

    deadAssignmentsElimination.process(null, null);
    nodeTraversal.traverse(compiler, node, deadAssignmentsElimination);

    assertTrue(assign.getParent() == null);
  }
}