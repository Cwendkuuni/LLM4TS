
package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MaybeReachingVariableUseTest {

    @Test
    public void testFlowThrough() {
        ControlFlowGraph<Node> cfg = Mockito.mock(ControlFlowGraph.class);
        Scope jsScope = Mockito.mock(Scope.class);
        AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
        MaybeReachingVariableUse testObject = new MaybeReachingVariableUse(cfg, jsScope, compiler);
        
        Node n = Mockito.mock(Node.class);
        MaybeReachingVariableUse.ReachingUses input = Mockito.mock(MaybeReachingVariableUse.ReachingUses.class);
        
        MaybeReachingVariableUse.ReachingUses output = testObject.flowThrough(n, input);
        
        // assert statements
    }

    @Test
    public void testCreateEntryLattice() {
        MaybeReachingVariableUse testObject = new MaybeReachingVariableUse(null, null, null);
        
        MaybeReachingVariableUse.ReachingUses result = testObject.createEntryLattice();
        
        // assert statements
    }

    @Test
    public void testCreateInitialEstimateLattice() {
        MaybeReachingVariableUse testObject = new MaybeReachingVariableUse(null, null, null);
        
        MaybeReachingVariableUse.ReachingUses result = testObject.createInitialEstimateLattice();
        
        // assert statements
    }

    @Test
    public void testGetUses() {
        MaybeReachingVariableUse testObject = new MaybeReachingVariableUse(null, null, null);
        
        Collection<Node> result = testObject.getUses("", null);
        
        // assert statements
    }
}
