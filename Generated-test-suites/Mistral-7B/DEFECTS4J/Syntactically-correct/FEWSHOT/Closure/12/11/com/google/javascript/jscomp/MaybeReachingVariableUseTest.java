package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.graph.GraphNode;
import com.google.javascript.jscomp.graph.LatticeElement;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class MaybeReachingVariableUseTest {

  private MaybeReachingVariableUse maybeReachingVariableUse;
  private Node node1;
  private Node node2;
  private Node node3;
  private Node node4;
  private Node node5;
  private Node node6;
  private Node node7;
  private ControlFlowGraph<Node> controlFlowGraph;
  private Scope jsScope;

  @Before
  public void setup() {
    jsScope = new Scope();
    Var var1 = jsScope.declare("A", Node.NEW_INT(1));
    Var var2 = jsScope.declare("B", Node.NEW_INT(2));

    node1 = Node.newBlock(ImmutableList.of(Node.newName("A")), ImmutableList.of());
    node2 = Node.newName("N2");
    node3 = Node.newName("N3");
    node4 = Node.newName("N4");
    node5 = Node.newName("N5");
    node6 = Node.newName("N6");
    node7 = Node.newName("N7");

    controlFlowGraph = new ControlFlowGraph<>();
    controlFlowGraph.addNodes(
        node1, node2, node3, node4, node5, node6, node7);

    maybeReachingVariableUse = new MaybeReachingVariableUse(controlFlowGraph, jsScope, null);
  }

  @Test
  public void testEmpty() {
    ReachingUses result = maybeReachingVariableUse.computeReachingUses(node1);
    assertNull(result);
  }

  @Test
  public void testSimpleVariable() {
    controlFlowGraph.addEdge(node1, node2, null);
    ReachingUses input = new ReachingUses();
    input.mayUseMap.put(var1, node1);

    ReachingUses output = maybeReachingVariableUse.flowThrough(node2, input);
    Collection<Node> uses = maybeReachingVariableUse.getUses("A", node2);
    assertThat(uses).containsExactly(node1);
  }

  @Test
  public void testSimpleAssignment() {
    controlFlowGraph.addEdge(node1, node2, null);
    controlFlowGraph.addEdge(node2, node3, null);
    controlFlowGraph.addEdge(node3, node4, null);
    controlFlowGraph.addEdge(node4, node5, null);
    controlFlowGraph.addEdge(node5, node6, null);
    controlFlowGraph.addEdge(node6, node7, null);

    ReachingUses input = new ReachingUsses();
    input.mayUseMap.put(var1, node1);

    ReachingUses output = maybeReachingVariableUse.flowThrough(node5, input);
    Collection<Node> uses = maybeReachingVariableUse.getUses("A", node5);
    assertThat(uses).containsExactly(node1, node5);
  }

  @Test
  public void testNestedIf() {
    controlFlowGraph.addEdge(node1, node2, null);
    controlFlowGraph.addEdge(node2, node3, null);
    controlFlowGraph.addEdge(node3, node4, null);
    controlFlowGraph.addEdge(node3, node5, null);
    controlFlowGraph.addEdge(node5, node6, null);
    controlFlowGraph.addEdge(node6, node7, null);

    ReachingUses input = new ReachingUses();
    input.mayUseMap.put(var1, node1);

    ReachingUses output = maybeReachingVariableUse.flowThrough(node5, input);
    Collection<Node> uses = maybeReachingVariableUse.getUses("A", node5);
    assertThat(uses).containsExactly(node1, node5);
  }

  @Test
  public void testForLoop() {
    controlFlowGraph.addEdge(node1, node2, null);
    controlFlowGraph.addEdge(node2, node3, null);
    controlFlowGraph.addEdge(node3, node4, null);
    controlFlowGraph.addEdge(node4, node5, null);
    controlFlowGraph.addEdge(node5, node6, null);
    controlFlowGraph.addEdge(node6, node7, null);

    controlFlowGraph.addEdge(node2, node3, new Branch(node3));

    ReachingUses input = new ReachingUses();
    input.mayUseMap.put(var1, node1);

    ReachingUses output = maybeReachingVariableUse.flowThrough(node5, input);
    Collection<Node> uses = maybeReachingVariableUse.getUses("A", node5);
    assertThat(uses).containsExactly(node1);
  }

  @Test
  public void testFunctionCall() {
    controlFlowGraph.addEdge(node1, node2, null);
    controlFlowGraph.addEdge(node2, node3, null);
    controlFlowGraph.addEdge(node3, node4, null);
    controlFlowGraph.addEdge(node4, node5, null);
    controlFlowGraph.addEdge(node5, node6, null);
    controlFlowGraph.addEdge(node6, node7, null);

    Node functionCallNode = Node.newCall(Node.newName("print"), ImmutableList.of(Node.newName("A")));
    controlFlowGraph.addEdge(node5, functionCallNode, null);

    ReachingUses input = new ReachingUses();
    input.mayUseMap.put(var1, node1);

    ReachingUses output = maybeReachingVariableUse.flowThrough(functionCallNode, input);
    Collection<Node> uses = maybeReachingVariableUse.getUses("A", functionCallNode);
    assertThat(uses).containsExactly(node1);
  }
}