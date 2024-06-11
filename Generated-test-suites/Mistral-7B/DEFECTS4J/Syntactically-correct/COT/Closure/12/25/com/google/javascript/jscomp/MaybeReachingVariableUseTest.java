package com.google.javascript.jscomp;

import com.google.common.collect.ImmutableSet;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MaybeReachingVariableUseTest {

  private MaybeReachingVariableUse maybeReachingVariableUse;
  private ControlFlowGraph<Node> controlFlowGraphMock;
  private Scope jsScopeMock;

  @Before
  public void setUp() {
    controlFlowGraphMock = mock(ControlFlowGraph.class);
    jsScopeMock = mock(Scope.class);

    maybeReachingVariableUse = new MaybeReachingVariableUse(controlFlowGraphMock, jsScopeMock, null);
  }

  @Test
  public void testIsForward() {
    boolean result = maybeReachingVariableUse.isForward();
    assertEquals(false, result);
  }

  @Test
  public void testCreateEntryLattice() {
    ReachingUses result = maybeReachingVariableUse.createEntryLattice();
    assertEquals(new ReachingUses(), result);
  }

  @Test
  public void testCreateInitialEstimateLattice() {
    ReachingUses result = maybeReachingVariableUse.createInitialEstimateLattice();
    assertEquals(new ReachingUses(), result);
  }

  @Test
  public void testFlowThroughSimpleAssignment() {
    Node defNode = Mockito.mock(Node.class);
    Node useNode = Mockito.mock(Node.class);

    when(controlFlowGraphMock.getNode(defNode)).thenReturn(Mockito.mock(GraphNode.class));
    when(controlFlowGraphMock.getNode(useNode)).thenReturn(Mockito.mock(GraphNode.class));

    ReachingUses input = new ReachingUses();
    input.mayUseMap.put(jsScopeMock.getVar("A"), defNode);

    ReachingUses output = maybeReachingVariableUse.flowThrough(useNode, input);

    assertEquals(input, output); // The join operation is a simple union.
  }

  @Test
  public void testHasExceptionHandler() {
    Node cfgNode = Mockito.mock(Node.class);
    boolean result = maybeReachingVariableUse.hasExceptionHandler(cfgNode);
    assertEquals(false, result);
  }

  @Test(expected = AssertionError.class)
  public void testAddToUseIfLocalWithEscapedVariable() {
    String name = "foo";
    Node node = Mockito.mock(Node.class);
    ReachingUses use = new ReachingUses();

    when(jsScopeMock.getVar(name)).thenReturn(new Var(name, null, null, null, null, null, ImmutableSet.of(), null, null, false));

    maybeReachingVariableUse.addToUseIfLocal(name, node, use);
  }

  @Test
  public void testGetUsesWithLocalVariable() {
    String name = "A";
    Node defNode = Mockito.mock(Node.class);
    Node useNode1 = Mockito.mock(Node.class);
    Node useNode2 = Mockito.mock(Node.class);

    when(controlFlowGraphMock.getNode(defNode)).thenReturn(Mockito.mock(GraphNode.class));
    when(controlFlowGraphMock.getNode(useNode1)).thenReturn(Mockito.mock(GraphNode.class));
    when(controlFlowGraphMock.getNode(useNode2)).thenReturn(Mockito.mock(GraphNode.class));

    FlowState<ReachingUses> stateDefNode = Mockito.mock(FlowState.class);
    FlowState<ReachingUses> stateUseNode1 = Mockito.mock(FlowState.class);
    FlowState<ReachingUses> stateUseNode2 = Mockito.mock(FlowState.class);

    when(jsScopeMock.getVar(name)).thenReturn(new Var(name, null, null, null, null, null, ImmutableSet.of(), stateDefNode, null, false));
    when(stateDefNode.getOut()).thenReturn(new ReachingUses());
    when(stateUseNode1.getOut()).thenReturn(new ReachingUses());
    when(stateUseNode2.getOut()).thenReturn(new ReachingUses());

    when(controlFlowGraphMock.getAnnotation(defNode)).thenReturn(stateDefNode);
    when(controlFlowGraphMock.getAnnotation(useNode1)).thenReturn(stateUseNode1);
    when(controlFlowGraphMock.getAnnotation(useNode2)).thenReturn(stateUseNode2);

    Collection<Node> result = maybeReachingVariableUse.getUses(name, defNode);
    assertEquals(ImmutableSet.of(useNode1, useNode2), result);
  }

  @Test
  public void testGetUsesWithNonLocalVariable() {
    String name = "global";
    Node defNode = Mockito.mock(Node.class);

    when(jsScopeMock.getVar(name)).thenReturn(null);

    Collection<Node> result = maybeReachingVariableUse.getUses(name, defNode);
    assertEquals(Collections.emptyList(), result);
  }

  @Test
  public void testGetUsesWithFunctionParameter() {
    String name = "param";
    Node defNode = Mockito.mock(Node.class);

    when(jsScopeMock.getVar(name)).thenReturn(new Var(name, null, null, null, null, null, ImmutableSet.of(), null, null, true));

    Collection<Node> result = maybeReachingVariableUse.getUses(name, defNode);
    assertEquals(Collections.emptyList(), result);
  }

  @Test
  public void testGetUsesWithEscapedVariable() {
    String name = "escaped";
    Node defNode = Mockito.mock(Node.class);

    when(jsScopeMock.getVar(name)).thenReturn(new Var(name, null, null, null, null, null, ImmutableSet.of(), null, null, true));

    Collection<Node> result = maybeReachingVariableUse.getUses(name, defNode);
    assertEquals(Collections.emptyList(), result);
  }

  @Test
  public void testGetUsesWithVariableInCatchBlock() {
    String name = "catchVar";
    Node defNode = Mockito.mock(Node.class);

    when(jsScopeMock.getVar(name)).thenReturn(new Var(name, null, null, null, null, null, ImmutableSet.of(), null, null, false) {
      @Override
      public boolean isInCatchBlock() {
        return true;
      }
    });

    Collection<Node> result = maybeReachingVariableUse.getUses(name, defNode);
    assertEquals(Collections.emptyList(), result);
  }
}