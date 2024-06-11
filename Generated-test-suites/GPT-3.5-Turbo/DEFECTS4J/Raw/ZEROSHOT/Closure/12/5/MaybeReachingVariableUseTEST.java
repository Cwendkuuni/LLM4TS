import org.junit.Test;

import static org.junit.Assert.*;

public class MaybeReachingVariableUseTest {

    @Test
    public void testFlowThrough() {
        ControlFlowGraph<Node> cfg = null; // create ControlFlowGraph instance here
        Scope jsScope = null; // create Scope instance here
        AbstractCompiler compiler = null; // create AbstractCompiler instance here
        MaybeReachingVariableUse reachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, compiler);
        Node n = null; // create Node instance here
        MaybeReachingVariableUse.ReachingUses input = null; // create ReachingUses instance here
        MaybeReachingVariableUse.ReachingUses expectedOutput = null; // create ReachingUses instance here

        MaybeReachingVariableUse.ReachingUses output = reachingVariableUse.flowThrough(n, input);

        assertEquals(expectedOutput, output);
    }

    @Test
    public void testCreateEntryLattice() {
        MaybeReachingVariableUse reachingVariableUse = new MaybeReachingVariableUse(null, null, null);
        MaybeReachingVariableUse.ReachingUses expectedLattice = null; // create ReachingUses instance here

        MaybeReachingVariableUse.ReachingUses lattice = reachingVariableUse.createEntryLattice();

        assertEquals(expectedLattice, lattice);
    }

    @Test
    public void testCreateInitialEstimateLattice() {
        MaybeReachingVariableUse reachingVariableUse = new MaybeReachingVariableUse(null, null, null);
        MaybeReachingVariableUse.ReachingUses expectedLattice = null; // create ReachingUses instance here

        MaybeReachingVariableUse.ReachingUses lattice = reachingVariableUse.createInitialEstimateLattice();

        assertEquals(expectedLattice, lattice);
    }

    @Test
    public void testComputeEscaped() {
        Scope jsScope = null; // create Scope instance here
        Set<Scope.Var> escaped = null; // create Set instance here
        AbstractCompiler compiler = null; // create AbstractCompiler instance here

        MaybeReachingVariableUse.computeEscaped(jsScope, escaped, compiler);

        // Add assertion here to test the method
    }

    @Test
    public void testFlowThrough() {
        Node n = null; // create Node instance here
        Node cfgNode = null; // create Node instance here
        MaybeReachingVariableUse.ReachingUses input = null; // create ReachingUses instance here
        MaybeReachingVariableUse.ReachingUses expectedOutput = null; // create ReachingUses instance here

        MaybeReachingVariableUse.ReachingUses output = reachingVariableUse.flowThrough(n, cfgNode, input);

        assertEquals(expectedOutput, output);
    }

    @Test
    public void testComputeMayUse() {
        Node n = null; // create Node instance here
        Node cfgNode = null; // create Node instance here
        MaybeReachingVariableUse.ReachingUses output = null; // create ReachingUses instance here
        boolean conditional = false; // set conditional value here
        MaybeReachingVariableUse reachingVariableUse = new MaybeReachingVariableUse(null, null, null);

        reachingVariableUse.computeMayUse(n, cfgNode, output, conditional);

        // Add assertion here to test the method
    }

    @Test
    public void testAddToUseIfLocal() {
        String name = null; // set name value here
        Node node = null; // create Node instance here
        MaybeReachingVariableUse.ReachingUses use = null; // create ReachingUses instance here
        MaybeReachingVariableUse reachingVariableUse = new MaybeReachingVariableUse(null, null, null);

        reachingVariableUse.addToUseIfLocal(name, node, use);

        // Add assertion here to test the method
    }

    @Test
    public void testRemoveFromUseIfLocal() {
        String name = null; // set name value here
        MaybeReachingVariableUse.ReachingUses use = null; // create ReachingUses instance here
        MaybeReachingVariableUse reachingVariableUse = new MaybeReachingVariableUse(null, null, null);

        reachingVariableUse.removeFromUseIfLocal(name, use);

        // Add assertion here to test the method
    }

    @Test
    public void testGetUses() {
        String name = null; // set name value here
        Node defNode = null; // create Node instance here
        MaybeReachingVariableUse reachingVariableUse = new MaybeReachingVariableUse(null, null, null);
        Collection<Node> expectedUses = null; // create Collection instance here

        Collection<Node> uses = reachingVariableUse.getUses(name, defNode);

        assertEquals(expectedUses, uses);
    }
}