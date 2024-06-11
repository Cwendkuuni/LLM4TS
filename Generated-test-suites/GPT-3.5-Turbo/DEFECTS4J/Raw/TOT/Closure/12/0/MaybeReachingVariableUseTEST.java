import static org.junit.Assert.*;

import com.google.javascript.jscomp.*;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Set;

// Test Class for MaybeReachingVariableUse
public class MaybeReachingVariableUseTest {

    private MaybeReachingVariableUse reachingVariableUse;
    private ControlFlowGraph<Node> cfg;
    private Scope jsScope;
    private AbstractCompiler compiler;
    
    @Before
    public void setUp() {
        jsScope = new Scope();
        cfg = new ControlFlowGraph<>(new DiGraph<Node, Branch>());
        compiler = new AbstractCompiler();
        reachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, compiler);
    }

    @Test
    public void testCreateEntryLattice() {
        MaybeReachingVariableUse.ReachingUses reachingUses = reachingVariableUse.createEntryLattice();
        assertNotNull(reachingUses);
    }

    @Test
    public void testCreateInitialEstimateLattice() {
        MaybeReachingVariableUse.ReachingUses reachingUses = reachingVariableUse.createInitialEstimateLattice();
        assertNotNull(reachingUses);
    }

    @Test
    public void testFlowThrough() {
        MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();
        MaybeReachingVariableUse.ReachingUses output = reachingVariableUse.flowThrough(new Node(), input);
        assertNotNull(output);
    }

    @Test
    public void testGetUses() {
        Node defNode = new Node();
        Collection<Node> uses = reachingVariableUse.getUses("variableName", defNode);
        assertNotNull(uses);
    }

}