
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.Set;

public class LiveVariablesAnalysisTest {

    private LiveVariablesAnalysis liveVariablesAnalysis;

    @Before
    public void setUp() {
        liveVariablesAnalysis = new LiveVariablesAnalysis(null, null, null);
    }

    @Test
    public void testGetEscapedLocals() {
        Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();
        assertNotNull(escapedLocals);
        assertEquals(0, escapedLocals.size());
    }

    @Test
    public void testGetVarIndex() {
        int varIndex = liveVariablesAnalysis.getVarIndex("var");
        assertEquals(0, varIndex);
    }

    @Test
    public void testIsForward() {
        assertFalse(liveVariablesAnalysis.isForward());
    }

    @Test
    public void testCreateEntryLattice() {
        LiveVariablesAnalysis.LiveVariableLattice entryLattice = liveVariablesAnalysis.createEntryLattice();
        assertNotNull(entryLattice);
        assertEquals(0, entryLattice.liveSet.size());
    }

    @Test
    public void testCreateInitialEstimateLattice() {
        LiveVariablesAnalysis.LiveVariableLattice initialEstimateLattice = liveVariablesAnalysis.createInitialEstimateLattice();
        assertNotNull(initialEstimateLattice);
        assertEquals(0, initialEstimateLattice.liveSet.size());
    }

    @Test
    public void testFlowThrough() {
        LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(5);
        LiveVariablesAnalysis.LiveVariableLattice output = liveVariablesAnalysis.flowThrough(null, input);
        assertNotNull(output);
    }
}
