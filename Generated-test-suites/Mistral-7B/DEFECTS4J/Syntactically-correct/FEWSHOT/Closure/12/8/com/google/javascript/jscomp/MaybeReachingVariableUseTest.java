package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class MaybeReachingVariableUseTest {

    @Test
    public void testEmpty() {
        Scope scope = new Scope();
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);
        ReachingUses result = analysis.computeResult(null);
        assertNull(result);
    }

    @Test
    public void testSimpleAssignment() {
        String jsCode = "var x = 1;";
        Scope scope = new Scope(jsCode);
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsCode);
        MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);

        ReachingUses result = analysis.computeResult(cfg.getNode(0));
        assertNotNull(result);
        Multimap<Var, Node> mayUseMap = result.mayUseMap;
        assertTrue(mayUseMap.isEmpty());
    }

    @Test
    public void testSimpleAssignmentWithUse() {
        String jsCode = "var x = 1; print(x);";
        Scope scope = new Scope(jsCode);
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsCode);
        MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);

        ReachingUses result = analysis.computeResult(cfg.getNode(1));
        assertNotNull(result);
        Multimap<Var, Node> mayUseMap = result.mayUseMap;
        Set<Var> vars = Sets.newHashSet(scope.getVar("x"));
        Collection<Node> uses = analysis.getUses("x", cfg.getNode(1));
        assertTrue(mayUseMap.containsAll(Sets.newHashMap(Sets.entrySet(vars))));
        assertTrue(uses.contains(cfg.getNode(0)));
    }

    @Test
    public void testSimpleIfStatement() {
        String jsCode = "var x = 1; if (x > 0) { print(x); }";
        Scope scope = new Scope(jsCode);
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsCode);
        MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);

        ReachingUses result = analysis.computeResult(cfg.getNode(1));
        assertNotNull(result);
        Multimap<Var, Node> mayUseMap = result.mayUseMap;
        Set<Var> vars = Sets.newHashSet(scope.getVar("x"));
        Collection<Node> uses = analysis.getUses("x", cfg.getNode(1));
        assertTrue(mayUseMap.containsAll(Sets.newHashMap(Sets.entrySet(vars))));
        assertTrue(uses.contains(cfg.getNode(1)));
    }

    @Test
    public void testComplexStatement() {
        String jsCode =
                "var x = 1;\n" +
                "if (x > 0) {\n" +
                "  x += 2;\n" +
                "  print(x);\n" +
                "} else {\n" +
                "  x = 3;\n" +
                "  print(x);\n" +
                "}";
        Scope scope = new Scope(jsCode);
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsCode);
        MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);

        ReachingUses result1 = analysis.computeResult(cfg.getNode(1));
        assertNotNull(result1);
        Multimap<Var, Node> mayUseMap1 = result1.mayUseMap;
        Set<Var> vars1 = Sets.newHashSet(scope.getVar("x"));
        Collection<Node> uses1 = analysis.getUsages("x", cfg.getNode(1));
        assertTrue(mayUseMap1.containsAll(Sets.newHashMap(Sets.entrySet(vars1))));
        assertTrue(uses1.contains(cfg.getNode(1)));

        ReachingUses result2 = analysis.computeResult(cfg.getNode(3));
        assertNotNull(result2);
        Multimap<Var, Node> mayUseMap2 = result2.mayUseMap;
        Set<Var> vars2 = Sets.newHashSet(scope.getVar("x"));
        Collection<Node> uses2 = analysis.getUsages("x", cfg.getNode(3));
        assertTrue(mayUseMap2.isEmpty());
        assertFalse(uses2.contains(cfg.getNode(1)));
        assertTrue(uses2.contains(cfg.getNode(3)));
    }

    @Test
    public void testForLoop() {
        String jsCode = "for (var x in [1, 2, 3]) { print(x); }";
        Scope scope = new Scope(jsCode);
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsCode);
        MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);

        ReachingUses result = analysis.computeResult(cfg.getNode(1));
        assertNotNull(result);
        Multimap<Var, Node> mayUseMap = result.mayUseMap;
        Set<Var> vars = Sets.newHashSet(scope.getVar("x"));
        Collection<Node> uses = analysis.getUses("x", cfg.getNode(1));
        assertTrue(mayUseMap.isEmpty());
        assertFalse(uses.contains(cfg.getNode(0)));
        for (int i = 0; i < 3; i++) {
            assertTrue(uses.contains(cfg.getNode(i + 2)));
        }
    }

    @Test
    public void testFunctionCall() {
        String jsCode = "function foo() { return 1; } var x = foo(); print(x);";
        Scope scope = new Scope(jsCode);
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsCode);
        MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);

        ReachingUses result = analysis.computeResult(cfg.getNode(2));
        assertNotNull(result);
        Multimap<Var, Node> mayUseMap = result.mayUseMap;
        Set<Var> vars = Sets.newHashSet(scope.getVar("x"));
        Collection<Node> uses = analysis.getUses("x", cfg.getNode(2));
        assertTrue(mayUseMap.containsAll(Sets.newHashMap(Sets.entrySet(vars))));
        assertTrue(uses.contains(cfg.getNode(1)));
    }

    @Test
    public void testFunctionDeclaration() {
        String jsCode = "function foo(x) { return x + 1; } var x = 1; var y = foo(x); print(y);";
        Scope scope = new Scope(jsCode);
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsCode);
        MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);

        ReachingUses result = analysis.computeResult(cfg.getNode(2));
        assertNotNull(result);
        Multimap<Var, Node> mayUseMap = result.mayUseMap;
        Set<Var> vars = Sets.newHashSet(scope.getVar("x"), scope.getVar("y"));
        Collection<Node> uses = analysis.getUsages("x", cfg.getNode(2));
        assertTrue(mayUseMap.containsAll(Sets.newHashMap(Sets.entrySet(vars))));
        assertTrue(uses.contains(cfg.getNode(1)));

        result = analysis.computeResult(cfg.getNode(4));
        assertNotNull(result);
        mayUseMap = result.mayUseMap;
        vars = Sets.newHashSet(scope.getVar("x"), scope.getVar("y"));
        uses = analysis.getUsages("x", cfg.getNode(4));
        assertTrue(mayUseMap.containsAll(Sets.newHashMap(Sets.entrySet(vars))));
        assertTrue(uses.isEmpty());

        uses = analysis.getUsages("y", cfg.getNode(4));
        assertTrue(uses.contains(cfg.getNode(3)));
    }

    @Test
    public void testCatchBlock() {
        String jsCode = "try { throw new Error('Test'); } catch (e) { print(e.message); }";
        Scope scope = new Scope(jsCode);
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsCode);
        MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);

        ReachingUses result = analysis.computeResult(cfg.getNode(2));
        assertNotNull(result);
        Multimap<Var, Node> mayUseMap = result.mayUseMap;
        Set<Var> vars = Sets.newHashSet(scope.getVar("e"));
        Collection<Node> uses = analysis.getUsages("e", cfg.getNode(2));
        assertTrue(mayUseMap.isEmpty());
        assertFalse(uses.contains(cfg.getNode(0)));
        assertTrue(uses.contains(cfg.getNode(2)));
    }
}