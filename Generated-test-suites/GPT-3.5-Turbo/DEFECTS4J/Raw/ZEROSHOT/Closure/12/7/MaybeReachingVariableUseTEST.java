import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MaybeReachingVariableUseTest {

    @Test
    public void testFlowThrough() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>();
        MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();
        Node n = new Node(Token.BLOCK);

        MaybeReachingVariableUse flow = new MaybeReachingVariableUse(cfg, null, null);
        MaybeReachingVariableUse.ReachingUses output = flow.flowThrough(n, input);

        assertEquals(output, input);
    }

    @Test
    public void testCreateEntryLattice() {
        MaybeReachingVariableUse.ReachingUses entryLattice = new MaybeReachingVariableUse().createEntryLattice();

        MaybeReachingVariableUse.ReachingUses expected = new MaybeReachingVariableUse.ReachingUses();

        assertEquals(expected, entryLattice);
    }

    @Test
    public void testCreateInitialEstimateLattice() {
        MaybeReachingVariableUse.ReachingUses initialEstimateLattice = new MaybeReachingVariableUse().createInitialEstimateLattice();

        MaybeReachingVariableUse.ReachingUses expected = new MaybeReachingVariableUse.ReachingUses();

        assertEquals(expected, initialEstimateLattice);
    }

    @Test
    public void testFlowThrough() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>();
        MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();
        Node n = new Node(Token.BLOCK);

        MaybeReachingVariableUse flow = new MaybeReachingVariableUse(cfg, null, null);
        MaybeReachingVariableUse.ReachingUses output = flow.flowThrough(n, input);

        assertEquals(output, input);
    }

    @Test
    public void testGetUses() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>();
        MaybeReachingVariableUse flow = new MaybeReachingVariableUse(cfg, null, null);
        Node name = new Node(Token.NAME, "A");
        Node defNode = new Node(Token.BLOCK);

        Collection<Node> uses = flow.getUses("A", defNode);

        assertEquals(0, uses.size());
    }
}