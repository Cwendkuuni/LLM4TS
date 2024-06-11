package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.BitSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class LiveVariablesAnalysisTest {
    private ControlFlowAnalysis<Node, LiveVariablesAnalysis.LiveVariableLattice> analysis;
    private static final String ARGUMENT_ARRAY_ALIAS = "arguments";

    @Before
    public void setUp() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        Scope jsScope = new Scope();
        AbstractCompiler compiler = new AbstractCompiler();
        analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    }

    @Test
    public void testGetEscapedLocals() {
        Set<Var> escaped = analysis.getEscapedLocals();
        assertTrue(escaped.isEmpty());
    }

    @Test
    public void testGetVarIndex() {
        int index = analysis.getVarIndex("var");
        assertEquals(-1, index);
    }

    @Test
    public void testIsForward() {
        boolean isForward = analysis.isForward();
        assertFalse(isForward);
    }

    @Test
    public void testCreateEntryLattice() {
        LiveVariablesAnalysis.LiveVariableLattice entryLattice = analysis.createEntryLattice();
        BitSet liveSet = entryLattice.getLiveSet();
        assertEquals(0, liveSet.cardinality());
    }

    @Test
    public void testCreateInitialEstimateLattice() {
        LiveVariablesAnalysis.LiveVariableLattice initialEstimateLattice = analysis.createInitialEstimateLattice();
        BitSet liveSet = initialEstimateLattice.getLiveSet();
        assertEquals(0, liveSet.cardinality());
    }

    @Test
    public void testFlowThrough() {
        Node node = new Node(Token.STRING);
        LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice();
        LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(node, input);
        BitSet liveSet = result.getLiveSet();
        assertEquals(0, liveSet.cardinality());
    }

    @Test
    public void testComputeGenKill() {
        Node node = new Node(Token.STRING);
        BitSet gen = new BitSet();
        BitSet kill = new BitSet();
        boolean conditional = false;
        analysis.computeGenKill(node, gen, kill, conditional);
        assertEquals(0, gen.cardinality());
        assertEquals(0, kill.cardinality());
    }

    @Test
    public void testAddToSetIfLocal() {
        String name = "var";
        Node node = new Node(Token.NAME);
        node.setString(name);
        BitSet set = new BitSet();
        analysis.addToSetIfLocal(node, set);
        assertEquals(0, set.cardinality());
    }

    @Test
    public void testMarkAllParametersEscaped() {
        analysis.markAllParametersEscaped();
        Set<Var> escaped = analysis.getEscapedLocals();
        assertEquals(0, escaped.size());
    }

    @Test
    public void testIsArgumentsName() {
        Node node = new Node(Token.NAME);
        node.setString(ARGUMENT_ARRAY_ALIAS);
        boolean isArguments = analysis.isArgumentsName(node);
        assertTrue(isArguments);
    }
}