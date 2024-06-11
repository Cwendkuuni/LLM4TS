
import com.google.javascript.jscomp.*;

import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.assertEquals;

public class MaybeReachingVariableUseTest {

    private ControlFlowGraph<Node> cfg;
    private Scope jsScope;
    private AbstractCompiler compiler;

    @Before
    public void setup() {
        cfg = new ControlFlowGraph<>();
        jsScope = new Scope();
        compiler = new AbstractCompiler();
    }

    @Test
    public void testMaybeReachingVariableUse() {
        MaybeReachingVariableUse reachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, compiler);

        // Test createEntryLattice()
        MaybeReachingVariableUse.ReachingUses entryLattice = reachingVariableUse.createEntryLattice();
        assertEquals(0, entryLattice.mayUseMap.size());

        // Test createInitialEstimateLattice()
        MaybeReachingVariableUse.ReachingUses initialEstimateLattice = reachingVariableUse.createInitialEstimateLattice();
        assertEquals(0, initialEstimateLattice.mayUseMap.size());

        // Test isForward()
        assertEquals(false, reachingVariableUse.isForward());

        // Test flowThrough()
        Node n = new Node(Token.NAME, "name");
        MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();
        MaybeReachingVariableUse.ReachingUses output = reachingVariableUse.flowThrough(n, input);
        assertEquals(input.mayUseMap, output.mayUseMap);

        // Test getUses()
        reachingVariableUse.getUses("name", n);
    }
}
