import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LiveVariablesAnalysisTest {
    
    private LiveVariablesAnalysis liveVariablesAnalysis;
    
    @Before
    public void setup() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        Scope jsScope = new Scope();
        AbstractCompiler compiler = new AbstractCompiler();
        liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    }
    
    @Test
    public void testGetEscapedLocals() {
        // Test case 1
        Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();
        assertNotNull(escapedLocals);
        
        // Test case 2
        assertEquals(0, escapedLocals.size());
    }
    
    @Test
    public void testGetVarIndex() {
        // Test case 1
        int varIndex = liveVariablesAnalysis.getVarIndex("var");
        assertEquals(0, varIndex);
        
        // Test case 2
        varIndex = liveVariablesAnalysis.getVarIndex("x");
        assertEquals(1, varIndex);
        
        // Test case 3
        varIndex = liveVariablesAnalysis.getVarIndex("y");
        assertEquals(2, varIndex);
    }
    
    @Test
    public void testIsForward() {
        // Test case 1
        boolean isForward = liveVariablesAnalysis.isForward();
        assertFalse(isForward);
    }
    
    @Test
    public void testCreateEntryLattice() {
        // Test case 1
        LiveVariablesAnalysis.LiveVariableLattice entryLattice = liveVariablesAnalysis.createEntryLattice();
        assertNotNull(entryLattice);
        
        // Test case 2
        assertEquals(3, entryLattice.liveSet.size());
    }
    
    @Test
    public void testCreateInitialEstimateLattice() {
        // Test case 1
        LiveVariablesAnalysis.LiveVariableLattice initialEstimateLattice = liveVariablesAnalysis.createInitialEstimateLattice();
        assertNotNull(initialEstimateLattice);
        
        // Test case 2
        assertEquals(3, initialEstimateLattice.liveSet.size());
    }
    
    @Test
    public void testFlowThrough() {
        // Test case 1
        Node node = new Node(Token.SCRIPT);
        LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(3);
        LiveVariablesAnalysis.LiveVariableLattice output = liveVariablesAnalysis.flowThrough(node, input);
        assertNotNull(output);
        
        // Test case 2
        node = new Node(Token.NAME, "var");
        input = new LiveVariablesAnalysis.LiveVariableLattice(3);
        output = liveVariablesAnalysis.flowThrough(node, input);
        assertNotNull(output);
        
        // Test case 3
        node = new Node(Token.NAME, "x");
        input = new LiveVariablesAnalysis.LiveVariableLattice(3);
        output = liveVariablesAnalysis.flowThrough(node, input);
        assertNotNull(output);
        
        // Test case 4
        node = new Node(Token.NAME, "y");
        input = new LiveVariablesAnalysis.LiveVariableLattice(3);
        output = liveVariablesAnalysis.flowThrough(node, input);
        assertNotNull(output);
    }
    
    @Test
    public void testComputeGenKill() {
        // Test case 1
        Node node = new Node(Token.VAR);
        BitSet gen = new BitSet();
        BitSet kill = new BitSet();
        boolean conditional = false;
        liveVariablesAnalysis.computeGenKill(node, gen, kill, conditional);
        assertNotNull(gen);
        assertNotNull(kill);
        
        // Test case 2
        node = new Node(Token.NAME, "var");
        gen = new BitSet();
        kill = new BitSet();
        conditional = false;
        liveVariablesAnalysis.computeGenKill(node, gen, kill, conditional);
        assertNotNull(gen);
        assertNotNull(kill);
        
        // Test case 3
        node = new Node(Token.NAME, "x");
        gen = new BitSet();
        kill = new BitSet();
        conditional = false;
        liveVariablesAnalysis.computeGenKill(node, gen, kill, conditional);
        assertNotNull(gen);
        assertNotNull(kill);
        
        // Test case 4
        node = new Node(Token.NAME, "y");
        gen = new BitSet();
        kill = new BitSet();
        conditional = false;
        liveVariablesAnalysis.computeGenKill(node, gen, kill, conditional);
        assertNotNull(gen);
        assertNotNull(kill);
    }
    
    @Test
    public void testAddToSetIfLocal() {
        // Test case 1
        Node node = new Node(Token.NAME, "var");
        BitSet set = new BitSet();
        liveVariablesAnalysis.addToSetIfLocal(node, set);
        assertNotNull(set);
        
        // Test case 2
        node = new Node(Token.NAME, "x");
        set = new BitSet();
        liveVariablesAnalysis.addToSetIfLocal(node, set);
        assertNotNull(set);
        
        // Test case 3
        node = new Node(Token.NAME, "y");
        set = new BitSet();
        liveVariablesAnalysis.addToSetIfLocal(node, set);
        assertNotNull(set);
    }
    
    @Test
    public void testMarkAllParametersEscaped() {
        // Test case 1
        liveVariablesAnalysis.markAllParametersEscaped();
        assertNotNull(liveVariablesAnalysis.getEscapedLocals());
        
        // Test case 2
        assertEquals(3, liveVariablesAnalysis.getEscapedLocals().size());
    }
    
    @Test
    public void testIsArgumentsName() {
        // Test case 1
        Node node = new Node(Token.NAME, "var");
        boolean isArgumentsName = liveVariablesAnalysis.isArgumentsName(node);
        assertFalse(isArgumentsName);
        
        // Test case 2
        node = new Node(Token.NAME, "arguments");
        isArgumentsName = liveVariablesAnalysis.isArgumentsName(node);
        assertTrue(isArgumentsName);
    }
}