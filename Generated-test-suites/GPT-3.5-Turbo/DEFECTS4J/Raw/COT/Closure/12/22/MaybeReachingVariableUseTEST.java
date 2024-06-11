import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MaybeReachingVariableUseTest {

    MaybeReachingVariableUse maybeReachingVariableUse;

    @Before
    public void setUp() {
        maybeReachingVariableUse = new MaybeReachingVariableUse(null, null, null);
    }

    @Test
    public void testAddToUseIfLocal() {
        MaybeReachingVariableUse.ReachingUses reachingUses = new MaybeReachingVariableUse.ReachingUses();
        maybeReachingVariableUse.addToUseIfLocal("A", null, reachingUses);
        assertTrue(reachingUses.mayUseMap.containsKey(null));
    }

    @Test
    public void testRemoveFromUseIfLocal() {
        MaybeReachingVariableUse.ReachingUses reachingUses = new MaybeReachingVariableUse.ReachingUses();
        maybeReachingVariableUse.addToUseIfLocal("A", null, reachingUses);
        maybeReachingVariableUse.removeFromUseIfLocal("A", reachingUses);
        assertFalse(reachingUses.mayUseMap.containsKey(null));
    }

    @Test
    public void testGetUses() {
        MaybeReachingVariableUse.ReachingUses reachingUses = new MaybeReachingVariableUse.ReachingUses();
        Node defNode = new Node();
        Node useNode = new Node();
        Node anotherUseNode = new Node();
        reachingUses.mayUseMap.put(null, useNode);
        reachingUses.mayUseMap.put(null, anotherUseNode);
        Collection<Node> uses = maybeReachingVariableUse.getUses("A", defNode);
        assertEquals(uses.size(), 2);
        assertTrue(uses.contains(useNode));
        assertTrue(uses.contains(anotherUseNode));
    }
}