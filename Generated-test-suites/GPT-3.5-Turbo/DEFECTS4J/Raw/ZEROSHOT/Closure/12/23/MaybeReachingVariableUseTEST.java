import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MaybeReachingVariableUseTest {

    private MaybeReachingVariableUse maybeReachingVariableUse;

    @Before
    public void setUp() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        Scope jsScope = new Scope();
        AbstractCompiler compiler = new AbstractCompiler();
        maybeReachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, compiler);
    }

    @Test
    public void testIsForward() {
        boolean isForward = maybeReachingVariableUse.isForward();
        assertEquals(false, isForward);
    }

    @Test
    public void testCreateEntryLattice() {
        MaybeReachingVariableUse.ReachingUses entryLattice = maybeReachingVariableUse.createEntryLattice();
        MaybeReachingVariableUse.ReachingUses expected = new MaybeReachingVariableUse.ReachingUses();
        assertEquals(expected, entryLattice);
    }

    @Test
    public void testCreateInitialEstimateLattice() {
        MaybeReachingVariableUse.ReachingUses initialEstimateLattice = maybeReachingVariableUse.createInitialEstimateLattice();
        MaybeReachingVariableUse.ReachingUses expected = new MaybeReachingVariableUse.ReachingUses();
        assertEquals(expected, initialEstimateLattice);
    }

    @Test
    public void testFlowThrough() {
        Node n = new Node(Token.EMPTY);
        MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();
        MaybeReachingVariableUse.ReachingUses output = maybeReachingVariableUse.flowThrough(n, input);
        MaybeReachingVariableUse.ReachingUses expected = new MaybeReachingVariableUse.ReachingUses(input);
        assertEquals(expected, output);
    }

    @Test
    public void testGetUses() {
        String name = "testVariable";
        Node defNode = new Node(Token.EMPTY);
        Collection<Node> uses = maybeReachingVariableUse.getUses(name, defNode);
        Collection<Node> expected = null;
        assertEquals(expected, uses);
    }
}