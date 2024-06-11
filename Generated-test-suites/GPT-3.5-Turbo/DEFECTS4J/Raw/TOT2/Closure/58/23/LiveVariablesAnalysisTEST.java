import static org.junit.Assert.*;
import org.junit.Test;

public class LiveVariablesAnalysisTest {

    @Test
    public void testLiveVariableLattice() {
        LiveVariablesAnalysis.LiveVariableLattice lattice1 = new LiveVariablesAnalysis.LiveVariableLattice(5);
        LiveVariablesAnalysis.LiveVariableLattice lattice2 = new LiveVariablesAnalysis.LiveVariableLattice(5);

        assertEquals(lattice1, lattice2);
        assertNotEquals(lattice1.hashCode(), lattice2.hashCode());

        lattice1.liveSet.set(0);
        lattice1.liveSet.set(2);

        assertTrue(lattice1.isLive(0));
        assertFalse(lattice1.isLive(1));
        assertTrue(lattice1.isLive(2));
    }

    @Test
    public void testCreateEntryLattice() {
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(null, null, null);
        LiveVariablesAnalysis.LiveVariableLattice entryLattice = liveVariablesAnalysis.createEntryLattice();

        assertNotNull(entryLattice);
        assertEquals(0, entryLattice.liveSet.cardinality());
    }

    @Test
    public void testCreateInitialEstimateLattice() {
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(null, null, null);
        LiveVariablesAnalysis.LiveVariableLattice initialLattice = liveVariablesAnalysis.createInitialEstimateLattice();

        assertNotNull(initialLattice);
        assertEquals(0, initialLattice.liveSet.cardinality());
    }

    @Test
    public void testFlowThrough() {
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(null, null, null);
        LiveVariablesAnalysis.LiveVariableLattice inputLattice = new LiveVariablesAnalysis.LiveVariableLattice(5);

        inputLattice.liveSet.set(1);
        inputLattice.liveSet.set(3);

        LiveVariablesAnalysis.LiveVariableLattice result = liveVariablesAnalysis.flowThrough(null, inputLattice);

        assertNotNull(result);
        assertTrue(result.isLive(1));
        assertTrue(result.isLive(3));
    }

    @Test
    public void testGetVarIndex() {
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(null, null, null);
        assertEquals(2, liveVariablesAnalysis.getVarIndex("variable"));
    }
}