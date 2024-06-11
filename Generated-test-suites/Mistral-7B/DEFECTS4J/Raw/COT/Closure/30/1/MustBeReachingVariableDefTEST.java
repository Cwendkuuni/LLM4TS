package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.javascript.jscomp.MustBeReachingVariableDef;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

public class MustBeReachingVariableDefTest {

  @Test
  public void testMustBeReachingVariableDefConstructor() {
    MustBeReachingVariableDef mustBeReachingVariableDef = new MustBeReachingVariableDef(null, null, null);
    assertNotNull(mustBeReachingVariableDef);
  }

  @Test
  public void testDefinitionClassConstructor() {
    Node node = Node.newIntLiteral(1);
    Definition definition = new MustBeReachingVariableDef.Definition(node);
    assertNotNull(definition);
  }

  @Test
  public void testMustDefClassConstructor() {
    MustBeReachingVariableDef.MustDef mustDef = new MustBeReachingVariableDef.MustDef();
    assertNotNull(mustDef);
  }

  @Test
  public void testMustDefJoinClass() {
    MustBeReachingVariableDef.MustDef a = new MustBeReachingVariableDef.MustDef();
    MustBeReachingVariableDef.MustDef b = new MustBeReachingVariableDef.MustDef();
    MustBeReachingVariableDef.MustDefJoin join = new MustBeReachingVariableDef.MustDefJoin();
    MustBeReachingVariableDef.MustDef result = join.apply(a, b);
    assertNotNull(result);
  }

  @Test
  public void testIsForwardMethod() {
    MustBeReachingVariableDef mustBeReachingVariableDef = new MustBeReachingVariableDef(null, null, null);
    assertTrue(mustBeReachingVariableDef.isForward());
  }

  @Test
  public void testCreateEntryLatticeMethod() {
    MustBeReachingVariableDef mustBeReachingVariableDef = new MustBeReachingVariableDef(null, null, null);
    MustBeReachingVariableDef.MustDef entryLattice = mustBeReachingVariableDef.createEntryLattice();
    assertNotNull(entryLattice);
  }

  @Test
  public void testCreateInitialEstimateLatticeMethod() {
    MustBeReachingVariableDef mustBeReachingVariableDef = new MustBeReachingVariableDef(null, null, null);
    MustBeReachingVariableDef.MustDef initialEstimateLattice = mustBeReachingVariableDef.createInitialEstimateLattice();
    assertNotNull(initialEstimateLattice);
  }

  @Test
  public void testFlowThroughMethod() {
    MustBeReachingVariableDef mustBeReachingVariableDef = new MustBeReachingVariableDef(null, null, null);
    Node n = Node.newIntLiteral(1);
    MustBeReachingVariableDef.MustDef input = new MustBeReachingVariableDef.MustDef();
    MustBeReachingVariableDef.MustDef output = mustBeReachingVariableDef.flowThrough(n, input);
    assertNotNull(output);
  }

  @Test(expected = AssertionError.class)
  public void testFlowThroughMethodWithNullInput() {
    MustBeReachingVariableDef mustBeReachingVariableDef = new MustBeReachingVariableDef(null, null, null);
    MustBeReachingVariableDef.MustDef output = mustBeReachingVariableDef.flowThrough(null, null);
  }

  @Test
  public void testComputeMustDefMethod() {
    MustBeReachingVariableDef mustBeReachingVariableDef = new MustBeReachingVariableDef(null, null, null);
    Node n = Node.newIntLiteral(1);
    Node cfgNode = Node.newIntLiteral(2);
    MustBeReachingVariableDef.MustDef output = mustBeReachingVariableDef.createEntryLattice();
    mustBeReachingVariableDef.computeMustDef(n, cfgNode, output, false);
    assertNotNull(output);
  }

  @Test
  public void testAddToDefIfLocalMethod() {
    MustBeReachingVariableDef mustBeReachingVariableDef = new MustBeReachingVariableDef(null, null, null);
    Node n = Node.newIntLiteral(1);
    Node rValue = Node.newIntLiteral(2);
    String name = "test";
    mustBeReachingVariableDef.addToDefIfLocal(name, n, rValue, mustBeReachingVariableDef.createEntryLattice());
    Definition def = mustBeReachingVariableDef.createEntryLattice().reachingDef.get(mustBeReachingVariableDef.jsScope.getVar(name));
    assertNotNull(def);
  }

  @Test
  public void testEscapeParametersMethod() {
    MustBeReachingVariableDef mustBeReachingVariableDef = new MustBeReachingVariableDef(null, null, null);
    MustBeReachingVariableDef.MustDef output = mustBeReachingVariableDef.createEntryLattice();
    mustBeReachingVariableDef.escapeParameters(output);
    assertFalse(output.reachingDef.isEmpty());
  }

  @Test
  public void testGetDefMethod() {
    MustBeReachingVariableDef mustBeReachingVariableDef = new MustBeReachingVariableDef(null, null, null);
    Node n = Node.newIntLiteral(1);
    Scope jsScope = new Scope();
    jsScope.declareVar("test", Node.newName("test"));
    GraphNode<Node, Branch> cfgNode = new ControlFlowGraph.GraphNode(n, null);
    cfgNode.setAnnotation(new FlowNodeAnnotation<>(mustBeReachingVariableDef.createEntryLattice()));
    MustBeReachingVariableDef.MustDef entryLattice = mustBeReachingVariableDef.createEntryLattice();
    Node useNode = Node.newName("test");
    Node def = mustBeReachingVariableDef.getDef("test", useNode);
    assertEquals(n, def);
  }

  @Test
  public void testDependsOnOuterScopeVarsMethod() {
    MustBeReachingVariableDef mustBeReachingVariableDef = new MustBeReachingVariableDef(null, null, null);
    Node n = Node.newIntLiteral(1);
    Scope jsScope = new Scope();
    jsScope.declareVar("test", Node.newName("test"));
    GraphNode<Node, Branch> cfgNode = new ControlFlowGraph.GraphNode(n, null);
    cfgNode.setAnnotation(new FlowNodeAnnotation<>(mustBeReachingVariableDef.createEntryLattice()));
    Scope outerScope = new Scope();
    outerScope.declareVar("outerTest", Node.newName("outerTest"));
    MustBeReachingVariableDef.MustDef entryLattice = mustBeReachingVariableDef.createEntryLattice();
    Node useNode = Node.newName("test");
    assertFalse(mustBeReachingVariableDef.dependsOnOuterScopeVars("test", useNode));

    MustBeReachingVariableDef.MustDef outerEntryLattice = new MustBeReachingVariableDef.MustDef();
    outerEntryLattice.reachingDef.put(outerScope.getVar("outerTest"), new Definition(null));
    MustBeReachingVariableDef mustBeReachingVariableDefWithOuterScope = new MustBeReachingVariableDef(null, null, null);
    Node outerUseNode = Node.newName("outerTest");
    assertTrue(mustBeReachingVariableDefWithOuterScope.dependsOnOuterScopeVars("test", useNode));
  }
}