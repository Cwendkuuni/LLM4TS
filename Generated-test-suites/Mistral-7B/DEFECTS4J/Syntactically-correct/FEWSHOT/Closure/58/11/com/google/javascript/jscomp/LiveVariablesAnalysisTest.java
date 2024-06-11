package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph;

import org.junit.Before;
import org.junit.Test;

import java.util.BitSet;
import java.util.List;
import java.util.Set;

public class LiveVariablesAnalysisTest {

  private ControlFlowGraph<Node> cfg;
  private Scope jsScope;
  private AbstractCompiler compilerMock;

  @Before
  public void setup() {
    jsScope = mock(Scope.class);
    compilerMock = mock(AbstractCompiler.class);
    cfg = new ControlFlowGraph<Node>(mock(DiGraph.class), jsScope, null);
  }

  @Test
  public void testGetEscapedLocalsIsEmpty() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compilerMock);
    Set<Var> escaped = analysis.getEscapedLocals();
    assertTrue(escaped.isEmpty());
  }

  @Test
  public void testGetEscapedLocalsIsNotEmpty() {
    Var variable1 = new Var("variable1", 0);
    Var variable2 = new Var("variable2", 1);

    when(jsScope.getVar("variable1")).thenReturn(variable1);
    when(jsScope.getVar("variable2")).thenReturn(variable2);

    Set<Var> escaped = Sets.newHashSet(variable1, variable2);
    analysis = new LiveVariablesAnalysis(cfg, jsScope, compilerMock);

    Set<Var> actualEscaped = analysis.getEscapedLocals();
    assertEquals(escaped, actualEscaped);
  }

  @Test
  public void testCreateEntryLatticeHasExpectedSize() {
    int numVars = 5;
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compilerMock);
    LiveVariableLattice lattice = analysis.createEntryLattice();

    assertEquals(numVars, lattice.liveSet.size());
  }

  @Test
  public void testIsForwardIsFalse() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compilerMock);
    boolean result = analysis.isForward();

    assertFalse(result);
  }

  @Test
  public void testCreateInitialEstimateLatticeHasExpectedSize() {
    int numVars = 5;
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compilerMock);
    LiveVariableLattice lattice = analysis.createInitialEstimateLattice();

    assertEquals(numVars, lattice.liveSet.size());
  }

  @Test
  public void testFlowThroughNodeDoesNotChangeLatticeForVariableDeclaration() {
    int numVars = 5;
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compilerMock);
    LiveVariableLattice latticeEntry = analysis.createEntryLattice();
    LiveVariableLattice latticeInitial = analysis.createInitialEstimateLattice();

    LiveVariableLattice latticeResult = analysis.flowThrough(
        createVariableDeclarationNode("var x"), latticeInitial);

    assertEquals(latticeEntry, latticeResult);
  }

  @Test
  public void testFlowThroughNodeMakesVariablesLiveForAssignment() {
    int numVars = 5;
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compilerMock);
    LiveVariableLattice latticeEntry = analysis.createEntryLattice();
    LiveVariableLattice latticeInitial = analysis.createInitialEstimateLattice();

    LiveVariableLattice latticeResult = analysis.flowThrough(
        createAssignmentNode("x = 5"), latticeInitial);

    BitSet expectedLiveSet = new BitSet(numVars);
    expectedLiveSet.set(0); // x

    assertEquals(expectedLiveSet, latticeResult.liveSet);
  }

  @Test
  public void testFlowThroughNodeMakesVariablesDeadForComparison() {
    int numVars = 5;
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compilerMock);
    LiveVariableLattice latticeEntry = analysis.createEntryLattice();
    LiveVariableLattice latticeInitial = analysis.createInitialEstimateLattice();

    LiveVariableLattice latticeResult = analysis.flowThrough(
        createComparisonNode("x < 5"), latticeInitial);

    BitSet expectedLiveSet = new BitSet(numVars);

    assertEquals(expectedLiveSet, latticeResult.liveSet);
    assertFalse(latticeResult.liveSet.get(0)); // x
  }

  @Test
  public void testFlowThroughNodeMakesVariablesLiveForAssignmentInCondition() {
    int numVars = 5;
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compilerMock);
    LiveVariableLattice latticeEntry = analysis.createEntryLattice();
    LiveVariableLattice latticeInitial = analysis.createInitialEstimateLattice();

    LiveVariableLattice latticeResult = analysis.flowThrough(
        createConditionalAssignmentNode("if (x = 5) {}", latticeInitial), latticeInitial);

    BitSet expectedLiveSet = new BitSet(numVars);
    expectedLiveSet.set(0); // x

    assertEquals(expectedLiveSet, lakeResult.liveSet);
  }

  @Test
  public void testFlowThroughNodeMakesVariablesLiveForFunctionCall() {
    int numVars = 5;
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compilerMock);
    LiveVariableLattice latticeEntry = analysis.createEntryLattice();
    LiveVariableLattice latticeInitial = analysis.createInitialEstimateLattice();

    LiveVariableLattice latticeResult = analysis.flowThrough(
        createFunctionCallNode("max(3, 5)"), latticeInitial);

    BitSet expectedLiveSet = new BitSet(numVars);

    assertEquals(expectedLiveSet, latticeResult.liveSet);
  }

  private Node createVariableDeclarationNode(String nodeName) {
    return new Node(Token.VAR, new Node(Token.NAME, nodeName));
  }

  private Node createAssignmentNode(String nodeName) {
    return new Node(Token.ASSIGN, new Node(Token.NAME, nodeName), null);
  }

  private Node createComparisonNode(String nodeName) {
    return new Node(Token.LT, new Node(Token.NAME, "x"), new Node(Token.INT_LITERAL, 5));
  }

  private Node createConditionalAssignmentNode(String nodeName) {
    return new Node(Token.IF, createAssignmentNode("x = 5"), new Node(Token.BLOCK, null));
  }

  private Node createFunctionCallNode(String nodeName) {
    return new Node(Token.NAME, nodeName, new Node(Token.LPAREN), null, new Node(Token.RPAREN));
  }
}