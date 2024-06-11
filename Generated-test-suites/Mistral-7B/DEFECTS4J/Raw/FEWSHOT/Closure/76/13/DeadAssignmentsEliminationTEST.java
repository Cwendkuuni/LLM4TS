package org.apache.commons.cli;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.LiveVariablesAnalysis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;

public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ControlFlowGraph cfg = mock(ControlFlowGraph.class);
    LiveVariablesAnalysis liveness = mock(LiveVariablesAnalysis.class);
    when(compiler.getControlFlowGraph()).thenReturn(cfg);
    when(compiler.getLiveVariablesAnalysis()).thenReturn(liveness);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    Node root = createNode("a = 1; b = a;");
    Node externs = createNode("var a, b;");

    deadAssignmentsElimination.process(externs, root);

    Node expectedRoot = createNode("var a, b; a = 1;");

    assertNodeEqual(expectedRoot, root);
  }

  @Test
  public void testDeadAssignmentsEliminationWithIfStatement() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ControlFlowGraph cfg = mock(ControlFlowGraph.class);
    LiveVariablesAnalysis liveness = mock(LiveVariablesAnalysis.class);
    when(compiler.getControlFlowGraph()).thenReturn(cfg);
    when(compiler.getLiveVariablesAnalysis()).thenReturn(liveness);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    Node root = createNode("if (a = 1) { b = a; }");
    Node externs = createNode("var a, b;");

    deadAssignmentsElimination.process(externs, root);

    Node expectedRoot = createNode("if (1) { b = a; }");

    assertNodeEqual(expectedRoot, root);
  }

  @Test
  public void testDeadAssignmentsEliminationWithWhileStatement() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ControlFlowGraph cfg = mock(ControlFlowGraph.class);
    LiveVariablesAnalysis liveness = mock(LiveVariablesAnalysis.class);
    when(compiler.getControlFlowGraph()).thenReturn(cfg);
    when(compiler.getLiveVariablesAnalysis()).thenReturn(liveness);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    Node root = createNode("while (a = 1) { b = a; }");
    Node externs = createNode("var a, b;");

    deadAssignmentsElimination.process(externs, root);

    Node expectedRoot = createNode("while (1) { b = a; }");

    assertNodeEqual(expectedRoot, root);
  }

  @Test
  public void testDeadAssignmentsEliminationWithForStatement() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ControlFlowGraph cfg = mock(ControlFlowGraph.class);
    LiveVariablesAnalysis liveness = mock(LiveVariablesAnalysis.class);
    when(compiler.getControlFlowGraph()).thenReturn(cfg);
    when(compiler.getLiveVariablesAnalysis()).thenReturn(liveness);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    Node root = createNode("for (var i = 0; i = 1; i++) { b = i; }");
    Node externs = createNode("var i, b;");

    deadAssignmentsElimination.process(externs, root);

    Node expectedRoot = createNode("for (var i = 0; i = 1; ) { b = i; }");

    assertNodeEqual(expectedRoot, root);
  }

  @Test
  public void testDeadAssignmentsEliminationWithSwitchStatement() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ControlFlowGraph cfg = mock(ControlFlowGraph.class);
    LiveVariablesAnalysis liveness = mock(LiveVariablesAnalysis.class);
    when(compiler.getControlFlowGraph()).thenReturn(cfg);
    when(compiler.getLiveVariablesAnalysis()).thenReturn(liveness);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    Node root = createNode("switch (a) { case 1: b = a; }");
    Node externs = createNode("var a, b;");

    deadAssignmentsElimination.process(externs, root);

    Node expectedRoot = createNode("switch (a) {}");

    assertNodeEqual(expectedRoot, root);
  }

  @Test
  public void testDeadAssignmentsEliminationWithReturnStatement() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ControlFlowGraph cfg = mock(ControlFlowGraph.class);
    LiveVariablesAnalysis liveness = mock(LiveVariablesAnalysis.class);
    when(compiler.getControlFlowGraph()).thenReturn(cfg);
    when(compiler.getLiveVariablesAnalysis()).thenReturn(liveness);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    Node root = createNode("return a = 1;");
    Node externs = createNode("var a;");

    deadAssignmentsElimination.process(externs, root);

    Node expectedRoot = createNode("return 1;");

    assertNodeEqual(expectedRoot, root);
  }

  @Test
  public void testDeadAssignmentsEliminationWithVariableDeclaration() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ControlFlowGraph cfg = mock(ControlFlowGraph.class);
    LiveVariablesAnalysis liveness = mock(LiveVariablesAnalysis.class);
    when(compiler.getControlFlowGraph()).thenReturn(cfg);
    when(compiler.getLiveVariablesAnalysis()).thenReturn(liveness);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    Node root = createNode("var a = 1, b = a;");
    Node externs = createNode("var a, b;");

    deadAssignmentsElimination.process(externs, root);

    Node expectedRoot = createNode("var a = 1; var b = a;");

    assertNodeEqual(expectedRoot, root);
  }

  @Test
  public void testDeadAssignmentsEliminationWithMultipleAssignments() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ControlFlowGraph cfg = mock(ControlFlowGraph.class);
    LiveVariablesAnalysis liveness = mock(LiveVariablesAnalysis.class);
    when(compiler.getControlFlowGraph()).thenReturn(cfg);
    when(compiler.getLiveVariablesAnalysis()).thenReturn(liveness);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    Node root =
        createNode("a = 1, b = a, c = a, d = c;");
    Node externs = createNode("var a, b, c, d;");

    deadAssignmentsElimination.process(externs, root);

    Node expectedRoot = createNode("a = 1, b = a, c = a;");

    assertNodeEqual(expectedRoot, root);
  }

  @Test
  public void testDeadAssignmentsEliminationWithIncrementDecrementOperator() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ControlFlowGraph cfg = mock(ControlFlowGraph.class);
    LiveVariablesAnalysis liveness = mock(LiveVariablesAnalysis.class);
    when(compiler.getControlFlowGraph()).thenReturn(cfg);
    when(compiler.getLiveVariablesAnalysis()).thenReturn(liveness);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    Node root = createNode("a++, b--");
    Node externs = createNode("var a, b;");

    deadAssignmentsElimination.process(externs, root);

    Node expectedRoot = createNode("a++, b--");

    assertNodeEqual(expectedRoot, root);
  }

  @Test
  public void testDeadAssignmentsEliminationWithComplexExpression() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ControlFlowGraph cfg = mock(ControlFlowGraph.class);
    LiveVariablesAnalysis liveness = mock(LiveVariablesAnalysis.class);
    when(compiler.getControlFlowGraph()).thenReturn(cfg);
    when(compiler.getLiveVariablesAnalysis()).thenReturn(liveness);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    Node root = createNode(
        "if (a = 1 && b = 2) { c = a + b; d = c * 3; e = d + 1; }");
    Node externs = createNode("var a, b, c, d, e;");

    deadAssignmentsElimination.process(externs, root);

    Node expectedRoot = createNode(
        "if (1 && 2) { c = a + b; d = c * 3; e = d + 1; }");

    assertNodeEqual(expectedRoot, root);
  }

  private Node createNode(String nodeString) {
    NodeUtil nodeUtil = new NodeUtil();
    return nodeUtil.parseTree(nodeString);
  }

  private void assertNodeEqual(Node expectedNode, Node actualNode) {
    assertEquals(expectedNode.toSource(), actualNode.toSource());
  }
}