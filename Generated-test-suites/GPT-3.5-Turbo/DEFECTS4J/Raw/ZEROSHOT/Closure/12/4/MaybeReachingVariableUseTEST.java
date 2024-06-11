import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.*;

public class MaybeReachingVariableUseTest {

    private ControlFlowGraph<Node> cfg;
    private Scope jsScope;
    private AbstractCompiler compiler;

    @Before
    public void setUp() {
        cfg = new ControlFlowGraph<>();
        jsScope = new Scope();
        compiler = new AbstractCompiler();
    }

    @Test
    public void testFlowThrough() {
        Node n = new Node(Token.BLOCK);
        MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();
        MaybeReachingVariableUse.ReachingUses expectedOutput = new MaybeReachingVariableUse.ReachingUses();

        MaybeReachingVariableUse instance = new MaybeReachingVariableUse(cfg, jsScope, compiler);

        MaybeReachingVariableUse.ReachingUses result = instance.flowThrough(n, input);

        assertEquals(expectedOutput, result);
    }

    @Test
    public void testIsForward() {
        MaybeReachingVariableUse instance = new MaybeReachingVariableUse(cfg, jsScope, compiler);

        boolean result = instance.isForward();

        assertEquals(false, result);
    }

    @Test
    public void testCreateEntryLattice() {
        MaybeReachingVariableUse instance = new MaybeReachingVariableUse(cfg, jsScope, compiler);

        MaybeReachingVariableUse.ReachingUses result = instance.createEntryLattice();

        MaybeReachingVariableUse.ReachingUses expectedLattice = new MaybeReachingVariableUse.ReachingUses();

        assertEquals(expectedLattice, result);
    }

    @Test
    public void testCreateInitialEstimateLattice() {
        MaybeReachingVariableUse instance = new MaybeReachingVariableUse(cfg, jsScope, compiler);

        MaybeReachingVariableUse.ReachingUses result = instance.createInitialEstimateLattice();

        MaybeReachingVariableUse.ReachingUses expectedLattice = new MaybeReachingVariableUse.ReachingUses();

        assertEquals(expectedLattice, result);
    }

    @Test
    public void testComputeMayUse() {
        Node n = new Node(Token.BLOCK);
        Node cfgNode = new Node();
        MaybeReachingVariableUse.ReachingUses output = new MaybeReachingVariableUse.ReachingUses();
        boolean conditional = false;

        MaybeReachingVariableUse instance = new MaybeReachingVariableUse(cfg, jsScope, compiler);

        instance.computeMayUse(n, cfgNode, output, conditional);

        MaybeReachingVariableUse.ReachingUses expectedOutput = new MaybeReachingVariableUse.ReachingUses();

        assertEquals(expectedOutput, output);
    }

    @Test
    public void testAddToUseIfLocal() {
        String name = "testName";
        Node node = new Node();
        MaybeReachingVariableUse.ReachingUses use = new MaybeReachingVariableUse.ReachingUses();

        MaybeReachingVariableUse instance = new MaybeReachingVariableUse(cfg, jsScope, compiler);

        instance.addToUseIfLocal(name, node, use);

        MaybeReachingVariableUse.ReachingUses expectedUse = new MaybeReachingVariableUse.ReachingUses();

        assertEquals(expectedUse, use);
    }

    @Test
    public void testRemoveFromUseIfLocal() {
        String name = "testName";
        MaybeReachingVariableUse.ReachingUses use = new MaybeReachingVariableUse.ReachingUses();

        MaybeReachingVariableUse instance = new MaybeReachingVariableUse(cfg, jsScope, compiler);

        instance.removeFromUseIfLocal(name, use);

        MaybeReachingVariableUse.ReachingUses expectedUse = new MaybeReachingVariableUse.ReachingUses();

        assertEquals(expectedUse, use);
    }

    @Test
    public void testGetUses() {
        String name = "testName";
        Node defNode = new Node();

        MaybeReachingVariableUse instance = new MaybeReachingVariableUse(cfg, jsScope, compiler);

        Collection<Node> result = instance.getUses(name, defNode);

        Collection<Node> expectedUses = null;

        assertEquals(expectedUses, result);
    }
}