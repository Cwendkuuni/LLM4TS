import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class MaybeReachingVariableUseTest {
    
    MaybeReachingVariableUse maybeReachingVariableUse;
    ControlFlowGraph<Node> cfg;
    Scope jsScope;
    AbstractCompiler compiler;
    
    @Before
    public void setup() {
        cfg = new ControlFlowGraph<>();
        jsScope = new Scope();
        compiler = new AbstractCompiler();
        maybeReachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, compiler);
    }
    
    @Test
    public void testCreateEntryLattice() {
        MaybeReachingVariableUse.ReachingUses reachingUses = maybeReachingVariableUse.createEntryLattice();
        Assert.assertNotNull(reachingUses);
        Assert.assertTrue(reachingUses.mayUseMap.isEmpty());
    }
    
    @Test
    public void testCreateInitialEstimateLattice() {
        MaybeReachingVariableUse.ReachingUses reachingUses = maybeReachingVariableUse.createInitialEstimateLattice();
        Assert.assertNotNull(reachingUses);
        Assert.assertTrue(reachingUses.mayUseMap.isEmpty());
    }
    
    @Test
    public void testIsForward() {
        boolean result = maybeReachingVariableUse.isForward();
        Assert.assertFalse(result);
    }
    
    @Test
    public void testFlowThrough() {
        Node n = new Node();
        MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();
        
        MaybeReachingVariableUse.ReachingUses output = maybeReachingVariableUse.flowThrough(n, input);
        Assert.assertNotNull(output);
        Assert.assertEquals(input.mayUseMap, output.mayUseMap);
    }
    
    @Test
    public void testGetUses() {
        String name = "test";
        Node defNode = new Node();
        Set<Node> expectedResult = new HashSet<>();
        MaybeReachingVariableUse.ReachingUses reachingUses = new MaybeReachingVariableUse.ReachingUses();
        reachingUses.mayUseMap.put(new Var(name), defNode);
        
        MaybeReachingVariableUse.ReachingUses.FlowState<MaybeReachingVariableUse.ReachingUses> state = new MaybeReachingVariableUse.ReachingUses.FlowState<>(reachingUses);
        MaybeReachingVariableUse.ReachingUses.FlowState<MaybeReachingVariableUse.ReachingUses> annotation = new MaybeReachingVariableUse.ReachingUses.FlowState<>(reachingUses);
        
        MaybeReachingVariableUse.ReachingUses.FlowState<MaybeReachingVariableUse.ReachingUses> annotation.setOut(annotation.getOut());
        MaybeReachingVariableUse.ReachingUses.FlowState<MaybeReachingVariableUse.ReachingUses> annotation.setIn(annotation.getOut());
        MaybeReachingVariableUse.ReachingUses.FlowState<MaybeReachingVariableUse.ReachingUses> annotation.setAnnotation(annotation.getOut());
        
        
        MaybeReachingVariableUse.ReachingUses.FlowState<MaybeReachingVariableUse.ReachingUses> annotation.setIn(state.getOut());
        MaybeReachingVariableUse.ReachingUses.FlowState<MaybeReachingVariableUse.ReachingUses> annotation.setAnnotation(state.getOut());
        
        MaybeReachingVariableUse.ReachingUses use = maybeReachingVariableUse.getUses(name, defNode);
        Assert.assertEquals(reachingUses.mayUseMap.get(new Var(name)), use);
    }
}