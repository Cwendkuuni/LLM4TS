import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LiveVariablesAnalysisTest {

    private LiveVariablesAnalysis.LiveVariableLattice lattice;
    private LiveVariablesAnalysis analysis;

    @Before
    public void setUp() {
        lattice = new LiveVariablesAnalysis.LiveVariableLattice(10);
        analysis = new LiveVariablesAnalysis(null, null, null);
    }

    @Test
    public void testEquals() {
        LiveVariablesAnalysis.LiveVariableLattice other = new LiveVariablesAnalysis.LiveVariableLattice(10);
        assertNotEquals(lattice, null);
        assertNotEquals(lattice, new Object());
        assertNotEquals(lattice, other);
        assertEquals(lattice, lattice);
    }

    @Test
    public void testIsLive() {
        assertFalse(lattice.isLive(0));
        assertFalse(lattice.isLive(1));
        assertFalse(lattice.isLive(2));
        assertFalse(lattice.isLive(3));
        assertFalse(lattice.isLive(4));
        assertFalse(lattice.isLive(5));
        assertFalse(lattice.isLive(6));
        assertFalse(lattice.isLive(7));
        assertFalse(lattice.isLive(8));
        assertFalse(lattice.isLive(9));

        lattice.liveSet.set(0);
        lattice.liveSet.set(9);

        assertTrue(lattice.isLive(0));
        assertTrue(lattice.isLive(9));
        assertFalse(lattice.isLive(1));
        assertFalse(lattice.isLive(2));
        assertFalse(lattice.isLive(3));
        assertFalse(lattice.isLive(4));
        assertFalse(lattice.isLive(5));
        assertFalse(lattice.isLive(6));
        assertFalse(lattice.isLive(7));
        assertFalse(lattice.isLive(8));
    }

    @Test
    public void testToString() {
        assertEquals("{}", lattice.toString());

        lattice.liveSet.set(0);
        lattice.liveSet.set(1);

        assertEquals("{0, 1}", lattice.toString());
    }

    @Test
    public void testHashCode() {
        LiveVariablesAnalysis.LiveVariableLattice other = new LiveVariablesAnalysis.LiveVariableLattice(10);
        assertNotEquals(lattice.hashCode(), other.hashCode());

        LiveVariablesAnalysis.LiveVariableLattice same = new LiveVariablesAnalysis.LiveVariableLattice(lattice);
        assertEquals(lattice.hashCode(), same.hashCode());
    }

    @Test
    public void testFlowThrough() {
        LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(10);
        LiveVariablesAnalysis.LiveVariableLattice output = analysis.flowThrough(null, input);
        assertEquals(input, output);
    }
}