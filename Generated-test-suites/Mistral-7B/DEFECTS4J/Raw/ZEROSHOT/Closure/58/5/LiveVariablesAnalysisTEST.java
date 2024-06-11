import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;

import java.util.BitSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class LiveVariablesAnalysisTest {

  @Test
  public void testLiveVariablesAnalysis() {
    // Setup mock objects
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ControlFlowGraph<Node> cfg = mock(ControlFlowGraph.class);
    Scope jsScope = mock(Scope.class);

    // Configure mock objects
    when(compiler.getControlFlowGraph()).thenReturn(cfg);
    when(cfg.getNumNodes()).thenReturn(10);
    when(jsScope.getVarCount()).thenReturn(5);

    // Create LiveVariablesAnalysis object
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

    // Test getEscapedLocals method
    Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();
    assertEquals(Sets.newHashSet(jsScope.getVar(0), jsScope.getVar(1)), escapedLocals);

    // Test getVarIndex method
    assertEquals(0, liveVariablesAnalysis.getVarIndex("x"));

    // Test isForward method
    assertFalse(liveVariablesAnalysis.isForward());

    // Test createEntryLattice and createInitialEstimateLattice methods
    LiveVariableLattice entryLattice = liveVariablesAnalysis.createEntryLattice();
    LiveVariableLattice initialEstimateLattice = liveVariablesAnalysis.createInitialEstimateLattice();

    assertEquals(Sets.newHashSet(0, 1, 2, 3, 4), entryLattice.liveSet);
    assertEquals(Sets.newHashSet(0, 1, 2, 3, 4), initialEstimateLattice.liveSet);

    // Test flowThrough method
    Node node = mock(Node.class);
    LiveVariableLattice inputLattice = new LiveVariableLattice(Sets.newHashSet(0, 1, 2, 3, 4));

    LiveVariableLattice outputLattice = liveVariablesAnalysis.flowThrough(node, inputLattice);

    assertEquals(Sets.newHashSet(0, 1, 2, 3, 4), outputLattice.liveSet);
  }
}

class LiveVariableLattice {
  private final BitSet liveSet;

  LiveVariableLattice(int numVars) {
    this.liveSet = new BitSet(numVars);
  }

  LiveVariableLattice(LiveVariableLattice other) {
    this.liveSet = (BitSet) other.liveSet.clone();
  }

  boolean isLive(Var v) {
    return liveSet.get(v.index);
  }

  boolean isLive(int index) {
    return liveSet.get(index);
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof LiveVariableLattice) {
      LiveVariableLattice otherLattice = (LiveVariableLattice) other;
      return liveSet.equals(otherLattice.liveSet);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return liveSet.hashCode();
  }

  @Override
  public String toString() {
    return liveSet.toString();
  }
}