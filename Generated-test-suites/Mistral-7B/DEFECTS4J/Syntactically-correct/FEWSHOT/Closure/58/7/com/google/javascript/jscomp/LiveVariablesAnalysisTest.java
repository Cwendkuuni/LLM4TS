package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;

import java.util.BitSet;
import java.util.List;

public class LiveVariablesAnalysisTest {

    @Test
    public void testLiveVariablesAnalysis() {
        Scope jsScope = new Scope();
        AbstractCompiler compiler = new AbstractCompiler(jsScope);
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(compiler);
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

        // Test empty CFG
        assertNull(analysis.getEntryLattice());

        // Test simple assignment statement with no local variables
        String code1 = "var x = 7;";
        Node node1 = compiler.compile(code1, null);
        LiveVariablesAnalysis analysis1 = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        LiveVariableLattice entryLattice1 = analysis1.getEntryLattice();
        LiveVariableLattice result1 = analysis1.flowThrough(node1, entryLattice1);
        BitSet liveSet1 = result1.liveSet;
        assertFalse(liveSet1.get(0)); // x is not live since it's not a local variable

        // Test simple assignment statement with local variables
        String code2 = "function test(x) { var y = x + 1; }";
        Node node2 = compiler.compile(code2, null);
        LiveVariablesAnalysis analysis2 = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        LiveVariableLattice entryLattice2 = analysis2.getEntryLattice();
        LiveVariableLattice result2 = analysis2.flowThrough(node2.getFirstChild(), entryLattice2);
        BitSet liveSet2 = result2.liveSet;
        assertTrue(liveSet2.get(0)); // x is live since it's an argument
        assertTrue(liveSet2.get(1)); // y is live since it's a local variable

        // Test conditional statement
        String code3 = "function test(x) { if (x > 0) { var y = x + 1; } }";
        Node node3 = compiler.compile(code3, null);
        LiveVariablesAnalysis analysis3 = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        LiveVariableLattice entryLattice3 = analysis3.getEntryLattice();
        LiveVariableLattice result3 = analysis3.flowThrough(node3.getFirstChild(), entryLattice3);
        BitSet liveSet3 = result3.liveSet;
        assertTrue(liveSet3.get(0)); // x is live since it's an argument
        assertTrue(liveSet3.get(1)); // y is live since it's a local variable when x > 0

        // Test loop statement
        String code4 = "function test(x) { for (var i = 0; i < x; i++) { } }";
        Node node4 = compiler.compile(code4, null);
        LiveVariablesAnalysis analysis4 = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        LiveVariableLattice entryLattice4 = analysis4.getEntryLattice();
        LiveVariableLattice result4 = analysis4.flowThrough(node4.getFirstChild(), entryLattice4);
        BitSet liveSet4 = result4.liveSet;
        assertTrue(liveSet4.get(0)); // x is live since it's an argument
        assertTrue(liveSet4.get(1)); // i is live since it's a local variable

        // Test function call with arguments
        String code5 = "function test(x, y) { var z = x + y; return z; }";
        Node node5 = compiler.compile(code5, null);
        LiveVariablesAnalysis analysis5 = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        LiveVariableLattice entryLattice5 = analysis5.getEntryLattice();
        LiveVariableLattice result5 = analysis5.flowThrough(node5, entryLattice5);
        BitSet liveSet5 = result5.liveSet;
        assertTrue(liveSet5.get(0)); // x is live since it's an argument
        assertTrue(liveSet5.get(1)); // y is live since it's an argument
        assertTrue(liveSet5.get(2)); // z is live since it's a local variable

        // Test escaped locals
        String code6 = "function test(x, y) { function innerFunction() { return x + y; } }";
        Node node6 = compiler.compile(code6, null);
        LiveVariablesAnalysis analysis6 = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        LiveVariableLattice entryLattice6 = analysis6.getEntryLattice();
        LiveVariableLattice result6 = analysis6.flowThrough(node6, entryLattice6);
        Set<Var> escapedLocals = analysis6.getEscapedLocals();
        assertTrue(escapedLocals.size() > 0);
        assertTrue(escapedLocals.contains(jsScope.getVar("x")));
        assertTrue(escapedLocals.contains(jsScope.getVar("y")));
    }
}