import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;

import java.util.BitSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class LiveVariablesAnalysisTest {

    private LiveVariablesAnalysis analysis;
    private ControlFlowGraph<Node> cfgMock;
    private Scope jsScopeMock;
    private AbstractCompiler compilerMock;
    
    @Before
    public void setUp() {
        cfgMock = mock(ControlFlowGraph.class);
        jsScopeMock = mock(Scope.class);
        compilerMock = mock(AbstractCompiler.class);
        analysis = new LiveVariablesAnalysis(cfgMock, jsScopeMock, compilerMock);
    }

    @Test
    public void testGetEscapedLocals() {
        Set<Var> escaped = analysis.getEscapedLocals();
        assertTrue(escaped.isEmpty());
    }

    @Test
    public void testGetVarIndex() {
        when(jsScopeMock.getVar("x")).thenReturn(new Var());
        int index = analysis.getVarIndex("x");
        assertEquals(0, index);
    }
    
    @Test
    public void testIsForward() {
        boolean isForward = analysis.isForward();
        assertFalse(isForward);
    }
    
    @Test
    public void testCreateEntryLattice() {
        LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.createEntryLattice();
        BitSet liveSet = new BitSet();
        assertEquals(liveSet, lattice.liveSet);
    }
    
    @Test
    public void testCreateInitialEstimateLattice() {
        LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.createInitialEstimateLattice();
        BitSet liveSet = new BitSet();
        assertEquals(liveSet, lattice.liveSet);
    }
    
    @Test
    public void testFlowThrough() {
        Node nodeMock = mock(Node.class);
        LiveVariablesAnalysis.LiveVariableLattice inputMock = mock(LiveVariablesAnalysis.LiveVariableLattice.class);
        BitSet gen = new BitSet();
        BitSet kill = new BitSet();
        when(cfgMock.getOutEdges(any(Node.class))).thenReturn(null);
        when(inputMock.liveSet.size()).thenReturn(0);
        when(nodeMock.getType()).thenReturn(Token.SCRIPT);
        LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(nodeMock, inputMock);
        assertEquals(inputMock, result);
    }
    
    @Test
    public void testComputeGenKill() {
        Node nodeMock = mock(Node.class);
        BitSet gen = new BitSet();
        BitSet kill = new BitSet();
        when(nodeMock.getType()).thenReturn(Token.SCRIPT);
        analysis.computeGenKill(nodeMock, gen, kill, false);
        assertTrue(gen.isEmpty());
        assertTrue(kill.isEmpty());
    }
    
    @Test
    public void testAddToSetIfLocal() {
        Node nodeMock = mock(Node.class);
        BitSet setMock = mock(BitSet.class);
        when(nodeMock.getString()).thenReturn("x");
        when(jsScopeMock.isDeclared("x", false)).thenReturn(true);
        when(jsScopeMock.getVar("x")).thenReturn(new Var());
        when(setMock.get(anyInt())).thenReturn(true);
        analysis.addToSetIfLocal(nodeMock, setMock);
        assertTrue(setMock.get(0));
    }
    
    @Test
    public void testMarkAllParametersEscaped() {
        analysis.markAllParametersEscaped();
        Set<Var> escaped = analysis.getEscapedLocals();
        assertFalse(escaped.isEmpty());
    }
    
    @Test
    public void testIsArgumentsName() {
        Node nodeMock = mock(Node.class);
        when(nodeMock.getType()).thenReturn(Token.NAME);
        when(nodeMock.getString()).thenReturn(LiveVariablesAnalysis.ARGUMENT_ARRAY_ALIAS);
        when(jsScopeMock.isDeclared(LiveVariablesAnalysis.ARGUMENT_ARRAY_ALIAS, false)).thenReturn(false);

        boolean isArgumentsName = analysis.isArgumentsName(nodeMock);
        assertTrue(isArgumentsName);
    }
}