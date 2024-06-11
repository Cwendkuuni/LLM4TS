import static org.junit.Assert.*;

import org.junit.Test;

public class LiveVariablesAnalysisTest {

  @Test
  public void testLiveVariableLatticeConstructor() {
    LiveVariablesAnalysis.LiveVariableLattice lattice = new LiveVariablesAnalysis.LiveVariableLattice(5);
    assertEquals(0, lattice.liveSet.cardinality());
  }

  @Test
  public void testLiveVariableLatticeEquals() {
    LiveVariablesAnalysis.LiveVariableLattice lattice1 = new LiveVariablesAnalysis.LiveVariableLattice(5);
    LiveVariablesAnalysis.LiveVariableLattice lattice2 = new LiveVariablesAnalysis.LiveVariableLattice(5);
    assertEquals(lattice1, lattice2);
  }

  @Test
  public void testIsLive() {
    LiveVariablesAnalysis.LiveVariableLattice lattice = new LiveVariablesAnalysis.LiveVariableLattice(5);
    assertFalse(lattice.isLive(0));
  }

  @Test
  public void testGetVarIndex() {
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(controlFlowGraph, jsScope, compiler);
    assertEquals(2, liveVariablesAnalysis.getVarIndex("varName"));
  }

  @Test
  public void testComputeEscaped() {
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(controlFlowGraph, jsScope, compiler);
    liveVariablesAnalysis.computeEscaped(jsScope, escaped, compiler);
    assertNotNull(liveVariablesAnalysis.getEscapedLocals());
  }

  @Test
  public void testLiveVariableJoinOpApply() {
    LiveVariablesAnalysis.LiveVariableLattice lattice1 = new LiveVariablesAnalysis.LiveVariableLattice(5);
    LiveVariablesAnalysis.LiveVariableLattice lattice2 = new LiveVariablesAnalysis.LiveVariableLattice(5);
    LiveVariablesAnalysis.LiveVariableLattice joined = new LiveVariablesAnalysis.LiveVariableLattice(5);
    joined.liveSet.set(0, 1, 2);
    assertEquals(joined, new LiveVariablesAnalysis.LiveVariableJoinOp().apply(List.of(lattice1, lattice2)));
  }

  @Test
  public void testIsForward() {
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(controlFlowGraph, jsScope, compiler);
    assertFalse(liveVariablesAnalysis.isForward());
  }

  @Test
  public void testFlowThrough() {
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(controlFlowGraph, jsScope, compiler);
    LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(5);
    LiveVariablesAnalysis.LiveVariableLattice output = liveVariablesAnalysis.flowThrough(node, input);
    assertNotNull(output);
  }

  @Test
  public void testComputeGenKill() {
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(controlFlowGraph, jsScope, compiler);
    BitSet gen = new BitSet(5);
    BitSet kill = new BitSet(5);
    liveVariablesAnalysis.computeGenKill(node, gen, kill, false);
    assertNotNull(gen);
    assertNotNull(kill);
  }
}