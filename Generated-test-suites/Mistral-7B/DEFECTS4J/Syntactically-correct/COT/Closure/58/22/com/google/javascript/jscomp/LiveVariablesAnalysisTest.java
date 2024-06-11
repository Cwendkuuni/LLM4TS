package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class LiveVariablesAnalysisTest {

  private ControlFlowGraph<Node> cfg;
  private Scope jsScope;
  private LiveVariablesAnalysis liveVariablesAnalysis;

  @Before
  public void setup() {
    // Initialize the ControlFlowGraph and Scope for testing.
    // Replace this with your actual setup code.
    Node rootNode = Node.createTree("function test() {var a = 1; var b = 2;}");
    cfg = new ControlFlowGraph<Node>(rootNode);
    jsScope = new Scope(rootNode.getChild(1));
  }

  @Test
  public void testLiveVariablesAnalysis() {
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, null);
    Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();
    assertEquals(0, escapedLocals.size());

    LiveVariablesAnalysis liveVariablesAnalysisWithArguments =
        new LiveVariablesAnalysis(cfg, jsScope, null);
    Set<Var> escapedLocalsWithArguments = liveVariablesAnalysisWithArguments.getEscapedLocals();
    assertEquals(1, escapedLocalsWithArguments.size());
    assertEquals(jsScope.getVar("arguments"), escapedLocalsWithArguments.iterator().next());
  }

  @Test
  public void testCreateEntryLattice() {
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, null);
    LiveVariableLattice entryLattice = liveVariablesAnalysis.createEntryLattice();
    BitSet liveSet = entryLattice.liveSet;
    assertEquals(jsScope.getVarCount(), liveSet.size());
    assertTrue(liveSet.isEmpty());
  }

  @Test
  public void testCreateInitialEstimateLattice() {
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, null);
    LiveVariableLattice initialEstimateLattice = liveVariablesAnalysis.createInitialEstimateLattice();
    BitSet liveSet = initialEstimateLattice.liveSet;
    assertEquals(jsScope.getVarCount(), liveSet.size());
    assertTrue(liveSet.isEmpty());
  }

  @Test
  public void testIsForward() {
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, null);
    assertFalse(liveVariablesAnalysis.isForward());
  }

  @Test
  public void testGetVarIndex() {
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, null);
    int indexA = liveVariablesAnalysis.getVarIndex("a");
    int indexB = liveVariablesAnalysis.getVarIndex("b");
    assertEquals(indexA, jsScope.getVar("a").index);
    assertEquals(indexB, jsScope.getVar("b").index);
  }

  @Test
  public void testComputeEscapedLocals() {
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, null);
    Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();
    assertEquals(0, escapedLocals.size());

    Scope newScope = new Scope(Node.createTree("function test() {function innerFunction() {} }"));
    LiveVariablesAnalysis liveVariablesAnalysisWithInnerFunction =
        new LiveVariablesAnalysis(cfg, newScope, null);
    Set<Var> escapedLocalsWithInnerFunction = liveVariablesAnalysisWithInnerFunction.getEscapedLocals();
    assertEquals(1, escapedLocalsWithInnerFunction.size());
    assertEquals(newScope.getVar("innerFunction"), escapedLocalsWithInnerFunction.iterator().next());
  }

  @Test
  public void testFlowThrough_Assignment() {
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, null);

    LiveVariableLattice input = liveVariablesAnalysis.createEntryLattice();
    LiveVariableLattice output = liveVariablesAnalysis.flowThrough(
        Node.createTree("var a = 1;"), input);
    BitSet liveSet = output.liveSet;
    assertTrue(liveSet.get(jsScope.getVar("a").index));
  }

  @Test
  public void testFlowThrough_Condition() {
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, null);

    LiveVariableLattice input = liveVariablesAnalysis.createEntryLattice();
    LiveVariableLattice output = liveVariablesAnalysis.flowThrough(
        Node.createTree("if (condition) { var a = 1; }"), input);
    BitSet liveSet = output.liveSet;
    assertTrue(liveSet.get(jsScope.getVar("a").index));
  }

  @Test
  public void testFlowThrough_Loop() {
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, null);

    LiveVariableLattice input = liveVariablesAnalysis.createEntryLattice();
    LiveVariableLattice output = liveVariablesAnalysis.flowThrough(
        Node.createTree("for (var i = 0; i < 10; i++) { var a = i; }"), input);
    BitSet liveSet = output.liveSet;
    for (int i = 0; i < 11; i++) {
      assertTrue(liveSet.get(jsScope.getVar("i").index));
      assertTrue(liveSet.get(jsScope.getVar("a").index));
    }
  }

  @Test
  public void testFlowThrough_Return() {
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, null);

    LiveVariableLattice input = liveVariablesAnalysis.createEntryLattice();
    LiveVariableLattice output = liveVariablesAnalysis.flowThrough(
        Node.createTree("function test() { return 1; }"), input);
    BitSet liveSet = output.liveSet;
    assertTrue(liveSet.isEmpty());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFlowThrough_NullInput() {
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, null);
    liveVariablesAnalysis.flowThrough(null, liveVariablesAnalysis.createEntryLattice());
  }
}