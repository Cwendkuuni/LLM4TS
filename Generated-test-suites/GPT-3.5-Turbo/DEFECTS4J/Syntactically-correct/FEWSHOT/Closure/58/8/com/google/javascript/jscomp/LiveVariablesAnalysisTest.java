package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class LiveVariablesAnalysisTest {
    ControlFlowGraph<Node> cfg;
    Scope jsScope;
    AbstractCompiler compiler;
    LiveVariablesAnalysis analysis;

    @Before
    public void setUp() {
        cfg = new ControlFlowGraph<>();
        jsScope = new Scope(new Node(Token.ROOT));
        compiler = new AbstractCompiler();
        analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    }

    @Test
    public void testGetEscapedLocals() {
        assertEquals(0, analysis.getEscapedLocals().size());
    }

    @Test
    public void testGetVarIndex() {
        assertEquals(0, analysis.getVarIndex("var"));
    }

    @Test
    public void testIsForward() {
        assertFalse(analysis.isForward());
    }

    @Test
    public void testCreateEntryLattice() {
        LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.createEntryLattice();
        assertEquals(0, lattice.liveSet.size());
    }

    @Test
    public void testCreateInitialEstimateLattice() {
        LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.createInitialEstimateLattice();
        assertEquals(0, lattice.liveSet.size());
    }

    @Test
    public void testFlowThrough() {
        Node node = new Node(Token.AND);
        LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(10);
        LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(node, input);
        assertEquals(input.liveSet, result.liveSet);
    }

    @Test
    public void testComputeGenKill() {
        Node n = new Node(Token.NAME, "x");
        BitSet gen = new BitSet(10);
        BitSet kill = new BitSet(10);
        boolean conditional = false;
        analysis.computeGenKill(n, gen, kill, conditional);
        assertTrue(gen.isEmpty());
        assertTrue(kill.isEmpty());
    }

    @Test
    public void testAddToSetIfLocal() {
        Node node = new Node(Token.NAME, "x");
        BitSet set = new BitSet(10);
        analysis.addToSetIfLocal(node, set);
        assertTrue(set.isEmpty());
    }

    @Test
    public void testMarkAllParametersEscaped() {
        analysis.markAllParametersEscaped();
        assertEquals(0, analysis.getEscapedLocals().size());
    }

    @Test
    public void testIsArgumentsName() {
        Node n = new Node(Token.NAME, "arguments");
        assertTrue(analysis.isArgumentsName(n));
    }
}