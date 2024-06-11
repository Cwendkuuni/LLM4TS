import static org.junit.Assert.*;
import org.junit.Test;

public class LiveVariablesAnalysisTest {
  
  @Test
  public void testIsLive() {
    LiveVariablesAnalysis.LiveVariableLattice lattice = new LiveVariablesAnalysis.LiveVariableLattice(5);
    assertFalse(lattice.isLive(0));
    
    lattice.liveSet.set(0);
    assertTrue(lattice.isLive(0));
  }
  
  @Test
  public void testEquals() {
    LiveVariablesAnalysis.LiveVariableLattice lattice1 = new LiveVariablesAnalysis.LiveVariableLattice(5);
    LiveVariablesAnalysis.LiveVariableLattice lattice2 = new LiveVariablesAnalysis.LiveVariableLattice(5);
    
    assertTrue(lattice1.equals(lattice2));
  }
  
  @Test
  public void testCreateEntryLattice() {
    LiveVariablesAnalysis liveVarsAnalysis = new LiveVariablesAnalysis(null, null, null);
    LiveVariablesAnalysis.LiveVariableLattice lattice = liveVarsAnalysis.createEntryLattice();
    
    assertNotNull(lattice);
    assertEquals(0, lattice.liveSet.cardinality());
  }
  
  @Test
  public void testFlowThrough() {
    LiveVariablesAnalysis liveVarsAnalysis = new LiveVariablesAnalysis(null, null, null);
    LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(5);
    input.liveSet.set(0, true);
    Node node = new Node(Token.NAME);
    LiveVariablesAnalysis.LiveVariableLattice result = liveVarsAnalysis.flowThrough(node, input);
    
    assertNotNull(result);
    assertTrue(result.isLive(0));
  }
  
  @Test
  public void testMarkAllParametersEscaped() {
    LiveVariablesAnalysis liveVarsAnalysis = new LiveVariablesAnalysis(null, null, null);
    liveVarsAnalysis.markAllParametersEscaped();
    
    Set<Var> escapedLocals = liveVarsAnalysis.getEscapedLocals();
    
    assertNotNull(escapedLocals);
    // Check if all parameters are marked as escaped
    // Assert size of escapedLocals is equal to the number of parameters
  }
  
  @Test
  public void testGetVarIndex() {
    LiveVariablesAnalysis liveVarsAnalysis = new LiveVariablesAnalysis(null, null, null);
    int varIndex = liveVarsAnalysis.getVarIndex("testVar");
    
    // Assert varIndex is the correct index of the variable "testVar"
  }
}