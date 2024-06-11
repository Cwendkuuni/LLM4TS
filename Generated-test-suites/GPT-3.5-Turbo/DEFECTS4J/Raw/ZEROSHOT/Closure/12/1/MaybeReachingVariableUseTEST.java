package com.google.javascript.jscomp;

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
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MaybeReachingVariableUseTest {

  private MaybeReachingVariableUse reachingVariableUse;

  @Before
  public void setUp() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(null);
    Scope jsScope = new Scope(null);
    AbstractCompiler compiler = new AbstractCompiler();
    reachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, compiler);
  }

  @Test
  public void testGetUses() {
    Node defNode = new Node(Token.BLOCK);
    Node nameNode = new Node(Token.NAME, "name");
    defNode.addChildToFront(nameNode);

    MaybeReachingVariableUse.ReachingUses out = reachingVariableUse.createEntryLattice();
    MaybeReachingVariableUse.ReachingUses in = reachingVariableUse.createInitialEstimateLattice();

    reachingVariableUse.computeMayUse(nameNode, defNode, in, false);

    Collection<Node> uses = reachingVariableUse.getUses("name", defNode);
    assertNotNull(uses);
    assertTrue(uses.isEmpty());
  }

  @Test
  public void testFlowThrough() {
    Node defNode = new Node(Token.BLOCK);
    Node nameNode = new Node(Token.NAME, "name");
    defNode.addChildToFront(nameNode);

    MaybeReachingVariableUse.ReachingUses input = reachingVariableUse.createInitialEstimateLattice();
    MaybeReachingVariableUse.ReachingUses output = reachingVariableUse.flowThrough(defNode, input);

    assertEquals(input.mayUseMap, output.mayUseMap);
  }
}