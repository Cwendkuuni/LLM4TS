import static org.junit.Assert.*;
import org.junit.Test;

public class LiveVariablesAnalysisTest {

    @Test
    public void testLiveVariableLatticeEquals() {
        LiveVariablesAnalysis.LiveVariableLattice lattice1 = new LiveVariablesAnalysis.LiveVariableLattice(5);
        LiveVariablesAnalysis.LiveVariableLattice lattice2 = new LiveVariablesAnalysis.LiveVariableLattice(5);
        assertEquals(lattice1, lattice2);
    }

    @Test
    public void testLiveVariableLatticeNotEquals() {
        LiveVariablesAnalysis.LiveVariableLattice lattice1 = new LiveVariablesAnalysis.LiveVariableLattice(5);
        LiveVariablesAnalysis.LiveVariableLattice lattice2 = new LiveVariablesAnalysis.LiveVariableLattice(5);
        lattice2.liveSet.set(1); // Set an index to differentiate the lattices
        assertNotEquals(lattice1, lattice2);
    }

    @Test
    public void testLiveVariableLatticeIsLive() {
        LiveVariablesAnalysis.LiveVariableLattice lattice = new LiveVariablesAnalysis.LiveVariableLattice(5);
        lattice.liveSet.set(3);
        assertTrue(lattice.isLive(3));
    }

    @Test
    public void testLiveVariableLatticeIsNotLive() {
        LiveVariablesAnalysis.LiveVariableLattice lattice = new LiveVariablesAnalysis.LiveVariableLattice(5);
        lattice.liveSet.set(1);
        assertFalse(lattice.isLive(3));
    }

}