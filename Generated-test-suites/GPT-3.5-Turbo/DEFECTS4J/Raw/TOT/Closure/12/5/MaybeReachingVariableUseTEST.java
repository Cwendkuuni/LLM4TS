import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Collection;
import com.google.javascript.rhino.Node;

public class MaybeReachingVariableUseTest {

  @Test
  public void testFlowThrough() {
    Node n = new Node(Token.VAR);
    n.addChildToBack(new Node(Token.NAME, new Node(Token.STRING, "x")));
    MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();
    MaybeReachingVariableUse.ReachingUses output = new MaybeReachingVariableUse.ReachingUses();
    
    output.mayUseMap.put(var, n); // Add some dummy data to output for testing purposes
    
    MaybeReachingVariableUse instance = new MaybeReachingVariableUse();
    MaybeReachingVariableUse.ReachingUses result = instance.flowThrough(n, input);
    
    assertNotEquals(result, input);
    assertEquals(result, output);
  }

  @Test
  public void testAddToUseIfLocal() {
    Node node = new Node(Token.STRING, "x");
    String name = "x";
    MaybeReachingVariableUse.ReachingUses use = new MaybeReachingVariableUse.ReachingUses();
    
    MaybeReachingVariableUse instance = new MaybeReachingVariableUse();
    instance.addToUseIfLocal(name, node, use);
    
    assertTrue(use.mayUseMap.containsKey(name));
    assertTrue(use.mayUseMap.get(name).contains(node));
  }

  @Test
  public void testRemoveFromUseIfLocal() {
    Node node = new Node(Token.STRING, "x");
    String name = "x";
    MaybeReachingVariableUse.ReachingUses use = new MaybeReachingVariableUse.ReachingUses();
    use.mayUseMap.put(name, node);
    
    MaybeReachingVariableUse instance = new MaybeReachingVariableUse();
    instance.removeFromUseIfLocal(name, use);
    
    assertTrue(use.mayUseMap.containsKey(name));
    assertTrue(use.mayUseMap.get(name).isEmpty());
  }
  
  @Test
  public void testGetUses() {
    Node defNode = new Node(Token.VAR);
    defNode.addChildToBack(new Node(Token.NAME, new Node(Token.STRING, "x")));
    MaybeReachingVariableUse instance = new MaybeReachingVariableUse();
    
    Collection<Node> uses = instance.getUses("x", defNode);
    
    assertNotNull(uses);
    assertTrue(uses.isEmpty());
  }
}