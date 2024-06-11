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
        Node node = new Node(Token.BLOCK);
        MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();
        MaybeReachingVariableUse analyzer = new MaybeReachingVariableUse(cfg, jsScope, compiler);
        MaybeReachingVariableUse.ReachingUses output = analyzer.flowThrough(node, input);
        assertNotNull(output);
    }

    @Test
    public void testGetUses() {
        Node defNode = new Node(Token.BLOCK);
        MaybeReachingVariableUse analyzer = new MaybeReachingVariableUse(cfg, jsScope, compiler);
        Collection<Node> uses = analyzer.getUses("name", defNode);
        assertNotNull(uses);
    }

}