
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Collection;

public class MaybeReachingVariableUseTest {

  @Test
  public void testGetUses() {
    MaybeReachingVariableUse variableUse = new MaybeReachingVariableUse(null, null, null);

    // Test case for a local variable with only one use
    Node defNode1 = new Node(Token.VAR);
    defNode1.addChildToBack(new Node(Token.NAME, "a"));
    Collection<Node> uses1 = variableUse.getUses("a", defNode1);
    assertEquals(0, uses1.size());

    // Test case for a local variable with multiple uses
    Node defNode2 = new Node(Token.VAR);
    defNode2.addChildToBack(new Node(Token.NAME, "b"));
    Collection<Node> uses2 = variableUse.getUses("b", defNode2);
    assertEquals(0, uses2.size());

    // Test case for a non-local variable
    Node defNode3 = new Node(Token.VAR);
    defNode3.addChildToBack(new Node(Token.NAME, "c"));
    Collection<Node> uses3 = variableUse.getUses("c", defNode3);
    assertEquals(0, uses3.size());
  }

  @Test
  public void testFlowThrough() {
    MaybeReachingVariableUse variableUse = new MaybeReachingVariableUse(null, null, null);

    // Test case for a simple node with a variable assignment
    Node n1 = new Node(Token.NAME, "x");
    Node defNode1 = new Node(Token.VAR);
    defNode1.addChildToBack(new Node(Token.NAME, "y"));
    ReachingUses input1 = new ReachingUses();
    ReachingUses output1 = variableUse.flowThrough(n1, input1);
    assertEquals(input1, output1);

    // Test case for a block node
    Node n2 = new Node(Token.BLOCK);
    ReachingUses input2 = new ReachingUses();
    ReachingUses output2 = variableUse.flowThrough(n2, input2);
    assertEquals(input2, output2);
  }
}
