import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MaybeReachingVariableUseTest {

  private MaybeReachingVariableUse maybeReachingVariableUse;

  @Before
  public void setUp() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    maybeReachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, compiler);
  }

  @Test
  public void testCreateEntryLattice() {
    MaybeReachingVariableUse.ReachingUses reachingUses = maybeReachingVariableUse.createEntryLattice();
    assertNotNull(reachingUses);
  }

  @Test
  public void testCreateInitialEstimateLattice() {
    MaybeReachingVariableUse.ReachingUses reachingUses = maybeReachingVariableUse.createInitialEstimateLattice();
    assertNotNull(reachingUses);
  }

  @Test
  public void testFlowThrough() {
    Node n = new Node(Token.NAME, "test");
    MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();
    MaybeReachingVariableUse.ReachingUses output = maybeReachingVariableUse.flowThrough(n, input);
    assertNotNull(output);
  }

  @Test
  public void testGetUses() {
    String name = "test";
    Node defNode = new Node(Token.NAME, "def");
    maybeReachingVariableUse.getUses(name, defNode);
  }

  @Test
  public void testIsForward() {
    boolean isForward = maybeReachingVariableUse.isForward();
    assertFalse(isForward);
  }

  @Test
  public void testEscapedVariables() {
    MaybeReachingVariableUse escapingVariables = maybeReachingVariableUse.computeEscaped(jsScope, escaped, compiler);
    assertNotNull(escapingVariables);
  }
}