package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Unit tests for MaybeReachingVariableUse class.
 */
public class MaybeReachingVariableUseTest {

  @Test
  public void testSimpleDefinitionUse() {
    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);

    Node defNode = Node.newIntLiteral(0);
    Node useNode = Node.newName("x");
    Node assignmentNode = Node.newAssignment(Node.newName("x"), defNode);

    analysis.flowThrough(assignmentNode, analysis.createInitialEstimateLattice());
    Collection<Node> uses = analysis.getUses("x", defNode);

    assertTrue(uses.contains(useNode));
  }

  @Test
  public void testMultipleDefinitionsUse() {
    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);

    Node defNode1 = Node.newIntLiteral(0);
    Node assignmentNode1 = Node.newAssignment(Node.newName("x"), defNode1);
    Node defNode2 = Node.newIntLiteral(1);
    Node useNode = Node.newName("x");
    Node assignmentNode2 = Node.newAssignment(Node.newName("x"), defNode2);

    analysis.flowThrough(assignmentNode1, analysis.createInitialEstimateLattice());
    analysis.flowThrough(assignmentNode2, analysis.createInitialEstimateLattice());
    Collection<Node> uses = analysis.getUsages();

    assertTrue(uses.contains(useNode));
    assertTrue(uses.get(0).mayUseMap.containsKey(jsScope.getVar("x")));
    assertTrue(uses.get(0).mayUseMap.get(jsScope.getVar("x")).equals(defNode2));
    assertTrue(uses.get(1).mayUseMap.containsKey(jsScope.getVar("x")));
    assertTrue(uses.get(1).mayUseMap.get(jsScope.getVar("x")).equals(defNode1));
  }

  @Test
  public void testNoDefinitionUse() {
    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);

    Node useNode = Node.newName("x");

    Collection<Node> uses = analysis.getUsages();

    assertNull(uses.get(0).mayUseMap.get(jsScope.getVar("x")));
  }

  @Test
  public void testEscapedVariableUse() {
    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);
    analysis.escaped.add(jsScope.getVar("x"));

    Node defNode = Node.newIntLiteral(0);
    Node useNode = Node.newName("x");
    Node assignmentNode = Node.newAssignment(Node.newName("x"), defNode);

    analysis.flowThrough(assignmentNode, analysis.createInitialEstimateLattice());
    Collection<Node> uses = analysis.getUsages();

    assertFalse(uses.get(0).mayUseMap.containsKey(jsScope.getVar("x")));
  }
}