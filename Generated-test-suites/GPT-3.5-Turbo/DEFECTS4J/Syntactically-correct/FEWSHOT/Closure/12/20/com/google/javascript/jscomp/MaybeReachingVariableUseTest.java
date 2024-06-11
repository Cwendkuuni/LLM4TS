package com.google.javascript.jscomp;

import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class MaybeReachingVariableUseTest {
    private MaybeReachingVariableUse reachingVariableUse;

    @Before
    public void setUp() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(null);
        Scope jsScope = new Scope(null);
        AbstractCompiler compiler = new AbstractCompiler();
        reachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, compiler);
    }

    @Test
    public void testCreateEntryLattice() {
        MaybeReachingVariableUse.ReachingUses result = reachingVariableUse.createEntryLattice();
        assertNotNull(result);
    }

    @Test
    public void testCreateInitialEstimateLattice() {
        MaybeReachingVariableUse.ReachingUses result = reachingVariableUse.createInitialEstimateLattice();
        assertNotNull(result);
    }

    @Test
    public void testIsForward() {
        assertFalse(reachingVariableUse.isForward());
    }

    @Test
    public void testFlowThrough() {
        Node n = new Node(Token.BLOCK);
        MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();
        MaybeReachingVariableUse.ReachingUses result = reachingVariableUse.flowThrough(n, input);
        assertNotNull(result);
    }

    @Test
    public void testGetUses() {
        String name = "A";
        Node defNode = new Node(Token.BLOCK);
        Collection<Node> result = reachingVariableUse.getUses(name, defNode);
        assertNotNull(result);
    }
}