
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.graph.DiGraph.DiGraphEdge;
import com.google.javascript.jscomp.graph.GraphNode;
import com.google.javascript.jscomp.graph.LatticeElement;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Set;

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
    public void testCreateEntryLattice() {
        MaybeReachingVariableUse.ReachingUses entryLattice = maybeReachingVariableUse.createEntryLattice();
        Assert.assertNotNull(entryLattice);
    }

    @Test
    public void testCreateInitialEstimateLattice() {
        MaybeReachingVariableUse.ReachingUses initialEstimateLattice = maybeReachingVariableUse.createInitialEstimateLattice();
        Assert.assertNotNull(initialEstimateLattice);
    }

    @Test
    public void testFlowThrough() {
        Node n = new Node(Token.NAME);
        Node cfgNode = new Node(Token.NAME);
        MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();
        boolean conditional = true;
        MaybeReachingVariableUse.ReachingUses output = maybeReachingVariableUse.flowThrough(n, input);
        Assert.assertNotNull(output);
    }

    @Test
    public void testGetUses() {
        String name = "testVariable";
        Node defNode = new Node(Token.NAME);
        Collection<Node> uses = maybeReachingVariableUse.getUses(name, defNode);
        Assert.assertNotNull(uses);
    }
}
