package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Sets;

import java.util.BitSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class LiveVariablesAnalysisTest {
  private ControlFlowGraph<Node> cfg;
  private Scope jsScope;
  private AbstractCompiler compiler;

  @Before
  public void setup() {
    // Initialize ControlFlowGraph, Scope, and AbstractCompiler for testing
    // Here, we assume you have a way to create them with appropriate setup.
  }

  @Test
  public void testLiveVariablesAnalysis_EmptyProgram() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    Set<Var> escapedLocals = analysis.getEscapedLocals();
    assertEquals(Sets.newHashSet(), escapedLocals);
  }

  @Test
  public void testLiveVariablesAnalysis_LocalVariables() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

    // Test for a simple local variable case
    analysis.computeLattice(NodeUtil.createProgram("var x; x = 1;"));
    Set<Var> escapedLocals = analysis.getEscapedLocals();
    Var x = jsScope.getVar("x");
    assertTrue(analysis.isLive(x));
    assertTrue(escapedLocals.contains(x));
  }

  @Test
  public void testLiveVariablesAnalysis_DeadLocalVariables() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

    // Test for a simple dead local variable case
    analysis.computeLattice(NodeUtil.createProgram("var x; x = 1; if (false) { var y; } x = 2;"));
    Set<Var> escapedLocals = analysis.getEscapedLocals();
    Var x = jsScope.getVar("x");
    assertTrue(analysis.isLive(x));
    assertFalse(escapedLocals.contains(x));

    Var y = jsScope.getVar("y");
    assertNull(jsScope.getVar("y"));
    assertFalse(analysis.isLive(y));
    assertFalse(escapedLocals.contains(y));
  }

  @Test
  public void testLiveVariablesAnalysis_EscapedLocalVariables() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

    // Test for a simple escaped local variable case
    analysis.computeLattice(NodeUtil.createProgram("function foo() { var x; return function() { x = 1; }; } var y = foo(); y(); }"));
    Set<Var> escapedLocals = analysis.getEscapedLocals();
    Var x = jsScope.getFunction("foo").getVariable("x");
    assertTrue(analysis.isLive(x));
    assertTrue(escapedLocals.contains(x));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLiveVariablesAnalysis_InvalidNode() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    analysis.flowThrough(NodeUtil.createProgram("1 + 1")
        .getFirstChild(), new LiveVariableLattice(cfg.getNodes().size()));
  }

  @Test
  public void testLiveVariablesAnalysis_ComplexCondition() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

    // Test for a complex condition statement
    analysis.computeLattice(NodeUtil.createProgram("if (x < 1 && y > 2) { var z; } x = 1; y = 2;"));
    Set<Var> escapedLocals = analysis.getEscapedLocals();
    Var x = jsScope.getVar("x");
    assertTrue(analysis.isLive(x));
    assertFalse(escapedLocals.contains(x));

    Var y = jsScope.getVar("y");
    assertTrue(analysis.isLive(y));
    assertFalse(escapedLocals.contains(y));

    Var z = jsScope.getVar("z");
    assertNull(jsScope.getVar("z"));
    assertFalse(analysis.isLive(z));
    assertFalse(escapedLocals.contains(z));
  }

  @Test
  public void testLiveVariablesAnalysis_FunctionCall() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

    // Test for a function call case
    analysis.computeLattice(NodeUtil.createProgram("function foo() { var x; } function bar() { foo(); } bar();"));
    Set<Var> escapedLocals = analysis.getEscapedLocals();
    Var x = jsScope.getFunction("foo").getVariable("x");
    assertTrue(analysis.isLive(x));
    assertTrue(escapedLocals.contains(x));
  }
}