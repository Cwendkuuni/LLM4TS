import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class LiveVariablesAnalysisTest {

  @Test
  public void testIsLive() {
    LiveVariablesAnalysis.LiveVariableLattice lattice = new LiveVariablesAnalysis.LiveVariableLattice(5);
    lattice.liveSet.set(0);
    lattice.liveSet.set(2);
    lattice.liveSet.set(4);
    assertEquals(true, lattice.isLive(0));
    assertEquals(false, lattice.isLive(1));
    assertEquals(true, lattice.isLive(2));
    assertEquals(false, lattice.isLive(3));
    assertEquals(true, lattice.isLive(4));
  }

  @Test
  public void testCreateEntryLattice() {
    LiveVariablesAnalysis liveVariables = new LiveVariablesAnalysis(null, null, null);
    LiveVariablesAnalysis.LiveVariableLattice lattice = liveVariables.createEntryLattice();
    assertEquals(0, lattice.liveSet.cardinality());
  }

  @Test
  public void testCreateInitialEstimateLattice() {
    LiveVariablesAnalysis liveVariables = new LiveVariablesAnalysis(null, null, null);
    LiveVariablesAnalysis.LiveVariableLattice lattice = liveVariables.createInitialEstimateLattice();
    assertEquals(0, lattice.liveSet.cardinality());
  }

  @Test
  public void testComputeGenKill() {
    LiveVariablesAnalysis liveVariables = new LiveVariablesAnalysis(null, null, null);
    LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(5);
    input.liveSet.set(0);
    input.liveSet.set(2);
    input.liveSet.set(4);
    LiveVariablesAnalysis.LiveVariableLattice gen = new LiveVariablesAnalysis.LiveVariableLattice(5);
    LiveVariablesAnalysis.LiveVariableLattice kill = new LiveVariablesAnalysis.LiveVariableLattice(5);
    liveVariables.computeGenKill(NodeUtil.newName("var a = 1;"), gen.liveSet, kill.liveSet, false);
    assertEquals(false, gen.isLive(0));
    assertEquals(false, kill.isLive(0));
    assertEquals(false, gen.isLive(1));
    assertEquals(false, kill.isLive(1));
    assertEquals(false, gen.isLive(2));
    assertEquals(false, kill.isLive(2));
    assertEquals(false, gen.isLive(3));
    assertEquals(false, kill.isLive(3));
    assertEquals(false, gen.isLive(4));
    assertEquals(false, kill.isLive(4));
  }

  @Test
  public void testMarkAllParametersEscaped() {
    LiveVariablesAnalysis liveVariables = new LiveVariablesAnalysis(null, null, null);
    liveVariables.markAllParametersEscaped();
    assertEquals(true, liveVariables.getEscapedLocals().contains("arguments"));
  }

  @Test
  public void testIsArgumentsName() {
    LiveVariablesAnalysis liveVariables = new LiveVariablesAnalysis(null, null, null);
    assertEquals(true, liveVariables.isArgumentsName(NodeUtil.newName("arguments")));
    assertEquals(false, liveVariables.isArgumentsName(NodeUtil.newName("arg")));
  }
}