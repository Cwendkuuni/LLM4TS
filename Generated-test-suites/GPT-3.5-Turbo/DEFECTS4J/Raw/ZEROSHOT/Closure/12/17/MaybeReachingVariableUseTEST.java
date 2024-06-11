import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.jscomp.*;

public class MaybeReachingVariableUseTest {

  @Test
  public void testFlowThrough() {
    ControlFlowGraph cfg = new ControlFlowGraph<Node>();
    Node n = new Node(Token.BLOCK);
    cfg.createNode(n);
    MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();
    MaybeReachingVariableUse.ReachingUses output = new MaybeReachingVariableUse.ReachingUses();

    MaybeReachingVariableUse flow = new MaybeReachingVariableUse(cfg, jsScope, compiler);
    output = flow.flowThrough(n, input);
    // TODO: add assertions
  }

  @Test
  public void testCreateEntryLattice() {
    MaybeReachingVariableUse.ReachingUses lattice = new MaybeReachingVariableUse.ReachingUses();
    // TODO: add assertions
  }

  @Test
  public void testCreateInitialEstimateLattice() {
    MaybeReachingVariableUse.ReachingUses lattice = new MaybeReachingVariableUse.ReachingUses();
    // TODO: add assertions
  }

  @Test
  public void testJoin() {
    MaybeReachingVariableUse flow = new MaybeReachingVariableUse(cfg, jsScope, compiler);
    List<MaybeReachingVariableUse.ReachingUses> list = new ArrayList<>();
    MaybeReachingVariableUse.ReachingUses lattice1 = new MaybeReachingVariableUse.ReachingUses();
    MaybeReachingVariableUse.ReachingUses lattice2 = new MaybeReachingVariableUse.ReachingUses();
    list.add(lattice1);
    list.add(lattice2);
    MaybeReachingVariableUse.ReachingUses result = flow.join(list);
    // TODO: add assertions
  }

  @Test
  public void testGetUses() {
    MaybeReachingVariableUse flow = new MaybeReachingVariableUse(cfg, jsScope, compiler);
    String name = "testName";
    Node defNode = new Node(Token.BLOCK);
    Collection<Node> uses = flow.getUses(name, defNode);
    // TODO: add assertions
  }
}