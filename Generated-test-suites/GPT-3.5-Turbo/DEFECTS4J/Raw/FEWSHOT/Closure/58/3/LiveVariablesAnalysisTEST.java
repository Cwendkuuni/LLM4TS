package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

import java.util.Set;

public class LiveVariablesAnalysisTest {
  private static final String JS_CODE =
      "var a, b, c;                  \n" +
      "function foo() {              \n" +
      "  var d, e, a;                \n" +
      "  if (a) {                    \n" +
      "    var b = 10;               \n" +
      "    var f = b + 5;            \n" +
      "  } else {                    \n" +
      "    var c = 20;               \n" +
      "    var f = c + 10;           \n" +
      "  }                           \n" +
      "  var g = f + 5;              \n" +
      "}                             \n";

  private Compiler compiler;
  private ControlFlowGraph<Node> cfg;
  private Scope jsScope;
  private LiveVariablesAnalysis analysis;
  private Node root;

  @Before
  public void setUp() {
    compiler = new Compiler(System.err);
    compiler.initOptions(new CompilerOptions());
    Node script = compiler.parseTestCode(JS_CODE);
    root = IR.root(script);
    Node externs = IR.root();
    compiler.compile(externs, root);
    cfg = (new SyntacticScopeCreator(compiler)).createCFG(script);
    jsScope = SyntacticScopeCreator.getScope(compiler, script);
    analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
  }

  @Test
  public void testGetEscapedLocals() {
    Set<Var> escapedLocals = analysis.getEscapedLocals();
    Set<Var> expectedLocals = Sets.newHashSet();
    expectedLocals.add(findVariable("a", getVarCount()));
    expectedLocals.add(findVariable("b", getVarCount()));
    expectedLocals.add(findVariable("c", getVarCount()));

    assertEquals(expectedLocals, escapedLocals);
  }

  @Test
  public void testGetVarIndex() {
    assertEquals(0, analysis.getVarIndex("a"));
    assertEquals(1, analysis.getVarIndex("b"));
    assertEquals(2, analysis.getVarIndex("c"));
    assertEquals(3, analysis.getVarIndex("d"));
    assertEquals(4, analysis.getVarIndex("e"));
    assertEquals(5, analysis.getVarIndex("f"));
    assertEquals(6, analysis.getVarIndex("g"));
  }

  @Test
  public void testIsForward() {
    assertFalse(analysis.isForward());
  }

  @Test
  public void testCreateEntryLattice() {
    LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.createEntryLattice();
    BitSet liveSet = new BitSet(getVarCount());
    LiveVariablesAnalysis.LiveVariableLattice expectedLattice =
        new LiveVariablesAnalysis.LiveVariableLattice(liveSet);

    assertEquals(expectedLattice, lattice);
  }

  @Test
  public void testCreateInitialEstimateLattice() {
    LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.createInitialEstimateLattice();
    BitSet liveSet = new BitSet(getVarCount());
    LiveVariablesAnalysis.LiveVariableLattice expectedLattice =
        new LiveVariablesAnalysis.LiveVariableLattice(liveSet);

    assertEquals(expectedLattice, lattice);
  }

  @Test
  public void testFlowThrough() {
    Node declaration = findDeclaration("a", root.getFirstChild().getFirstChild());
    LiveVariablesAnalysis.LiveVariableLattice inputLattice =
        analysis.createInitialEstimateLattice();
    setBit(inputLattice, 0);

    LiveVariablesAnalysis.LiveVariableLattice flowLattice =
        analysis.flowThrough(declaration, inputLattice);
    LiveVariablesAnalysis.LiveVariableLattice expectedLattice =
        analysis.createInitialEstimateLattice();
    expectedLattice.liveSet.set(0);

    assertEquals(expectedLattice, flowLattice);
  }

  @Test
  public void testMarkAllParametersEscaped() {
    analysis.markAllParametersEscaped();

    Set<Var> escapedLocals = analysis.getEscapedLocals();
    Set<Var> expectedLocals = Sets.newHashSet();
    expectedLocals.add(findVariable("a", getVarCount()));
    expectedLocals.add(findVariable("b", getVarCount()));
    expectedLocals.add(findVariable("c", getVarCount()));

    assertEquals(expectedLocals, escapedLocals);
  }

  @Test
  public void testIsArgumentsName() {
    Node varDecl = findDeclaration("a", root.getFirstChild().getFirstChild());

    assertFalse(analysis.isArgumentsName(varDecl.getFirstChild()));
  }

  private Var findVariable(String name, int varCount) {
    for (Var var : jsScope.getVars()) {
      if (varCount < jsScope.getVarCount()) {
        if (var.getName().equals(name)) {
          return var;
        }
      }
    }
    return null;
  }

  private int getVarCount() {
    return jsScope.getVarCount();
  }

  private void setBit(LiveVariablesAnalysis.LiveVariableLattice lattice, int index) {
    lattice.liveSet.set(index);
  }

  private Node findDeclaration(String varName, Node scope) {
    if (scope.isVar()) {
      for (Node child : scope.children()) {
        if (child.getFirstChild().getString().equals(varName)) {
          return child;
        }
      }
    }
    for (Node child : scope.children()) {
      Node result = findDeclaration(varName, child);
      if (result != null) {
        return result;
      }
    }
    return null;
  }
}