import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.graph.DiGraph.DiGraphEdge;
import com.google.javascript.jscomp.graph.GraphNode;
import com.google.javascript.jscomp.graph.LatticeElement;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class MaybeReachingVariableUseTest {

    private MaybeReachingVariableUse maybeReachingVariableUse;

    @Before
    public void setUp() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>();
        Scope jsScope = new Scope();
        AbstractCompiler compiler = new AbstractCompiler();
        maybeReachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, compiler);
    }

    @Test
    public void testFlowThrough() {
        Node n = new Node(Token.BLOCK);
        MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();
        MaybeReachingVariableUse.ReachingUses output = maybeReachingVariableUse.flowThrough(n, input);
        
        assertEquals(input, output);
    }

    @Test
    public void testCreateEntryLattice() {
        MaybeReachingVariableUse.ReachingUses lattice = maybeReachingVariableUse.createEntryLattice();
        assertNotNull(lattice);
    }

    @Test
    public void testCreateInitialEstimateLattice() {
        MaybeReachingVariableUse.ReachingUses lattice = maybeReachingVariableUse.createInitialEstimateLattice();
        assertNotNull(lattice);
    }

    @Test
    public void testGetUses() {
        String name = "test";
        Node defNode = new Node(Token.BLOCK);
        Collection<Node> uses = maybeReachingVariableUse.getUses(name, defNode);
        assertNotNull(uses);
    }
}