import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Mock;

public class MaybeReachingVariableUseTest {
    
    @Mock
    private ControlFlowGraph<Node> cfg;
    
    @Mock
    private Scope jsScope;
    
    @Mock
    private AbstractCompiler compiler;
    
    @Test
    public void testIsForward() {
        MaybeReachingVariableUse reachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, compiler);
        boolean isForward = reachingVariableUse.isForward();
        assertFalse(isForward);
    }
    
    @Test
    public void testCreateEntryLattice() {
        MaybeReachingVariableUse reachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, compiler);
        MaybeReachingVariableUse.ReachingUses entryLattice = reachingVariableUse.createEntryLattice();
        assertNotNull(entryLattice);
    }
    
    @Test
    public void testCreateInitialEstimateLattice() {
        MaybeReachingVariableUse reachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, compiler);
        MaybeReachingVariableUse.ReachingUses initialEstimateLattice = reachingVariableUse.createInitialEstimateLattice();
        assertNotNull(initialEstimateLattice);
    }
    
    @Test
    public void testFlowThrough() {
        MaybeReachingVariableUse reachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, compiler);
        Node n = mock(Node.class);
        MaybeReachingVariableUse.ReachingUses input = mock(MaybeReachingVariableUse.ReachingUses.class);
        MaybeReachingVariableUse.ReachingUses output = reachingVariableUse.flowThrough(n, input);
        assertNotNull(output);
    }
    
    @Test
    public void testGetUses() {
        MaybeReachingVariableUse reachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, compiler);
        String name = "test";
        Node defNode = mock(Node.class);
        Collection<Node> uses = reachingVariableUse.getUses(name, defNode);
        assertNotNull(uses);
    }
}