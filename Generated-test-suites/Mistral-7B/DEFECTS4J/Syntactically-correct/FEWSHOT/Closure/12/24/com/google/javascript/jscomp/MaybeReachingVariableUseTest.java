package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

public class MaybeReachingVariableUseTest {

  private ControlFlowGraph<Node> cfg;
  private Scope jsScope;

  @Before
  public void setup() {
    jsScope = new Scope();
    jsScope.declareLocal("x", VarType.NUMBER, 0);
    jsScope.declareLocal("y", VarType.NUMBER, 1);
    jsScope.declareLocal("z", VarType.NUMBER, 2);

    cfg = new ControlFlowGraph<Node>(new Node("start"), new Node("end"));
  }

  @Test
  public void testEmpty() {
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);
    ReachingUses result = analysis.computeAnalysis(analysis.createEntryLattice());
    assertNull(result.mayUseMap);
  }

  @Test
  public void testName() {
    Node nameNode = new Node("name");
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);

    analysis.computeMayUse(nameNode, new Node("root"), analysis.createEntryLattice(), false);
    ReachingUses result = analysis.computeAnalysis(analysis.createEntryLattice());
    Multimap<Var, Node> mayUseMap = result.mayUseMap;

    Var xVar = jsScope.getVar("x");
    Var yVar = jsScope.getVar("y");
    Var zVar = jsScope.getVar("z");

    assertNull(mayUseMap.get(xVar));
    assertNull(mayUseMap.get(yVar));
    assertNull(mayUseMap.get(zVar));
  }

  @Test
  public void testNameWithUses() {
    Node nameNode = new Node("name");
    Node assignmentNode = new Node("name = 1");
    Node useNode = new Node("print(name)");

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);

    analysis.computeMayUse(nameNode, new Node("root"), analysis.createEntryLattice(), false);
    analysis.computeMayUse(assignmentNode, assignmentNode, analysis.createEntryLattice(), false);
    analysis.computeMayUse(useNode, new Node("root"), analysis.createEntryLattice(), false);

    ReachingUses result = analysis.computeAnalysis(analysis.createEntryLattice());

    Multimap<Var, Node> mayUseMap = result.mayUseMap;

    Var xVar = jsScope.getVar("x");
    Var yVar = jsScope.getVar("y");
    Var zVar = jsScope.getVar("z");

    Set<Node> uses = analysis.getUses("name", useNode);
    assertNull(mayUseMap.get(xVar));
    assertNull(mayUseMap.get(yVar));
    assertNull(mayUseMap.get(zVar));
    assertThat(uses).containsExactly(useNode);
  }

  @Test
  public void testIf() {
    Node ifNode = new Node("if (x > 1)");
    Node assignmentNode = new Node("x = 2");
    Node useNode = new Node("print(x)");

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);

    analysis.computeMayUse(ifNode, new Node("root"), analysis.createEntryLattice(), false);
    analysis.computeMayUse(assignmentNode, assignmentNode, analysis.createEntryLattice(), false);
    analysis.computeMayUse(useNode, new Node("root"), analysis.createEntryLattice(), false);

    ReachingUses result = analysis.computeAnalysis(analysis.createEntryLattice());

    Multimap<Var, Node> mayUseMap = result.mayUseMap;

    Var xVar = jsScope.getVar("x");
    Var yVar = jsScope.getVar("y");
    Var zVar = jsScope.getVar("z");

    Set<Node> uses = analysis.getUses("x", useNode);
    assertNull(mayUseMap.get(xVar));
    assertNull(mayUseMap.get(yVar));
    assertNull(mayUseMap.get(zVar));
    assertThat(uses).containsExactly(useNode);
  }

  @Test
  public void testFor() {
    Node forNode = new Node("for (x in y)");
    Node assignmentNode = new Node("x = 2");
    Node useNode = new Node("print(x)");

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);

    analysis.computeMayUse(forNode, new Node("root"), analysis.createEntryLattice(), false);
    analysis.computeMayUse(assignmentNode, assignmentNode, analysis.createEntryLattice(), false);
    analysis.computeMayUse(useNode, new Node("root"), analysis.createEntryLattice(), false);

    ReachingUses result = analysis.computeAnalysis(analysis.createEntryLattice());

    Multimap<Var, Node> mayUseMap = result.mayUseMap;

    Var xVar = jsScope.getVar("x");
    Var yVar = jsScope.getVar("y");
    Var zVar = jsScope.getVar("z");

    Set<Node> uses = analysis.getUses("x", useNode);
    assertNull(mayUseMap.get(xVar));
    assertNull(mayUseMap.get(yVar));
    assertNull(mayUseMap.get(zVar));
    assertThat(uses).containsExactly(useNode);
  }
}