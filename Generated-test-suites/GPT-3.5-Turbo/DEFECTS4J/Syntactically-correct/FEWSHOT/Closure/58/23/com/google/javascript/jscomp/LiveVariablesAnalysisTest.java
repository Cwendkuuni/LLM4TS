package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class LiveVariablesAnalysisTest {

    private ControlFlowGraph<Node> cfg;
    private Scope jsScope;
    private LiveVariablesAnalysis analysis;

    @Before
    public void setUp() {
        cfg = new ControlFlowGraph<>(null);
        jsScope = new Scope(null);
        analysis = new LiveVariablesAnalysis(cfg, jsScope, null);
    }

    @Test
    public void testGetEscapedLocals() {
        assertTrue(analysis.getEscapedLocals().isEmpty());
    }

    @Test
    public void testGetVarIndex() {
        assertEquals(0, analysis.getVarIndex(""));
    }

    @Test
    public void testIsForward() {
        assertFalse(analysis.isForward());
    }

    @Test
    public void testCreateEntryLattice() {
        LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.createEntryLattice();
        assertEquals(0, lattice.toString().length());
    }

    @Test
    public void testCreateInitialEstimateLattice() {
        LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.createInitialEstimateLattice();
        assertEquals(0, lattice.toString().length());
    }

    @Test
    public void testFlowThrough() {
        Node node = new Node(Token.SCRIPT);
        LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(0);
        LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(node, input);
        assertEquals(input, result);
    }

    @Test
    public void testMarkAllParametersEscaped() {
        analysis.markAllParametersEscaped();
        assertTrue(analysis.getEscapedLocals().isEmpty());
    }

    @Test
    public void testIsArgumentsName() {
        Node node = new Node(Token.NAME, LiveVariablesAnalysis.ARGUMENT_ARRAY_ALIAS);
        assertTrue(analysis.isArgumentsName(node));
        node = new Node(Token.NAME, "test");
        assertFalse(analysis.isArgumentsName(node));
    }
}