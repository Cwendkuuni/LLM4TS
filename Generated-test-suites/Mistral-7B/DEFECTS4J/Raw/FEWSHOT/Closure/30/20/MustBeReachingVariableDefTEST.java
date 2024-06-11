package com.google.javascript.jscomp;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.graph.GraphNode;
import com.google.javascript.rhino.Node;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MustBeReachingVariableDefTest {

  @Test
  public void testMustDefJoin() {
    Var a = new Var("a");
    Var b = new Var("b");
    Var c = new Var("c");

    MustDef joinA = new MustDef();
    MustDef joinB = new MustDef();
    MustDef joinAB = new MustDefJoin().apply(joinA, joinB);

    assertSame(joinA, joinAB);

    joinA.reachingDef.put(a, new Definition(Node.createIntLit(0)));
    joinB.reachingDef.put(b, new Definition(Node.createIntLit(1)));

    MustDef joinAB2 = new MustDefJoin().apply(joinA, joinB);

    assertSame(joinAB, joinAB2);

    joinA.reachingDef.put(c, new Definition(Node.createIntLit(2)));

    MustDef joinAB3 = new MustDefJoin().apply(joinA, joinB);

    assertSame(joinAB, joinAB3);

    assertNull(joinAB.reachingDef.get(c));
  }

  @Test
  public void testBasicVariableDef() {
    Var a = new Var("a");
    Var b = new Var("b");

    Scope scope = new Scope();
    scope.setRootNode(Node.createBlock(Collections.emptyList()));
    scope.declare(a, Node.createVar("a"));
    scope.declare(b, Node.createVar("b"));

    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    AbstractCompiler compiler = new AbstractCompiler();

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        cfg, scope, compiler);

    Node useNode1 = Node.createGetProp(Node.createName("x"), "a");
    Node defNode1 = Node.createName("a");
    analysis.addToDefIfLocal("a", defNode1, null, analysis.createEntryLattice());

    Node useNode2 = Node.createGetProp(Node.createName("x"), "b");
    Node defNode2 = Node.createName("b");
    analysis.addToDefIfLocal("b", defNode2, null, analysis.createEntryLattice());

    Node useNode3 = Node.createGetProp(Node.createName("x"), "a");
    Node defNode3 = Node.createName("a");
    analysis.addToDefIfLocal("a", defNode3, null, analysis.createEntryLattice());

    Node useNode4 = Node.createGetProp(Node.createName("x"), "b");
    Node defNode4 = Node.createName("b");
    analysis.addToDefIfLocal("b", defNode4, null, analysis.createEntryLattice());

    Node useNode5 = Node.createGetProp(Node.createName("x"), "a");
    Node defNode5 = Node.createName("c");
    analysis.addToDefIfLocal("a", defNode5, null, analysis.createEntryLattice());

    assertSame(defNode1, analysis.getDef("a", useNode1));
    assertSame(defNode2, analysis.getDef("b", useNode2));
    assertNull(analysis.getDef("c", useNode5));

    Map<Var, Definition> expectedDefs = new HashMap<>();
    expectedDefs.put(a, new Definition(defNode1));
    expectedDefs.put(b, new Definition(defNode2));

    MustDef expectedOutput = new MustDef(expectedDefs.entrySet().iterator());
    MustDef actualOutput = analysis.compute(Node.createBlock(Collections.emptyList()));
    MustDefJoin join = new MustDefJoin();
    MustDef expectedJoinOutput = join.apply(analysis.createEntryLattice(), expectedOutput);

    assertSame(expectedJoinOutput, actualOutput);
  }

  @Test
  public void testVariableDefWithDependencies() {
    Var a = new Var("a");
    Var b = new Var("b");
    Var c = new Var("c");

    Scope scope = new Scope();
    scope.setRootNode(Node.createBlock(Collections.emptyList()));
    scope.declare(a, Node.createVar("a"));
    scope.declare(b, Node.createVar("b"));
    scope.declare(c, Node.createVar("c"));

    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    AbstractCompiler compiler = new AbstractCompiler();

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        cfg, scope, compiler);

    Node useNode1 = Node.createGetProp(Node.createName("x"), "a");
    Node defNode1 = Node.createName("a");
    analysis.addToDefIfLocal("a", defNode1, null, analysis.createEntryLattice());

    Node useNode2 = Node.createGetProp(Node.createName("x"), "b");
    Node defNode2 = Node.createName("b");
    analysis.addToDefIfLocal("b", defNode2, Node.createName("a"), analysis.createEntryLattice());

    Node useNode3 = Node.createGetProp(Node.createName("x"), "c");
    Node defNode3 = Node.createName("c");
    analysis.addToDefIfLocal("c", defNode3, null, analysis.createEntryLattice());

    Node useNode4 = Node.createGetProp(Node.createName("x"), "b");
    Node defNode4 = Node.createName("b");
    analysis.addToDefIfLocal("b", defNode4, null, analysis.createEntryLattice());

    Node useNode5 = Node.createGetProp(Node.createName("x"), "b");
    Node defNode5 = Node.createName("a");
    analysis.addToDefIfLocal("b", defNode5, null, analysis.createEntryLattice());

    assertSame(defNode1, analysis.getDef("a", useNode1));
    assertSame(defNode2, analysis.getDef("b", useNode2));
    assertNull(analysis.getDef("c", useNode3));

    Map<Var, Definition> expectedDefs = new HashMap<>();
    expectedDefs.put(a, new Definition(defNode1));
    expectedDefs.put(b, new Definition(defNode2));

    MustDef expectedOutput = new MustDef(expectedDefs.entrySet().iterator());
    MustDef actualOutput = analysis.compute(Node.createBlock(Collections.emptyList()));
    MustDefJoin join = new MustDefJoin();
    MustDef expectedJoinOutput = join.apply(analysis.createEntryLattice(), expectedOutput);

    assertSame(expectedJoinOutput, actualOutput);

    Definition expectedDefB = expectedDefs.get(b);
    assertNotNull(expectedDefB);
    assertEquals(Arrays.asList(a), expectedDefB.depends);
  }

  @Test
  public void testVariableDefWithDependenciesAndEscapedVariables() {
    Var a = new Var("a");
    Var b = new Var("b");
    Var c = new Var("c");
    Var d = new Var("d");

    Scope scope = new Scope();
    scope.setRootNode(Node.createBlock(Collections.emptyList()));
    scope.declare(a, Node.createVar("a"));
    scope.declare(b, Node.createVar("b"));
    scope.declare(c, Node.createVar("c"));
    scope.declare(d, Node.createVar("d"));

    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    AbstractCompiler compiler = new AbstractCompiler();

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        cfg, scope, compiler);

    analysis.escaped.add(a);

    Node useNode1 = Node.createGetProp(Node.createName("x"), "a");
    Node defNode1 = Node.createName("a");
    analysis.addToDefIfLocal("a", defNode1, null, analysis.createEntryLattice());

    Node useNode2 = Node.createGetProp(Node.createName("x"), "b");
    Node defNode2 = Node.createName("b");
    analysis.addToDefIfLocal("b", defNode2, null, analysis.createEntryLattice());

    Node useNode3 = Node.createGetProp(Node.createName("x"), "c");
    Node defNode3 = Node.createName("c");
    analysis.addToDefIfLocal("c", defNode3, null, analysis.createEntryLattice());

    Node useNode4 = Node.createGetProp(Node.createName("x"), "d");
    Node defNode4 = Node.createName("d");
    analysis.addToDefIfLocal("d", defNode4, null, analysis.createEntryLattice());

    Node useNode5 = Node.createGetProp(Node.createName("x"), "b");
    Node defNode5 = Node.createName("a");
    analysis.addToDefIfLocal("b", defNode5, null, analysis.createEntryLattice());

    assertSame(defNode1, analysis.getDef("a", useNode1));
    assertSame(defNode2, analysis.getDef("b", useNode2));
    assertNull(analysis.getDef("c", useNode3));
    assertNull(analysis.getDef("d", useNode4));

    Map<Var, Definition> expectedDefs = new HashMap<>();
    expectedDefs.put(a, new Definition(defNode1));
    expectedDefs.put(b, new Definition(defNode2));

    MustDef expectedOutput = new MustDef(expectedDefs.entrySet().iterator());
    MustDef actualOutput = analysis.compute(Node.createBlock(Collections.emptyList()));

    MustDefJoin join = new MustDefJoin();
    MustDef expectedJoinOutput = join.apply(analysis.createEntryLattice(), expectedOutput);

    assertSame(expectedJoinOutput, actualOutput);

    Definition expectedDefB = expectedDefs.get(b);
    assertNotNull(expectedDefB);
    assertEquals(ImmutableSet.of(a), expectedDefB.depends);
  }
}