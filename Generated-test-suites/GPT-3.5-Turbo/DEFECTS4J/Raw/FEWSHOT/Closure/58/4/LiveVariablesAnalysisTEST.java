import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LiveVariablesAnalysisTest {
    @Test
    public void testGetEscapedLocals() {
        Scope jsScope = new Scope(null);
        AbstractCompiler compiler = new AbstractCompiler();
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>();
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        
        assertTrue(analysis.getEscapedLocals().isEmpty());
    }

    @Test
    public void testGetVarIndex() {
        Scope jsScope = new Scope(null);
        AbstractCompiler compiler = new AbstractCompiler();
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>();
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

        assertEquals(0, analysis.getVarIndex("var1"));
        assertEquals(1, analysis.getVarIndex("var2"));
        assertEquals(2, analysis.getVarIndex("var3"));
        assertEquals(3, analysis.getVarIndex("var4"));
    }

    @Test
    public void testIsForward() {
        Scope jsScope = new Scope(null);
        AbstractCompiler compiler = new AbstractCompiler();
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>();
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

        assertFalse(analysis.isForward());
    }

    @Test
    public void testCreateEntryLattice() {
        Scope jsScope = new Scope(null);
        AbstractCompiler compiler = new AbstractCompiler();
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>();
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        
        LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.createEntryLattice();
        assertNull(lattice.toString());
    }

    @Test
    public void testCreateInitialEstimateLattice() {
        Scope jsScope = new Scope(null);
        AbstractCompiler compiler = new AbstractCompiler();
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>();
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        
        LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.createInitialEstimateLattice();
        assertNull(lattice.toString());
    }

    @Test
    public void testFlowThrough() {
        Scope jsScope = new Scope(null);
        AbstractCompiler compiler = new AbstractCompiler();
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>();
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        
        Node node = new Node(Token.SCRIPT);
        LiveVariablesAnalysis.LiveVariableLattice lattice = new LiveVariablesAnalysis.LiveVariableLattice(4);
        
        LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(node, lattice);
        assertNull(result.toString());
    }

    @Test
    public void testComputeGenKill() {
        Scope jsScope = new Scope(null);
        AbstractCompiler compiler = new AbstractCompiler();
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>();
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        
        Node node = new Node(Token.SCRIPT);
        BitSet gen = new BitSet();
        BitSet kill = new BitSet();
        boolean conditional = false;
        
        analysis.computeGenKill(node, gen, kill, conditional);
        assertTrue(gen.isEmpty());
        assertTrue(kill.isEmpty());
    }

    @Test
    public void testAddToSetIfLocal() {
        Scope jsScope = new Scope(null);
        AbstractCompiler compiler = new AbstractCompiler();
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>();
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        
        Node node = new Node(Token.NAME, "var1");
        BitSet set = new BitSet();
        
        analysis.addToSetIfLocal(node, set);
        assertTrue(set.isEmpty());
    }

    @Test
    public void testMarkAllParametersEscaped() {
        Scope jsScope = new Scope(null);
        AbstractCompiler compiler = new AbstractCompiler();
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>();
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        
        analysis.markAllParametersEscaped();
        assertTrue(analysis.getEscapedLocals().isEmpty());
    }

    @Test
    public void testIsArgumentsName() {
        Scope jsScope = new Scope(null);
        AbstractCompiler compiler = new AbstractCompiler();
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>();
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        
        Node node1 = new Node(Token.NAME, "arguments");
        Node node2 = new Node(Token.NAME, "var");
        
        assertTrue(analysis.isArgumentsName(node1));
        assertFalse(analysis.isArgumentsName(node2));
    }
}