
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MaybeReachingVariableUseTest {
  @Test
  public void testFlowThrough() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();

    MaybeReachingVariableUse.ReachingUses expectedOutput = new MaybeReachingVariableUse.ReachingUses();
    MaybeReachingVariableUse.ReachingUses actualOutput;

    MaybeReachingVariableUse reachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    // Test flowThrough method
    actualOutput = reachingVariableUse.flowThrough(n, input);
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testComputeMayUse() {
    MaybeReachingVariableUse reachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    // Test computeMayUse method

    // Test case 1: Valid local variable
    Node n1 = new Node(Token.NAME, "x");
    Node cfgNode1 = new Node(Token.IF);
    MaybeReachingVariableUse.ReachingUses output1 = new MaybeReachingVariableUse.ReachingUses();
    boolean conditional1 = true;
    reachingVariableUse.computeMayUse(n1, cfgNode1, output1, conditional1);
    Collection<Node> expectedOutput1 = new ArrayList<>();
    expectedOutput1.add(n1);
    Collection<Node> actualOutput1 = output1.mayUseMap.get(jsScope.getVar("x"));
    assertEquals(expectedOutput1, actualOutput1);

    // Test case 2: Escaped variable
    Node n2 = new Node(Token.NAME, "y");
    Node cfgNode2 = new Node(Token.IF);
    MaybeReachingVariableUse.ReachingUses output2 = new MaybeReachingVariableUse.ReachingUses();
    boolean conditional2 = true;
    reachingVariableUse.computeMayUse(n2, cfgNode2, output2, conditional2);
    Collection<Node> expectedOutput2 = new ArrayList<>();
    Collection<Node> actualOutput2 = output2.mayUseMap.get(jsScope.getVar("y"));
    assertEquals(expectedOutput2, actualOutput2);
  }

  @Test
  public void testGetUses() {
    MaybeReachingVariableUse reachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    // Test getUses method

    // Test case 1: Valid local variable
    String name1 = "x";
    Node defNode1 = new Node(Token.NAME, "x");
    Collection<Node> expectedOutput1 = new ArrayList<>();
    expectedOutput1.add(defNode1);
    Collection<Node> actualOutput1 = reachingVariableUse.getUses(name1, defNode1);
    assertEquals(expectedOutput1, actualOutput1);

    // Test case 2: Escaped variable
    String name2 = "y";
    Node defNode2 = new Node(Token.NAME, "y");
    Collection<Node> expectedOutput2 = new ArrayList<>();
    Collection<Node> actualOutput2 = reachingVariableUse.getUses(name2, defNode2);
    assertEquals(expectedOutput2, actualOutput2);
  }
}
