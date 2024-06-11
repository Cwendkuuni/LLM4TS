import static org.junit.Assert.*;
import org.junit.Test;

public class MaybeReachingVariableUseTest {

  @Test
  public void testCreateEntryLattice() {
    MaybeReachingVariableUse.ReachingUses lattice = new MaybeReachingVariableUse.ReachingUses();
    assertNotNull(lattice.mayUseMap);
    assertTrue(lattice.mayUseMap.isEmpty());
  }

  @Test
  public void testCreateInitialEstimateLattice() {
    MaybeReachingVariableUse.ReachingUses lattice = new MaybeReachingVariableUse.ReachingUses();
    assertNotNull(lattice.mayUseMap);
    assertTrue(lattice.mayUseMap.isEmpty());
  }

  @Test
  public void testFlowThrough() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Node n = new Node(Token.BLOCK);
    MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();
    MaybeReachingVariableUse.ReachingUses output = new MaybeReachingVariableUse.ReachingUses();
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(cfg, null, null);

    MaybeReachingVariableUse.ReachingUses result = maybeReachingVariableUse.flowThrough(n, input);
    assertEquals(output, result);
  }

  @Test
  public void testJoinOp() {
    MaybeReachingVariableUse.ReachingUses uses1 = new MaybeReachingVariableUse.ReachingUses();
    MaybeReachingVariableUse.ReachingUses uses2 = new MaybeReachingVariableUse.ReachingUses();
    MaybeReachingVariableUse.ReachingUses result = new MaybeReachingVariableUse.ReachingUses();
    MaybeReachingVariableUse.ReachingUsesJoinOp joinOp = new MaybeReachingVariableUse.ReachingUsesJoinOp();

    MaybeReachingVariableUse.ReachingUses actualResult = joinOp.apply(Arrays.asList(uses1, uses2));
    assertEquals(result, actualResult);
  }

  @Test
  public void testIsForward() {
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(null, null, null);
    boolean result = maybeReachingVariableUse.isForward();
    assertFalse(result);
  }

  @Test
  public void testComputeMayUse() {
    Node n = new Node(Token.BLOCK);
    Node cfgNode = new Node(Token.FUNCTION);
    MaybeReachingVariableUse.ReachingUses output = new MaybeReachingVariableUse.ReachingUses();
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(null, null, null);

    maybeReachingVariableUse.computeMayUse(n, cfgNode, output, false);
    // Add test cases here for all possible cases
  }

  @Test
  public void testAddToUseIfLocal() {
    String name = "testVariableName";
    Node node = new Node(Token.NAME);
    MaybeReachingVariableUse.ReachingUses use = new MaybeReachingVariableUse.ReachingUses();
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(null, null, null);

    maybeReachingVariableUse.addToUseIfLocal(name, node, use);
    // Add test cases here for all possible cases
  }

  @Test
  public void testRemoveFromUseIfLocal() {
    String name = "testVariableName";
    MaybeReachingVariableUse.ReachingUses use = new MaybeReachingVariableUse.ReachingUses();
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(null, null, null);

    maybeReachingVariableUse.removeFromUseIfLocal(name, use);
    // Add test cases here for all possible cases
  }

  @Test
  public void testGetUses() {
    String name = "testVariableName";
    Node defNode = new Node(Token.BLOCK);
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(null, null, null);

    Collection<Node> result = maybeReachingVariableUse.getUses(name, defNode);
    // Add test cases here for all possible cases
  }
}