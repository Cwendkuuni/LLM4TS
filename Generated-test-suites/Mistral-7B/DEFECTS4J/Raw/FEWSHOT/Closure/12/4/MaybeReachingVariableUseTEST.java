package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.jscomp.graph.DiGraphEdge;
import com.google.javascript.jscomp.graph.GraphNode;
import com.google.javascript.jscomp.graph.LatticeElement;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class MaybeReachingVariableUseTest {

  private static final String JS_CODE = "function foo() {" +
      "  var a = 1;" +
      "  var b = 2;" +
      "  if (1) {" +
      "    a = 2;" +
      "  } else {" +
      "    b = 3;" +
      "  }" +
      "  return a; // returns 2" +
      "}";

  @Test
  public void testReachingUses() {
    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(JS_CODE, jsScope);
    MaybeReachingVariableUse maybeReachingVariableUse =
        new MaybeReachingVariableUse(cfg, jsScope, null);

    List<Node> definitions = cfg.getTop().getSuccList();
    for (Node defNode : definitions) {
      Var var = jsScope.getVar("a");
      Collection<Node> uses = maybeReachingVariableUse.getUses("a", defNode);
      assertNotNull(uses);
      assertThat(uses).containsExactly(defNode);

      var = jsScope.getVar("b");
      uses = maybeReachingVariableUse.getUsages("b", defNode);
      assertNotNull(uses);
      if (defNode.getIndex() == 2) {
        // b = 3 is the last definition
        assertThat(uses).containsExactly(defNode, cfg.getNode(3));
      } else {
        // b = 2 is the first definition
        assertThat(uses).containsExactly(defNode);
      }
    }
  }

  @Test
  public void testEscapedVariables() {
    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>("function foo(a) {" +
        "  var x = a;" +
        "  return x; }", jsScope);
    MaybeReachingVariableUse maybeReachingVariableUse =
        new MaybeReachingVariableUse(cfg, jsScope, null);

    Var aVar = jsScope.getVar("a");
    Collection<Node> uses = maybeReachingVariableUse.getUsages("a", cfg.getNode(0));
    assertNotNull(uses);
    assertThat(uses).containsExactly(cfg.getNode(0));

    Var xVar = jsScope.getVar("x");
    uses = maybeReachingVariableUse.getUsages("x", cfg.getNode(0));
    assertNull(uses);
  }

  @Test
  public void testConditionalPaths() {
    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>("function foo() {" +
        "  var a = 1;" +
        "  if (1) {" +
        "    a = 2;" +
        "  } else {" +
        "    a = 3;" +
        "  }" +
        "  return a; // returns 2" +
        "}", jsScope);
    MaybeReachingVariableUse maybeReachingVariableUse =
        new MaybeReachingVariableUse(cfg, jsScope, null);

    List<Node> definitions = cfg.getTop().getSuccList();
    for (Node defNode : definitions) {
      Var aVar = jsScope.getVar("a");
      Collection<Node> uses = maybeReachingVariableUse.getUsages("a", defNode);
      assertNotNull(uses);
      if (defNode.getIndex() == 1) {
        // a = 2 is the definition on the conditionally executed path
        assertThat(uses).containsExactly(defNode, cfg.getNode(1));
      } else {
        // a = 3 is the definition on the other path
        assertThat(uses).containsExactly(defNode);
      }
    }
  }

  @Test
  public void testForLoops() {
    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>("function foo() {" +
        "  for (var i in [1, 2]) {" +
        "    if (i === 1) {" +
        "      i = 3;" +
        "    } else {" +
        "      i = 4;" +
        "    }" +
        "    print(i);" +
        "  }" +
        "}", jsScope);
    MaybeReachingVariableUse maybeReachingVariableUse =
        new MaybeReachingVariableUse(cfg, jsScope, null);

    List<Node> definitions = cfg.getTop().getSuccList();
    for (Node defNode : definitions) {
      Var iVar = jsScope.getVar("i");
      Collection<Node> uses = maybeReachingVariableUse.getUsages("i", defNode);
      assertNotNull(uses);
      if (defNode.getIndex() == 2 || defNode.getIndex() == 6) {
        // i = 3 and i = 4 are definitions on different paths
        assertThat(uses).containsExactly(defNode, cfg.getNode(2), cfg.getNode(6));
      } else {
        // i = 1 is the initial definition
        assertThat(uses).containsExactly(defNode, cfg.getNode(0));
      }
    }
  }

  @Test
  public void testAssignmentOps() {
    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>("function foo() {" +
        "  var a = 1;" +
        "  a += 'abc';" +
        "  print(a);" +
        "}", jsScope);
    MaybeReachingVariableUse maybeReachingVariableUse =
        new MaybeReachingVariableUse(cfg, jsScope, null);

    List<Node> definitions = cfg.getTop().getSuccList();
    for (Node defNode : definitions) {
      Var aVar = jsScope.getVar("a");
      Collection<Node> uses = maybeReachingVariableUse.getUsages("a", defNode);
      assertNotNull(uses);
      if (defNode.getIndex() == 2) {
        // a += 'abc' is the assignment
        assertThat(uses).containsExactly(defNode, cfg.getNode(1));
      } else {
        // a = 1 is the initial definition
        assertThat(uses).containsExactly(defNode);
      }
    }
  }

  @Test
  public void testAndOrExpressions() {
    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>("function foo() {" +
        "  if (1 && 2 && 3) {" +
        "    print('1 && 2 && 3 is true');" +
        "  } else {" +
        "    print('1 && 2 && 3 is false');" +
        "  }" +
        "}", jsScope);
    MaybeReachingVariableUse maybeReachingVariableUse =
        new MaybeReachingVariableUse(cfg, jsScope, null);

    List<Node> definitions = cfg.getTop().getSuccList();
    for (Node defNode : definitions) {
      Var unused = jsScope.getVar("unused");
      Collection<Node> uses = maybeReachingVariableUse.getUsages("unused", defNode);
      assertNull(uses);
    }

    definitions = cfg.getNode(1).getSuccList();
    for (Node defNode : definitions) {
      Var unused1 = jsScope.getVar("unused1");
      Collection<Node> uses = maybeReachingVariableUse.getUsages("unused1", defNode);
      assertNull(uses);

      Var unused2 = jsScope.getVar("unused2");
      uses = maybeReachingVariableUse.getUsages("unused2", defNode);
      assertNull(uses);

      Var unused3 = jsScope.getVar("unused3");
      uses = maybeReachingVariableUse.getUsages("unused3", defNode);
      assertNull(uses);

      Var cond1 = jsScope.getVar("cond1");
      uses = maybeReachingVariableUse.getUsages("cond1", defNode);
      assertEquals(1, uses.size());
      assertThat(uses).containsExactly(cfg.getNode(1));

      Var cond2 = jsScope.getVar("cond2");
      uses = maybeReachingVariableUse.getUsages("cond2", defNode);
      assertEquals(1, uses.size());
      assertThat(uses).containsExactly(cfg.getNode(2));

      Var cond3 = jsScope.getVar("cond3");
      uses = maybeReachingVariableUse.getUsages("cond3", defNode);
      assertEquals(1, uses.size());
      assertThat(uses).containsExactly(cfg.getNode(3));
    }
  }

  @Test
  public void testHooks() {
    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>("function foo() {" +
        "  if (/* ... */) {" +
        "    return; // return statement in the normal control flow path" +
        "  }" +
        "  hooks:" +
        "  if (/* ... */) {" +
        "    // return statement in the hook control flow path" +
        "    return; // <-- this one" +
        "  }" +
        "  if (/* ... */) {" +
        "    return; // return statement in another hook control flow path" +
        "  }" +
        "  print('foo');" +
        "}", jsScope);
    MaybeReachingVariableUse maybeReachingVariableUse =
        new MaybeReachingVariableUse(cfg, jsScope, null);

    List<Node> definitions = cfg.getTop().getSuccList();
    for (Node defNode : definitions) {
      Var unused = jsScope.getVar("unused");
      Collection<Node> uses = maybeReachingVariableUse.getUsages("unused", defNode);
      assertNull(uses);
    }

    definitions = cfg.getNode(2).getSuccList();
    for (Node defNode : definitions) {
      Var unused1 = jsScope.getVar("unused1");
      Collection<Node> uses = maybeReachingVariableUse.getUsages("unused1", defNode);
      assertNull(uses);

      Var unused2 = jsScope.getVar("unused2");
      uses = maybeReachingVariableUse.getUsages("unused2", defNode);
      assertNull(uses);

      Var cond1 = jsScope.getVar("cond1");
      uses = maybeReachingVariableUse.getUsages("cond1", defNode);
      assertEquals(1, uses.size());
      assertThat(uses).containsExactly(cfg.getNode(2));

      Var cond2 = jsScope.getVar("cond2");
      uses = maybeReachingVariableUse.getUsages("cond2", defNode);
      assertEquals(1, uses.size());
      assertThat(uses).containsExactly(cfg.getNode(3));

      Var cond3 = jsScope.getVar("cond3");
      uses = maybeReachingVariableUse.getUsages("cond3", defNode);
      assertEquals(1, uses.size());
      assertThat(uses).containsExactly(cfg.getNode(4));
    }
  }
}