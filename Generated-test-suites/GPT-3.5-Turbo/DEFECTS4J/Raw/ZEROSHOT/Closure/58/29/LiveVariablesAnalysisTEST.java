import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LiveVariablesAnalysisTest {

    private LiveVariablesAnalysis.LiveVariableLattice lattice;

    @Before
    public void setUp() {
        lattice = new LiveVariablesAnalysis.LiveVariableLattice(10);
    }

    @Test
    public void testIsLive() {
        assertEquals(false, lattice.isLive(0));
    }

    @Test
    public void testToString() {
        assertEquals("{}", lattice.toString());
    }

    @Test
    public void testHashCode() {
        assertEquals(0, lattice.hashCode());
    }

    @Test
    public void testFlowThrough() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, null, null);
        LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(10);

        LiveVariablesAnalysis.LiveVariableLattice result = liveVariablesAnalysis.flowThrough(new Node(Token.SCRIPT), input);

        assertEquals(input, result);
    }

    @Test
    public void testIsForward() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, null, null);

        boolean result = liveVariablesAnalysis.isForward();

        assertEquals(false, result);
    }

    @Test
    public void testCreateEntryLattice() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, null, null);

        LiveVariablesAnalysis.LiveVariableLattice result = liveVariablesAnalysis.createEntryLattice();

        assertEquals(10, result.liveSet.size());
        assertEquals(false, result.liveSet.get(0));
    }

    @Test
    public void testCreateInitialEstimateLattice() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, null, null);

        LiveVariablesAnalysis.LiveVariableLattice result = liveVariablesAnalysis.createInitialEstimateLattice();

        assertEquals(10, result.liveSet.size());
        assertEquals(false, result.liveSet.get(0));
    }

    @Test
    public void testComputeGenKill() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, null, null);

        liveVariablesAnalysis.computeGenKill(new Node(Token.SCRIPT), new BitSet(), new BitSet(), false);

        assertEquals(0, lattice.liveSet.size());
    }

    @Test
    public void testGetEscapedLocals() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, null, null);

        assertEquals(null, liveVariablesAnalysis.getEscapedLocals());
    }

    @Test
    public void testGetVarIndex() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, null, null);

        int result = liveVariablesAnalysis.getVarIndex("var");

        assertEquals(0, result);
    }

    @Test
    public void testMarkAllParametersEscaped() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, null, null);

        liveVariablesAnalysis.markAllParametersEscaped();
    }

    @Test
    public void testIsArgumentsName() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, null, null);

        boolean result = liveVariablesAnalysis.isArgumentsName(new Node(Token.NAME));

        assertEquals(false, result);
    }
}