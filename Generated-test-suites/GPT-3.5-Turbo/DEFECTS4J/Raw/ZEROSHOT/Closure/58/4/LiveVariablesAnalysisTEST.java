import org.junit.*;
import static org.junit.Assert.*;

public class LiveVariablesAnalysisTest {
    
    @Test
    public void testConstructor() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        Scope jsScope = new Scope();
        AbstractCompiler compiler = new AbstractCompiler();
        LiveVariablesAnalysis lva = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        assertNotNull(lva);
    }
    
    @Test
    public void testGetEscapedLocals() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        Scope jsScope = new Scope();
        AbstractCompiler compiler = new AbstractCompiler();
        LiveVariablesAnalysis lva = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        assertNotNull(lva.getEscapedLocals());
    }
    
    @Test
    public void testGetVarIndex() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        Scope jsScope = new Scope();
        AbstractCompiler compiler = new AbstractCompiler();
        LiveVariablesAnalysis lva = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        
        jsScope.addVar(new Var(1, "var1"));
        jsScope.addVar(new Var(2, "var2"));
        
        assertEquals(1, lva.getVarIndex("var1"));
        assertEquals(2, lva.getVarIndex("var2"));
    }
    
    @Test
    public void testIsForward() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        Scope jsScope = new Scope();
        AbstractCompiler compiler = new AbstractCompiler();
        LiveVariablesAnalysis lva = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        
        assertFalse(lva.isForward());
    }
    
    @Test
    public void testCreateEntryLattice() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        Scope jsScope = new Scope();
        AbstractCompiler compiler = new AbstractCompiler();
        LiveVariablesAnalysis lva = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        
        LiveVariablesAnalysis.LiveVariableLattice lattice = lva.createEntryLattice();
        assertNotNull(lattice);
        assertEquals(0, lattice.liveSet.cardinality());
    }
    
    @Test
    public void testCreateInitialEstimateLattice() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        Scope jsScope = new Scope();
        AbstractCompiler compiler = new AbstractCompiler();
        LiveVariablesAnalysis lva = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        
        LiveVariablesAnalysis.LiveVariableLattice lattice = lva.createInitialEstimateLattice();
        assertNotNull(lattice);
        assertEquals(0, lattice.liveSet.cardinality());
    }
    
    @Test
    public void testFlowThrough() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        Scope jsScope = new Scope();
        AbstractCompiler compiler = new AbstractCompiler();
        LiveVariablesAnalysis lva = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        
        Node node = new Node(Token.SCRIPT);
        LiveVariablesAnalysis.LiveVariableLattice input = lva.createInitialEstimateLattice();
        LiveVariablesAnalysis.LiveVariableLattice result = lva.flowThrough(node, input);
        assertNotNull(result);
    }
    
    @Test
    public void testComputeGenKill() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        Scope jsScope = new Scope();
        AbstractCompiler compiler = new AbstractCompiler();
        LiveVariablesAnalysis lva = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        
        Node n = new Node(Token.VAR);
        Node c = new Node(Token.NAME, "var1");
        n.addChildToBack(c);
        
        BitSet gen = new BitSet();
        BitSet kill = new BitSet();
        boolean conditional = false;
        
        lva.computeGenKill(n, gen, kill, conditional);
        assertTrue(gen.get(1));
        assertFalse(kill.get(1));
    }
    
    @Test
    public void testAddToSetIfLocal() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        Scope jsScope = new Scope();
        AbstractCompiler compiler = new AbstractCompiler();
        LiveVariablesAnalysis lva = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        
        Node node = new Node(Token.NAME, "var1");
        jsScope.addVar(new Var(1, "var1"));
        
        BitSet set = new BitSet();
        lva.addToSetIfLocal(node, set);
        
        assertTrue(set.get(1));
    }
    
    @Test
    public void testMarkAllParametersEscaped() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        Scope jsScope = new Scope();
        AbstractCompiler compiler = new AbstractCompiler();
        LiveVariablesAnalysis lva = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        
        lva.markAllParametersEscaped();
        assertFalse(lva.getEscapedLocals().isEmpty());
    }
    
    @Test
    public void testIsArgumentsName() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        Scope jsScope = new Scope();
        AbstractCompiler compiler = new AbstractCompiler();
        LiveVariablesAnalysis lva = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        
        Node n1 = new Node(Token.NAME, "arguments");
        Node n2 = new Node(Token.NAME, "var1");
        
        assertTrue(lva.isArgumentsName(n1));
        assertFalse(lva.isArgumentsName(n2));
    }
    
}