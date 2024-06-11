import org.junit.Test;

import com.google.javascript.rhino.Node;

import static org.junit.Assert.*;

public class MaybeReachingVariableUseTest {

  @Test
  public void testFlowThrough() {
    ControlFlowGraph<Node> cfg = createTestCFG();
    MaybeReachingVariableUse.ReachingUses result = new MaybeReachingVariableUse(cfg, jsScope, compiler)
        .flowThrough(node, input);
    MaybeReachingVariableUse.ReachingUses expected = createExpectedLattice();
    assertEquals(expected, result);
  }

  @Test
  public void testJoinOp() {
    MaybeReachingVariableUse.ReachingUses result = new MaybeReachingVariableUse.ReachingUsesJoinOp()
        .apply(from);
    MaybeReachingVariableUse.ReachingUses expected = createExpectedLattice();
    assertEquals(expected, result);
  }

  @Test
  public void testCreateEntryLattice() {
    MaybeReachingVariableUse.ReachingUses result = new MaybeReachingVariableUse()
        .createEntryLattice();
    MaybeReachingVariableUse.ReachingUses expected = createExpectedLattice();
    assertEquals(expected, result);
  }

  @Test
  public void testCreateInitialEstimateLattice() {
    MaybeReachingVariableUse.ReachingUses result = new MaybeReachingVariableUse()
        .createInitialEstimateLattice();
    MaybeReachingVariableUse.ReachingUses expected = createExpectedLattice();
    assertEquals(expected, result);
  }

  @Test
  public void testGetUses() {
    MaybeReachingVariableUse.ReachingUses input = createInputLattice();
    MaybeReachingVariableUse target = new MaybeReachingVariableUse();
    Collection<Node> result = target.getUses("name", defNode);
    Collection<Node> expected = createExpectedUses();
    assertEquals(expected, result);
  }

  private ControlFlowGraph<Node> createTestCFG() {
    // TODO: Implement test CFG creation
    return null;
  }

  private MaybeReachingVariableUse.ReachingUses createExpectedLattice() {
    // TODO: Implement expected lattice creation
    return null;
  }

  private MaybeReachingVariableUse.ReachingUses createInputLattice() {
    // TODO: Implement input lattice creation
    return null;
  }

  private Collection<Node> createExpectedUses() {
    // TODO: Implement expected uses creation
    return null;
  }
}