import static org.junit.Assert.assertEquals;
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
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class MaybeReachingVariableUseTest {
  
  private MaybeReachingVariableUse reachingVariableUse;

  @Before
  public void setUp() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    reachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, new AbstractCompiler());
  }

  @Test
  public void testCreateEntryLattice() {
    MaybeReachingVariableUse.ReachingUses reachingUses = reachingVariableUse.createEntryLattice();
    assertTrue(reachingUses.mayUseMap.isEmpty());
  }

  @Test
  public void testCreateInitialEstimateLattice() {
    MaybeReachingVariableUse.ReachingUses reachingUses = reachingVariableUse.createInitialEstimateLattice();
    assertTrue(reachingUses.mayUseMap.isEmpty());
  }

  @Test
  public void testFlowThrough() {
    Node n = new Node(Token.BLOCK);
    n.addChildToFront(new Node(Token.VAR));
    n.addChildToBack(new Node(Token.NAME, "a"));
    MaybeReachingVariableUse.ReachingUses input = reachingVariableUse.createInitialEstimateLattice();
    MaybeReachingVariableUse.ReachingUses output = reachingVariableUse.flowThrough(n, input);
    assertTrue(output.mayUseMap.containsKey(new Var("a", new Scope())));
    assertEquals(1, output.mayUseMap.get(new Var("a", new Scope())).size());
  }

  @Test
  public void testGetUses() {
    Node n = new Node(Token.BLOCK);
    n.addChildToFront(new Node(Token.VAR));
    n.addChildToBack(new Node(Token.NAME, "a"));
    MaybeReachingVariableUse.ReachingUses input = reachingVariableUse.createInitialEstimateLattice();
    MaybeReachingVariableUse.ReachingUses output = reachingVariableUse.flowThrough(n, input);
    Collection<Node> uses = reachingVariableUse.getUses("a", n);
    assertTrue(uses.contains(new Node(Token.VAR)));
  }

  @Test
  public void testComputeMayUse() {
    Node n = new Node(Token.BLOCK);
    n.addChildToFront(new Node(Token.NAME, "a"));
    MaybeReachingVariableUse.ReachingUses output = reachingVariableUse.createInitialEstimateLattice();
    reachingVariableUse.computeMayUse(n, n, output, false);
    assertTrue(output.mayUseMap.containsKey(new Var("a", new Scope())));
    assertEquals(1, output.mayUseMap.get(new Var("a", new Scope())).size());
  }

  @Test
  public void testAddToUseIfLocal() {
    Node n = new Node(Token.BLOCK);
    n.addChildToFront(new Node(Token.NAME, "a"));
    MaybeReachingVariableUse.ReachingUses reachingUses = reachingVariableUse.createInitialEstimateLattice();
    reachingVariableUse.addToUseIfLocal("a", n, reachingUses);
    assertTrue(reachingUses.mayUseMap.containsKey(new Var("a", new Scope())));
    assertEquals(1, reachingUses.mayUseMap.get(new Var("a", new Scope())).size());
  }

  @Test
  public void testRemoveFromUseIfLocal() {
    Node n = new Node(Token.BLOCK);
    n.addChildToFront(new Node(Token.NAME, "a"));
    MaybeReachingVariableUse.ReachingUses reachingUses = reachingVariableUse.createInitialEstimateLattice();
    reachingVariableUse.addToUseIfLocal("a", n, reachingUses);
    reachingVariableUse.removeFromUseIfLocal("a", reachingUses);
    assertTrue(reachingUses.mayUseMap.isEmpty());
  }

}