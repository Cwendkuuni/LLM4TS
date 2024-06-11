import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LiveVariablesAnalysisTest {
  
  // Variables used for testing
  private LiveVariablesAnalysis.LiveVariableLattice lattice;
  private LiveVariablesAnalysis analysis;
  
  @Before
  public void setUp() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope scope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    analysis = new LiveVariablesAnalysis(cfg, scope, compiler);

    // Create a dummy LiveVariableLattice with some live variables
    lattice = new LiveVariablesAnalysis.LiveVariableLattice(5);
    lattice.liveSet.set(2);
    lattice.liveSet.set(4);
  }
  
  @Test
  public void testIsLive_True() {
    // Test isLive method when the variable is live
    assertTrue(lattice.isLive(2));
  }

  @Test
  public void testIsLive_False() {
    // Test isLive method when the variable is not live
    assertFalse(lattice.isLive(1));
  }

  @Test
  public void testComputeGenKill() {
    // Test computeGenKill method with a sample Node
    Node node = new Node(Token.BLOCK, new Node(Token.NAME, "var"));
    BitSet gen = new BitSet();
    BitSet kill = new BitSet();
    boolean conditional = true;
    
    analysis.computeGenKill(node, gen, kill, conditional);
    
    // Check if the variables are correctly added to gen and kill sets
    assertTrue(gen.get(2));
    assertTrue(kill.isEmpty());
  }

  @Test
  public void testMarkAllParametersEscaped() {
    // Test markAllParametersEscaped() method
    analysis.markAllParametersEscaped();
    
    // Check if all parameters are added to the escaped set
    assertTrue(analysis.getEscapedLocals().containsAll(analysis.jsScope.getParameters()));
  }

  @Test
  public void testGetVarIndex() {
    // Test getVarIndex() method
    int varIndex = analysis.getVarIndex("testVar");
    
    // Check if the returned varIndex is correct
    assertEquals(2, varIndex);
  }
}