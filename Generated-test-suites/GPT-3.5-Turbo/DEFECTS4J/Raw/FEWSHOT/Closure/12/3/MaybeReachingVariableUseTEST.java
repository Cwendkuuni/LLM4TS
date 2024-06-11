package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MaybeReachingVariableUseTest {
    @Test
    public void testFlowThrough() {
        ControlFlowGraph<Node> cfg = null; // TODO: Initialize with a valid ControlFlowGraph
        Scope jsScope = null; // TODO: Initialize with a valid Scope
        AbstractCompiler compiler = null; // TODO: Initialize with a valid AbstractCompiler

        MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

        Node n = null; // TODO: Initialize with a valid Node
        MaybeReachingVariableUse.ReachingUses input = null; // TODO: Initialize with a valid ReachingUses object

        MaybeReachingVariableUse.ReachingUses output = analysis.flowThrough(n, input);

        MaybeReachingVariableUse.ReachingUses expectedOutput = null; // TODO: Initialize with a valid ReachingUses object

        assertEquals(expectedOutput, output);
    }

    @Test
    public void testGetUses() {
        ControlFlowGraph<Node> cfg = null; // TODO: Initialize with a valid ControlFlowGraph
        Scope jsScope = null; // TODO: Initialize with a valid Scope
        AbstractCompiler compiler = null; // TODO: Initialize with a valid AbstractCompiler

        MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

        String name = ""; // TODO: Provide a valid name
        Node defNode = null; // TODO: Initialize with a valid Node

        Collection<Node> uses = analysis.getUses(name, defNode);

        Collection<Node> expectedUses = null; // TODO: Provide the expected collection of Nodes

        assertEquals(expectedUses, uses);
    }
}