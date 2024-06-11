package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.graph.DiGraph.DiGraphEdge;
import com.google.javascript.jscomp.graph.GraphNode;
import com.google.javascript.jscomp.graph.LatticeElement;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class MaybeReachingVariableUseTest {
  @Test
  public void testBasicUsage() {
    Scope scope = new Scope();
    String code = "var a = 1;\n" + "var b = a;\n" + "var c = 2;\n" + "a = c;";
    ControlFlowGraph cfg = new ControlFlowGraph(code, scope);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);

    ReachingUses result = analysis.compute();

    // The first definition of 'a' should be reachable from all uses.
    Collection<Node> usesOfA = analysis.getUses("a", cfg.getEntryNode());
    assertTrue(usesOfA.containsAll(Sets.newHashSet(cfg.getNode(1), cfg.getNode(3))));

    // No other variables should be reachable from 'a'.
    assertNull(analysis.getUses("b", cfg.getNode(1)));
    assertNull(analysis.getUses("c", cfg.getNode(1)));

    // The definition of 'b' should be reachable from its use.
    Collection<Node> usesOfB = analysis.getUses("b", cfg.getNode(2));
    assertTrue(usesOfB.contains(cfg.getNode(2)));

    // No other definitions of 'b' should be reachable from its use.
    assertNull(analysis.getUses("a", cfg.getNode(2)));
  }

  @Test
  public void testLoop() {
    Scope scope = new Scope();
    String code = "var a = 1;\n" + "while (true) {\n" + "  var a = a + 1;\n" + "}";
    ControlFlowGraph cfg = new ControlFlowGraph(code, scope);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);

    ReachingUses result = analysis.compute();

    // The definition of 'a' in the loop header is reachable from all uses.
    Collection<Node> usesOfALoopHeader = analysis.getUses("a", cfg.getNode(1));
    assertTrue(usesOfALoopHeader.containsAll(Sets.newHashSet(cfg.getNode(1))));

    // No other definition of 'a' should be reachable from its use.
    assertNull(analysis.getUses("a", cfg.getNode(1)));
  }

  @Test
  public void testConditional() {
    Scope scope = new Scope();
    String code = "var a = 1;\n" + "if (true) {\n" + "  var b = a;\n" + "} else {\n" + "  var a = 2;\n" + "}";
    ControlFlowGraph cfg = new ControlFlowGraph(code, scope);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);

    ReachingUses result = analysis.compute();

    // The definition of 'a' in the 'if' branch is reachable from uses in the 'if' branch.
    Collection<Node> usesOfAIfBranch = analysis.getUses("a", cfg.getNode(1));
    assertTrue(usesOfAIfBranch.containsAll(Sets.newHashSet(cfg.getNode(1))));

    // The definition of 'a' in the 'else' branch is reachable from uses in the 'else' branch.
    Collection<Node> usesOfAElseBranch = analysis.getUses("a", cfg.getNode(3));
    assertTrue(usesOfAElseBranch.containsAll(Sets.newHashSet(cfg.getNode(3))));

    // No definition of 'a' should be reachable from uses in the other branch.
    assertNull(analysis.getUses("a", cfg.getNode(2)));
  }

  @Test
  public void testFunction() {
    Scope scope = new Scope();
    String code = "function foo() {\n" + "  var a = 1;\n" + "  return a;\n" + "}";
    ControlFlowGraph cfg = new ControlFlowGraph(code, scope);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);

    ReachingUses result = analysis.compute();

    // The definition of 'a' in the function should be reachable from uses in the function.
    Collection<Node> usesOfAFunction = analysis.getUses("a", cfg.getNode(1));
    assertTrue(usesOfAFunction.containsAll(Sets.newHashSet(cfg.getNode(1))));

    // No definition of 'a' should be reachable from uses outside the function.
    assertNull(analysis.getUses("a", cfg.getEntryNode()));
  }

  @Test
  public void testEscape() {
    Scope scope = new Scope();
    String code = "function foo(x) {\n" + "  var a = x;\n" + "  try {\n" + "    x = 2;\n" + "    return a;\n" + "  } finally {\n" + "    x = 3;\n" + "  } \n" + "}";
    ControlFlowGraph cfg = new ControlFlowGraph(code, scope);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);

    ReachingUses result = analysis.compute();

    // 'x' is an escaped variable, so it should not be included in the result.
    assertNull(analysis.getUses("x", cfg.getNode(1)));
  }

  @Test
  public void testNonLocal() {
    Scope scope = new Scope();
    String code = "function foo(x) {\n" + "  var a = x;\n" + "  return function(y) {\n" + "    a = y;\n" + "    return a;\n" + "  }();\n" + "}";
    ControlFlowGraph cfg = new ControlFlowGraph(code, scope);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);

    ReachingUses result = analysis.compute();

    // 'a' should not be reachable from uses of the inner function.
    assertNull(analysis.getUses("a", cfg.getNode(2)));
  }

  @Test
  public void testVarInFor() {
    Scope scope = new Scope();
    String code = "for (var i = 0; i < 10; i++) {\n" + "  var j = i;\n" + "  // some computations\n" + "}";
    ControlFlowGraph cfg = new ControlFlowGraph(code, scope);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);

    ReachingUses result = analysis.compute();

    // 'i' should not be reachable from uses of 'j'.
    assertNull(analysis.getUses("i", cfg.getNode(2)));
  }

  @Test
  public void testVarInForIn() {
    Scope scope = new Scope();
    String code = "for (var i in ['a', 'b']) {\n" + "  var j = i;\n" + "  // some computations\n" + "}";
    ControlFlowGraph cfg = new ControlFlowGraph(code, scope);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);

    ReachingUses result = analysis.compute();

    // 'i' should not be reachable from uses of 'j'.
    assertNull(analysis.getUses("i", cfg.getNode(2)));
  }

  @Test
  public void testNoDef() {
    Scope scope = new Scope();
    String code = "var a = 1;\n" + "var b = a;\n" + "alert(c);";
    ControlFlowGraph cfg = new ControlFlowGraph(code, scope);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);

    ReachingUses result = analysis.compute();

    // There is no definition for 'c', so no uses of 'c' should be reported.
    assertNull(analysis.getUses("c", cfg.getNode(3)));
  }

  @Test
  public void testUnreachable() {
    Scope scope = new Scope();
    String code = "function foo() {\n" + "  var a = 1;\n" + "  return;\n" + "  var b = 2;\n" + "}";
    ControlFlowGraph cfg = new ControlFlowGraph(code, scope);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);

    ReachingUses result = analysis.compute();

    // 'b' is unreachable, so no uses of 'b' should be reported.
    assertNull(analysis.getUses("b", cfg.getNode(1)));
  }

  @Test
  public void testMultipleDefs() {
    Scope scope = new Scope();
    String code = "function foo() {\n" + "  var a = 1;\n" + "  var a = 2;\n" + "  return a;\n" + "}";
    ControlFlowGraph cfg = new ControlFlowGraph(code, scope);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);

    ReachingUses result = analysis.compute();

    // There should be one use of the first definition of 'a'.
    Collection<Node> usesOfAAFirst = analysis.getUses("a", cfg.getNode(1));
    assertEquals(1, usesOfAAFirst.size());
    assertEquals(cfg.getNode(1), usesOfAAFirst.iterator().next());

    // There should be no use of the second definition of 'a'.
    Collection<Node> usesOfAASecond = analysis.getUses("a", cfg.getNode(2));
    assertNull(usesOfAASecond);
  }
}