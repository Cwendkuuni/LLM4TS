import org.junit.Test;

import static org.junit.Assert.*;

public class LiveVariablesAnalysisTest {
    @Test
    public void testCreateEntryLattice() {
        LiveVariablesAnalysis.LiveVariableLattice lattice = new LiveVariablesAnalysis.LiveVariableLattice(10);
        for (int i = 0; i < 10; i++) {
            assertFalse(lattice.isLive(i));
        }
    }

    @Test
    public void testCreateInitialEstimateLattice() {
        LiveVariablesAnalysis.LiveVariableLattice lattice = new LiveVariablesAnalysis.LiveVariableLattice(10);
        for (int i = 0; i < 10; i++) {
            assertFalse(lattice.isLive(i));
        }
    }

    @Test
    public void testFlowThrough() {
        LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(5);
        input.liveSet.set(0);
        input.liveSet.set(1);

        Node node = new Node(Token.ASSIGN,
                new Node(Token.NAME, "x"),
                new Node(Token.NAME, "y"));

        LiveVariablesAnalysis.LiveVariableLattice result = new LiveVariablesAnalysis.LiveVariableLattice(5);
        result.liveSet.set(1);
        result.liveSet.set(2);

        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        assertEquals(result, analysis.flowThrough(node, input));
    }

    @Test
    public void testIsLive() {
        LiveVariablesAnalysis.LiveVariableLattice lattice = new LiveVariablesAnalysis.LiveVariableLattice(5);
        lattice.liveSet.set(0);
        lattice.liveSet.set(3);

        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        assertTrue(analysis.isLive(lattice, 0));
        assertFalse(analysis.isLive(lattice, 1));
        assertFalse(analysis.isLive(lattice, 2));
        assertTrue(analysis.isLive(lattice, 3));
        assertFalse(analysis.isLive(lattice, 4));
    }

    @Test
    public void testGetEscapedLocals() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        assertNotNull(analysis.getEscapedLocals());
    }

    @Test
    public void testGetVarIndex() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        assertEquals(3, analysis.getVarIndex("x"));
        assertEquals(7, analysis.getVarIndex("y"));
    }
}