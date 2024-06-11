import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.BitSet;
import java.util.List;
import java.util.Set;

public class LiveVariablesAnalysisTest {
    
    private LiveVariablesAnalysis analysis;
    private ControlFlowGraph<Node> cfg;
    private Scope jsScope;
    private AbstractCompiler compiler;
    
    @Before
    public void setUp() {
        jsScope = new Scope();
        cfg = new ControlFlowGraph<Node>();
        compiler = new AbstractCompiler();
        analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    }

    @Test
    public void testGetEscapedLocals() {
        Set<Var> escaped = analysis.getEscapedLocals();
        assertNotNull(escaped);
    }
    
    @Test
    public void testGetVarIndex() {
        String var = "testVar";
        int index = analysis.getVarIndex(var);
        assertEquals(-1, index);
    }
    
    @Test
    public void testIsForward() {
        boolean forward = analysis.isForward();
        assertFalse(forward);
    }
    
    @Test
    public void testCreateEntryLattice() {
        LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.createEntryLattice();
        BitSet liveSet = lattice.liveSet;
        assertNotNull(liveSet);
    }
    
    @Test
    public void testCreateInitialEstimateLattice() {
        LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.createInitialEstimateLattice();
        BitSet liveSet = lattice.liveSet;
        assertNotNull(liveSet);
    }
    
    @Test
    public void testFlowThrough() {
        Node node = new Node(Token.SCRIPT);
        LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(1);
        LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(node, input);
        BitSet liveSet = result.liveSet;
        assertNotNull(liveSet);
    }
    
    @Test
    public void testComputeGenKill() {
        Node node = new Node(Token.VAR);
        BitSet gen = new BitSet();
        BitSet kill = new BitSet();
        boolean conditional = false;
        analysis.computeGenKill(node, gen, kill, conditional);
        // TODO: Check gen and kill values
    }
    
    @Test
    public void testAddToSetIfLocal() {
        Node node = new Node(Token.NAME, "variable");
        BitSet set = new BitSet();
        analysis.addToSetIfLocal(node, set);
        // TODO: Check value of set
    }
    
    @Test
    public void testMarkAllParametersEscaped() {
        analysis.markAllParametersEscaped();
        // TODO: Check escaped parameters
    }
    
    @Test
    public void testIsArgumentsName() {
        Node node = new Node(Token.NAME, "arguments");
        boolean isArguments = analysis.isArgumentsName(node);
        assertTrue(isArguments);
    }
}