package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import com.google.common.collect.Multimap;
import com.google.javascript.jscomp.MaybeReachingVariableUse.ReachingUses;
import com.google.javascript.rhino.Node;
import java.util.Collection;
import org.junit.Before;
import org.junit.Test;

public class MaybeReachingVariableUseTest {

  private ControlFlowGraph<Node> cfg;
  private Scope jsScope;

  @Before
  public void setUp() {
    cfg = new ControlFlowGraph<>(new Node(Token.BLOCK));
    jsScope = null; // Need to initialize this object
  }

  @Test
  public void testIsForward() {
    MaybeReachingVariableUse variableUse = createVariableUse();
    assertFalse(variableUse.isForward());
  }

  @Test
  public void testCreateEntryLattice() {
    MaybeReachingVariableUse variableUse = createVariableUse();
    ReachingUses entryLattice = variableUse.createEntryLattice();
    Multimap<Var, Node> mayUseMap = entryLattice.mayUseMap;
    assertEquals(0, mayUseMap.size());
  }

  @Test
  public void testCreateInitialEstimateLattice() {
    MaybeReachingVariableUse variableUse = createVariableUse();
    ReachingUses initialEstimateLattice = variableUse.createInitialEstimateLattice();
    Multimap<Var, Node> mayUseMap = initialEstimateLattice.mayUseMap;
    assertEquals(0, mayUseMap.size());
  }

  @Test
  public void testFlowThrough() {
    MaybeReachingVariableUse variableUse = createVariableUse();
    Node n = new Node(Token.NAME, "x");
    ReachingUses input = new ReachingUses();
    ReachingUses output = variableUse.flowThrough(n, input);
    Multimap<Var, Node> mayUseMap = output.mayUseMap;
    assertEquals(1, mayUseMap.size());
    Collection<Node> nodes = mayUseMap.get(jsScope.getVar("x"));
    assertEquals(1, nodes.size());
    assertEquals(n, nodes.iterator().next());
  }

  @Test
  public void testGetUses() {
    MaybeReachingVariableUse variableUse = createVariableUse();
    Node defNode = new Node(Token.NAME, "x");
    Collection<Node> uses = variableUse.getUses("x", defNode);
    assertEquals(0, uses.size());
  }

  private MaybeReachingVariableUse createVariableUse() {
    return new MaybeReachingVariableUse(cfg, jsScope, new AbstractCompiler());
  }
}