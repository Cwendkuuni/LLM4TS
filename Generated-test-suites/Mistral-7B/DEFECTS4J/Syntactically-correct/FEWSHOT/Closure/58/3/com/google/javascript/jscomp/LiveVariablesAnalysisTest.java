package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.BitSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class LiveVariablesAnalysisTest {

  private Scope scope;
  private ControlFlowGraph<Node> cfg;

  @Before
  public void setUp() {
    // Set up test data here, for example by creating a mock Scope and ControlFlowGraph.
    // Replace the following code with your own setup logic.
    this.scope = new Scope(null, null, null, null);
    this.cfg = new ControlFlowGraph<Node>(null, null);
  }

  public void testCreateEntryLattice() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);
    LiveVariableLattice lattice = analysis.createEntryLattice();
    Preconditions.checkState(lattice != null);
    assertThat(lattice.liveSet.cardinality()).isEqualTo(scope.getVarCount());
  }

  @Test
  public void testCreateInitialEstimateLattice() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);
    LiveVariableLattice lattice = analysis.createInitialEstimateLattice();
    Preconditions.checkState(lattice != null);
    assertThat(lattice.liveSet.cardinality()).isEqualTo(scope.getVarCount());
  }

  @Test
  public void testIsForward() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);
    assertFalse(analysis.isForward());
  }

  @Test
  public void testFlowThroughTokenScript() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);
    Node scriptNode = Node.newScript(null);
    LiveVariableLattice input = analysis.createEntryLattice();
    LiveVariableLattice output = analysis.flowThrough(scriptNode, input);
    Preconditions.checkState(output != null);
    assertEquals(input, output);
  }

  @Test
  public void testFlowThroughTokenFunction() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);
    Node functionNode = Node.newFunction(null, null, null, null, null, null, null);
    LiveVariableLattice input = analysis.createEntryLattice();
    LiveVariableLattice output = analysis.flowThrough(functionNode, input);
    Preconditions.checkState(output != null);
    assertEquals(input, output);
  }

  @Test
  public void testFlowThroughTokenVar() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);
    Node varNode = Node.newName("x");
    LiveVariableLattice input = analysis.createEntryLattice();
    LiveVariableLattice output = analysis.flowThrough(varNode, input);
    Preconditions.checkState(output != null);
    assertFalse(output.isLive(0));
  }

  @Test
  public void testFlowThroughTokenAssign() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);
    Node assignNode = Node.newAssign(Node.newName("x"), Node.newNumber(1));
    Node lhs = assignNode.getFirstChild();
    LiveVariableLattice input = analysis.createEntryLattice();
    LiveVariableLattice output = analysis.flowThrough(assignNode, input);
    Preconditions.checkState(output != null);
    assertTrue(output.isLive(0));
  }

  @Test
  public void testFlowThroughTokenIf() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);
    Node ifNode = Node.newIf(Node.newBooleanLiteral(true), Node.newName("x"), null, null);
    LiveVariableLattice input = analysis.createEntryLattice();
    LiveVariableLattice output = analysis.flowThrough(ifNode, input);
    Preconditions.checkState(output != null);
    assertTrue(output.isLive(0));
  }

  @Test
  public void testFlowThroughTokenWhile() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);
    Node whileNode = Node.newWhile(Node.newBooleanLiteral(true), null, null, null);
    LiveVariableLattice input = analysis.createEntryLattice();
    LiveVariableLattice output = analysis.flowThrough(whileNode, input);
    Preconditions.checkState(output != null);
    assertTrue(output.isLive(0));
  }

  @Test
  public void testFlowThroughTokenFor() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);
    Node forNode = Node.newFor(Node.newName("i"), Node.newNumber(0), Node.newNumber(1), Node.newName("arr"), null, null, null, null);
    LiveVariableLattice input = analysis.createEntryLattice();
    LiveVariableLattice output = analysis.flowThrough(forNode, input);
    Preconditions.checkState(output != null);
    assertTrue(output.isLive(0));
    assertTrue(output.isLive(1));
  }

  @Test
  public void testFlowThroughTokenAnd() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);
    Node andNode = Node.newAnd(Node.newBooleanLiteral(true), Node.newBooleanLiteral(true));
    LiveVariableLattice input = analysis.createEntryLattice();
    LiveVariableLattice output = analysis.flowThrough(andNode, input);
    Preconditions.checkState(output != null);
    assertTrue(output.isLive(0));
    assertTrue(output.isLive(1));
  }

  @Test
  public void testFlowThroughTokenNameArguments() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);
    Node argumentsNode = Node.newName("arguments");
    LiveVariableLattice input = analysis.createEntryLattice();
    LiveVariableLattice output = analysis.flowThrough(argumentsNode, input);
    Preconditions.checkState(output != null);
    assertTrue(output.isLive(0)); // arguments[0]
    assertTrue(output.isLive(1)); // arguments[1]
    // Add more tests for arguments[N] where N > 1
  }
}