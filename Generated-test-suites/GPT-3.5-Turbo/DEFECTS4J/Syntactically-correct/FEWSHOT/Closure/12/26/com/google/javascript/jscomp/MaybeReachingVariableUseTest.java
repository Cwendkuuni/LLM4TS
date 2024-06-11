
package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Multimap;
import com.google.javascript.jscomp.MaybeReachingVariableUse.ReachingUses;
import com.google.javascript.rhino.Node;


public class MaybeReachingVariableUseTest {

    private MaybeReachingVariableUse reachingVariableUse;

    private ControlFlowGraph<Node> cfg;
    private Scope jsScope;
    private AbstractCompiler compiler;

    @Before
    public void setUp() {
        jsScope = mock(Scope.class);
        cfg = mock(ControlFlowGraph.class);
        compiler = mock(AbstractCompiler.class);
        reachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, compiler);
    }

    @Test
    public void testIsForward() {
        boolean isForward = reachingVariableUse.isForward();
        assertEquals(false, isForward);
    }

    @Test
    public void testCreateEntryLattice() {
        ReachingUses entryLattice = reachingVariableUse.createEntryLattice();
        Multimap<Var, Node> mayUseMap = entryLattice.mayUseMap;
        assertEquals(0, mayUseMap.keys().size());
        assertEquals(0, mayUseMap.values().size());
    }

    @Test
    public void testCreateInitialEstimateLattice() {
        ReachingUses initialEstimateLattice = reachingVariableUse.createInitialEstimateLattice();
        Multimap<Var, Node> mayUseMap = initialEstimateLattice.mayUseMap;
        assertEquals(0, mayUseMap.keys().size());
        assertEquals(0, mayUseMap.values().size());
    }

    @Test
    public void testFlowThrough() {
        Node n = mock(Node.class);
        ReachingUses input = mock(ReachingUses.class);

        ReachingUses output = reachingVariableUse.flowThrough(n, input);

        assertEquals(input.mayUseMap, output.mayUseMap);
    }

    @Test
    public void testGetUses() {
        String name = "test";
        Node defNode = mock(Node.class);

        Multimap<Var, Node> mayUseMap = mock(Multimap.class);
        ReachingUses reachingUses = new ReachingUses();
        reachingUses.mayUseMap = mayUseMap;

        GraphNode<Node, Branch> graphNode = mock(GraphNode.class);
        when(cfg.getNode(defNode)).thenReturn(graphNode);
        FlowState<ReachingUses> state = mock(FlowState.class);
        when(graphNode.getAnnotation()).thenReturn(state);
        when(state.getOut()).thenReturn(reachingUses);

        Collection<Node> uses = reachingVariableUse.getUses(name, defNode);

        assertEquals(mayUseMap, uses);
    }

}
