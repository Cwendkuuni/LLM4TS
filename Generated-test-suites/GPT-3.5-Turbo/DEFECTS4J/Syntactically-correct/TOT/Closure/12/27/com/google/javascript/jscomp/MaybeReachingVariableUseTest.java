import org.junit.Assert;
import org.junit.Test;

public class MaybeReachingVariableUseTest {

    @Test
    public void testCreateEntryLattice() {
        MaybeReachingVariableUse.ReachingUses uses = new MaybeReachingVariableUse.ReachingUses();
        Assert.assertNotNull(uses);
    }

    @Test
    public void testCreateInitialEstimateLattice() {
        MaybeReachingVariableUse.ReachingUses uses = new MaybeReachingVariableUse.ReachingUses();
        Assert.assertNotNull(uses);
    }

    @Test
    public void testIsForward() {
        MaybeReachingVariableUse variableUse = new MaybeReachingVariableUse(null, null, null);
        boolean isForward = variableUse.isForward();
        Assert.assertEquals(false, isForward);
    }

    @Test
    public void testFlowThrough() {
        MaybeReachingVariableUse variableUse = new MaybeReachingVariableUse(null, null, null);
        MaybeReachingVariableUse.ReachingUses input = variableUse.createEntryLattice();
        Node n = new Node(Token.BLOCK);
        MaybeReachingVariableUse.ReachingUses output = variableUse.flowThrough(n, input);
        Assert.assertNotNull(output);
    }

    @Test
    public void testGetUses() {
        MaybeReachingVariableUse variableUse = new MaybeReachingVariableUse(null, null, null);
        Node defNode = new Node(Token.BLOCK);
        Collection<Node> uses = variableUse.getUses("name", defNode);
        Assert.assertNotNull(uses);
    }

    @Test
    public void testAddToUseIfLocal() {
        MaybeReachingVariableUse.ReachingUses uses = new MaybeReachingVariableUse.ReachingUses();
        Node node = new Node(Token.BLOCK);
        MaybeReachingVariableUse variableUse = new MaybeReachingVariableUse(null, null, null);
        variableUse.addToUseIfLocal("name", node, uses);
        Assert.assertEquals(1, uses.mayUseMap.size());
    }

    @Test
    public void testRemoveFromUseIfLocal() {
        MaybeReachingVariableUse.ReachingUses uses = new MaybeReachingVariableUse.ReachingUses();
        MaybeReachingVariableUse variableUse = new MaybeReachingVariableUse(null, null, null);
        variableUse.addToUseIfLocal("name", new Node(Token.BLOCK), uses);
        variableUse.removeFromUseIfLocal("name", uses);
        Assert.assertEquals(0, uses.mayUseMap.size());
    }

    @Test
    public void testComputeMayUse() {
        MaybeReachingVariableUse.ReachingUses uses = new MaybeReachingVariableUse.ReachingUses();
        Node node = new Node(Token.BLOCK);
        MaybeReachingVariableUse variableUse = new MaybeReachingVariableUse(null, null, null);
        variableUse.computeMayUse(node, node, uses, false);
        Assert.assertNotNull(uses);
    }

    @Test
    public void testHasExceptionHandler() {
        MaybeReachingVariableUse variableUse = new MaybeReachingVariableUse(null, null, null);
        boolean hasExceptionHandler = variableUse.hasExceptionHandler(new Node(Token.BLOCK));
        Assert.assertEquals(false, hasExceptionHandler);
    }

    @Test
    public void testReachingUsesEquals() {
        MaybeReachingVariableUse.ReachingUses uses1 = new MaybeReachingVariableUse.ReachingUses();
        MaybeReachingVariableUse.ReachingUses uses2 = new MaybeReachingVariableUse.ReachingUses();
        Assert.assertEquals(true, uses1.equals(uses2));
    }

    @Test
    public void testReachingUsesHashCode() {
        MaybeReachingVariableUse.ReachingUses uses = new MaybeReachingVariableUse.ReachingUses();
        Assert.assertEquals(uses.mayUseMap.hashCode(), uses.hashCode());
    }

    @Test
    public void testReachingUsesJoinOp() {
        MaybeReachingVariableUse.ReachingUses uses1 = new MaybeReachingVariableUse.ReachingUses();
        MaybeReachingVariableUse.ReachingUses uses2 = new MaybeReachingVariableUse.ReachingUses();
        MaybeReachingVariableUse.ReachingUsesJoinOp joinOp = new MaybeReachingVariableUse.ReachingUsesJoinOp();
        MaybeReachingVariableUse.ReachingUses result = joinOp.apply(Arrays.asList(uses1, uses2));
        Assert.assertNotNull(result);
    }

}