import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class MaybeReachingVariableUseTest {

  private MaybeReachingVariableUse.ReachingUses reachingUses;

  @Before
  public void setUp() {
    reachingUses = new MaybeReachingVariableUse.ReachingUses();
  }

  @Test
  public void testEquals() {
    MaybeReachingVariableUse.ReachingUses reachingUses2 = new MaybeReachingVariableUse.ReachingUses();
    assertTrue(reachingUses.equals(reachingUses2));
  }

  @Test
  public void testHashCode() {
    MaybeReachingVariableUse.ReachingUses reachingUses2 = new MaybeReachingVariableUse.ReachingUses();
    assertEquals(reachingUses.hashCode(), reachingUses2.hashCode());
  }

  @Test
  public void testFlowThrough() {
    // Create a test node
    Node node = new Node(Token.NAME, "test");
    MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();
    // Call the method
    MaybeReachingVariableUse.ReachingUses result = MaybeReachingVariableUse.flowThrough(node, input);
    // Verify the result
    assertEquals(input, result);
  }

  @Test
  public void testAddToUseIfLocal() {
    // Create test variables
    String name = "test";
    Node node = new Node(Token.NAME, "test");
    MaybeReachingVariableUse.ReachingUses use = new MaybeReachingVariableUse.ReachingUses();
    // Call the method
    MaybeReachingVariableUse.addToUseIfLocal(name, node, use);
    // Verify the result
    assertTrue(use.mayUseMap.containsKey(name));
    assertEquals(node, use.mayUseMap.get(name));
  }

  @Test
  public void testRemoveFromUseIfLocal() {
    // Create test variables
    String name = "test";
    MaybeReachingVariableUse.ReachingUses use = new MaybeReachingVariableUse.ReachingUses();
    Node node = new Node(Token.NAME, "test");
    use.mayUseMap.put(name, node);
    // Call the method
    MaybeReachingVariableUse.removeFromUseIfLocal(name, use);
    // Verify the result
    assertFalse(use.mayUseMap.containsKey(name));
  }

  @Test
  public void testGetUses() {
    // Create test variables
    String name = "test";
    Node defNode = new Node(Token.NAME, "defNode");
    MaybeReachingVariableUse.ReachingUses use = new MaybeReachingVariableUse.ReachingUses();
    Node node = new Node(Token.NAME, "test");
    use.mayUseMap.put(name, node);
    // Set up the mock objects
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    cfg.addNode(defNode);
    GraphNode<Node, Branch> graphNode = new GraphNode<>(defNode);
    FlowState<MaybeReachingVariableUse.ReachingUses> state = new FlowState<>(use);
    graphNode.setAnnotation(state);
    cfg.addSyntheticDependency(defNode, cfg.getImplicitReturn());
    MaybeReachingVariableUse<ReachingUses> maybeReachingVariableUse = new MaybeReachingVariableUse(cfg, null, null);
    // Call the method
    Collection<Node> result = maybeReachingVariableUse.getUses(name, defNode);
    // Verify the result
    assertTrue(result.contains(node));
  }
}