package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class MaybeReachingVariableUseTest {

  private MaybeReachingVariableUse maybeReachingVariableUse;
  private ControlFlowGraph<Node> controlFlowGraph;
  private Scope jsScope;

  @Before
  public void setUp() {
    // Assuming a simple ControlFlowGraph and Scope for testing
    Node node1 = Node.newIntLiteral(1);
    Node node2 = Node.newName("x");
    Node node3 = Node.newName("a");
    Node node4 = Node.newName("b");
    Node node5 = Node.newName("y");
    Node node6 = Node.newName("z");
    Node node7 = Node.newName("c");
    Node node8 = Node.newName("d");
    Node node9 = Node.newName("e");

    controlFlowGraph = new ControlFlowGraph<>();
    controlFlowGraph.addEdge(node1, node2);
    controlFlowGraph.addEdge(node2, node3);
    controlFlowGraph.addEdge(node2, node4);
    controlFlowGraph.addEdge(node3, node5);
    controlFlowGraph.addEdge(node3, node6);
    controlFlowGraph.addEdge(node4, node7);
    controlFlowGraph.addEdge(node4, node8);
    controlFlowGraph.addEdge(node5, node9);

    jsScope = new Scope();
    jsScope.declareVar("x", new Var(Node.scopeTypeVar(Node.VAR_LOCAL), null, null));
    jsScope.declareVar("a", new Var(Node.scopeTypeVar(Node.VAR_LOCAL), null, null));
    jsScope.declareVar("b", new Var(Node.scopeTypeVar(Node.VAR_LOCAL), null, null));
    jsScope.declareVar("y", new Var(Node.scopeTypeVar(Node.VAR_LOCAL), null, null));
    jsScope.declareVar("z", new Var(Node.scopeTypeVar(Node.VAR_LOCAL), null, null));
    jsScope.declareVar("c", new Var(Node.scopeTypeVar(Node.VAR_LOCAL), null, null));
    jsScope.declareVar("d", new Var(Node.scopeTypeVar(Node.VAR_LOCAL), null, null));
    jsScope.declareVar("e", new Var(Node.scopeTypeVar(Node.VAR_LOCAL), null, null));

    MaybeReachingVariableUse maybeReachingVariableUse =
        new MaybeReachingVariableUse(controlFlowGraph, jsScope, new AbstractCompiler() {});
  }

  @Test
  public void testEmptyUseMap() {
    ReachingUses reachingUses = maybeReachingVariableUse.compute(node1);
    assertNotNull(reachingUses);
    assertTrue(reachingUses.mayUseMap.isEmpty());
  }

  @Test
  public void testLocalVariableUse() {
    ReachingUses reachingUses = maybeReachingVariableUse.compute(node3);
    assertNotNull(reachingUses);
    assertFalse(reachingUses.mayUseMap.isEmpty());
    assertNotNull(reachingUses.mayUseMap.get(jsScope.getVar("a")));
    assertNotNull(reachingUses.mayUseMap.get(jsScope.getVar("b")));
  }

  @Test
  public void testEscapedVariableUse() {
    ImmutableSet<String> escapedVariables = Sets.newHashSet("x");
    MaybeReachingVariableUse maybeReachingVariableUse =
        new MaybeReachingVariableUse(controlFlowGraph, jsScope, new AbstractCompiler() {});
    maybeReachingVariableUse.escaped = Sets.newHashSet("x");

    ReachingUses reachingUses = maybeReachingVariableUse.compute(node3);
    assertNotNull(reachingUses);
    assertTrue(reachingUses.mayUseMap.isEmpty());
  }

  @Test
  public void testConditionalUse() {
    Node conditionNode = Node.newBinaryOp("||", Node.newBooleanLiteral(true), Node.newBooleanLiteral(true));
    controlFlowGraph.addEdge(node2, conditionNode);
    controlFlowGraph.addEdge(conditionNode, node3);
    controlFlowGraph.addEdge(conditionNode, node4);

    ReachingUses reachingUsesA = maybeReachingVariableUse.compute(node3);
    ReachingUses reachingUsesB = maybeReachingVariableUse.compute(node4);

    assertNotNull(reachingUsesA);
    assertNotNull(reachingUsesB);
    assertTrue(reachingUsesA.mayUseMap.isEmpty());
    assertTrue(reachingUsesB.mayUseMap.isEmpty());
  }

  @Test
  public void testAssignmentUse() {
    Node assignmentNode = Node.newAssign(node3, Node.newName("x"));
    controlFlowGraph.addEdge(node3, assignmentNode);
    controlFlowGraph.addEdge(assignmentNode, node5);

    ReachingUses reachingUsesA = maybeReachingVariableUse.compute(node3);
    ReachingUses reachingUsesB = maybeReachingVariableUse.compute(node5);

    assertNotNull(reachingUsesA);
    assertNotNull(reachingUsesB);
    assertNotNull(reachingUsesA.mayUseMap.get(jsScope.getVar("a")));
    assertTrue(reachingUsesB.mayUseMap.isEmpty());
  }

  @Test
  public void testGetUses() {
    String variableName = "a";
    Collection<Node> uses = maybeReachingVariableUse.getUses(variableName, node3);
    assertNotNull(uses);
    assertFalse(uses.isEmpty());
    assertNotNull(uses.iterator().next());
    assertEquals(node3, uses.iterator().next());
  }

  @Test
  public void testGetUsesEmpty() {
    String variableName = "z";
    Collection<Node> uses = maybeReachingVariableUse.getUsages(variableName, node3);
    assertNotNull(uses);
    assertTrue(uses.isEmpty());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetUsesNullNode() {
    String variableName = "a";
    maybeReachingVariableUse.getUsages(variableName, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetUsesNullVariableName() {
    Node node = Node.newName("a");
    maybeReachingVariableUse.getUsages(null, node);
  }
}