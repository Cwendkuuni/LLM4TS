
import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope.Var;
import java.util.Collection;
import java.util.List;

public class MaybeReachingVariableUseTest {

  @Test
  public void testGetUses() {
    MaybeReachingVariableUse.ReachingUses reachingUses = new MaybeReachingVariableUse.ReachingUses();
    Node defNode = new Node(Token.VAR);
    Var var = new Var("testVar");
    
    // Add a single use node to the mayUseMap
    reachingUses.mayUseMap.put(var, defNode);
    
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse();
    Collection<Node> uses = maybeReachingVariableUse.getUses("testVar", defNode);
    
    assertEquals(1, uses.size());
    assertTrue(uses.contains(defNode));
  }

  @Test
  public void testFlowThrough() {
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse();
  
    Node node = new Node(Token.VAR);
    MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();
    MaybeReachingVariableUse.ReachingUses output = maybeReachingVariableUse.flowThrough(node, input);
    
    assertEquals(input, output);
  }

  @Test
  public void testCreateInitialEstimateLattice() {
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse();
    MaybeReachingVariableUse.ReachingUses reachingUses = maybeReachingVariableUse.createInitialEstimateLattice();
    
    assertNotNull(reachingUses);
  }

  @Test
  public void testCreateEntryLattice() {
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse();
    MaybeReachingVariableUse.ReachingUses reachingUses = maybeReachingVariableUse.createEntryLattice();
    
    assertNotNull(reachingUses);
  }

  @Test
  public void testIsForward() {
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse();
    
    assertFalse(maybeReachingVariableUse.isForward());
  }

  @Test
  public void testMaybeReachingVariableUseConstructor() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, compiler);
    
    assertNotNull(maybeReachingVariableUse);
  }
}
