import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class LiveVariablesAnalysisTest {
    private LiveVariablesAnalysis.LiveVariableLattice liveVariableLattice;
    private LiveVariablesAnalysis liveVariablesAnalysis;

    @Before
    public void setUp() {
        liveVariableLattice = new LiveVariablesAnalysis.LiveVariableLattice(10);
        liveVariablesAnalysis = new LiveVariablesAnalysis(null, null, null);
        liveVariableLattice.liveSet.set(0);
    }

    @Test
    public void testLiveVariableLatticeEquals() {
        LiveVariablesAnalysis.LiveVariableLattice lattice1 = new LiveVariablesAnalysis.LiveVariableLattice(10);
        LiveVariablesAnalysis.LiveVariableLattice lattice2 = new LiveVariablesAnalysis.LiveVariableLattice(10);
        assertEquals(lattice1, lattice2);
    }

    @Test
    public void testLiveVariableLatticeNotEquals() {
        LiveVariablesAnalysis.LiveVariableLattice lattice1 = new LiveVariablesAnalysis.LiveVariableLattice(10);
        LiveVariablesAnalysis.LiveVariableLattice lattice2 = new LiveVariablesAnalysis.LiveVariableLattice(5);
        assertNotEquals(lattice1, lattice2);
    }

    @Test
    public void testLiveVariableLatticeIsLive() {
        assertTrue(liveVariableLattice.isLive(0));
    }

    @Test
    public void testLiveVariableLatticeIsNotLive() {
        assertFalse(liveVariableLattice.isLive(1));
    }

    @Test
    public void testLiveVariableLatticeToString() {
        assertEquals("{0}", liveVariableLattice.toString());
    }

    @Test
    public void testLiveVariableLatticeHashCode() {
        LiveVariablesAnalysis.LiveVariableLattice lattice1 = new LiveVariablesAnalysis.LiveVariableLattice(10);
        LiveVariablesAnalysis.LiveVariableLattice lattice2 = new LiveVariablesAnalysis.LiveVariableLattice(10);
        assertEquals(lattice1.hashCode(), lattice2.hashCode());
    }

    @Test
    public void testGetEscapedLocals() {
        Set<Var> expectedSet = Sets.newHashSet();
        assertEquals(expectedSet, liveVariablesAnalysis.getEscapedLocals());
    }

    @Test
    public void testGetVarIndex() {
        assertEquals(0, liveVariablesAnalysis.getVarIndex("testVar"));
    }

    @Test
    public void testCreateEntryLattice() {
        LiveVariablesAnalysis.LiveVariableLattice expectedLattice = new LiveVariablesAnalysis.LiveVariableLattice(10);
        assertEquals(expectedLattice, liveVariablesAnalysis.createEntryLattice());
    }

    @Test
    public void testCreateInitialEstimateLattice() {
        LiveVariablesAnalysis.LiveVariableLattice expectedLattice = new LiveVariablesAnalysis.LiveVariableLattice(10);
        assertEquals(expectedLattice, liveVariablesAnalysis.createInitialEstimateLattice());
    }

    @Test
    public void testIsForward() {
        assertFalse(liveVariablesAnalysis.isForward());
    }

    @Test
    public void testFlowThrough() {
        LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(10);
        input.liveSet.set(1);
        LiveVariablesAnalysis.LiveVariableLattice expectedLattice = new LiveVariablesAnalysis.LiveVariableLattice(10);
        expectedLattice.liveSet.set(0);
        expectedLattice.liveSet.set(1);
        expectedLattice.liveSet.set(2);
        assertEquals(expectedLattice, liveVariablesAnalysis.flowThrough(new Node(1), input));
    }

    @Test
    public void testComputeGenKill() {
        Node node = new Node(1);
        BitSet gen = new BitSet();
        BitSet kill = new BitSet();
        boolean conditional = false;
        liveVariablesAnalysis.computeGenKill(node, gen, kill, conditional);
        assertEquals(1, gen.cardinality());
        assertEquals(0, kill.cardinality());
    }

    @Test
    public void testMarkAllParametersEscaped() {
        liveVariablesAnalysis.markAllParametersEscaped();
        Set<Var> expectedSet = Sets.newHashSet();
        assertEquals(expectedSet, liveVariablesAnalysis.getEscapedLocals());
    }
}