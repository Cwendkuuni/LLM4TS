package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LiveVariablesAnalysisTest {

    private static final String ARGUMENT_ARRAY_ALIAS = "arguments";

    @Test
    public void testCreateEntryLattice() {
        Scope jsScope = new Scope(LiveVariablesAnalysisTest.class.getName());
        jsScope.declare(ARGUMENT_ARRAY_ALIAS, null, null, null);
        LiveVariablesAnalysis.LiveVariableLattice lattice = new LiveVariablesAnalysis(jsScope).createEntryLattice();
        assertEquals(0, lattice.liveSet.cardinality());
    }

    @Test
    public void testCreateInitialEstimateLattice() {
        Scope jsScope = new Scope(LiveVariablesAnalysisTest.class.getName());
        jsScope.declare(ARGUMENT_ARRAY_ALIAS, null, null, null);
        LiveVariablesAnalysis.LiveVariableLattice lattice = new LiveVariablesAnalysis(jsScope).createInitialEstimateLattice();
        assertEquals(0, lattice.liveSet.cardinality());
    }

    @Test
    public void testFlowThrough() {
        Scope jsScope = new Scope(LiveVariablesAnalysisTest.class.getName());
        jsScope.declare(ARGUMENT_ARRAY_ALIAS, null, null, null);
        LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis(jsScope).createInitialEstimateLattice();
        Node node = new Node(Token.VAR);
        LiveVariablesAnalysis.LiveVariableLattice result = new LiveVariablesAnalysis(jsScope).flowThrough(node, input);
        assertEquals(0, result.liveSet.cardinality());
    }

    @Test
    public void testGetEscapedLocals() {
        Scope jsScope = new Scope(LiveVariablesAnalysisTest.class.getName());
        jsScope.declare(ARGUMENT_ARRAY_ALIAS, null, null, null);
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(jsScope);
        analysis.markAllParametersEscaped();
        assertEquals(0, analysis.getEscapedLocals().size());
    }

    @Test
    public void testGetVarIndex() {
        Scope jsScope = new Scope(LiveVariablesAnalysisTest.class.getName());
        jsScope.declare(ARGUMENT_ARRAY_ALIAS, null, null, null);
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(jsScope);
        assertEquals(0, analysis.getVarIndex(ARGUMENT_ARRAY_ALIAS));
    }

    @Test
    public void testIsForward() {
        Scope jsScope = new Scope(LiveVariablesAnalysisTest.class.getName());
        jsScope.declare(ARGUMENT_ARRAY_ALIAS, null, null, null);
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(jsScope);
        assertEquals(false, analysis.isForward());
    }
}