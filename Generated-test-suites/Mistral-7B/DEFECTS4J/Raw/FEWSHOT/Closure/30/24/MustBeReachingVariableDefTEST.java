package com.google.javascript.jscomp;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class MustBeReachingVariableDefTest {

  private Scope mockScope;
  private AbstractCompiler mockCompiler;
  private ControlFlowGraph<Node> mockCfg;
  private MustBeReachingVariableDef mustBeReachingVariableDef;

  @Before
  public void setup() {
    mockScope = mock(Scope.class);
    mockCompiler = mock(AbstractCompiler.class);
    mockCfg = mock(ControlFlowGraph.class);

    when(mockScope.getVars()).thenReturn(Sets.newHashSet(new Var("a"), new Var("b"))).iterator();
    when(mockCfg.getNode(null)).thenReturn(null);

    mustBeReachingVariableDef = new MustBeReachingVariableDef(mockCfg, mockScope, mockCompiler);
  }

  @Test
  public void testCreateEntryLattice() {
    MustDef expected = new MustDef();
    expected.reachingDef.put(new Var("a"), new Definition(new Node()));
    expected.reachingDef.put(new Var("b"), new Definition(new Node()));

    MustDef actual = mustBeReachingVariableDef.createEntryLattice();

    assertEquals(expected, actual);
  }

  @Test
  public void testCreateInitialEstimateLattice() {
    MustDef expected = new MustDef();

    MustDef actual = mustBeReachingVariableDef.createInitialEstimateLattice();

    assertEquals(expected, actual);
  }

  @Test
  public void testFlowThrough() {
    Node node = new Node();
    MustDef input = new MustDef();
    MustDef output = new MustDef();

    when(mockCfg.getNode(node)).thenReturn(new GraphNode<Node, Branch>(node, null));

    mustBeReachingVariableDef.flowThrough(node, input, output);

    assertNull(output.reachingDef.get(new Var("a")));
    assertNull(output.reachingDef.get(new Var("b")));
  }

  @Test
  public void testComputeMustDefForSimpleAssignment() {
    Node assignmentNode = new Node();
    Node definitionNode = new Node();

    MustDef input = new MustDef();
    MustDef output = new MustDef();

    when(mockCfg.getNode(assignmentNode)).thenReturn(
        new GraphNode<Node, Branch>(assignmentNode, null));
    when(mockCfg.getNode(definitionNode)).thenReturn(
        new GraphNode<Node, Branch>(definitionNode, null));

    when(mockScope.getVar("a")).thenReturn(new Var("a"));

    mustBeReachingVariableDef.computeMustDef(assignmentNode, definitionNode, input, output);

    Definition expectedDefinition = new Definition(definitionNode);
    expectedDefinition.depends.add(new Var("a"));

    Definition actualDefinition = output.reachingDef.get(new Var("a"));

    assertSame(expectedDefinition, actualDefinition);
  }

  @Test
  public void testComputeMustDefForAssignmentWithDependentVariable() {
    Node assignmentNode = new Node();
    Node definitionNode = new Node();
    Node dependentNode = new Node();

    MustDef input = new MustDef();
    MustDef output = new MustDef();

    when(mockCfg.getNode(assignmentNode)).thenReturn(
        new GraphNode<Node, Branch>(assignmentNode, null));
    when(mockCfg.getNode(definitionNode)).thenReturn(
        new GraphNode<Node, Branch>(definitionNode, null));
    when(mockCfg.getNode(dependentNode)).thenReturn(
        new GraphNode<Node, Branch>(dependentNode, null));

    when(mockScope.getVar("a")).thenReturn(new Var("a"));
    when(mockScope.getVar("b")).thenReturn(new Var("b"));

    mustBeReachingVariableDef.computeMustDef(assignmentNode, definitionNode, input, output);

    Definition expectedDefinition = new Definition(definitionNode);
    expectedDefinition.depends.add(new Var("a"));

    Definition actualDefinition = output.reachingDef.get(new Var("a"));

    assertSame(expectedDefinition, actualDefinition);

    Definition expectedDependentDefinition = new Definition(dependentNode);

    Definition actualDependentDefinition = output.reachingDef.get(new Var("b"));

    assertNull(actualDependentDefinition);
  }
}