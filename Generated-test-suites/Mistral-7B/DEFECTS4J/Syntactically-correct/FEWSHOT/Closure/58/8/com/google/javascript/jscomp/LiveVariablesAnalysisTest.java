package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.Scope;

import java.util.BitSet;
import java.util.List;
import java.util.Set;

public class LiveVariablesAnalysisTest {

  @Test
  public void testIsForward() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
    assertFalse(analysis.isForward());
  }

  @Test
  public void testCreateEntryLattice() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
    LiveVariableLattice entryLattice = analysis.createEntryLattice();
    BitSet liveSet = entryLattice.liveSet;
    int numVars = jsScope.getVarCount();
    for (int i = 0; i < numVars; i++) {
      liveSet.set(i);
    }
    assertEquals(Integer.toBinaryString(numVars - 1), liveSet.toString());
  }

  @Test
  public void testCreateInitialEstimateLattice() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
    LiveVariableLattice initialEstimateLattice = analysis.createInitialEstimateLattice();
    BitSet liveSet = initialEstimateLattice.liveSet;
    int numVars = jsScope.getVarCount();
    for (int i = 0; i < numVars; i++) {
      liveSet.set(i);
    }
    assertEquals(Integer.toBinaryString(numVars - 1), liveSet.toString());
  }

  @Test
  public void testFlowThroughNodeWithNoAssignments() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
    Node node = Node.newIntLiteral(1);
    LiveVariableLattice input = analysis.createEntryLattice();
    LiveVariableLattice output = analysis.flowThrough(node, input);
    assertFalse(output.liveSet.isEmpty());
  }

  @Test
  public void testFlowThroughNodeWithAssignments() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
    Node lhs = Node.newName("a");
    Node rhs = Node.newIntLiteral(1);
    Node assignNode = Node.newAssignment(lhs, rhs);
    LiveVariableLattice input = analysis.createEntryLattice();
    LiveVariableLattice output = analysis.flowThrough(assignNode, input);
    assertTrue(output.isLive(getVarIndex("a")));
  }

  @Test
  public void testFlowThroughNodeWithConditionalAssignment() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
    Node condition = Node.newBooleanLiteral(true);
    Node lhs = Node.newName("a");
    Node rhs = Node.newIntLiteral(1);
    Node assignNode = Node.newConditionalAssignment(condition, lhs, rhs);
    LiveVariableLattice input = analysis.createEntryLattice();
    LiveVariableLattice output = analysis.flowThrough(assignNode, input);
    // The variable "a" is only alive when the condition is true.
    assertTrue(output.isLive(getVarIndex("a")) && condition.isTrue());
  }

  @Test
  public void testFlowThroughNodeWithMultipleAssignments() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
    Node lhs = Node.newName("a");
    Node rhs1 = Node.newIntLiteral(1);
    Node rhs2 = Node.newIntLiteral(2);
    Node assignNode1 = Node.newAssignment(lhs, rhs1);
    Node assignNode2 = Node.newAssignment(lhs, rhs2);
    LiveVariableLattice input = analysis.createEntryLattice();
    LiveVariableLattice output = analysis.flowThrough(assignNode2, output);
    LiveVariableLattice finalOutput = analysis.flowThrough(assignNode1, finalOutput);
    // The variable "a" is alive at the end of the assignments.
    assertTrue(finalOutput.isLive(getVarIndex("a")));
  }

  @Test
  public void testFlowThroughNodeWithMultipleConditionalAssignments() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
    Node condition1 = Node.newBooleanLiteral(false);
    Node condition2 = Node.newBooleanLiteral(true);
    Node lhs = Node.newName("a");
    Node rhs1 = Node.newIntLiteral(1);
    Node rhs2 = Node.newIntLiteral(2);
    Node assignNode1 = Node.newConditionalAssignment(condition1, lhs, rhs1);
    Node assignNode2 = Node.newConditionalAssignment(condition2, lhs, rhs2);
    LiveVariableLattice input = analysis.createEntryLattice();
    LiveVariableLattice output = analysis.flowThrough(assignNode2, input);
    // The variable "a" is only alive when the second condition is true.
    assertTrue(output.isLive(getVarIndex("a")) && condition2.isTrue());
    LiveVariableLattice finalOutput = analysis.flowThrough(assignNode1, finalOutput);
    // The variable "a" is only alive when both conditions are true.
    assertTrue(finalOutput.isLive(getVarIndex("a")) && condition1.isTrue() && condition2.isTrue());
  }

  @Test
  public void testFlowThroughNodeWithLoop() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
    Node condition = Node.newBooleanLiteral(true);
    Node lhs = Node.newName("a");
    Node assignNode = Node.newAssignment(lhs, Node.newName("i"));
    Node loopNode = Node.newWhile(condition, Node.newBlock(assignNode, Node.newEmptyStatement()));
    LiveVariableLattice input = analysis.createEntryLattice();
    LiveVariableLattice output = analysis.flowThrough(loopNode, input);
    // The variable "a" is alive at the end of the loop.
    assertTrue(output.isLive(getVarIndex("a")));
  }

  @Test
  public void testFlowThroughNodeWithFunctionCall() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
    Node functionCall = Node.newName("myFunction");
    LiveVariableLattice input = analysis.createEntryLattice();
    LiveVariableLattice output = analysis.flowThrough(functionCall, input);
    // Assumes all function arguments are live.
    assertTrue(output.liveSet.size() >= getScope().getVarCount());
  }

  @Test
  public void testFlowThroughNodeWithReturnStatement() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
    Node returnNode = Node.newReturnStatement(Node.newName("a"));
    LiveVariableLattice input = analysis.createEntryLattice();
    LiveVariableLattice output = analysis.flowThrough(returnNode, input);
    // The returned variable is alive at the end of the function.
    assertTrue(output.isLive(getVarIndex("a")));
  }

  @Test
  public void testComputeGenKill() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
    Node node = Node.newName("a");
    BitSet gen = new BitSet();
    BitSet kill = new BitSet();
    analysis.computeGenKill(node, gen, kill, false);
    // The variable "a" is always alive.
    assertTrue(gen.get(getVarIndex("a")) && !kill.get(getVarIndex("a")));
  }

  @Test
  public void testComputeGenKillWithConditionalAssignment() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
    Node condition = Node.newBooleanLiteral(true);
    Node lhs = Node.newName("a");
    Node rhs = Node.newIntLiteral(1);
    Node assignNode = Node.newConditionalAssignment(condition, lhs, rhs);
    BitSet gen = new BitSet();
    BitSet kill = new BitSet();
    analysis.computeGenKill(assignNode, gen, kill, false);
    // The variable "a" is only alive when the condition is true.
    assertTrue(gen.get(getVarIndex("a")) && condition.isTrue());
    assertFalse(kill.get(getVarIndex("a")));
  }

  @Test
  public void testComputeGenKillWithFunctionCall() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
    Node functionCall = Node.newName("myFunction");
    BitSet gen = new BitSet();
    BitSet kill = new BitSet();
    analysis.computeGenKill(functionCall, gen, kill, false);
    // Assumes all function arguments are live.
    assertTrue(gen.size() >= getScope().getVarCount());
    assertFalse(kill.isEmpty());
  }

  @Test
  public void testComputeGenKillWithLoop() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
    Node condition = Node.newBooleanLiteral(true);
    Node lhs = Node.newName("a");
    Node assignNode = Node.newAssignment(lhs, Node.newName("i"));
    Node loopNode = Node.newWhile(condition, Node.newBlock(assignNode, Node.newEmptyStatement()));
    BitSet gen = new BitSet();
    BitSet kill = new BitSet();
    analysis.computeGenKill(loopNode, gen, kill, false);
    // The variable "a" is alive at the end of the loop.
    assertTrue(gen.get(getVarIndex("a")));
    assertFalse(kill.get(getVarIndex("a")));
  }

  @Test
  public void testGetEscapedLocals() {
    ControlFlowGraph<Node> cfg = mock(ControlFlowGraph.class);
    Scope jsScope = mock(Scope.class);
    when(cfg.getOutEdges(Node.newName("myFunction"))).thenReturn(Arrays.asList(new DiGraphEdge<Node, Branch>(Node.newName("myFunction"), Node.newName("returnStatement"), Branch.ALWAYS)));
    when(jsScope.getVarCount()).thenReturn(1);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, null);
    Set<Var> escapedLocals = analysis.getEscapedLocals();
    assertFalse(escapedLocals.isEmpty());
  }

  private int getVarIndex(String var) {
    Scope jsScope = new Scope();
    return jsScope.getVar(var).index;
  }

  private Scope getScope() {
    return mock(Scope.class);
  }
}