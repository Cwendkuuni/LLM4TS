import static org.junit.Assert.*;

import org.junit.Test;

public class LiveVariablesAnalysisTest {

    @Test
    public void testCreateEntryLattice() {
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis();
        LiveVariablesAnalysis.LiveVariableLattice lattice = liveVariablesAnalysis.createEntryLattice();
        assertNotNull(lattice);
    }

    @Test
    public void testCreateInitialEstimateLattice() {
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis();
        LiveVariablesAnalysis.LiveVariableLattice lattice = liveVariablesAnalysis.createInitialEstimateLattice();
        assertNotNull(lattice);
    }

    @Test
    public void testFlowThrough() {
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis();
        Node node = new Node();
        LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(10);
        LiveVariablesAnalysis.LiveVariableLattice result = liveVariablesAnalysis.flowThrough(node, input);
        assertNotNull(result);
    }

    @Test
    public void testGetEscapedLocals() {
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis();
        Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();
        assertNotNull(escapedLocals);
    }

    @Test
    public void testGetVarIndex() {
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis();
        int varIndex = liveVariablesAnalysis.getVarIndex("x");
        assertEquals(1, varIndex);
    }

    @Test
    public void testIsForward() {
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis();
        boolean isForward = liveVariablesAnalysis.isForward();
        assertFalse(isForward);
    }

    @Test
    public void testComputeGenKill() {
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis();
        Node node = new Node();
        BitSet gen = new BitSet();
        BitSet kill = new BitSet();
        boolean conditional = false;
        liveVariablesAnalysis.computeGenKill(node, gen, kill, conditional);
        assertNotNull(gen);
        assertNotNull(kill);
    }

    @Test
    public void testAddToSetIfLocal() {
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis();
        Node node = new Node();
        BitSet set = new BitSet();
        liveVariablesAnalysis.addToSetIfLocal(node, set);
        assertNull(set);
    }

    @Test
    public void testMarkAllParametersEscaped() {
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis();
        liveVariablesAnalysis.markAllParametersEscaped();
    }

    @Test
    public void testIsArgumentsName() {
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis();
        Node node = new Node();
        boolean isArgumentsName = liveVariablesAnalysis.isArgumentsName(node);
        assertFalse(isArgumentsName);
    }
}