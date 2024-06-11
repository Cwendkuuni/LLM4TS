import static org.junit.Assert.assertEquals;

import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

public class MaybeReachingVariableUseTest {

  private MaybeReachingVariableUse reachingVariableUse;

  @Before
  public void setUp() {
    ControlFlowGraph<Node> cfg = ControlFlowGraph.build(null);
    Scope jsScope = new Scope(null);
    AbstractCompiler compiler = new AbstractCompiler();
    reachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, compiler);
  }

  @Test
  public void testFlowThrough() {
    Node n = new Node(Token.NAME);
    MaybeReachingVariableUse.ReachingUses input = reachingVariableUse.createEntryLattice();
    MaybeReachingVariableUse.ReachingUses output = reachingVariableUse.flowThrough(n, input);
    assertEquals(input, output);
  }

  @Test
  public void testComputeMayUse() {
    Node n = new Node(Token.BLOCK);
    Node cfgNode = new Node(Token.BLOCK);
    MaybeReachingVariableUse.ReachingUses output = new MaybeReachingVariableUse.ReachingUses();
    reachingVariableUse.computeMayUse(n, cfgNode, output, false);
    assertEquals(new MaybeReachingVariableUse.ReachingUses(), output);
  }

  @Test
  public void testAddToUseIfLocal() {
    String name = "A";
    Node node = new Node(Token.NAME, name);
    MaybeReachingVariableUse.ReachingUses use = new MaybeReachingVariableUse.ReachingUses();
    reachingVariableUse.addToUseIfLocal(name, node, use);
    assertEquals(1, use.mayUseMap.size());
    assertEquals(node, use.mayUseMap.get(null).iterator().next());
  }

  @Test
  public void testRemoveFromUseIfLocal() {
    String name = "A";
    MaybeReachingVariableUse.ReachingUses use = new MaybeReachingVariableUse.ReachingUses();
    reachingVariableUse.removeFromUseIfLocal(name, use);
    assertEquals(new MaybeReachingVariableUse.ReachingUses(), use);
  }

  @Test
  public void testGetUses() {
    String name = "A";
    Node defNode = new Node(Token.NAME, name);
    reachingVariableUse.getUses(name, defNode);
    // insert verification here
  }

}