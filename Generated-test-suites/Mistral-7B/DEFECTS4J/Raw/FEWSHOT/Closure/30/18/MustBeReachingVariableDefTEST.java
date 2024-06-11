package com.google.javascript.jscomp;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.GraphNode;
import com.google.javascript.rhino.Node;

import java.util.HashMap;
import java.util.Map;

@RunWith(JUnit4.class)
public class MustBeReachingVariableDefTest {

  private static final Class<?> CLASS_UNDER_TEST = MustBeReachingVariableDef.class;

  @Test
  public void testCreateEntryLattice() {
    Scope scope = new Scope(new NodeFactory());
    MustBeReachingVariableDef dataFlowAnalysis =
        new MustBeReachingVariableDef(null, scope, null);

    MustDef expected = new MustDef();
    MustDef actual = dataFlowAnalysis.createEntryLattice();

    assertEquals(expected, actual);
  }

  @Test
  public void testCreateInitialEstimateLattice() {
    Scope scope = new Scope(new NodeFactory());
    MustBeReachingVariableDef dataFlowAnalysis =
        new MustBeReachingVariableDef(null, scope, null);

    MustDef expected = new MustDef();
    MustDef actual = dataFlowAnalysis.createInitialEstimateLattice();

    assertEquals(expected, actual);
  }

  @Test
  public void testFlowThrough() {
    Scope scope = new Scope(new NodeFactory());
    MustBeReachingVariableDef dataFlowAnalysis =
        new MustBeReachingVariableDef(null, scope, null);

    Node inputNode = Node.newBinaryOp(Token.PLUS, Node.newNumberLiteral(1), Node.newNumberLiteral(2));
    MustDef input = new MustDef();
    MustDef output = dataFlowAnalysis.flowThrough(inputNode, input);

    Map<Var, Definition> expectedReachingDef = new HashMap<>();
    expectedReachingDef.put(scope.getVar("it"), new Definition(inputNode));
    MustDef expected = new MustDef(expectedReachingDef.entrySet().iterator());

    assertEquals(expected, output);
  }

  @Test
  public void testComputeMustDef() {
    Scope scope = new Scope(new NodeFactory());
    MustBeReachingVariableDef dataFlowAnalysis =
        new MustBeReachingVariableDef(null, scope, null);

    Node ifNode = Node.newIfStatement(Node.newBooleanLiteral(true), Node.newBlock(null), Node.newBlock(null));
    Node assignNode = Node.newAssignment(Node.newName("x"), Node.newNumberLiteral(1));
    Node assignNode2 = Node.newAssignment(Node.newName("x"), Node.newNumberLiteral(2));

    Node useNode = Node.newName("x");

    // Test case when definition node is not reached.
    dataFlowAnalysis.computeMustDef(ifNode, null, new MustDef(), false);
    MustDef output = dataFlowAnalysis.getAnnotation(ifNode);
    assertNull(output);

    // Test case when definition node is reached.
    dataFlowAnalysis.computeMustDef(assignNode, ifNode, new MustDef(), false);
    output = dataFlowAnalysis.getAnnotation(ifNode);
    Definition definition = new Definition(assignNode);
    assertEquals(new MustDef(ImmutableSet.of(definition)), output);

    // Test case when definition node is reached and depends on other local variables.
    dataFlowAnalysis.computeMustDef(assignNode2, ifNode, new MustDef(), false);
    definition = new Definition(assignNode2);
    Map<Var, Definition> expectedReachingDef = new HashMap<>();
    expectedReachingDef.put(scope.getVar("x"), definition);
    expectedReachingDef.put(scope.getVar("it"), new Definition(assignNode));
    MustDef expected = new MustDef(expectedReachingDef.entrySet().iterator());
    assertEquals(expected, output);

    // Test case when definition node is conditionally reached.
    dataFlowAnalysis.computeMustDef(assignNode, Node.newIfStatement(Node.newBooleanLiteral(false), Node.newBlock(null), Node.newBlock(null)), new MustDef(), true);
    output = dataFlowAnalysis.getAnnotation(assignNode);
    assertNull(output);
  }

  @Test
  public void testAddToDefIfLocal() {
    Scope scope = new Scope(new NodeFactory());
    MustBeReachingVariableDef dataFlowAnalysis =
        new MustBeReachingVariableDef(null, scope, null);

    String name = "x";
    Node assignNode = Node.newAssignment(Node.newName(name), Node.newNumberLiteral(1));
    Node useNode = Node.newName(name);
    MustDef input = new MustDef();

    dataFlowAnalysis.addToDefIfLocal(name, assignNode, null, input);

    Definition definition = new Definition(assignNode);
    Map<Var, Definition> expectedReachingDef = new HashMap<>();
    expectedReachingDef.put(scope.getVar(name), definition);
    MustDef expected = new MustDef(expectedReachingDef.entrySet().iterator());

    assertEquals(expected, input);
  }

  @Test
  public void testEscapeParameters() {
    Scope scope = new Scope(new NodeFactory());
    MustBeReachingVariableDef dataFlowAnalysis =
        new MustBeReachingVariableDef(null, scope, null);

    String name = "arguments";
    Node useNode = Node.newName(name);
    MustDef input = new MustDef();

    dataFlowAnalysis.escapeParameters(input);

    assertNull(input.reachingDef);
  }

  @Test
  public void testGetDef() {
    Scope scope = new Scope(new NodeFactory());
    MustBeReachingVariableDef dataFlowAnalysis =
        new MustBeReachingVariableDef(null, scope, null);

    String name = "x";
    Node assignNode = Node.newAssignment(Node.newName(name), Node.newNumberLiteral(1));
    Node useNode = Node.newName(name);
    MustDef input = new MustDef();
    dataFlowAnalysis.computeMustDef(assignNode, null, input, false);

    Node definitionNode = dataFlowAnalysis.getDef(name, useNode);
    assertEquals(assignNode, definitionNode);
  }

  @Test
  public void testDependsOnOuterScopeVars() {
    Scope scope = new Scope(new NodeFactory());
    MustBeReachingVariableDef dataFlowAnalysis =
        new MustBeReachingVariableDef(null, scope, null);

    String name = "outerScopeVar";
    Node useNode = Node.newName(name);
    MustDef input = new MustDef();
    dataFlowAnalysis.computeMustDef(Node.newName(name), null, input, false);

    boolean result = dataFlowAnalysis.dependsOnOuterScopeVars(name, useNode);
    assertFalse(result);

    scope.setVar(name, new Var(Node.newName(name), null, null, null, null, null));
    result = dataFlowAnalysis.dependsOnOuterScopeVars(name, useNode);
    assertTrue(result);
  }
}