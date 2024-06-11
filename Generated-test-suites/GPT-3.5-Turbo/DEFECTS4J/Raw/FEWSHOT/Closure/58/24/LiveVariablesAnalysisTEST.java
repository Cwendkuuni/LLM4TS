import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

import java.util.BitSet;
import java.util.List;
import java.util.Set;

public class LiveVariablesAnalysisTest {
    LiveVariablesAnalysis.LiveVariableLattice initialLattice;

    @Before
    public void setUp() {
        initialLattice = new LiveVariablesAnalysis.LiveVariableLattice(10);
    }

    @Test
    public void testLiveVariableLatticeEquals() {
        LiveVariablesAnalysis.LiveVariableLattice lattice1 = new LiveVariablesAnalysis.LiveVariableLattice(10);
        LiveVariablesAnalysis.LiveVariableLattice lattice2 = new LiveVariablesAnalysis.LiveVariableLattice(10);
        assertEquals(lattice1, lattice2);
    }

    @Test
    public void testLiveVariableLatticeHashCode() {
        LiveVariablesAnalysis.LiveVariableLattice lattice1 = new LiveVariablesAnalysis.LiveVariableLattice(10);
        LiveVariablesAnalysis.LiveVariableLattice lattice2 = new LiveVariablesAnalysis.LiveVariableLattice(10);
        assertEquals(lattice1.hashCode(), lattice2.hashCode());
    }

    @Test
    public void testLiveVariableLatticeToString() {
        LiveVariablesAnalysis.LiveVariableLattice lattice = new LiveVariablesAnalysis.LiveVariableLattice(10);
        assertEquals("{}", lattice.toString());
    }

    @Test
    public void testGetEscapedLocals() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(new Node(Token.SCRIPT));
        Scope jsScope = new Scope(new Node(Token.SCRIPT), null);
        AbstractCompiler compiler = new Compiler();
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        Set<Var> escapedLocals = analysis.getEscapedLocals();
        assertTrue(escapedLocals.isEmpty());
    }

    @Test
    public void testGetVarIndex() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(new Node(Token.SCRIPT));
        Scope jsScope = new Scope(new Node(Token.SCRIPT), null);
        AbstractCompiler compiler = new Compiler();
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        int varIndex = analysis.getVarIndex("x");
        assertEquals(-1, varIndex);
    }

    @Test
    public void testIsForward() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(new Node(Token.SCRIPT));
        Scope jsScope = new Scope(new Node(Token.SCRIPT), null);
        AbstractCompiler compiler = new Compiler();
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        assertFalse(analysis.isForward());
    }

    @Test
    public void testCreateEntryLattice() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(new Node(Token.SCRIPT));
        Scope jsScope = new Scope(new Node(Token.SCRIPT), null);
        AbstractCompiler compiler = new Compiler();
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.createEntryLattice();
        assertEquals(initialLattice, lattice);
    }

    @Test
    public void testCreateInitialEstimateLattice() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(new Node(Token.SCRIPT));
        Scope jsScope = new Scope(new Node(Token.SCRIPT), null);
        AbstractCompiler compiler = new Compiler();
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.createInitialEstimateLattice();
        assertEquals(initialLattice, lattice);
    }

    @Test
    public void testFlowThrough() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(new Node(Token.SCRIPT));
        Node node = new Node(Token.SCRIPT);
        LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(10);
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, null, null);
        LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(node, input);
        assertEquals(initialLattice, result);
    }

    @Test
    public void testComputeGenKill() {
        BitSet gen = new BitSet(initialLattice.liveSet.size());
        BitSet kill = new BitSet(initialLattice.liveSet.size());
        boolean conditional = false;
        Node node = new Node(Token.SCRIPT);
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        analysis.computeGenKill(node, gen, kill, conditional);
        assertEquals(new BitSet(), gen);
        assertEquals(new BitSet(), kill);
    }

    @Test
    public void testAddToSetIfLocal() {
        BitSet set = new BitSet(initialLattice.liveSet.size());
        Node node = new Node(Token.SCRIPT, new Node(Token.NAME, new Node(Token.STRING, "x")));
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        analysis.addToSetIfLocal(node, set);
        assertEquals(initialLattice.liveSet, set);
    }

    @Test
    public void testMarkAllParametersEscaped() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        analysis.markAllParametersEscaped();
        assertEquals(Sets.newHashSet(), analysis.getEscapedLocals());
    }

    @Test
    public void testIsArgumentsName() {
        Node node = new Node(Token.NAME, new Node(Token.STRING, "arguments"));
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        assertTrue(analysis.isArgumentsName(node));
    }
}