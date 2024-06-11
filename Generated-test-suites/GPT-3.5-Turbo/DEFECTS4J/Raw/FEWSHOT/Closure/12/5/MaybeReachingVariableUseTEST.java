package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MaybeReachingVariableUseTest {
    @Test
    public void testCreateEntryLattice() {
        MaybeReachingVariableUse.ReachingUses reachingUses = new MaybeReachingVariableUse.ReachingUses();
        assertEquals(0, reachingUses.mayUseMap.size());
    }

    @Test
    public void testCreateInitialEstimateLattice() {
        MaybeReachingVariableUse.ReachingUses reachingUses = new MaybeReachingVariableUse.ReachingUses();
        assertEquals(0, reachingUses.mayUseMap.size());
    }

    @Test
    public void testFlowThrough() {
        ControlFlowGraph cfg = new ControlFlowGraph();

        Node node = new Node(Token.BLOCK);
        ReachingUses input = new ReachingUses();
        ReachingUses output = new ReachingUses();

        MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(cfg, new Scope(), new AbstractCompiler());

        ReachingUses result = maybeReachingVariableUse.flowThrough(node, input);
        assertEquals(output, result);
    }

    @Test
    public void testIsForward() {
        MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(new ControlFlowGraph(), new Scope(), new AbstractCompiler());
        boolean result = maybeReachingVariableUse.isForward();
        assertEquals(false, result);
    }

    @Test
    public void testJoinOp() {
        MaybeReachingVariableUse.ReachingUses reachingUses1 = new MaybeReachingVariableUse.ReachingUses();
        MaybeReachingVariableUse.ReachingUses reachingUses2 = new MaybeReachingVariableUse.ReachingUses();
        MaybeReachingVariableUse.ReachingUses reachingUses3 = new MaybeReachingVariableUse.ReachingUses();

        MaybeReachingVariableUse.ReachingUses result = new MaybeReachingVariableUse.ReachingUses();

        result.mayUseMap.put("var1", new Node());
        result.mayUseMap.put("var2", new Node());

        reachingUses1.mayUseMap.put("var1", new Node());
        reachingUses2.mayUseMap.put("var2", new Node());
        reachingUses3.mayUseMap.put("var3", new Node());

        MaybeReachingVariableUse.ReachingUsesJoinOp joinOp = new MaybeReachingVariableUse.ReachingUsesJoinOp();

        assertEquals(joinOp.apply(Arrays.asList(reachingUses1, reachingUses2, reachingUses3)), result);
    }

    @Test
    public void testGetUses() {
        MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(new ControlFlowGraph(), new Scope(), new AbstractCompiler());

        Node defNode = new Node();
        Collection<Node> result = maybeReachingVariableUse.getUses("var", defNode);
        assertEquals(0, result.size());
    }
}