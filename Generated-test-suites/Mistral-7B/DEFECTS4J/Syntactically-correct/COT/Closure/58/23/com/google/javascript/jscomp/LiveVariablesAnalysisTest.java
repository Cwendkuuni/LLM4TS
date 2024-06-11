package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.jscomp.graph.Node;
import com.google.javascript.rhino.NodeOperations;

public class LiveVariablesAnalysisTest {

  private ControlFlowGraph<Node> controlFlowGraph;
  private Scope jsScope;
  private LiveVariablesAnalysis liveVariablesAnalysis;

  @Before
  public void setUp() {
    // Create a simple control flow graph for testing.
    jsScope = new Scope();
    jsScope.declareVariable("a", 0);
    jsScope.declareVariable("b", 1);
    jsScope.declareVariable("c", 2);
    jsScope.declareVariable("d", 3);

    Node rootNode = NodeOperations.newBlock(null, null);
    Node ifNode = NodeOperations.newIfStatement(null, null, null, null, null);
    Node assignNode1 = NodeOperations.newAssignment(null, "a", NodeOperations.newNumber(1), null, null);
    Node assignNode2 = NodeOperations.newAssignment(null, "b", NodeOperations.newName("a"), null, null);
    Node assignNode3 = NodeOperations.newAssignment(null, "c", NodeOperations.newName("b"), null, null);
    Node assignNode4 = NodeOperations.newAssignment(null, "d", NodeOperations.newNumber(2), null, null);
    Node conditionNode = NodeOperations.newBinaryOp(Token.OP_EQ, NodeOperations.newName("a"), NodeOperations.newNumber(1), null, null);

    rootNode.addChild(ifNode);
    ifNode.addChild(conditionNode);
    ifNode.addChild(assignNode1);
    ifNode.addChild(assignNode2);
    ifNode.addChild(assignNode3);
    ifNode.addChild(assignNode4);

    controlFlowGraph = new ControlFlowGraph<>(rootNode, false);

    liveVariablesAnalysis = new LiveVariablesAnalysis(controlFlowGraph, jsScope, null);
  }

