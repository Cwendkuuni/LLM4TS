import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.AbstractCompiler;

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
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>();
        Scope jsScope = new Scope();
        AbstractCompiler compiler = new AbstractCompiler();
        MaybeReachingVariableUse reachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, compiler);

        Node n = new Node(Token.BLOCK);
        MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();
        MaybeReachingVariableUse.ReachingUses output = reachingVariableUse.flowThrough(n, input);
        assertNotNull(output);
    }

    @Test
    public void testGetUses() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>();
        Scope jsScope = new Scope();
        AbstractCompiler compiler = new AbstractCompiler();
        MaybeReachingVariableUse reachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, compiler);

        Node defNode = new Node(Token.NAME);
        Collection<Node> uses = reachingVariableUse.getUses("testVar", defNode);
        assertNotNull(uses);
    }
}