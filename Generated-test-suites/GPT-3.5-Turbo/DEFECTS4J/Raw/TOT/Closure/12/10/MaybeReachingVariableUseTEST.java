import org.junit.Test;

public class MaybeReachingVariableUseTest {
  
  // Test for the addToUseIfLocal method
  @Test
  public void testAddToUseIfLocal() {
    MaybeReachingVariableUse.ReachingUses use = new MaybeReachingVariableUse.ReachingUses();
    use.mayUseMap.put(new Var("x"), new Node("Node1"));
    
    String name = "x";
    Node node = new Node("Node2");
    
    MaybeReachingVariableUse.addToUseIfLocal(name, node, use);
    
    assertTrue(use.mayUseMap.containsValue(node));
  }
  
  // Test for the removeFromUseIfLocal method
  @Test
  public void testRemoveFromUseIfLocal() {
    MaybeReachingVariableUse.ReachingUses use = new MaybeReachingVariableUse.ReachingUses();
    use.mayUseMap.put(new Var("x"), new Node("Node1"));
    
    String name = "x";
    
    MaybeReachingVariableUse.removeFromUseIfLocal(name, use);
    
    assertTrue(use.mayUseMap.isEmpty());
  }
  
  // Test for the getUses method
  @Test
  public void testGetUses() {
    MaybeReachingVariableUse mrvu = new MaybeReachingVariableUse();
    
    Node defNode = new Node("defNode");
    defNode.addChildToFront(new Node(Token.NAME, "x"));
    
    Collection<Node> uses = mrvu.getUses("x", defNode);
    
    assertTrue(uses.isEmpty());
  }
  
}