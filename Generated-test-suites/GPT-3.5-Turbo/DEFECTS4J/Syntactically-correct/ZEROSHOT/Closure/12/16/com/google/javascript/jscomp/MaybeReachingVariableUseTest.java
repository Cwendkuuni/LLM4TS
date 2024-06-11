package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class MaybeReachingVariableUseTest {
    
    private MaybeReachingVariableUse.ReachingUses reachingUses;
    
    @Before
    public void setUp() {
        reachingUses = new MaybeReachingVariableUse.ReachingUses();
    }
    
    @Test
    public void testEquals() {
        MaybeReachingVariableUse.ReachingUses other = new MaybeReachingVariableUse.ReachingUses();
        assertTrue(reachingUses.equals(other));
    }
    
    @Test
    public void testHashCode() {
        MaybeReachingVariableUse.ReachingUses other = new MaybeReachingVariableUse.ReachingUses();
        assertEquals(reachingUses.hashCode(), other.hashCode());
    }
    
    @Test
    public void testFlowThrough() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>();
        Scope jsScope = new Scope();
        AbstractCompiler compiler = new AbstractCompiler();
        MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, compiler);
        Node n = new Node(Token.BLOCK);
        maybeReachingVariableUse.flowThrough(n, reachingUses);
        
        // TODO: Add assertions here
    }
    
    @Test
    public void testCreateInitialEstimateLattice() {
        MaybeReachingVariableUse.ReachingUses initialEstimateLattice = new MaybeReachingVariableUse.ReachingUses();
        assertEquals(reachingUses.getClass(), initialEstimateLattice.getClass());
    }
    
    @Test
    public void testCreateEntryLattice() {
        MaybeReachingVariableUse.ReachingUses entryLattice = new MaybeReachingVariableUse.ReachingUses();
        assertEquals(reachingUses.getClass(), entryLattice.getClass());
    }
    
    @Test
    public void testIsForward() {
        MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(null, null, null);
        assertFalse(maybeReachingVariableUse.isForward());
    }
    
    @Test
    public void testComputeMayUse() {
        Node n = new Node(Token.BLOCK);
        Node cfgNode = new Node(Token.BLOCK);
        boolean conditional = false;
        MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(null, null, null);
        maybeReachingVariableUse.computeMayUse(n, cfgNode, reachingUses, conditional);
        
        // TODO: Add assertions here
    }
    
    @Test
    public void testAddToUseIfLocal() {
        String name = "test";
        Node node = new Node(Token.BLOCK);
        MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(null, null, null);
        maybeReachingVariableUse.addToUseIfLocal(name, node, reachingUses);
        
        // TODO: Add assertions here
    }
    
    @Test
    public void testRemoveFromUseIfLocal() {
        String name = "test";
        MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(null, null, null);
        maybeReachingVariableUse.removeFromUseIfLocal(name, reachingUses);
        
        // TODO: Add assertions here
    }
    
    @Test
    public void testGetUses() {
        String name = "test";
        Node defNode = new Node(Token.BLOCK);
        MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(null, null, null);
        Collection<Node> uses = maybeReachingVariableUse.getUses(name, defNode);
        
        // TODO: Add assertions here
    }
}