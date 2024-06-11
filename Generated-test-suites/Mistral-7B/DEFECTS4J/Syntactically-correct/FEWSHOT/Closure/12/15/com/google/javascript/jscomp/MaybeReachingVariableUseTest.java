package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.graph.DiGraph.DiGraphEdge;
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

  private ControlFlowGraph<Node> cfg;
  private MaybeReachingVariableUse analyzer;
  private Scope jsScope;

  @Before
  public void setup() {
    // Setup your test data here, e.g., by creating a ControlFlowGraph instance
    // and setting up a Scope object.
    Node root = Node.newIntLiteral(0);
    jsScope = new Scope(null, null);
    jsScope.declareVar("x", VarType.VAR, null, null);
    cfg = new ControlFlowGraph<>();
    cfg.addEntryNode(Node.newBlock(null));
    cfg.addExitNode(Node.newBlock(null));
    cfg.addNode(Node.newAssign(root, Node.newName("x"), null));
    cfg.addNode(Node.newAssign(Node.newName("x"), Node.newIntLiteral(1), null));
    cfg.addEdge(DiGraphEdge.newEdge(cfg.getEntryNode(), cfg.getNode(0)));
    cfg.addEdge(DiGraphEdge.newEdge(cfg.getNode(0), cfg.getNode(1)));
    cfg.addEdge(DiGraphEdge.newEdge(cfg.getNode(1), cfg.getExitNode()));

    analyzer = new MaybeReachingVariableUse(cfg, jsScope, null);
  }

  @Test
  public void testEmptyGraph() {
    ReachingUses empty = analyzer.createInitialEstimateLattice();
    assertEquals(empty, analyzer.createEntryLattice());
  }

  @Test
  public void testSingleNode() {
    ReachingUses single = analyzer.computeFixedPoint();
    assertNull(single.mayUseMap);
  }

  @Test
  public void testSimpleControlFlow() {
    ReachingUses result = analyzer.computeFixedPoint();
    Multimap<Var, Node> uses = result.mayUseMap;
    Var x = jsScope.getVar("x");

    // The first assignment is the only definition of x
    Node defNode = cfg.getNode(0);
    List<Node> usesList = analyzer.getUses("x", defNode);
    assertEquals(1, usesList.size());
    assertEquals(cfg.getNode(1), usesList.get(0));

    // The second assignment does not affect the may be reaching uses of x
    assertEquals(uses, result.mayUseMap);
  }

  @Test
  public void testConditional() {
    // Add a conditional branch to the control flow graph
    Node ifNode = Node.newIf(Node.newBoolLiteral(true), Node.newBlock(null), null);
    cfg.addNode(ifNode);
    cfg.addEdge(DiGraphEdge.newEdge(cfg.getNode(1), ifNode));
    cfg.addEdge(DiGraphEdge.newEdge(ifNode, cfg.getNode(2)));

    ReachingUses result = analyzer.computeFixedPoint();
    Multimap<Var, Node> uses = result.mayUseMap;

    // The first assignment is the only definition of x
    Node defNode = cfg.getNode(0);
    List<Node> usesList1 = analyzer.getUses("x", defNode);
    assertEquals(1, usesList1.size());
    assertEquals(cfg.getNode(1), usesList1.get(0));

    // The second assignment is only executed if the condition is true
    Node conditionalDefNode = cfg.getNode(2);
    List<Node> usesList2 = analyzer.getUses("x", conditionalDefNode);
    assertNull(usesList2);

    // The may be reaching uses of x should only include the first assignment
    assertEquals(uses, result.mayUseMap);
  }

  @Test
  public void testWhileLoop() {
    // Add a while loop to the control flow graph
    Node whileNode = Node.newWhile(Node.newName("x"), Node.newBlock(null), null);
    cfg.addNode(whileNode);
    cfg.addEdge(DiGraphEdge.newEdge(cfg.getNode(1), whileNode));
    cfg.addEdge(DiGraphEdge.newEdge(whileNode, cfg.getNode(1)));

    ReachingUses result = analyzer.computeFixedPoint();
    Multimap<Var, Node> uses = result.mayUseMap;

    // The first assignment is the only definition of x
    Node defNode = cfg.getNode(0);
    List<Node> usesList1 = analyzer.getUses("x", defNode);
    assertEquals(1, usesList1.size());
    assertEquals(cfg.getNode(1), usesList1.get(0));

    // The may be reaching uses of x should only include the first assignment
    assertEquals(uses, result.mayUseMap);
  }
}