import static org.junit.Assert.*;
import org.junit.Test;

public class MaybeReachingVariableUseTest {

    @Test
    public void testCreateEntryLattice() {
        MaybeReachingVariableUse.ReachingUses reachingUses = new MaybeReachingVariableUse.ReachingUses();
        assertNotNull(reachingUses);
    }

    @Test
    public void testCreateInitialEstimateLattice() {
        MaybeReachingVariableUse.ReachingUses reachingUses = new MaybeReachingVariableUse.ReachingUses();
        assertNotNull(reachingUses);
    }

    @Test
    public void testFlowThrough() {
        MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse();
        MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();
        input.mayUseMap.put(var, node);
        MaybeReachingVariableUse.ReachingUses output = maybeReachingVariableUse.flowThrough(node, input);
        assertNotNull(output);
    }

    @Test
    public void testGetUses() {
        MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse();
        String name = "testName";
        Node defNode = new Node(Token.STRING);
        Collection<Node> uses = maybeReachingVariableUse.getUses(name, defNode);
        assertNotNull(uses);
    }
}