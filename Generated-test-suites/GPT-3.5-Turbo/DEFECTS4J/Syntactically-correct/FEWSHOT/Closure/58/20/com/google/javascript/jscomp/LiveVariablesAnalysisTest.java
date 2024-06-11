package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Sets;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class LiveVariablesAnalysisTest {

    private ControlFlowGraph<Node> cfg;
    private Scope jsScope;
    private LiveVariablesAnalysis analysis;

    @Before
    public void setUp() {
        cfg = new ControlFlowGraph<>();
        jsScope = new Scope(null, null);
        analysis = new LiveVariablesAnalysis(cfg, jsScope, null);
    }

    @Test
    public void testCreateEntryLattice() {
        LiveVariablesAnalysis.LiveVariableLattice result = analysis.createEntryLattice();
        assertEquals(0, result.liveSet.size());
    }

    @Test
    public void testCreateInitialEstimateLattice() {
        LiveVariablesAnalysis.LiveVariableLattice result = analysis.createInitialEstimateLattice();
        assertEquals(0, result.liveSet.size());
    }

    @Test
    public void testFlowThrough() {
        Node node = new Node(Token.VAR);
        LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(1);
        input.liveSet.set(0);
        LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(node, input);
        assertTrue(result.liveSet.get(0));
    }

    @Test
    public void testIsForward() {
        assertTrue(!analysis.isForward());
    }

    @Test
    public void testGetVarIndex() {
        jsScope.declare("var1", null, null, null);
        assertEquals(0, analysis.getVarIndex("var1"));
    }

    @Test
    public void testGetEscapedLocals() {
        Var var1 = jsScope.declare("var1", null, null, null);
        analysis.getEscapedLocals().add(var1);
        assertTrue(analysis.getEscapedLocals().contains(var1));
    }

    @Test
    public void testComputeGenKill() {
        Node n = new Node(Token.VAR);
        BitSet gen = new BitSet(1);
        BitSet kill = new BitSet(1);
        boolean conditional = false;
        analysis.computeGenKill(n, gen, kill, conditional);
        assertTrue(!gen.get(0));
        assertTrue(!kill.get(0));
    }

    @Test
    public void testAddToSetIfLocal() {
        Node node = new Node(Token.NAME, "var1");
        jsScope.declare("var1", null, null, null);
        BitSet set = new BitSet(1);
        analysis.addToSetIfLocal(node, set);
        assertTrue(set.get(0));
    }

    @Test
    public void testMarkAllParametersEscaped() {
        Node lp = new Node(Token.LP);
        Node arg1 = new Node(Token.NAME, "arg1");
        Node arg2 = new Node(Token.NAME, "arg2");
        lp.addChildToBack(arg1);
        lp.addChildToBack(arg2);
        jsScope.getRootNode().addChildToBack(lp);
        analysis.markAllParametersEscaped();
        assertTrue(analysis.getEscapedLocals().contains(jsScope.getVar("arg1")));
        assertTrue(analysis.getEscapedLocals().contains(jsScope.getVar("arg2")));
    }

    @Test
    public void testIsArgumentsName() {
        Node n1 = new Node(Token.NAME, "arguments");
        assertTrue(analysis.isArgumentsName(n1));
        Node n2 = new Node(Token.NAME, "arguments");
        jsScope.declare("arguments", null, null, null);
        assertTrue(!analysis.isArgumentsName(n2));
        Node n3 = new Node(Token.NAME, "args");
        assertTrue(!analysis.isArgumentsName(n3));
    }
}