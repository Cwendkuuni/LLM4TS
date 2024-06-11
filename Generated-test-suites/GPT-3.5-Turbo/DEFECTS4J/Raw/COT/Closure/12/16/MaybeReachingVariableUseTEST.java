import static org.junit.Assert.assertEquals;

import java.util.Collection;
import org.junit.Test;

public class MaybeReachingVariableUseTest {
  
  private MaybeReachingVariableUse maybeReachingVariableUse;
  
  @Test
  public void testComputeMayUse() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    maybeReachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    Node node = new Node(Token.NAME, "test");
    MaybeReachingVariableUse.ReachingUses input = maybeReachingVariableUse.createInitialEstimateLattice();
    MaybeReachingVariableUse.ReachingUses output = maybeReachingVariableUse.flowThrough(node, input);

    Collection<Node> result = output.mayUseMap.get(jsScope.getVar("test"));
    assertEquals(1, result.size());
    assertEquals(node, result.iterator().next());
  }

  @Test
  public void testAddToUseIfLocal() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    maybeReachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    Node node = new Node(Token.NAME, "test");
    MaybeReachingVariableUse.ReachingUses use = new MaybeReachingVariableUse.ReachingUses();
    maybeReachingVariableUse.addToUseIfLocal("test", node, use);

    Collection<Node> result = use.mayUseMap.get(jsScope.getVar("test"));
    assertEquals(1, result.size());
    assertEquals(node, result.iterator().next());
  }

  @Test
  public void testRemoveFromUseIfLocal() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    maybeReachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    Node node = new Node(Token.NAME, "test");
    MaybeReachingVariableUse.ReachingUses use = new MaybeReachingVariableUse.ReachingUses();
    use.mayUseMap.put(jsScope.getVar("test"), node);

    maybeReachingVariableUse.removeFromUseIfLocal("test", use);

    Collection<Node> result = use.mayUseMap.get(jsScope.getVar("test"));
    assertEquals(0, result.size());
  }

  @Test
  public void testGetUses() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    maybeReachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();
    MaybeReachingVariableUse.ReachingUses.ReachingUse use = new MaybeReachingVariableUse.ReachingUses.ReachingUse();
    use.node = new Node(Token.NAME, "test");
    Node defNode = new Node(Token.NAME, "test");
    input.mayUseMap.put(jsScope.getVar("test"), use);

    Collection<Node> result = maybeReachingVariableUse.getUses("test", defNode);
    assertEquals(1, result.size());
    assertEquals(use.node, result.iterator().next());
  }
}