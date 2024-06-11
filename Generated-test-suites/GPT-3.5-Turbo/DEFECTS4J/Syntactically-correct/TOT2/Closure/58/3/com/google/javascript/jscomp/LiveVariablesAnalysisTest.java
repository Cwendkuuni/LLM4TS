import static org.junit.Assert.*;
import org.junit.Test;

public class LiveVariablesAnalysisTest {

  @Test
  public void testLiveVariableLattice() {
    LiveVariablesAnalysis.LiveVariableLattice lattice = new LiveVariablesAnalysis.LiveVariableLattice(5);
    assertEquals(false, lattice.isLive(0));
    
    lattice.liveSet.set(2);
    assertEquals(true, lattice.isLive(2));
  }

  @Test
  public void testGetVarIndex() {
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(null, null, null);
    int index = liveVariablesAnalysis.getVarIndex("varName");
    assertEquals(-1, index);
  }

  @Test
  public void testIsForward() {
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(null, null, null);
    assertEquals(false, liveVariablesAnalysis.isForward());
  }

  @Test
  public void testCreateEntryLattice() {
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(null, null, null);
    LiveVariablesAnalysis.LiveVariableLattice lattice = liveVariablesAnalysis.createEntryLattice();
    assertNotNull(lattice);
  }

  @Test
  public void testCreateInitialEstimateLattice() {
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(null, null, null);
    LiveVariablesAnalysis.LiveVariableLattice lattice = liveVariablesAnalysis.createInitialEstimateLattice();
    assertNotNull(lattice);
  }

  @Test
  public void testFlowThrough() {
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(null, null, null);
    LiveVariablesAnalysis.LiveVariableLattice input = liveVariablesAnalysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice result = liveVariablesAnalysis.flowThrough(null, input);
    assertNotNull(result);
  }

  @Test
  public void testComputeGenKill() {
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(null, null, null);
    liveVariablesAnalysis.markAllParametersEscaped();
    assertTrue(liveVariablesAnalysis.getEscapedLocals().isEmpty());
  }

  @Test
  public void testMarkAllParametersEscaped() {
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(null, null, null);
    liveVariablesAnalysis.markAllParametersEscaped();
    assertTrue(liveVariablesAnalysis.getEscapedLocals().isEmpty());
  }

  @Test
  public void testIsArgumentsName() {
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(null, null, null);
    assertTrue(liveVariablesAnalysis.isArgumentsName(new Node(Token.NAME, new Node(Token.LEXICALSCOPE), "arguments")));
  }
}