import static org.junit.Assert.*;

import org.junit.Test;

public class LiveVariablesAnalysisTest {

    @Test
    public void testCreateEntryLattice() {
        LiveVariablesAnalysis.LiveVariableLattice lattice = new LiveVariablesAnalysis.LiveVariableLattice(5);
        assertNotNull(lattice);
        assertFalse(lattice.isLive(0));
    }

    @Test
    public void testCreateInitialEstimateLattice() {
        LiveVariablesAnalysis.LiveVariableLattice lattice = new LiveVariablesAnalysis.LiveVariableLattice(5);
        assertNotNull(lattice);
        assertFalse(lattice.isLive(3));
    }

    @Test
    public void testFlowThrough() {
        LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(5);
        input.liveSet.set(2);
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(node, input);
        assertTrue(result.isLive(2));
    }

    @Test
    public void testComputeGenKill() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        BitSet gen = new BitSet(5);
        BitSet kill = new BitSet(5);
        analysis.computeGenKill(node, gen, kill, false);
        assertTrue(gen.isEmpty());
        assertFalse(kill.isEmpty());
    }

    @Test
    public void testIsArgumentsName() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        Node n = new Node(Token.NAME, "arguments");
        assertTrue(analysis.isArgumentsName(n));
    }
}