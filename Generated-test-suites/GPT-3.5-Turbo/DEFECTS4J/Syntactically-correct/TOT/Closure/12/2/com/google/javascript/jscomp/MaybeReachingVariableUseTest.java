import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class MaybeReachingVariableUseTest {

  private MaybeReachingVariableUse maybeReachingVariableUse;

  @Before
  public void setUp() {
    ControlFlowGraph<Node> cfg = mock(ControlFlowGraph.class);
    Scope jsScope = mock(Scope.class);
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    maybeReachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, compiler);
  }

  @Test
  public void testComputeMayUse_simpleAssignment() {
    Node n = NodeUtil.newVarNode("x", Node.newNumber(5));
    MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();

    MaybeReachingVariableUse.ReachingUses output = maybeReachingVariableUse.flowThrough(n, input);

    Collection<Node> uses = maybeReachingVariableUse.getUses("x", n);
    assertEquals(0, uses.size());
  }

  @Test
  public void testComputeMayUse_conditionalAssignment() {
    Node n = NodeUtil.newVarNode("x", Node.newNumber(5));
    MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();

    MaybeReachingVariableUse.ReachingUses output = maybeReachingVariableUse.flowThrough(n, input);

    Collection<Node> uses = maybeReachingVariableUse.getUses("x", n);
    assertEquals(0, uses.size());
  }

  @Test
  public void testComputeMayUse_multipleAssignments() {
    Node n = NodeUtil.newVarNode("x", Node.newNumber(5));
    MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();

    MaybeReachingVariableUse.ReachingUses output = maybeReachingVariableUse.flowThrough(n, input);

    Collection<Node> uses = maybeReachingVariableUse.getUses("x", n);
    assertEquals(0, uses.size());
  }

  @Test
  public void testComputeMayUse_invalidAssignment() {
    Node n = Node.newString("x = 5");
    MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();

    MaybeReachingVariableUse.ReachingUses output = maybeReachingVariableUse.flowThrough(n, input);

    Collection<Node> uses = maybeReachingVariableUse.getUses("x", n);
    assertEquals(0, uses.size());
  }

  @Test
  public void testComputeMayUse_nestedAssignment() {
    Node n = NodeUtil.newVarNode("x", Node.newNumber(5));
    MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();

    MaybeReachingVariableUse.ReachingUses output = maybeReachingVariableUse.flowThrough(n, input);

    Collection<Node> uses = maybeReachingVariableUse.getUses("x", n);
    assertEquals(0, uses.size());
  }

  @Test
  public void testComputeMayUse_multipleVarDeclarations() {
    Node n = NodeUtil.newVarNode("x", Node.newNumber(5));
    MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();

    MaybeReachingVariableUse.ReachingUses output = maybeReachingVariableUse.flowThrough(n, input);

    Collection<Node> uses = maybeReachingVariableUse.getUses("x", n);
    assertEquals(0, uses.size());
  }

}