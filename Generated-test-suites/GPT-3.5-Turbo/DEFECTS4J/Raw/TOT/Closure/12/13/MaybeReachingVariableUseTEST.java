import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MaybeReachingVariableUseTest {

  private MaybeReachingVariableUse maybeReachingVariableUse;
  private ControlFlowGraph<Node> cfg;
  private Scope jsScope;

  @Before
  public void setUp() {
    cfg = new ControlFlowGraph<Node>();
    jsScope = new Scope();
    maybeReachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, new AbstractCompiler());
  }

  @Test
  public void testCreateEntryLattice() {
    MaybeReachingVariableUse.ReachingUses entryLattice = maybeReachingVariableUse.createEntryLattice();

    assertTrue(entryLattice.mayUseMap.isEmpty());
  }

  @Test
  public void testCreateInitialEstimateLattice() {
    MaybeReachingVariableUse.ReachingUses initialEstimateLattice = maybeReachingVariableUse.createInitialEstimateLattice();

    assertTrue(initialEstimateLattice.mayUseMap.isEmpty());
  }

  @Test
  public void testFlowThrough() {
    Node n = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NUMBER, 10));
    MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();
    MaybeReachingVariableUse.ReachingUses output = maybeReachingVariableUse.flowThrough(n, input);

    assertEquals(1, output.mayUseMap.size());
    assertTrue(output.mayUseMap.containsEntry(jsScope.getVar("x"), n));
  }

  @Test
  public void testGetUses() {
    Node defNode = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NUMBER, 10));
    jsScope.declare("x", defNode, null, Var.Type.NORMAL);

    maybeReachingVariableUse.flowThrough(defNode, maybeReachingVariableUse.createEntryLattice());
    Collection<Node> uses = maybeReachingVariableUse.getUses("x", defNode);

    assertEquals(1, uses.size());
    assertTrue(uses.contains(defNode));
  }

}