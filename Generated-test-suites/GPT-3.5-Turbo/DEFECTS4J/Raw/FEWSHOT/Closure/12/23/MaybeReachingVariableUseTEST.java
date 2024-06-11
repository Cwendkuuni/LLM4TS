package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MaybeReachingVariableUseTest {
    private ControlFlowGraph<Node> cfg;
    private Scope jsScope;
    private AbstractCompiler compiler;
    
    @Test
    public void testCreateEntryLattice() {
        MaybeReachingVariableUse.ReachingUses uses = new MaybeReachingVariableUse.ReachingUses();
        assertEquals(0, uses.mayUseMap.size());
    }
    
    @Test
    public void testCreateInitialEstimateLattice() {
        MaybeReachingVariableUse.ReachingUses uses = new MaybeReachingVariableUse.ReachingUses();
        assertEquals(0, uses.mayUseMap.size());
    }
    
    @Test
    public void testFlowThrough() {
        MaybeReachingVariableUse uses = new MaybeReachingVariableUse(cfg, jsScope, compiler);
        Node n = new Node(Token.NAME);
        MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();
        MaybeReachingVariableUse.ReachingUses output = uses.flowThrough(n, input);
        assertEquals(input, output);
    }
    
    @Test
    public void testGetUses() {
        MaybeReachingVariableUse uses = new MaybeReachingVariableUse(cfg, jsScope, compiler);
        String name = "test";
        Node defNode = new Node(Token.NAME);
        Collection<Node> result = uses.getUses(name, defNode);
        assertEquals(0, result.size());
    }
}