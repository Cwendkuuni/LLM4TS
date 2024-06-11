import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.MaybeReachingVariableUse;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

public class MaybeReachingVariableUseTest {
  
  @Test
  public void testCreateEntryLattice() {
    MaybeReachingVariableUse.ReachingUses uses = new MaybeReachingVariableUse.ReachingUses();
    assertNotNull(uses);
  }
  
  @Test
  public void testCreateInitialEstimateLattice() {
    MaybeReachingVariableUse.ReachingUses initialUses = new MaybeReachingVariableUse.ReachingUses();
    assertNotNull(initialUses);
  }
  
  @Test
  public void testFlowThrough() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope scope = new Scope();
    MaybeReachingVariableUse reachingVariableUse = new MaybeReachingVariableUse(cfg, scope, new AbstractCompiler());
    
    MaybeReachingVariableUse.ReachingUses input = reachingVariableUse.createEntryLattice();
    MaybeReachingVariableUse.ReachingUses output = reachingVariableUse.flowThrough(new Node(Token.FUNCTION), input);
    
    assertNotNull(output);
  }
  
  @Test
  public void testGetUses() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope scope = new Scope();
    MaybeReachingVariableUse reachingVariableUse = new MaybeReachingVariableUse(cfg, scope, new AbstractCompiler());
    
    MaybeReachingVariableUse.ReachingUses uses = new MaybeReachingVariableUse.ReachingUses();
    Node defNode = new Node(Token.NAME, new Node(Token.NAME, "varName"));
    uses.mayUseMap.put(new Var("varName", scope), defNode);
    
    Collection<Node> result = reachingVariableUse.getUses("varName", defNode);
    
    assertEquals(1, result.size());
  }
}