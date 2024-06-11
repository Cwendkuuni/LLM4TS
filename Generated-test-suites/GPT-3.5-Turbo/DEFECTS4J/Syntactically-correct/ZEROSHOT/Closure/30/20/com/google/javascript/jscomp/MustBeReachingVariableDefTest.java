import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class MustBeReachingVariableDefTest {

    @Mock
    private Scope jsScope;

    @Mock
    private AbstractCompiler compiler;

    @Mock
    private ControlFlowGraph<Node> cfg;

    @Mock
    private Node node;

    @Before
    public void setup() {
        jsScope = mock(Scope.class);
        compiler = mock(AbstractCompiler.class);
        cfg = mock(ControlFlowGraph.class);
        node = mock(Node.class);
    }

    @Test
    public void testCreateEntryLattice() {
        MustBeReachingVariableDef.MustDef expected = new MustBeReachingVariableDef.MustDef();
        // TODO: Set expected values
        MustBeReachingVariableDef.MustDef actual = new MustBeReachingVariableDef(cfg, jsScope, compiler).createEntryLattice();
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateInitialEstimateLattice() {
        MustBeReachingVariableDef.MustDef expected = new MustBeReachingVariableDef.MustDef();
        // TODO: Set expected values
        MustBeReachingVariableDef.MustDef actual = new MustBeReachingVariableDef(cfg, jsScope, compiler).createInitialEstimateLattice();
        assertEquals(expected, actual);
    }

    @Test
    public void testFlowThrough() {
        MustBeReachingVariableDef.MustDef input = new MustBeReachingVariableDef.MustDef();
        MustBeReachingVariableDef.MustDef expected = new MustBeReachingVariableDef.MustDef();
        // TODO: Set expected values
        MustBeReachingVariableDef.MustDef actual = new MustBeReachingVariableDef(cfg, jsScope, compiler).flowThrough(node, input);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDef() {
        String name = "";
        Node useNode = mock(Node.class);
        Node expected = mock(Node.class);
        when(cfg.hasNode(useNode)).thenReturn(true);
        FlowState<MustBeReachingVariableDef.MustDef> state = mock(FlowState.class);
        when(cfg.getNode(useNode)).thenReturn(new GraphNode<>(expected, state));
        MustBeReachingVariableDef.MustDef mustDef = new MustBeReachingVariableDef.MustDef();
        mustDef.reachingDef.put(mock(Var.class), new MustBeReachingVariableDef.Definition(expected));
        when(state.getIn()).thenReturn(mustDef);
        Node actual = new MustBeReachingVariableDef(cfg, jsScope, compiler).getDef(name, useNode);
        assertEquals(expected, actual);
    }

    @Test
    public void testDependsOnOuterScopeVars() {
        String name = "";
        Node useNode = mock(Node.class);
        when(cfg.hasNode(useNode)).thenReturn(true);
        FlowState<MustBeReachingVariableDef.MustDef> state = mock(FlowState.class);
        when(cfg.getNode(useNode)).thenReturn(new GraphNode<>(useNode, state));
        MustBeReachingVariableDef.MustDef mustDef = new MustBeReachingVariableDef.MustDef();
        mustDef.reachingDef.put(mock(Var.class), new MustBeReachingVariableDef.Definition(useNode));
        when(state.getIn()).thenReturn(mustDef);
        boolean actual = new MustBeReachingVariableDef(cfg, jsScope, compiler).dependsOnOuterScopeVars(name, useNode);
        assertTrue(actual);
    }

}