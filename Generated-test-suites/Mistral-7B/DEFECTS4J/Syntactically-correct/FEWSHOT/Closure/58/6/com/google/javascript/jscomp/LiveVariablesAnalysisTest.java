package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNotNull;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import org.junit.Test;

import java.util.BitSet;
import java.util.List;
import java.util.Set;

public class LiveVariablesAnalysisTest {

    @Test
    public void testLiveVariablesAnalysis() {
        // Sample test case for a simple function with local variables.
        String code = "function test() {" +
                "  var x = 1;" +
                "  var y = 2;" +
                "  var z = x + y;"
                + "}";

        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(NodeUtil.parse(code));
        Scope jsScope = new Scope();
        AbstractCompiler compiler = new Compiler();

        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();

        // Check that no variables are escaped by default.
        assertThat(escapedLocals).isEmpty();

        // Perform the analysis.
        LiveVariablesAnalysis result = liveVariablesAnalysis.compute();

        // Check that all local variables are live at the end of the function.
        for (Var var : jsScope.getVariables()) {
            int index = var.index;
            assertThat(result.lattice.isLive(index)).isTrue();
        }

        // Check that no global variables are live at the end of the function.
        List<Node> globalVariables = jsScope.getGlobalVariables();
        for (Node globalVariable : globalVariables) {
            int index = jsScope.getVar(globalVariable.getString()).index;
            assertThat(result.lattice.isLive(index)).isFalse();
        }

        // Check that local variables that are not assigned are not killed.
        Node xAssignment = NodeUtil.findAssignment(NodeUtil.findChildByName(cfg.getStartNode(), "x"), cfg);
        Node yAssignment = NodeUtil.findAssignment(NodeUtil.findChildByName(cfg.getStartNode(), "y"), cfg);
        Node zAssignment = NodeUtil.findAssignment(NodeUtil.findChildByName(cfg.getStartNode(), "z"), cfg);

        BitSet xGen = new BitSet(result.lattice.size());
        BitSet xKill = new BitSet(result.lattice.size());
       BitSet yGen = new BitSet(result.lattice.size());
        BitSet yKill = new BitSet(result.lattice.size());
        BitSet zGen = new BitSet(result.lattice.size());
        BitSet zKill = new BitSet(result.lattice.size());

        liveVariablesAnalysis.computeGenKill(xAssignment, xGen, xKill, false);
        liveVariablesAnalysis.computeGenKill(yAssignment, yGen, yKill, false);
        liveVariablesAnalysis.computeGenKill(zAssignment, zGen, zKill, false);

        // Check that x is live at the assignment and the end of the function.
        assertThat(xGen.get(xAssignment.getFirstChild().getIndex())).isTrue();
        assertThat(xKill.get(xAssignment.getFirstChild().getIndex())).isFalse();
        assertThat(result.lattice.isLive(xAssignment.getFirstChild().getIndex())).isTrue();

        // Check that y is live at the assignment and the end of the function.
        assertThat(yGen.get(yAssignment.getFirstChild().getIndex())).isTrue();
        assertThat(yKill.get(yAssignment.getFirstChild().getIndex())).isFalse();
        assertThat(result.lattice.isLive(yAssignment.getFirstChild().getIndex())).isTrue();

        // Check that z is live at the assignment and the end of the function.
        assertThat(zGen.get(zAssignment.getFirstChild().getIndex())).isTrue();
        assertThat(zKill.get(zAssignment.getFirstChild().getIndex())).isFalse();
        assertThat(result.lattice.isLive(zAssignment.getFirstChild().getIndex())).isTrue();

        // Check that variables are not killed by dead assignments.
        Node deadAssignment = NodeUtil.findAssignment(NodeUtil.findChildByName(cfg.getStartNode(), "x"), cfg);
        deadAssignment.setRight(Node.NULL);

        LiveVariablesAnalysis deadVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        LiveVariablesAnalysis deadResult = deadVariablesAnalysis.compute();

        // Check that x is still live at the end of the function.
        assertThat(deadResult.lattice.isLive(xAssignment.getFirstChild().getIndex())).isTrue();
    }

    @Test
    public void testEscapedLocals() {
        // Sample test case for a function with an escaped local variable.
        String code = "function test() {" +
                "  function innerFunction() {" +
                "    var x = 1;" +
                "  }" +
                "  innerFunction();" +
                "}"
                + "";

        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(NodeUtil.parse(code));
        Scope jsScope = new Scope();
        AbstractCompiler compiler = new Compiler();

        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();

        // Check that x is escaped.
        Node innerFunction = NodeUtil.findChildByName(cfg.getStartNode(), "innerFunction");
        Node xAssignment = NodeUtil.findAssignment(NodeUtil.findChildByName(innerFunction, "x"), cfg);
        Var x = jsScope.getVar(xAssignment.getString());
        int index = x.index;
        assertThat(escapedLocals).contains(x);
        assertThat(liveVariablesAnalysis.lattice.isLive(index)).isTrue();
    }
}