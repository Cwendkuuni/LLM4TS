
import static org.junit.Assert.*;
import org.junit.Test;

public class MaybeReachingVariableUseTest {
  @Test
  public void testFlowThrough() {
    MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();
    MaybeReachingVariableUse.ReachingUses output;

    // Test 1
    output = new MaybeReachingVariableUse().flowThrough(node1, input);
    assertEquals("Expected output1", expectedOutput1, output);

    // Test 2
    output = new MaybeReachingVariableUse().flowThrough(node2, input);
    assertEquals("Expected output2", expectedOutput2, output);

    // Test 3
    output = new MaybeReachingVariableUse().flowThrough(node3, input);
    assertEquals("Expected output3", expectedOutput3, output);
  }

  @Test
  public void testGetUses() {
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse();
    Collection<Node> uses;

    // Test 1
    uses = maybeReachingVariableUse.getUses(name1, defNode1);
    assertEquals("Expected uses1", expectedUses1, uses);

    // Test 2
    uses = maybeReachingVariableUse.getUses(name2, defNode2);
    assertEquals("Expected uses2", expectedUses2, uses);

    // Test 3
    uses = maybeReachingVariableUse.getUses(name3, defNode3);
    assertEquals("Expected uses3", expectedUses3, uses);
  }
}
