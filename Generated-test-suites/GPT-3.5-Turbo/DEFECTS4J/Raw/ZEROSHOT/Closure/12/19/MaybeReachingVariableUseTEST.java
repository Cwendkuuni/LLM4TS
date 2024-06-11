import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.MaybeReachingVariableUse;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.Collection;
import java.util.Set;

public class MaybeReachingVariableUseTest {

  private MaybeReachingVariableUse reachingVariableUse;
  private ControlFlowGraph<Node> cfg;
  private Scope jsScope;
  private Set<Scope.Var> escaped;
  
  @Before
  public void setUp() {
    cfg = new ControlFlowGraph<Node>();
    jsScope = new Scope();
    escaped = HashMultimap.create();
    reachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, null);
  }

  @Test
  public void testCreateEntryLattice() {
    MaybeReachingVariableUse.ReachingUses reachingUses = 
        reachingVariableUse.createEntryLattice();
    
    assertNotNull(reachingUses);
    assertTrue(reachingUses.mayUseMap.isEmpty());
  }

  @Test
  public void testCreateInitialEstimateLattice() {
    MaybeReachingVariableUse.ReachingUses reachingUses = 
        reachingVariableUse.createInitialEstimateLattice();
    
    assertNotNull(reachingUses);
    assertTrue(reachingUses.mayUseMap.isEmpty());
  }

  @Test
  public void testFlowThrough() {
    Node node = new Node(Token.NAME);
    node.setString("test");
    
    MaybeReachingVariableUse.ReachingUses input = 
        new MaybeReachingVariableUse.ReachingUses();
    input.mayUseMap.put(null, node);
    
    MaybeReachingVariableUse.ReachingUses output = 
        reachingVariableUse.flowThrough(node, input);
    
    assertNotEquals(input, output);
  }

  @Test
  public void testGetUses() {
    String name = "test";
    Node defNode = new Node(Token.NAME);
    defNode.setString(name);
    
    Collection<Node> uses = reachingVariableUse.getUses(name, defNode);
    
    assertNotNull(uses);
  }
}