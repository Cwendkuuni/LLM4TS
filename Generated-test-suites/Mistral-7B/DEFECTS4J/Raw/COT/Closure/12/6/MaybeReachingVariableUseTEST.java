package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraphEdge;
import com.google.javascript.jscomp.graph.GraphNode;
import com.google.javascript.jscomp.graph.LatticeElement;
import com.google.javascript.jscomp.graph.Node;
import com.google.javascript.rhino.NodeUtil;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class MaybeReachingVariableUseTest {

  private ControlFlowGraph<Node> cfg;
  private Scope jsScope;
  private AbstractCompiler compiler;

  @Before
  public void setup() {
    // Initialize the ControlFlowGraph, Scope, and AbstractCompiler for testing.
    // Replace this with the actual initialization logic.
  }

  @Test
  public void testMaybeReachingVariableUse() {
    MaybeReachingVariableUse maybeReachingVariableUse =
        new MaybeReachingVariableUse(cfg, jsScope, compiler);

    // Basic functionality test
    Node node = new Node(Token.NAME, "test");
    ReachingUses input = new ReachingUses();
    ReachingUses output = maybeReachingVariableUse.flowThrough(node, input);
    assertTrue(output.equals(input));

    // Test with a simple variable assignment
    Node defNode = new Node(Token.VAR, "x");
    Node useNode = new Node(Token.NAME, "x");
    GraphNode<Node, Branch> def = new GraphNode<>(defNode, null);
    GraphNode<Node, Branch> use = new GraphNode<>(useNode, null);
    DiGraphEdge<Node, Branch> edge = new DiGraphEdge<>(def, use, null);
    cfg.addEdge(edge);

    input = new ReachingUses();
    output = maybeReachingVariableUse.flowThrough(useNode, input);
    Collection<Node> uses = maybeReachingVariableUse.getUses("x", defNode);
    assertTrue(uses.contains(useNode));
    assertEquals(1, uses.size());

    // Test with a conditional statement
    Node ifNode = new Node(Token.IF, null);
    Node trueBranch = new Node(Token.BLOCK, null);
    Node falseBranch = new Node(Token.BLOCK, null);
    GraphNode<Node, Branch> ifGraphNode = new GraphNode<>(ifNode, null);
    GraphNode<Node, Branch> trueBranchGraphNode = new GraphNode<>(trueBranch, new Branch(ifNode, true));
    GraphNode<Node, Branch> falseBranchGraphNode = new GraphNode<>(falseBranch, new Branch(ifNode, false));

    cfg.addEdge(new DiGraphEdge<>(useNode, ifNode, null));
    cfg.addEdge(new DiGraphEdge<>(ifNode, trueBranchGraphNode, null));
    cfg.addEdge(new DiGraphEdge<>(ifNode, falseBranchGraphNode, null));

    input = new ReachingUses();
    output = maybeReachingVariableUse.flowThrough(useNode, input);

    uses = maybeReachingVariableUse.getUses("x", defNode);
    assertTrue(uses.contains(useNode));
    assertEquals(1, uses.size());

    input = output;
    output = maybeReachingVariableUse.flowThrough(trueBranch, input);

    uses = maybeReachingVariableUse.getUses("x", defNode);
    assertTrue(uses.contains(useNode));
    assertEquals(1, uses.size());

    input = output;
    output = maybeReachingVariableUse.flowThrough(falseBranch, input);

    uses = maybeReachingVariableUse.getUses("x", defNode);
    assertTrue(uses.isEmpty());

    // Test with a loop statement
    Node forNode = new Node(Token.FOR, null);
    Node initNode = new Node(Token.VAR, "i");
    Node conditionNode = new Node(Token.INT_LITERAL, 0);
    Node incrementNode = new Node(Token.PLUS, new Node(Token.NAME, "i"), NodeUtil.createInt(1));
    Node loopBodyNode = new Node(Token.BLOCK, null);

    GraphNode<Node, Branch> forGraphNode =
        new GraphNode<>(forNode, null);
    GraphNode<Node, Branch> initGraphNode = new GraphNode<>(initNode, new Branch(forNode, "init"));
    GraphNode<Node, Branch> conditionGraphNode = new GraphNode<>(conditionNode, new Branch(forNode, "cond"));
    GraphNode<Node, Branch> incrementGraphNode = new GraphNode<>(incrementNode, new Branch(forNode, "increment"));
    GraphNode<Node, Branch> loopBodyGraphNode = new GraphNode<>(loopBodyNode, null);

    cfg.addEdge(new DiGraphEdge<>(useNode, forNode, null));
    cfg.addEdge(new DiGraphEdge<>(forNode, initGraphNode, null));
    cfg.addEdge(new DiGraphEdge<>(initGraphNode, forNode, null));
    cfg.addEdge(new DiGraphEdge<>(forNode, conditionGraphNode, null));
    cfg.addEdge(
        new DiGraphEdge<>(conditionGraphNode, (conditionGraphNode.getValue().isTrue()) ? loopBodyGraphNode : null, null));
    cfg.addEdge(new DiGraphEdge<>(loopBodyGraphNode, forNode, null));

    input = new ReachingUses();
    output = maybeReachingVariableUse.flowThrough(useNode, input);

    uses = maybeReachingVariableUse.getUses("x", defNode);
    assertTrue(uses.contains(useNode));
    assertEquals(1, uses.size());

    input = output;
    output = maybeReachingVariableUse.flowThrough(initNode, input);

    uses = maybeReachingVariableUse.getUses("x", defNode);
    assertTrue(uses.contains(useNode));
    assertEquals(1, uses.size());

    input = output;
    output = maybeReachingVariableUse.flowThrough(loopBodyNode, input);

    uses = maybeReachingVariableUse.getUses("x", defNode);
    assertTrue(uses.contains(useNode));
    assertEquals(1, uses.size());

    input = output;
    output = maybeReachingVariableUse.flowThrough(forNode, input);

    uses = maybeReachingVariableUse.getUses("x", defNode);
    assertTrue(uses.contains(useNode));
    assertEquals(1, uses.size());
  }

  @Test(expected = AssertionError.class)
  public void testInvalidUse() {
    Node useNode = new Node(Token.NAME, "nonExistentVariable");
    Node defNode = new Node(Token.VAR, "x");

    GraphNode<Node, Branch> useGraphNode = new GraphNode<>(useNode, null);
    GraphNode<Node, Branch> defGraphNode = new GraphNode<>(defNode, null);

    DiGraphEdge<Node, Branch> edge = new DiGraphEdge<>(useNode, defNode, null);
    cfg.addEdge(edge);

    MaybeReachingVariableUse maybeReachingVariableUse =
        new MaybeReachingVariableUse(cfg, jsScope, compiler);

    ReachingUses input = new ReachingUses();
    ReachingUses output = maybeReachingVariableUse.flowThrough(useNode, input);

    Collection<Node> uses = maybeReachingVariableUse.getUses("nonExistentVariable", defNode);
    assertFalse(uses.isEmpty());
    assertEquals(0, uses.size());
  }
}