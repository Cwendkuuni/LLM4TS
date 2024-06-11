package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.Set;

import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class LiveVariablesAnalysisTest {
  @Test
  public void testLiveVariablesAnalysis() {
    

    // Test empty case
    Scope scope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(scope.getRootNode());
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);
    LiveVariablesAnalysis.LiveVariableLattice entryLattice = analysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice initLattice = analysis.createInitialEstimateLattice();
    assertEquals(entryLattice, initLattice);

    // Test simple case
    Node script = Node.makeScript("var x = 1; var y = x;");
    cfg = new ControlFlowGraph<>(script);
    analysis = new LiveVariablesAnalysis(cfg, scope, null);
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(script, initLattice);
    LiveVariablesAnalysis.LiveVariableLattice expected = new LiveVariablesAnalysis.LiveVariableLattice(2);
    expected.liveSet.set(0);
    expected.liveSet.set(1);
    assertEquals(expected, result);

    // Test assignment case
    script = Node.makeScript("var x = 1; x = 2;");
    cfg = new ControlFlowGraph<>(script);
    analysis = new LiveVariablesAnalysis(cfg, scope, null);
    result = analysis.flowThrough(script, initLattice);
    expected = new LiveVariablesAnalysis.LiveVariableLattice(2);
    expected.liveSet.set(0);
    expected.liveSet.set(1);
    assertEquals(expected, result);

    // Test conditional case
    script = Node.makeScript("if (true) { var x = 1; } else { var y = 1; }");
    cfg = new ControlFlowGraph<>(script);
    analysis = new LiveVariablesAnalysis(cfg, scope, null);
    result = analysis.flowThrough(script, initLattice);
    expected = new LiveVariablesAnalysis.LiveVariableLattice(1);
    expected.liveSet.set(0);
    assertEquals(expected, result);

    // Test loop case
    script = Node.makeScript("for (var i = 0; i < 10; i++) {}");
    cfg = new ControlFlowGraph<>(script);
    analysis = new LiveVariablesAnalysis(cfg, scope, null);
    result = analysis.flowThrough(script, initLattice);
    expected = new LiveVariablesAnalysis.LiveVariableLattice(1);
    expected.liveSet.set(0);
    assertEquals(expected, result);

    // Test function case
    script = Node.makeScript("function foo() { var x = 1; }");
    cfg = new ControlFlowGraph<>(script);
    analysis = new LiveVariablesAnalysis(cfg, scope, null);
    result = analysis.flowThrough(script, initLattice);
    expected = new LiveVariablesAnalysis.LiveVariableLattice(1);
    expected.liveSet.set(0);
    assertEquals(expected, result);

    // Test exception case
    script = Node.makeScript("try { var x = 1; } catch (e) {}");
    cfg = new ControlFlowGraph<>(script);
    analysis = new LiveVariablesAnalysis(cfg, scope, null);
    result = analysis.flowThrough(script, initLattice);
    expected = new LiveVariablesAnalysis.LiveVariableLattice(1);
    expected.liveSet.set(0);
    assertEquals(expected, result);

    // Test argument case
    script = Node.makeScript("function foo(x) { var y = x; }");
    cfg = new ControlFlowGraph<>(script);
    analysis = new LiveVariablesAnalysis(cfg, scope, null);
    analysis.markAllParametersEscaped();
    result = analysis.flowThrough(Node.makeScript("foo(1);"), initLattice);
    expected = new LiveVariablesAnalysis.LiveVariableLattice(1);
    expected.liveSet.set(1);
    assertEquals(expected, result);

    // Test variable escaping case
    scope = new Scope();
    Node rootNode = Node.makeScript("function foo() { var x = function() { return x; }; }");
    cfg = new ControlFlowGraph<>(rootNode);
    analysis = new LiveVariablesAnalysis(cfg, scope, null);
    Set<Var> escapedLocals = analysis.getEscapedLocals();
    assertTrue(escapedLocals.isEmpty());
    analysis = new LiveVariablesAnalysis(cfg, scope, null);
    analysis.markAllParametersEscaped();
    result = analysis.flowThrough(Node.makeScript("foo();"), initLattice);
    expected = new LiveVariablesAnalysis.LiveVariableLattice(1);
    expected.liveSet.set(0);
    assertEquals(expected, result);
    escapedLocals = analysis.getEscapedLocals();
    assertEquals(1, escapedLocals.size());
    assertTrue(escapedLocals.contains(scope.getVar("x")));

    
  }
}