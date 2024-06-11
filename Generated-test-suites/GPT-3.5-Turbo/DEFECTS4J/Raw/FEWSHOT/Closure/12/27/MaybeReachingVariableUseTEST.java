import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MaybeReachingVariableUseTest {
    @Test
    public void testCreateEntryLattice() {
        MaybeReachingVariableUse.ReachingUses reachingUses = new MaybeReachingVariableUse.ReachingUses();
        assertEquals(new MaybeReachingVariableUse.ReachingUses().mayUseMap, reachingUses.mayUseMap);
    }

    @Test
    public void testCreateInitialEstimateLattice() {
        MaybeReachingVariableUse.ReachingUses reachingUses = new MaybeReachingVariableUse.ReachingUses();
        assertEquals(new MaybeReachingVariableUse.ReachingUses().mayUseMap, reachingUses.mayUseMap);
    }

    @Test
    public void testFlowThrough() {
        MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();
        MaybeReachingVariableUse.ReachingUses output = new MaybeReachingVariableUse.ReachingUses(input);
        
        MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(new ControlFlowGraph<Node>(), new Scope(), new AbstractCompiler());
        MaybeReachingVariableUse.ReachingUses newOutput = maybeReachingVariableUse.flowThrough(new Node(), input);
        
        assertEquals(output.mayUseMap, newOutput.mayUseMap);
    }

    @Test
    public void testNameIsAddedToUseIfLocal() {
        MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(new ControlFlowGraph<Node>(), new Scope(), new AbstractCompiler());
        MaybeReachingVariableUse.ReachingUses use = new MaybeReachingVariableUse.ReachingUses();
        maybeReachingVariableUse.addToUseIfLocal("name", new Node(), use);
        assertEquals(1, use.mayUseMap.size());
    }

    @Test
    public void testNameIsNotAddedToUseIfLocal() {
        MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(new ControlFlowGraph<Node>(), new Scope(), new AbstractCompiler());
        MaybeReachingVariableUse.ReachingUses use = new MaybeReachingVariableUse.ReachingUses();
        maybeReachingVariableUse.addToUseIfLocal("escapedName", new Node(), use);
        assertEquals(0, use.mayUseMap.size());
    }

    @Test
    public void testRemoveFromUseIfLocal() {
        MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(new ControlFlowGraph<Node>(), new Scope(), new AbstractCompiler());
        MaybeReachingVariableUse.ReachingUses use = new MaybeReachingVariableUse.ReachingUses();
        use.mayUseMap.put(new Var(), new Node());
        maybeReachingVariableUse.removeFromUseIfLocal("name", use);
        assertEquals(0, use.mayUseMap.size());
    }

    @Test
    public void testGetUses() {
        MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(new ControlFlowGraph<Node>(), new Scope(), new AbstractCompiler());
        Node defNode = new Node();
        MaybeReachingVariableUse.ReachingUses reachingUses = maybeReachingVariableUse.getUses("name", defNode);
        assertEquals(0, reachingUses.size());
    }
}