import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MaybeReachingVariableUseTest {

  private MaybeReachingVariableUse analysis;

  @Before
  public void setUp() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();

    // Create a test instance of the MaybeReachingVariableUse class
    analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);
  }

  @Test
  public void testFlowThrough() {
    Node n = new Node(Token.BLOCK);
    ReachingUses input = new ReachingUses();
    ReachingUses output = analysis.flowThrough(n, input);
    
    assertEquals(input, output);
  }

  @Test
  public void testCreateEntryLattice() {
    ReachingUses entryLattice = analysis.createEntryLattice();
    
    assertNotNull(entryLattice.mayUseMap);
    assertTrue(entryLattice.mayUseMap.isEmpty());
  }

  @Test
  public void testCreateInitialEstimateLattice() {
    ReachingUses initialEstimateLattice = analysis.createInitialEstimateLattice();
    
    assertNotNull(initialEstimateLattice.mayUseMap);
    assertTrue(initialEstimateLattice.mayUseMap.isEmpty());
  }

  @Test
  public void testIsForward() {
    assertFalse(analysis.isForward());
  }

  @Test
  public void testJoinOp() {
    ReachingUses uses1 = new ReachingUses();
    ReachingUses uses2 = new ReachingUses();
    uses2.mayUseMap.put(var1, node1);
    uses2.mayUseMap.put(var2, node2);

    JoinOp<ReachingUses> joinOp = new Analysis.JoinOp<>();
    ReachingUses result = joinOp.apply(Arrays.asList(uses1, uses2));

    assertNotNull(result.mayUseMap);

    assertTrue(result.mayUseMap.containsKey(var1));
    assertTrue(result.mayUseMap.containsKey(var2));
    assertTrue(result.mayUseMap.containsValue(node1));
    assertTrue(result.mayUseMap.containsValue(node2));
  }

  @Test
  public void testGetUses() {
    analysis.flowThrough(n, input);
    Collection<Node> uses = analysis.getUses("exampleVariable", defNode);

    assertNotNull(uses);
    assertEquals(1, uses.size());
    assertTrue(uses.contains(expectedNode));
  }

}