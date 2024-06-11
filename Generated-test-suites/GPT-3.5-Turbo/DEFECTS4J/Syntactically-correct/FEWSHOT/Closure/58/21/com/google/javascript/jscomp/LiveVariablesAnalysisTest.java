
package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.BitSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class LiveVariablesAnalysisTest {

  private ControlFlowGraph<Node> cfg;
  private Scope jsScope;
  private AbstractCompiler compiler;
  private LiveVariablesAnalysis analysis;

  @Before
  public void setUp() {
    cfg = createControlFlowGraph();
    jsScope = createScope();
    compiler = createCompiler();
    analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
  }

  @Test
  public void testConstructor() {
    assertNotNull(analysis);
  }

  @Test
  public void testGetEscapedLocals() {
    Set<Var> escapedLocals = analysis.getEscapedLocals();
    assertNotNull(escapedLocals);
  }

  @Test
  public void testGetVarIndex() {
    assertEquals(0, analysis.getVarIndex("a"));
  }

  @Test
  public void testIsForward() {
    assertFalse(analysis.isForward());
  }

  @Test
  public void testCreateEntryLattice() {
    LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.createEntryLattice();
    assertNotNull(lattice);
  }

  @Test
  public void testCreateInitialEstimateLattice() {
    LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.createInitialEstimateLattice();
    assertNotNull(lattice);
  }

  @Test
  public void testFlowThrough() {
    Node node = cfg.getEntry().getValue();
    LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(1);
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(node, input);
    assertNotNull(result);
  }

  @Test
  public void testComputeGenKill() {
    Node node = cfg.getEntry().getValue();
    BitSet gen = new BitSet();
    BitSet kill = new BitSet();
    boolean conditional = false;
    analysis.computeGenKill(node, gen, kill, conditional);
    assertNotNull(gen);
    assertNotNull(kill);
  }

  @Test
  public void testAddToSetIfLocal() {
    Node node = new Node(Token.NAME, new Node(Token.STRING, "a"));
    BitSet set = new BitSet();
    analysis.addToSetIfLocal(node, set);
    assertNotNull(set);
  }

  @Test
  public void testMarkAllParametersEscaped() {
    analysis.markAllParametersEscaped();
    Set<Var> escapedLocals = analysis.getEscapedLocals();
    assertNotNull(escapedLocals);
  }

  @Test
  public void testIsArgumentsName() {
    Node node = new Node(Token.NAME, new Node(Token.STRING, "arguments"));
    boolean result = analysis.isArgumentsName(node);
    assertFalse(result);
  }

  private ControlFlowGraph<Node> createControlFlowGraph() {
    // TODO: Create the control flow graph for testing purposes.
    return null;
  }

  private Scope createScope() {
    // TODO: Create the scope for testing purposes.
    return null;
  }

  private AbstractCompiler createCompiler() {
    // TODO: Create the compiler for testing purposes.
    return null;
  }
}
