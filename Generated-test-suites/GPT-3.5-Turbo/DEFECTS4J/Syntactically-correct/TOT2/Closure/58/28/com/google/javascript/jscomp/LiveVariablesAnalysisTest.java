import static org.junit.Assert.*;
import org.junit.Test;

public class LiveVariablesAnalysisTest {

    @Test
    public void testLiveVariableLatticeEquals() {
        LiveVariablesAnalysis.LiveVariableLattice l1 = new LiveVariablesAnalysis.LiveVariableLattice(5);
        LiveVariablesAnalysis.LiveVariableLattice l2 = new LiveVariablesAnalysis.LiveVariableLattice(5);
        assertTrue(l1.equals(l2));
    }

    @Test
    public void testLiveVariableLatticeIsLive() {
        LiveVariablesAnalysis.LiveVariableLattice l = new LiveVariablesAnalysis.LiveVariableLattice(5);
        l.liveSet.set(1);
        assertTrue(l.isLive(1));
    }

    @Test
    public void testCreateEntryLattice() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(controlFlowGraph, jsScope, compiler);
        LiveVariablesAnalysis.LiveVariableLattice entryLattice = analysis.createEntryLattice();
        assertNotNull(entryLattice);
    }

    @Test
    public void testGetVarIndex() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(controlFlowGraph, jsScope, compiler);
        int varIndex = analysis.getVarIndex("x");
        assertEquals(1, varIndex);
    }

    @Test
    public void testComputeGenKill() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(controlFlowGraph, jsScope, compiler);
        Node node = new Node(Token.VAR);
        LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(5);
        LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(node, input);
        assertNotNull(result);
    }

    @Test
    public void testMarkAllParametersEscaped() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(controlFlowGraph, jsScope, compiler);
        analysis.markAllParametersEscaped();
        Set<Var> escapedLocals = analysis.getEscapedLocals();
        assertNotNull(escapedLocals);
    }
}