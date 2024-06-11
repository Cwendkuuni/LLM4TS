package com.google.javascript.jscomp;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
public class MaybeReachingVariableUseTest {
    @Test
    public void testCreateEntryLattice() {
        MaybeReachingVariableUse.ReachingUses reachingUses = new MaybeReachingVariableUse().createEntryLattice();
        assertEquals(0, reachingUses.mayUseMap.size());
    }
    
    @Test
    public void testCreateInitialEstimateLattice() {
        MaybeReachingVariableUse.ReachingUses reachingUses = new MaybeReachingVariableUse().createInitialEstimateLattice();
        assertEquals(0, reachingUses.mayUseMap.size());
    }
    
    @Test
    public void testFlowThrough() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>();
        MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse().createInitialEstimateLattice();
        MaybeReachingVariableUse.ReachingUses output = new MaybeReachingVariableUse().flowThrough(new Node(Token.EXPR_RESULT), input);
        assertEquals(input, output);
    }
    
    @Test
    public void testGetUses() {
        MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(new ControlFlowGraph<Node>(), new Scope(), new AbstractCompiler());
        Collection<Node> uses = maybeReachingVariableUse.getUses("name", new Node(Token.VAR));
        assertEquals(0, uses.size());
    }
}