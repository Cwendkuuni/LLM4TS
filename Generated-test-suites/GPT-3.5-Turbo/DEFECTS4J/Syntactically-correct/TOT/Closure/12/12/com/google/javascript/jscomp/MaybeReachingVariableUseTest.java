import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MaybeReachingVariableUseTest {

  private MaybeReachingVariableUse testObject;
  private ControlFlowGraph<Node> cfg;
  private Scope jsScope;

  @Before
  public void setUp() {
    cfg = new ControlFlowGraph<>();
    jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    testObject = new MaybeReachingVariableUse(cfg, jsScope, compiler);
  }

  @Test
  public void testFlowThrough() {
    // Create a node n of type Token.NAME
    // and set it as the current control flow graph node
    Node n = new Node(Token.NAME);
    testObject.cfg.setNode(n);

    // Create an input ReachingUses element
    MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();
    // Add a var to the input
    Var var = new Var();
    var.scope = jsScope;
    input.mayUseMap.put(var, n);

    // Flow through the node and obtain the output ReachingUses element
    MaybeReachingVariableUse.ReachingUses output = testObject.flowThrough(n, input);

    // Assert that the output equals the expected output
    MaybeReachingVariableUse.ReachingUses expectedOutput = new MaybeReachingVariableUse.ReachingUses();
    expectedOutput.mayUseMap.put(var, n);
    assertEquals(expectedOutput, output);
  }

  @Test
  public void testComputeMayUse() {
    // Create a node n of type Token.BLOCK
    Node n = new Node(Token.BLOCK);

    // Create an output ReachingUses element
    MaybeReachingVariableUse.ReachingUses output = new MaybeReachingVariableUse.ReachingUses();
    // Add a var to the output
    Var var = new Var();
    var.scope = jsScope;
    output.mayUseMap.put(var, n);

    // Compute the may use for the node
    testObject.computeMayUse(n, n, output, false);

    // Assert that the output equals the expected output
    MaybeReachingVariableUse.ReachingUses expectedOutput = new MaybeReachingVariableUse.ReachingUses();
    expectedOutput.mayUseMap.put(var, n);
    assertEquals(expectedOutput, output);
  }

  @Test
  public void testGetUses() {
    // Create a node defNode of type Token.NAME
    Node defNode = new Node(Token.NAME);
    testObject.cfg.setNode(defNode);
    
    // Create a node useNode of type Token.NAME
    Node useNode = new Node(Token.NAME);
    
    // Add the useNode to the mayUseMap of defNode in the ReachingUses element
    MaybeReachingVariableUse.ReachingUses element = new MaybeReachingVariableUse.ReachingUses();
    Var var = new Var();
    var.scope = jsScope;
    element.mayUseMap.put(var, useNode);
    testObject.cfg.getNode(defNode).setAnnotation(new FlowState<>(element));

    // Get the uses for the variable in defNode
    Collection<Node> uses = testObject.getUses("varName", defNode);

    // Assert that the uses contain the expected useNode
    assertTrue(uses.contains(useNode));
  }

}