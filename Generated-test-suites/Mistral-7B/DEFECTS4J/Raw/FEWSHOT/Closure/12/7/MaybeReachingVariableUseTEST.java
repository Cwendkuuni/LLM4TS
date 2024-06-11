package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class MaybeReachingVariableUseTest {

  @Test
  public void testEmpty() {
    Scope jsScope = new Scope(null);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);

    AnalysisResult<ReachingUses> result = analysis.analyze();

    for (Var var : jsScope.getVars()) {
      assertNotNull(result.getResultMap().get(var));
    }
  }

  @Test
  public void testSimpleAssignment() {
    String jsCode = "var a = 1;";
    Scope jsScope = new Scope(jsCode);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph(jsCode);
    AbstractCompiler compiler = new Compiler();

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    AnalysisResult<ReachingUses> result = analysis.analyze();

    Collection<Node> uses = analysis.getUses("a", result.getResultMap().get(jsScope.getVar("a")));
    assertTrue(uses.isEmpty());
  }

  @Test
  public void testAssignmentThroughVariable() {
    String jsCode = "var a = 1;\n" +
        "var b = a;";
    Scope jsScope = new Scope(jsCode);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph(jsCode);
    AbstractCompiler compiler = new Compiler();

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    AnalysisResult<ReachingUses> result = analysis.analyze();

    Collection<Node> uses = analysis.getUses("a", result.getResultMap().get(jsScope.getVar("a")));
    assertTrue(uses.size() == 1);
    assertEquals(cfg.getNode(1), uses.iterator().next()); // the assignment node

    uses = analysis.getUses("b", result.getResultMap().get(jsScope.getVar("b")));
    assertTrue(uses.size() == 1);
    assertEquals(cfg.getNode(1), uses.iterator().next()); // the assignment node
  }

  @Test
  public void testAssignmentWithCondition() {
    String jsCode = "var a = 1;\n" +
        "if (true) {\n" +
        "  var a = 2;\n" +
        "}";
    Scope jsScope = new Scope(jsCode);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph(jsCode);
    AbstractCompiler compiler = new Compiler();

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    AnalysisResult<ReachingUses> result = analysis.analyze();

    Collection<Node> uses = analysis.getUses("a", result.getResultMap().get(jsScope.getVar("a")));
    assertTrue(uses.size() == 2);
    assertEquals(cfg.getNode(0), uses.iterator().next()); // the first assignment node
    assertEquals(cfg.getNode(3), uses.iterator().next()); // the second assignment node
  }

  @Test
  public void testLocalVariableUse() {
    String jsCode = "function foo() {\n" +
        "  var a = 1;\n" +
        "  alert(a);\n" +
        "}" +
        "foo();";
    Scope jsScope = new Scope(jsCode);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph(jsCode);
    AbstractCompiler compiler = new Compiler();

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    AnalysisResult<ReachingUses> result = analysis.analyze();

    Collection<Node> uses = analysis.getUses("a", result.getResultMap().get(jsScope.getVar("a")));
    assertTrue(uses.size() == 1);
    assertEquals(cfg.getNode(4), uses.iterator().next()); // the alert statement
  }

  @Test
  public void testEscapedVariableUse() {
    String jsCode = "function foo() {\n" +
        "  var a = 1;\n" +
        "  try {\n" +
        "    throw a;\n" +
        "  } catch (e) {\n" +
        "    alert(e);\n" +
        "  }\n" +
        "}" +
        "foo();";
    Scope jsScope = new Scope(jsCode);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph(jsCode);
    AbstractCompiler compiler = new Compiler();

    Set<Var> escaped = Sets.newHashSet();
    analysis.computeEscaped(jsScope, escaped, compiler);

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    AnalysisResult<ReachingUses> result = analysis.analyze();

    Collection<Node> uses = analysis.getUses("a", result.getResultMap().get(jsScope.getVar("a")));
    assertFalse(uses.isEmpty()); // the thrown exception is shown as a use
  }

  @Test
  public void testFunctionParameterUse() {
    String jsCode = "function foo(a) {\n" +
        "  alert(a);\n" +
        "}" +
        "foo(1);";
    Scope jsScope = new Scope(jsCode);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph(jsCode);
    AbstractCompiler compiler = new Compiler();

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    AnalysisResult<ReachingUses> result = analysis.analyze();

    Collection<Node> uses = analysis.getUses("a", result.getResultMap().get(jsScope.getVar("a")));
    assertTrue(uses.isEmpty()); // function parameters are not shown as uses
  }

  @Test
  public void testVarDeclarationUse() {
    String jsCode = "var a;\n" +
        "alert(a);";
    Scope jsScope = new Scope(jsCode);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph(jsCode);
    AbstractCompiler compiler = new Compiler();

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    AnalysisResult<ReachingUsages> result = analysis.analyze();

    Collection<Node> uses = analysis.getUses("a", result.getResultMap().get(jsScope.getVar("a")));
    assertTrue(uses.isEmpty()); // uninitialized variables are not shown as uses
  }

  @Test
  public void testLoopVariableUse() {
    String jsCode = "for (var i = 0; i < 10; i++) {\n" +
        "  alert(i);\n" +
        "}";
    Scope jsScope = new Scope(jsCode);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph(jsCode);
    AbstractCompiler compiler = new Compiler();

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    AnalysisResult<ReachingUses> result = analysis.analyze();

    Collection<Node> uses = analysis.getUses("i", result.getResultMap().get(jsScope.getVar("i")));
    assertTrue(uses.size() == 11); // the initialization, condition and increment nodes are also considered as uses
  }

  private void assertEquals(Node expected, Node actual) {
    if (expected == null && actual == null) {
      return;
    }
    if (expected == null || actual == null) {
      fail("Expected: " + expected + ", but was: " + actual);
    }
    if (expected.getClass() != actual.getClass()) {
      fail("Expected: " + expected.getClass() + ", but was: " + actual.getClass());
    }
    if (!expected.toString().equals(actual.toString())) {
      fail("Expected: " + expected.toString() + ", but was: " + actual.toString());
    }
  }
}