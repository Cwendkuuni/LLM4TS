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
    public void testLiveVariableLatticeIsLive() {
        LiveVariablesAnalysis.LiveVariableLattice lattice = new LiveVariablesAnalysis.LiveVariableLattice(5);
        lattice.liveSet.set(2);

        assertTrue(lattice.isLive(2));
        assertFalse(lattice.isLive(3));
    }

    @Test
    public void testLiveVariableJoinOpApply() {
        LiveVariablesAnalysis.LiveVariableLattice lattice1 = new LiveVariablesAnalysis.LiveVariableLattice(5);
        lattice1.liveSet.set(1);

        LiveVariablesAnalysis.LiveVariableLattice lattice2 = new LiveVariablesAnalysis.LiveVariableLattice(5);
        lattice2.liveSet.set(3);

        LiveVariablesAnalysis.LiveVariableLattice result = new LiveVariablesAnalysis.LiveVariableJoinOp().apply(List.of(lattice1, lattice2));

        assertTrue(result.isLive(1));
        assertTrue(result.isLive(3));
        assertFalse(result.isLive(0));
    }

    // Add more test cases for other methods and scenarios

}