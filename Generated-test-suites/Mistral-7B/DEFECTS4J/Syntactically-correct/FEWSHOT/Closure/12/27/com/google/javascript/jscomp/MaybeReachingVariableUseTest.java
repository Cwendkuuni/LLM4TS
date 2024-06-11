package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class MaybeReachingVariableUseTest {
    private static final Scope testScope = new Scope();
    private static final ControlFlowGraph<Node> testGraph = new ControlFlowGraph<>();

    @Test
    public void testEmpty() {
        MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(testGraph, testScope, null);
        ReachingUses result = analysis.run(testGraph.getStartNode(), testGraph.getEndNode());
        assertTrue(result.mayUseMap.isEmpty());
    }

    @Test
    public void testSimpleVariable() {
        String code = "var a = 1; alert(a);";
        parseAndAnalyze(code);
        ReachingUses result = analysis.getResult();
        Collection<Node> uses = result.mayUseMap.get(testScope.getVar("a"));
        assertFalse(uses.isEmpty());
        assertTrue(uses.contains(testGraph.getNodeByIndex(1)));
    }

    @Test
    public void testVariableWithAssignment() {
        String code = "var a = 1; a = 2; alert(a);";
        parseAndAnalyze(code);
        ReachingUses result = analysis.getResult();
        Collection<Node> uses = result.mayUseMap.get(testScope.getVar("a"));
        assertFalse(uses.isEmpty());
        assertTrue(uses.contains(testGraph.getNodeByIndex(3)));
    }

    @Test
    public void testVariableWithConditional() {
        String code = "var a = 1; if (true) { a = 2; } alert(a);";
        parseAndAnalyze(code);
        ReachingUses result = analysis.getResult();
        Collection<Node> uses = result.mayUseMap.get(testScope.getVar("a"));
        assertFalse(uses.isEmpty());
        assertTrue(uses.contains(testGraph.getNodeByIndex(3)));
    }

    @Test
    public void testVariableWithLoop() {
        String code = "var a = 1; for (var i = 0; i < 10; i++) { a = i; } alert(a);";
        parseAndAnalyze(code);
        ReachingUses result = analysis.getResult();
        Collection<Node> uses = result.mayUseMap.get(testScope.getVar("a"));
        assertFalse(uses.isEmpty());
        assertTrue(uses.contains(testGraph.getNodeByIndex(11)));
    }

    @Test
    public void testVariableWithFunctionCall() {
        String code = "var a = 1; alert(foo(a)); function foo(x) { x = 2; return x; }";
        parseAndAnalyze(code);
        ReachingUses result = analysis.getResult();
        Collection<Node> uses = result.mayUseMap.get(testScope.getVar("a"));
        assertFalse(uses.isEmpty());
        assertTrue(uses.contains(testGraph.getNodeByIndex(2)));
    }

    @Test
    public void testEscapedVariable() {
        String code = "function foo() { var x = 1; } var a = foo().x; alert(a);";
        parseAndAnalyze(code);
        ReachingUses result = analysis.getResult();
        Collection<Node> uses = result.mayUseMap.get(testScope.getVar("a"));
        assertTrue(uses.isEmpty());
    }

    @Test
    public void testVariableWithExceptionHandler() {
        String code = "try { var a = 1; } catch (e) { a = 2; } alert(a);";
        parseAndAnalyze(code);
        ReachingUses result = analysis.getResult();
        Collection<Node> uses = result.mayUseMap.get(testScope.getVar("a"));
        assertFalse(uses.isEmpty());
        assertTrue(uses.contains(testGraph.getNodeByIndex(2)));
    }

    private void parseAndAnalyze(String code) {
        AbstractCompiler compiler = new AbstractCompiler();
        Scope scope = testScope;
        CompilationUnit cu = new CompilationUnit(code, null, null, null, null, scope, null);
        JsCompiler jsCompiler = new JsCompiler(compiler, null, null, null, null);
        jsCompiler.compile(cu, testGraph);
        MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(testGraph, scope, jsCompiler);
        analysis.run(testGraph.getStartNode(), testGraph.getEndNode());
    }

    private MaybeReachingVariableUse analysis;

    @After
    public void tearDown() {
        analysis = null;
    }
}