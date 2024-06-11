package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.common.collect.Sets;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

public class MustBeReachingVariableDefTest {
    @Test
    public void testAddToDefIfLocal() {
        Scope scope = new Scope();
        MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(
            new ControlFlowGraph<Node>(null), scope, null);

        String name1 = "x";
        String name2 = "y";
        String name3 = "z";

        Var var1 = new Var(name1, null, null, 0, null, null, null, null, true, null);
        Var var2 = new Var(name2, null, null, 0, null, null, null, null, true, null);
        Var var3 = new Var(name3, null, null, 0, null, null, null, null, true, null);

        scope.setVar(name1, var1);
        scope.setVar(name2, var2);
        scope.setVar(name3, var3);

        Node definitionNode1 = new Node();
        Node definitionNode2 = new Node();
        Node useNode = new Node();

        analyzer.addToDefIfLocal(name1, null, null, analyzer.createEntryLattice());
        assertNull(analyzer.getDef(name1, useNode));
        analyzer.addToDefIfLocal(name1, definitionNode1, null, analyzer.createEntryLattice());
        assertEquals(definitionNode1, analyzer.getDef(name1, useNode));

        analyzer.addToDefIfLocal(name2, null, null, analyzer.createEntryLattice());
        assertNull(analyzer.getDef(name2, useNode));
        analyzer.addToDefIfLocal(name2, definitionNode1, null, analyzer.createEntryLattice());
        assertNull(analyzer.getDef(name2, useNode));
        analyzer.addToDefIfLocal(name2, definitionNode2, null, analyzer.createEntryLattice());
        assertEquals(definitionNode2, analyzer.getDef(name2, useNode));

        analyzer.addToDefIfLocal(name3, null, null, analyzer.createEntryLattice());
        assertNull(analyzer.getDef(name3, useNode));
        analyzer.addToDefIfLocal(name3, definitionNode1, null, analyzer.createEntryLattice());
        assertNull(analyzer.getDef(name3, useNode));
        analyzer.addToDefIfLocal(name3, definitionNode2, null, analyzer.createEntryLattice());
        assertNull(analyzer.getDef(name3, useNode));
        analyzer.addToDefIfLocal(name3, definitionNode1, definitionNode2, analyzer.createEntryLattice());
        assertEquals(definitionNode1, analyzer.getDef(name3, useNode));

        analyzer.escapeParameters(analyzer.createEntryLattice());
        assertNull(analyzer.getDef(name1, useNode));
        assertNull(analyzer.getDef(name2, useNode));
        assertNull(analyzer.getDef(name3, useNode));
    }

    @Test
    public void testComputeDependence() {
        Scope scope = new Scope();
        MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(
            new ControlFlowGraph<Node>(null), scope, null);

        String name1 = "x";
        String name2 = "y";

        Var var1 = new Var(name1, null, null, 0, null, null, null, null, true, null);
        Var var2 = new Var(name2, null, null, 0, null, null, null, null, true, null);

        scope.setVar(name1, var1);
        scope.setVar(name2, var2);

        Node definitionNode1 = new Node();
        Node definitionNode2 = new Node();
        Node useNode = new Node();

        Definition def1 = new Definition(definitionNode1);
        Definition def2 = new Definition(definitionNode2);
        def1.depends.add(var2);
        def2.depends.add(var1);

        analyzer.computeDependence(def1, useNode);
        Set<Var> result = Sets.newHashSet(var1);
        assertEquals(result, def2.depends);
    }

    @Test
    public void testEscapeParameters() {
        Scope scope = new Scope();
        MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(
            new ControlFlowGraph<Node>(null), scope, null);

        String name1 = "x";
        String name2 = "y";
        String name3 = "arguments";

        Var var1 = new Var(name1, null, null, 0, null, null, null, null, true, null);
        Var var2 = new Var(name2, null, null, 0, null, null, null, null, true, null);
        Var argumentsVar = new Var(name3, null, null, 0, null, null, null, null, true, null);

        scope.setVar(name1, var1);
        scope.setVar(name2, var2);
        scope.setVar(name3, argumentsVar);

        Node definitionNode1 = new Node();
        Node useNode = new Node();

        analyzer.escapeParameters(analyzer.createEntryLattice());
        assertNull(analyzer.getDef(name1, useNode));
        assertNull(analyzer.getDef(name2, useNode));
        assertNull(analyzer.getDef(name3, useNode));
    }

    @Test
    public void testFlowThrough() {
        Scope scope = new Scope();
        MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(
            new ControlFlowGraph<Node>(null), scope, null);

        String name1 = "x";
        String name2 = "y";
        String name3 = "z";

        Var var1 = new Var(name1, null, null, 0, null, null, null, null, true, null);
        Var var2 = new Var(name2, null, null, 0, null, null, null, null, true, null);
        Var var3 = new Var(name3, null, null, 0, null, null, null, null, true, null);

        scope.setVar(name1, var1);
        scope.setVar(name2, var2);
        scope.setVar(name3, var3);

        Node definitionNode1 = new Node();
        Node definitionNode2 = new Node();

        MustDef mustDef = analyzer.createEntryLattice();

        analyzer.addToDefIfLocal(name1, definitionNode1, null, mustDef);
        analyzer.addToDefIfLocal(name2, definitionNode2, null, mustDef);

        Node useNode1 = new Node();
        Node useNode2 = new Node();

        MustDef result1 = analyzer.flowThrough(useNode1, mustDef);
        MustDef result2 = analyzer.flowThrough(useNode2, mustDef);

        assertEquals(mustDef, result1);
        assertEquals(mustDef, result2);
    }

    @Test
    public void testIsForward() {
        MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(
            new ControlFlowGraph<Node>(null), null, null);
        assertTrue(analyzer.isForward());
    }
}