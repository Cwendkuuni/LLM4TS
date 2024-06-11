import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Set;

public class MaybeReachingVariableUseTest {

    private MaybeReachingVariableUse maybeReachingVariableUse;

    @Before
    public void setup() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        Scope jsScope = new Scope();
        AbstractCompiler compiler = new AbstractCompiler();
        maybeReachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, compiler);
    }

    @Test
    public void testCreateEntryLattice() {
        MaybeReachingVariableUse.ReachingUses reachingUses = maybeReachingVariableUse.createEntryLattice();
        Multimap<Var, Node> expectedMap = HashMultimap.create();

        Assert.assertEquals(expectedMap, reachingUses.mayUseMap);
    }

    @Test
    public void testCreateInitialEstimateLattice() {
        MaybeReachingVariableUse.ReachingUses reachingUses = maybeReachingVariableUse.createInitialEstimateLattice();
        Multimap<Var, Node> expectedMap = HashMultimap.create();

        Assert.assertEquals(expectedMap, reachingUses.mayUseMap);
    }

    @Test
    public void testFlowThrough() {
        Node n = new Node(Token.BLOCK);
        MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();
        input.mayUseMap.put(new Var("a"), new Node(Token.AND));

        MaybeReachingVariableUse.ReachingUses output = maybeReachingVariableUse.flowThrough(n, input);

        Multimap<Var, Node> expectedMap = HashMultimap.create();
        expectedMap.put(new Var("a"), new Node(Token.AND));

        Assert.assertEquals(expectedMap, output.mayUseMap);
    }

    @Test
    public void testGetUses() {
        Node defNode = new Node(Token.BLOCK);
        MaybeReachingVariableUse.ReachingUses reachingUses = new MaybeReachingVariableUse.ReachingUses();
        reachingUses.mayUseMap.put(new Var("a"), new Node(Token.AND));

        Collection<Node> uses = maybeReachingVariableUse.getUses("a", defNode);
        Collection<Node> expectedUses = reachingUses.mayUseMap.get(new Var("a"));

        Assert.assertEquals(expectedUses, uses);
    }

    @Test
    public void testAddToUseIfLocal() {
        String name = "a";
        Node node = new Node(Token.AND);
        MaybeReachingVariableUse.ReachingUses use = new MaybeReachingVariableUse.ReachingUses();

        maybeReachingVariableUse.addToUseIfLocal(name, node, use);

        Multimap<Var, Node> expectedMap = HashMultimap.create();
        expectedMap.put(new Var("a"), new Node(Token.AND));

        Assert.assertEquals(expectedMap, use.mayUseMap);
    }

    @Test
    public void testRemoveFromUseIfLocal() {
        String name = "a";
        MaybeReachingVariableUse.ReachingUses use = new MaybeReachingVariableUse.ReachingUses();
        use.mayUseMap.put(new Var("a"), new Node(Token.AND));

        maybeReachingVariableUse.removeFromUseIfLocal(name, use);

        Multimap<Var, Node> expectedMap = HashMultimap.create();

        Assert.assertEquals(expectedMap, use.mayUseMap);
    }

}