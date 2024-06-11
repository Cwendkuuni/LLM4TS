package com.google.javascript.jscomp;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.Scope;

public class MustBeReachingVariableDefTest {
  private ControlFlowGraph<Node> cfg;
  private Scope jsScope;
  private AbstractCompiler compiler;
  private MustBeReachingVariableDef analysis;

  @Before
  public void setup() {
    // Set up test data.
    // Replace this with your setup code.
    jsScope = new Scope();
    compiler = new AbstractCompiler();
    cfg = new ControlFlowGraph<Node>(new Node("root"), new Node("entry"), new Node("exit"));
    analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
  }

  @Test
  public void testEmpty() {
    // Test empty case.
    // Replace this with your test code.
    MustDef expected = new MustDef();
    MustDef actual = analysis.createInitialEstimateLattice();
    assertSame(expected, actual);
  }

  @Test
  public void testSimpleAssignment() {
    // Test simple assignment case.
    // Replace this with your test code.
    Node n1 = new Node("n1");
    Node n2 = new Node("n2");
    Node assign = new Node("assign", Token.ASSIGN, n1, n2);

    analysis.flowThrough(assign, new MustDef());

    MustDef expected = new MustDef();
    expected.reachingDef.put(jsScope.getVar("n1"), new Definition(assign));

    MustDef actual = analysis.createEntryLattice();
    analysis.flowThrough(assign, actual);
    assertSame(expected, actual);
  }

  @Test
  public void testComplexControlFlow() {
    // Test complex control flow case.
    // Replace this with your test code.
    // Set up test nodes, edges, and control flow.
    // ...

    // Run analysis.
    MustDef expected = new MustDef();
    expected.reachingDef.put(jsScope.getVar("x"), new Definition(n2));
    expected.reachingDef.put(jsScope.getVar("y"), new Definition(n3));

    MustDef actual = analysis.createEntryLattice();
    analysis.flowThrough(n1, actual);
    analysis.flowThrough(n2, actual);
    analysis.flowThrough(n3, actual);
    analysis.flowThrough(n4, actual);
    analysis.flowThrough(n5, actual);
    analysis.flowThrough(n6, actual);

    assertSame(expected, actual);
  }

  @Test
  public void testEscapedVariables() {
    // Test escaped variables case.
    // Replace this with your test code.
    String escapedVarName = "escapedVar";
    Node escapeNode = new Node("escapeNode");
    Var escapedVar = jsScope.declareVar(escapedVarName, Node.ANY_NODE);
    jsScope.addToAncestors(escapedVar, escapeNode);

    Node useNode = new Node("useNode");
    analysis.flowThrough(useNode, new MustDef());
    Node defNode = analysis.getDef(escapedVarName, useNode);
    assertNull(defNode);
  }

  @Test
  public void testDependencies() {
    // Test dependencies case.
    // Replace this with your test code.
    String var1 = "var1";
    String var2 = "var2";

    Node assign1 = new Node("assign1", Token.ASSIGN, new Node(var1), new Node("constant1"));
    Node assign2 = new Node("assign2", Token.ASSIGN, new Node(var2), new Node(var1));

    analysis.flowThrough(assign1, new MustDef());
    analysis.flowThrough(assign2, new MustDef());

    MustDef expected = new MustDef();
    expected.reachingDef.put(jsScope.getVar(var1), new Definition(assign1));
    expected.reachingDef.put(jsScope.getVar(var2), new Definition(assign2));
    expected.reachingDef.get(jsScope.getVar(var1)).depends.add(jsScope.getVar(var2));

    MustDef actual = analysis.createEntryLattice();
    analysis.flowThrough(assign1, actual);
    analysis.flowThrough(assign2, actual);

    assertSame(expected, actual);
  }
}