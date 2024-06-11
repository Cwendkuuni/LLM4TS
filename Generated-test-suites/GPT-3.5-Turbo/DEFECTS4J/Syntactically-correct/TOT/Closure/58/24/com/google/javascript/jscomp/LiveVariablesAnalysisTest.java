import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class LiveVariablesAnalysisTest {

    private LiveVariablesAnalysis lva;
    private ControlFlowGraph<Node> cfg;
    private Scope jsScope;
    private AbstractCompiler compiler;

    @Before
    public void setUp() {
        cfg = new ControlFlowGraph<>();
        jsScope = new Scope();
        compiler = new AbstractCompiler();
        lva = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    }

    @Test
    public void testCreateEntryLattice() {
        LiveVariablesAnalysis.LiveVariableLattice entryLattice = lva.createEntryLattice();
        assertNotNull(entryLattice);
        assertEquals(jsScope.getVarCount(), entryLattice.liveSet.size());

        for (int i = 0; i < jsScope.getVarCount(); i++) {
            assertFalse(entryLattice.isLive(i));
        }
    }

    @Test
    public void testCreateInitialEstimateLattice() {
        LiveVariablesAnalysis.LiveVariableLattice initialEstimateLattice = lva.createInitialEstimateLattice();
        assertNotNull(initialEstimateLattice);
        assertEquals(jsScope.getVarCount(), initialEstimateLattice.liveSet.size());

        for (int i = 0; i < jsScope.getVarCount(); i++) {
            assertFalse(initialEstimateLattice.isLive(i));
        }
    }

    @Test
    public void testFlowThrough_NodeNotConditional() {
        LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(jsScope.getVarCount());
        input.liveSet.set(0, jsScope.getVarCount());
        Node node = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.STRING, "Hello"));
        LiveVariablesAnalysis.LiveVariableLattice result = lva.flowThrough(node, input);

        assertEquals(input.liveSet.size(), result.liveSet.size());

        assertTrue(result.isLive(0)); // variable x should still be live
        assertFalse(result.isLive(1)); // variable y should not be live
    }

    @Test
    public void testFlowThrough_NodeConditional() {
        LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(jsScope.getVarCount());
        input.liveSet.set(0, jsScope.getVarCount());
        Node node = new Node(Token.IF, new Node(Token.NAME, "flag"), new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.STRING, "Hello")));
        LiveVariablesAnalysis.LiveVariableLattice result = lva.flowThrough(node, input);

        assertEquals(input.liveSet.size(), result.liveSet.size());

        assertTrue(result.isLive(0)); // variable x should still be live
        assertTrue(result.isLive(1)); // variable y should still be live
    }

    @Test
    public void testComputeGenKill() {
        BitSet gen = new BitSet(jsScope.getVarCount());
        BitSet kill = new BitSet(jsScope.getVarCount());
        boolean conditional = false;

        Node node = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.STRING, "Hello"));
        lva.computeGenKill(node, gen, kill, conditional);

        assertFalse(kill.get(0)); // variable x should not be killed
        assertTrue(gen.get(0)); // variable x should be generated
        assertFalse(kill.get(1)); // variable y should not be killed
        assertFalse(gen.get(1)); // variable y should not be generated
    }

    @Test
    public void testGetEscapedLocals() {
        Set<Var> escapedLocals = lva.getEscapedLocals();
        assertNotNull(escapedLocals);
        assertTrue(escapedLocals.isEmpty());
    }

    @Test
    public void testGetVarIndex() {
        int varIndex = lva.getVarIndex("x");
        assertEquals(0, varIndex);
    }

    @Test
    public void testIsForward() {
        assertFalse(lva.isForward());
    }
}