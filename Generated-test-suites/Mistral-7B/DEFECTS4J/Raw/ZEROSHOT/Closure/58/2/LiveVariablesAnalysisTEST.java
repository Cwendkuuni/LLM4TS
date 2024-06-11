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
    // Given: A simple control flow graph and scope
    ControlFlowGraph<Node> cfg = mock(ControlFlowGraph.class);
    Scope jsScope = mock(Scope.class);
    when(cfg.getRootNode()).thenReturn(Node.newNode(Token.SCRIPT, null));
    when(cfg.getOutEdges(Node.newNode(Token.SCRIPT, null))).thenReturn(
        Sets.newArrayList());
    when(jsScope.getVarCount()).thenReturn(2);
    when(jsScope.getVar(0)).thenReturn(new Var("a", 0));
    when(jsScope.getVar(1)).thenReturn(new Var("b", 1));

    // When: Creating a LiveVariablesAnalysis instance
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, mock(AbstractCompiler.class));

    // Then: The entry lattice should contain all variables
    LiveVariablesAnalysis.LiveVariableLattice entryLattice = liveVariablesAnalysis
        .createEntryLattice();
    BitSet entryLatticeLiveSet = entryLattice.liveSet;
    assertEquals(2, entryLatticeLiveSet.cardinality());
    assertEquals(true, entryLatticeLiveSet.get(0));
    assertEquals(true, entryLatticeLiveSet.get(1));

    // When: Analyzing a simple script
    LiveVariablesAnalysis.LiveVariableLattice result = liveVariablesAnalysis
        .computeResult(Node.newNode(Token.SCRIPT, null));

    // Then: All variables should be live at the end of the script
    BitSet resultLiveSet = result.liveSet;
    assertEquals(2, resultLiveSet.cardinality());
    assertEquals(true, resultLiveSet.get(0));
    assertEquals(true, resultLiveSet.get(1));
  }
}