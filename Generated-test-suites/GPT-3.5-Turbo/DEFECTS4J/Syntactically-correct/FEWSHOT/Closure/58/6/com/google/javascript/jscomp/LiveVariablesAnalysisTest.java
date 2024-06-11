package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class LiveVariablesAnalysisTest {
    private ControlFlowGraph<Node> cfg;
    private Scope jsScope;
    private AbstractCompiler compiler;
    private LiveVariablesAnalysis analysis;

    @Before
    public void setUp() {
        this.jsScope = new Scope(null);
        this.cfg = new ControlFlowGraph<>(null);
        this.compiler = new AbstractCompiler();
        this.analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    }

    @Test
    public void testGetEscapedLocals() {
        assertTrue(analysis.getEscapedLocals().isEmpty());
    }

    @Test
    public void testGetVarIndex() {
        assertEquals(0, analysis.getVarIndex("testVar"));
    }

    @Test
    public void testIsForward() {
        assertFalse(analysis.isForward());
    }

    @Test
    public void testCreateEntryLattice() {
        LiveVariablesAnalysis.LiveVariableLattice result = analysis.createEntryLattice();
        assertTrue(result.liveSet.isEmpty());
    }

    @Test
    public void testCreateInitialEstimateLattice() {
        LiveVariablesAnalysis.LiveVariableLattice result = analysis.createInitialEstimateLattice();
        assertTrue(result.liveSet.isEmpty());
    }

    @Test
    public void testFlowThrough() {
        // TODO: Add test cases for flowThrough method
    }

    @Test
    public void testMarkAllParametersEscaped() {
        analysis.markAllParametersEscaped();
        assertTrue(analysis.getEscapedLocals().contains(jsScope.getVar(ARGUMENT_ARRAY_ALIAS)));
    }

    @Test
    public void testIsArgumentsName() {
        Node argumentsNode = new Node(Token.NAME, ARGUMENT_ARRAY_ALIAS);
        assertTrue(analysis.isArgumentsName(argumentsNode));

        Node localVarNode = new Node(Token.NAME, "localVar");
        assertFalse(analysis.isArgumentsName(localVarNode));
    }
}