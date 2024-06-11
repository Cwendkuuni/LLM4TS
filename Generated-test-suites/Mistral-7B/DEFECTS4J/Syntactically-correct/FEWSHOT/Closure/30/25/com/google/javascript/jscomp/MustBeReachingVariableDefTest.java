package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.common.collect.Sets;

import java.util.Map;
import java.util.Set;

public class MustBeReachingVariableDefTest {

  @Test
  public void testAddToDefIfLocal() {
    Scope scope = new Scope(null);
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        new ControlFlowGraph<Node>(null), scope, null);

    String variable1 = "var1";
    String variable2 = "var2";

    analysis.addToDefIfLocal(variable1, null, null, new MustDef());
    analysis.addToDefIfLocal(variable2, null, null, new MustDef());

    MustDef expected = new MustDef();
    expected.reachingDef.put(scope.getVar(variable1), null);
    expected.reachingDef.put(scope.getVar(variable2), null);

    assertEquals(expected, analysis.createEntryLattice());
  }

  @Test
  public void testAddToDefIfLocalWithDefinition() {
    Scope scope = new Scope(null);
    Node definitionNode = new Node();

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        new ControlFlowGraph<Node>(null), scope, null);

    String variable = "variable";

    analysis.addToDefIfLocal(variable, null, definitionNode, new MustDef());

    Definition expectedDefinition = new Definition(definitionNode);
    MustDef expected = new MustDef();
    expected.reachingDef.put(scope.getVar(variable), expectedDefinition);

    assertEquals(expected, analysis.createEntryLattice());
  }

  @Test
  public void testAddToDefIfLocalWithDependency() {
    Scope scope = new Scope(null);
    Node dependencyNode = new Node();

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        new ControlFlowGraph<Node>(null), scope, null);

    String variable = "variable";

    analysis.addToDefIfLocal(variable, null, null, new MustDef());
    analysis.addToDefIfLocal(variable, dependencyNode, null, new MustDef());

    MustDef expected = new MustDef();
    expected.reachingDef.put(scope.getVar(variable), null);

    assertNull(expected.reachingDef.get(scope.getVar(variable)).depends);

    analysis.computeDependence(
        new Definition(analysis.createEntryLattice().reachingDef.get(scope.getVar(variable))),
        dependencyNode);

    Set<Var> expectedDependencies = Sets.newHashSet(scope.getVar(variable));
    expectedDependencies.addAll(analysis.createEntryLattice().reachingDef.keySet());

    assertEquals(expectedDependencies, analysis.createEntryLattice().reachingDef.get(scope.getVar(variable)).depends);
  }

  @Test
  public void testEscapeParameters() {
    Scope scope = new Scope(null);
    Node functionNode = new Node();

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        new ControlFlowGraph<Node>(null), scope, null);

    for (Var variable : scope.getVars()) {
      if (variable.isParameter()) {
        analysis.escapeParameters(new MustDef());
      }
    }

    MustDef expected = new MustDef();

    for (Var variable : scope.getVars()) {
      if (!variable.isParameter()) {
        expected.reachingDef.put(variable, null);
      }
    }

    assertEquals(expected, analysis.createEntryLattice());
  }
}