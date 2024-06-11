package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Sets;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

import java.util.BitSet;
import java.util.List;
import java.util.Set;

public class LiveVariablesAnalysisTest {

    ControlFlowGraph<Node> cfg;
    Scope jsScope;
    AbstractCompiler compiler;

    @Before
    public void setup() {
        jsScope = new Scope(Sets.newHashSet());
        cfg = new ControlFlowGraph<>();
        compiler = new AbstractCompiler();
    }

    @Test
    public void testGetEscapedLocals() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        assertNotNull(analysis.getEscapedLocals());
        assertTrue(analysis.getEscapedLocals().isEmpty());
    }

    @Test
    public void testGetVarIndex() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        jsScope.declare("var1", null, false);
        jsScope.declare("var2", null, false);
        assertEquals(0, analysis.getVarIndex("var1"));
        assertEquals(1, analysis.getVarIndex("var2"));
    }

    @Test
    public void testIsForward() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        assertEquals(false, analysis.isForward());
    }

    @Test
    public void testCreateEntryLattice() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.createEntryLattice();
        assertNotNull(lattice.liveSet);
        assertEquals(0, lattice.liveSet.size());
    }

    @Test
    public void testCreateInitialEstimateLattice() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.createInitialEstimateLattice();
        assertNotNull(lattice.liveSet);
        assertEquals(0, lattice.liveSet.size());
    }

    @Test
    public void testFlowThrough() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        LiveVariablesAnalysis.LiveVariableLattice lattice = new LiveVariablesAnalysis.LiveVariableLattice(jsScope.getVarCount());

        // Test empty node
        Node node = new Node(Token.SCRIPT);
        LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(node, lattice);
        assertEquals(0, result.liveSet.size());

        // Test node with variable declaration
        node = new Node(Token.VAR);
        node.addChildToBack(NodeUtil.newName(compiler, "var1", null));
        node.addChildToBack(NodeUtil.newName(compiler, "var2", null));
        result = analysis.flowThrough(node, lattice);
        assertEquals(2, result.liveSet.size());

        // Test node with assignment
        Node assignment = NodeUtil.newAssign("var1", NodeUtil.newNumber(1));
        node = new Node(Token.EXPR_RESULT, assignment);
        result = analysis.flowThrough(node, lattice);
        assertEquals(1, result.liveSet.size());
        assertTrue(result.liveSet.get(0));

        // Test node with conditional assignment
        Node condition = NodeUtil.newNumber(1);
        Node conditionalAssignment = new Node(Token.HOOK, condition, NodeUtil.newNumber(2), NodeUtil.newNumber(3));
        node = new Node(Token.EXPR_RESULT, conditionalAssignment);
        result = analysis.flowThrough(node, lattice);
        assertEquals(0, result.liveSet.size());

        // Test node with variable read
        Node read = NodeUtil.newName(compiler, "var1", null);
        node = new Node(Token.EXPR_RESULT, read);
        result = analysis.flowThrough(node, lattice);
        assertEquals(1, result.liveSet.size());
        assertTrue(result.liveSet.get(0));

        // Test node with function call
        Node call = new Node(Token.CALL, NodeUtil.newName(compiler, "func", null));
        node = new Node(Token.EXPR_RESULT, call);
        result = analysis.flowThrough(node, lattice);
        assertEquals(0, result.liveSet.size());
    }

    @Test
    public void testMarkAllParametersEscaped() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        analysis.markAllParametersEscaped();
        Set<Scope.Var> escaped = analysis.getEscapedLocals();
        assertNotNull(escaped);
        assertTrue(escaped.isEmpty());
    }

    @Test
    public void testIsArgumentsName() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

        // Test arguments name declared as variable
        Node arguments = NodeUtil.newName(compiler, "arguments", null);
        assertTrue(analysis.isArgumentsName(arguments));

        // Test arguments name not declared as variable
        arguments = NodeUtil.newName(compiler, "args", null);
        assertTrue(!analysis.isArgumentsName(arguments));

        // Test arguments name declared as parameter
        jsScope = new Scope(Sets.newHashSet());
        Node function = new Node(Token.FUNCTION);
        function.addChildToBack(NodeUtil.newName(compiler, "args", null));
        jsScope.declare("arguments", null, false);
        jsScope.getRootNode().addChildToBack(function);
        analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        arguments = function.getFirstChild();
        assertTrue(analysis.isArgumentsName(arguments));
    }

    // TODO: Add more test methods to comprehensively test all the methods in the LiveVariablesAnalysis class

}