import org.junit.Test;
import static org.junit.Assert.*;

public class LiveVariablesAnalysisTest {

    @Test
    public void testLiveVariableLatticeEquals() {
        LiveVariablesAnalysis.LiveVariableLattice lattice1 = new LiveVariablesAnalysis.LiveVariableLattice(5);
        LiveVariablesAnalysis.LiveVariableLattice lattice2 = new LiveVariablesAnalysis.LiveVariableLattice(5);
        assertTrue(lattice1.equals(lattice2));
    }

    @Test
    public void testLiveVariableLatticeIsLive() {
        LiveVariablesAnalysis.LiveVariableLattice lattice = new LiveVariablesAnalysis.LiveVariableLattice(5);
        assertFalse(lattice.isLive(3)); // Initially false
        lattice.liveSet.set(3);
        assertTrue(lattice.isLive(3));
    }

    @Test
    public void testGetVarIndex() {
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(null, new Scope(), null);
        liveVariablesAnalysis.jsScope.createTempVar("testVar");
        assertEquals(0, liveVariablesAnalysis.getVarIndex("testVar"));
    }

    @Test
    public void testIsForward() {
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(null, new Scope(), null);
        assertFalse(liveVariablesAnalysis.isForward());
    }

    @Test
    public void testCreateEntryLattice() {
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(null, new Scope(), null);
        LiveVariablesAnalysis.LiveVariableLattice lattice = liveVariablesAnalysis.createEntryLattice();
        assertFalse(lattice.isLive(2)); // Should be initialized as false
    }

    @Test
    public void testComputeGenKill() {
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(null, new Scope(), null);
        liveVariablesAnalysis.jsScope.createTempVar("testVar");
        LiveVariablesAnalysis.LiveVariableLattice lattice = new LiveVariablesAnalysis.LiveVariableLattice(1);
        liveVariablesAnalysis.computeGenKill(new Node(Token.ASSIGN, new Node(Token.NAME, "testVar"), new Node("123")), lattice.liveSet, lattice.liveSet, false);
        assertTrue(lattice.isLive(0)); // testVar should be live after assignment
    }

    @Test
    public void testMarkAllParametersEscaped() {
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(null, new Scope(), null);
        liveVariablesAnalysis.jsScope.createTempVar("param1");
        liveVariablesAnalysis.jsScope.createTempVar("param2");
        liveVariablesAnalysis.markAllParametersEscaped();
        assertFalse(liveVariablesAnalysis.getEscapedLocals().isEmpty());
    }
}