  @Test
  public void testBasicFunctionality() {
    LiveVariablesAnalysis result = liveVariablesAnalysis.compute();
    Set<Var> expectedEscapedLocals = ImmutableSet.of(jsScope.getVar("a"));
    Set<Var> actualEscapedLocals = liveVariablesAnalysis.getEscapedLocals();

    assertEquals(expectedEscapedLocals, actualEscapedLocals);

    LiveVariableLattice expectedEntryLattice = new LiveVariableLattice(4);
    LiveVariableLattice actualEntryLattice = liveVariablesAnalysis.createEntryLattice();

    assertEquals(expectedEntryLattice, actualEntryLattice);

    LiveVariableLattice expectedInitialEstimateLattice = new LiveVariableLattice(4);
    LiveVariableLattice actualInitialEstimateLattice = liveVariablesAnalysis.createInitialEstimateLattice();

    assertEquals(expectedInitialEstimateLattice, actualInitialEstimateLattice);

    LiveVariableLattice expectedResultLattice = new LiveVariableLattice(4);
    expectedResultLattice.set(1);
    expectedResultLattice.set(2);
    expectedResultLattice.set(3);

    LiveVariableLattice actualResultLattice = result.getIntermediateResult(0);

    assertEquals(expectedResultLattice, actualResultLattice);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullScope() {
    new LiveVariablesAnalysis(null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullCompiler() {
    new LiveVariablesAnalysis(controlFlowGraph, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullControlFlowGraph() {
    new LiveVariablesAnalysis(null, jsScope, null);
  }

  @Test
  public void testIsForward() {
    assertFalse(liveVariablesAnalysis.isForward());
  }

  @Test
  public void testCreateEntryLatticeWithOneVariable() {
    LiveVariableLattice expectedEntryLattice = new LiveVariableLattice(1);
    LiveVariableLattice actualEntryLattice = liveVariablesAnalysis.createEntryLattice();

    assertEquals(expectedEntryLattice, actualEntryLattice);
  }

  @Test
  public void testCreateEntryLatticeWithMultipleVariables() {
    LiveVariableLattice expectedEntryLattice = new LiveVariableLattice(4);
    LiveVariableLattice actualEntryLattice = liveVariablesAnalysis.createEntryLattice();

    assertEquals(expectedEntryLattice, actualEntryLattice);
  }

  @Test
  public void testCreateInitialEstimateLatticeWithOneVariable() {
    LiveVariableLattice expectedInitialEstimateLattice = new LiveVariableLattice(1);
    LiveVariableLattice actualInitialEstimateLattice = liveVariablesAnalysis.createInitialEstimateLattice();

    assertEquals(expectedInitialEstimateLattice, actualInitialEstimateLattice);
  }

  @Test
  public void testCreateInitialEstimateLatticeWithMultipleVariables() {
    LiveVariableLattice expectedInitialEstimateLattice = new LiveVariableLattice(4);
    LiveVariableLattice actualInitialEstimateLattice = liveVariablesAnalysis.createInitialEstimateLattice();

    assertEquals(expectedInitialEstimateLattice, actualInitialEstimateLattice);
  }

  @Test
  public void testFlowThrough_NoGenOrKill() {
    LiveVariableLattice inputLattice = new LiveVariableLattice(4);
    LiveVariableLattice expectedResultLattice = new LiveVariableLattice(4);

    LiveVariableLattice actualResultLattice = liveVariablesAnalysis.flowThrough(NodeOperations.newName("test"), inputLattice);

    assertEquals(expectedResultLattice, actualResultLattice);
  }

  @Test
  public void testFlowThrough_OneGenAndNoKill() {
    LiveVariableLattice inputLattice = new LiveVariableLattice(4);
    inputLattice.set(1);

    LiveVariableLattice expectedResultLattice = new LiveVariableLattice(4);
    expectedResultLattice.set(1);

    LiveVariableLattice actualResultLattice = liveVariablesAnalysis.flowThrough(NodeOperations.newName("test"), inputLattice);

    assertEquals(expectedResultLattice, actualResultLattice);
  }

  @Test
  public void testFlowThrough_NoGenAndOneKill() {
    LiveVariableLattice inputLattice = new LiveVariableLattice(4);
    inputLattice.set(3);

    LiveVariableLattice expectedResultLattice = new LiveVariableLattice(4);

    LiveVariableLattice actualResultLattice = liveVariablesAnalysis.flowThrough(NodeOperations.newName("test"), inputLattice);

    assertEquals(expectedResultLattice, actualResultLattice);
  }

  @Test
  public void testFlowThrough_OneGenAndOneKill() {
    LiveVariableLattice inputLattice = new LiveVariableLattice(4);
    inputLattice.set(1);
    inputLattice.set(3);

    LiveVariableLattice expectedResultLattice = new LiveVariableLattice(4);
    expectedResultLattice.set(1);

    LiveVariableLattice actualResultLattice = liveVariablesAnalysis.flowThrough(NodeOperations.newName("test"), inputLattice);

    assertEquals(expectedResultLattice, actualResultLattice);
  }

  @Test
  public void testFlowThrough_OneGenAndMultipleKills() {
    LiveVariableLattice inputLattice = new LiveVariableLattice(4);
    inputLattice.set(1);
    inputLattice.set(2);
    inputLattice.set(3);

    LiveVariableLattice expectedResultLattice = new LiveVariableLattice(4);
    expectedResultLattice.set(1);

    LiveVariableLattice actualResultLattice = liveVariablesAnalysis.flowThrough(NodeOperations.newName("test"), inputLattice);

    assertEquals(expectedResultLattice, actualResultLattice);
  }

  @Test
  public void testFlowThrough_MultipleGensAndMultipleKills() {
    LiveVariableLattice inputLattice = new LiveVariableLattice(4);
    inputLattice.set(1);
    inputLattice.set(2);
    inputLattice.set(3);

    LiveVariableLattice expectedResultLattice = new LiveVariableLattice(4);
    expectedResultLattice.set(1);
    expectedResultLattice.set(2);

    LiveVariableLattice actualResultLattice = liveVariablesAnalysis.flowThrough(NodeOperations.newName("test"), inputLattice);

    assertEquals(expectedResultLattice, actualResultLattice);
  }

  @Test
  public void testComputeEscaped_NoEscapedVariables() {
    Set<Var> expectedEscapedLocals = ImmutableSet.of();
    Set<Var> actualEscapedLocals = liveVariablesAnalysis.computeEscapedLocals(jsScope, null, null);

    assertEquals(expectedEscapedLocals, actualEscapedLocals);
  }

  @Test
  public void testComputeEscaped_OneEscapedVariable() {
    Set<Var> expectedEscapedLocals = ImmutableSet.of(jsScope.getVar("a"));
    Set<Var> actualEscapedLocals = liveVariablesAnalysis.computeEscapedLocals(jsScope, null, null);

    assertEquals(expectedEscapedLocals, actualEscapedLocals);
  }

  @Test
  public void testComputeEscaped_MultipleEscapedVariables() {
    Set<Var> expectedEscapedLocals = ImmutableSet.of(jsScope.getVar("a"), jsScope.getVar("b"));
    Set<Var> actualEscapedLocals = liveVariablesAnalysis.computeEscapedLocals(jsScope, null, null);

    assertEquals(expectedEscapedLocals, actualEscapedLocals);
  }

  @Test
  public void testComputeEscaped_WithArgumentsVariable() {
    Set<Var> expectedEscapedLocals = ImmutableSet.of(jsScope.getVar("arguments"));
    Set<Var> actualEscapedLocals = liveVariablesAnalysis.computeEscapedLocals(jsScope, null, null);

    assertTrue(actualEscapedLocals.contains(jsScope.getVar("arguments")));
  }
}