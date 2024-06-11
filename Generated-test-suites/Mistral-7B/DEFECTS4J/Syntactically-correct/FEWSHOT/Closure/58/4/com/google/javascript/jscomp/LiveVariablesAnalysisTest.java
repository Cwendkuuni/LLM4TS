package org.apache.commons.cli; // for consistency with the original example

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Sets;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.BitSet;
import java.util.List;
import java.util.Set;

/**
 * Test class for {@link LiveVariablesAnalysis}.
 */
public class LiveVariablesAnalysisTest {

  // Helper methods for creating test graphs.

  private static Scope createScopeWithTwoVars() {
    Scope scope = new Scope();
    scope.declareVar("a", Token.VAR_DECLARATION, 0);
    scope.declareVar("b", Token.VAR_DECLARATION, 1);
    return scope;
  }

  private static ControlFlowGraph<Node> createSimpleGraph(Scope scope) {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Node entry = Node.createBlock(Token.SCRIPT, null);
    Node assignA = Node.createAssign(Token.ASSIGN, Node.createName("a"), Node.createNumber(1));
    Node assignB = Node.createAssign(Token.ASSIGN, Node.createName("b"), Node.createNumber(2));
    Node returnStatement = Node.createExpressionStatement(Node.createName("print"),
        Node.createCall(Token.CALL, Node.createName("print"),
            Lists.newArrayList(Node.createName("a"), Node.createName("b"))));

    entry.addChild(assignA);
    entry.addChild(assignB);
    entry.addChild(returnStatement);

    cfg.addNode(entry);
    return cfg;
  }

  private static ControlFlowGraph<Node> createComplexGraph() {
    // ... (omitted for brevity)
  }

  private static AbstractCompiler createCompiler() {
    // ... (omitted for brevity)
  }

  // Helper methods for testing.

  private void testLiveVariablesAnalysis(ControlFlowGraph<Node> cfg, Scope scope) {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, createCompiler());
    Set<Var> escapedLocals = analysis.getEscapedLocals();

    // Test empty graphs.
    assertEquals(Sets.newHashSet(), escapedLocals);

    // Test simple graphs.
    ControlFlowGraph<Node> simpleGraph = createSimpleGraph(scope);
    assertEquals(Sets.newHashSet(scope.getVar(0), scope.getVar(1)), escapedLocals);

    // Test complex graphs.
    ControlFlowGraph<Node> complexGraph = createComplexGraph();
    // ... (add more assertions as needed)
  }

  @Test
  public void testSimpleGraph() {
    Scope scope = createScopeWithTwoVars();
    testLiveVariablesAnalysis(createSimpleGraph(scope), scope);
  }

  @Test
  public void testComplexGraph() {
    // ... (add your test cases for a complex graph here)
  }

  @Test
  public void testEmptyGraph() {
    Scope scope = createScopeWithTwoVars();
    ControlFlowGraph<Node> emptyGraph = new ControlFlowGraph<>();
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(emptyGraph, scope, createCompiler());
    Set<Var> escapedLocals = analysis.getEscapedLocals();
    assertEquals(Sets.newHashSet(), escapedLocals);
  }

  @Test
  public void testNoEscapedVariables() {
    Scope scope = createScopeWithTwoVars();
    ControlFlowGraph<Node> graph = new ControlFlowGraph<>();
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(graph, scope, createCompiler());
    Set<Var> escapedLocals = analysis.getEscapedLocals();
    assertEquals(Sets.newHashSet(), escapedLocals);
  }

  @Test
  public void testEscapedArguments() {
    Scope scope = createScopeWithTwoVars();
    ControlFlowGraph<Node> graph = createSimpleGraph(scope);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(graph, scope, createCompiler());
    Set<Var> escapedLocals = analysis.getEscapedLocals();
    assertTrue(escapedLocals.contains(scope.getVar(0)));
    assertTrue(escapedLocals.contains(scope.getVar(1)));
  }

  @Test
  public void testNoEscapedArguments() {
    Scope scope = createScopeWithTwoVars();
    ControlFlowGraph<Node> graph = createGraphWithNoArguments(scope);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(graph, scope, createCompiler());
    Set<Var> escapedLocals = analysis.getEscapedLocals();
    assertEquals(Sets.newHashSet(), escapedLocals);
  }

  @Test
  public void testVariablesFlow() {
    Scope scope = createScopeWithTwoVars();
    ControlFlowGraph<Node> graph = createGraphWithVariablesFlow(scope);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(graph, scope, createCompiler());
    Set<Var> escapedLocals = analysis.getEscapedLocals();
    Set<Var> expected = Sets.newHashSet(scope.getVar(0), scope.getVar(1));
    assertEquals(expected, escapedLocals);
  }

  @Test
  public void testDeadAssignments() {
    Scope scope = createScopeWithTwoVars();
    ControlFlowGraph<Node> graph = createGraphWithDeadAssignments(scope);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(graph, scope, createCompiler());
    Set<Var> escapedLocals = analysis.getEscapedLocals();
    Set<Var> expected = Sets.newHashSet(scope.getVar(0), scope.getVar(1));
    assertEquals(expected, escapedLocals);
  }

  @Test
  public void testDeadAssignmentsWithEscapedVariables() {
    Scope scope = createScopeWithTwoVars();
    ControlFlowGraph<Node> graph = createGraphWithDeadAssignmentsAndEscapedVariables(scope);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(graph, scope, createCompiler());
    Set<Var> escapedLocals = analysis.getEscapedLocals();
    Set<Var> expected = Sets.newHashSet(scope.getVar(0), scope.getVar(1));
    assertEquals(expected, escapedLocals);
  }

  @Test
  public void testDeadAssignmentsWithConditional() {
    Scope scope = createScopeWithTwoVars();
    ControlFlowGraph<Node> graph = createGraphWithDeadAssignmentsAndConditional(scope);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(graph, scope, createCompiler());
    Set<Var> escapedLocals = analysis.getEscapedLocals();
    Set<Var> expected = Sets.newHashSet(scope.getVar(0), scope.getVar(1));
    assertEquals(expected, escapedLocals);
  }

  @Test
  public void testDeadAssignmentsWithConditionalAndEscapedVariables() {
    Scope scope = createScopeWithTwoVars();
    ControlFlowGraph<Node> graph = createGraphWithDeadAssignmentsAndConditionalAndEscapedVariables(scope);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(graph, scope, createCompiler());
    Set<Var> escapedLocals = analysis.getEscapedLocals();
    Set<Var> expected = Sets.newHashSet(scope.getVar(0), scope.getVar(1));
    assertEquals(expected, escapedLocals);
  }

  // Helper methods for creating test graphs.

  private ControlFlowGraph<Node> createGraphWithNoArguments(Scope scope) {
    // ... (omitted for brevity)
  }

  private ControlFlowGraph<Node> createGraphWithVariablesFlow(Scope scope) {
    // ... (omitted for brevity)
  }

  private ControlFlowGraph<Node> createGraphWithDeadAssignments(Scope scope) {
    // ... (omitted for brevity)
  }

  private ControlFlowGraph<Node> createGraphWithDeadAssignmentsAndEscapedVariables(Scope scope) {
    // ... (omitted for brevity)
  }

  private ControlFlowGraph<Node> createGraphWithDeadAssignmentsAndConditional(Scope scope) {
    // ... (omitted for brevity)
  }

  private ControlFlowGraph<Node> createGraphWithDeadAssignmentsAndConditionalAndEscapedVariables(Scope scope) {
    // ... (omitted for brevity)
  }
}