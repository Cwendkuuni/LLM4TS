import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

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
        Node n = new Node(Token.EXPR_RESULT);
        MaybeReachingVariableUse.ReachingUses reachingUses = new MaybeReachingVariableUse.ReachingUses();
        MaybeReachingVariableUse.ReachingUses output = new MaybeReachingVariableUse.ReachingUses();
        boolean conditional = false;

        MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(null, null, null);

        MaybeReachingVariableUse.ReachingUses result = maybeReachingVariableUse.flowThrough(n, reachingUses);
        assertNotNull(result);
    }

    @Test
    public void testGetUses() {
        String name = "A";
        Node defNode = new Node(Token.NAME);
        MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(null, null, null);
        Collection<Node> uses = maybeReachingVariableUse.getUses(name, defNode);
        assertNotNull(uses);
    }

    @Test
    public void testApply() {
        List<MaybeReachingVariableUse.ReachingUses> from = null;
        MaybeReachingVariableUse.ReachingUsesJoinOp reachingUsesJoinOp = new MaybeReachingVariableUse.ReachingUsesJoinOp();
        MaybeReachingVariableUse.ReachingUses result = reachingUsesJoinOp.apply(from);
        assertNotNull(result);
    }
}