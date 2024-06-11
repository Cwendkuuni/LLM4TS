
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

import java.util.BitSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;

public class LiveVariablesAnalysisTest {
    private LiveVariablesAnalysis liveVariablesAnalysis;
    private ControlFlowGraph<Node> cfg;
    private Scope jsScope;
    private AbstractCompiler compiler;

    @Before
    public void setup() {
        cfg = mock(ControlFlowGraph.class);
        jsScope = mock(Scope.class);
        compiler = mock(AbstractCompiler.class);
        liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    }

    @Test
    public void testGetEscapedLocals() {
        Set<Var> escapedLocals = Sets.newHashSet();
        escapedLocals.add(new Var("var1", 0));
        escapedLocals.add(new Var("var2", 1));
        when(jsScope.getVarCount()).thenReturn(2);
        when(liveVariablesAnalysis.getEscapedLocals()).thenReturn(escapedLocals);

        Set<Var> result = liveVariablesAnalysis.getEscapedLocals();
        assertEquals(escapedLocals, result);
    }

    @Test
    public void testGetVarIndex() {
        when(jsScope.getVar("var1")).thenReturn(new Var("var1", 1));
        when(liveVariablesAnalysis.getVarIndex("var1")).thenReturn(1);

        int result = liveVariablesAnalysis.getVarIndex("var1");
        assertEquals(1, result);
    }

    @Test
    public void testIsForward() {
        boolean result = liveVariablesAnalysis.isForward();
        assertEquals(false, result);
    }

    @Test
    public void testCreateEntryLattice() {
        when(jsScope.getVarCount()).thenReturn(2);
        LiveVariablesAnalysis.LiveVariableLattice result = liveVariablesAnalysis.createEntryLattice();
        BitSet expected = new BitSet(2);
        assertEquals(expected, result.liveSet);
    }

    @Test
    public void testCreateInitialEstimateLattice() {
        when(jsScope.getVarCount()).thenReturn(2);
        LiveVariablesAnalysis.LiveVariableLattice result = liveVariablesAnalysis.createInitialEstimateLattice();
        BitSet expected = new BitSet(2);
        assertEquals(expected, result.liveSet);
    }

    @Test
    public void testFlowThrough() {
        LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(2);
        input.liveSet.set(0);

        Node node = mock(Node.class);

        BitSet gen = new BitSet(input.liveSet.size());
        BitSet kill = new BitSet(input.liveSet.size());

        BitSet expectedGen = new BitSet(input.liveSet.size());
        BitSet expectedKill = new BitSet(input.liveSet.size());
        expectedGen.set(0);
        expectedKill.set(0);

        when(cfg.getOutEdges(node)).thenReturn(null);

        LiveVariablesAnalysis.LiveVariableLattice result = liveVariablesAnalysis.flowThrough(node, input);
        assertEquals(expectedGen, gen);
        assertEquals(expectedKill, kill);
        assertEquals(expectedGen, result.liveSet);
    }

    @Test
    public void testComputeGenKill() {
        Node node = mock(Node.class);

        BitSet gen = new BitSet(2);
        BitSet kill = new BitSet(2);
        boolean conditional = false;

        BitSet expectedGen = new BitSet(2);
        BitSet expectedKill = new BitSet(2);

        LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(2);
        input.liveSet.set(0);

        liveVariablesAnalysis.computeGenKill(node, gen, kill, conditional);
        assertEquals(expectedGen, gen);
        assertEquals(expectedKill, kill);
    }

    @Test
    public void testAddToSetIfLocal() {
        Node node = mock(Node.class);
        when(node.getType()).thenReturn(Token.NAME);
        when(node.getString()).thenReturn("var1");

        Var var = new Var("var1", 0);
        when(jsScope.isDeclared("var1", false)).thenReturn(true);
        when(jsScope.getVar("var1")).thenReturn(var);

        BitSet set = new BitSet(1);
        BitSet expected = new BitSet(1);
        expected.set(0);

        liveVariablesAnalysis.addToSetIfLocal(node, set);
        assertEquals(expected, set);
    }

    @Test
    public void testMarkAllParametersEscaped() {
        Node lp = mock(Node.class);
        Node arg1 = mock(Node.class);
        Node arg2 = mock(Node.class);
        when(jsScope.getRootNode()).thenReturn(mock(Node.class));
        when(jsScope.getRootNode().getFirstChild()).thenReturn(lp);
        when(lp.getNext()).thenReturn(arg1);
        when(arg1.getFirstChild()).thenReturn(null);
        when(arg1.getNext()).thenReturn(arg2);
        when(arg2.getFirstChild()).thenReturn(null);
        when(arg2.getNext()).thenReturn(null);
        when(arg1.getString()).thenReturn("arg1");
        when(arg2.getString()).thenReturn("arg2");
        when(jsScope.getVar("arg1")).thenReturn(new Var("arg1", 0));
        when(jsScope.getVar("arg2")).thenReturn(new Var("arg2", 1));


        liveVariablesAnalysis.markAllParametersEscaped();
        Set<Var> escaped = liveVariablesAnalysis.getEscapedLocals();
        Set<Var> expected = Sets.newHashSet();
        expected.add(new Var("arg1", 0));
        expected.add(new Var("arg2", 1));
        assertEquals(expected, escaped);
    }

    @Test
    public void testIsArgumentsName() {
        Node node = mock(Node.class);
        when(node.getType()).thenReturn(Token.NAME);
        when(node.getString()).thenReturn("arguments");
        when(jsScope.isDeclared("arguments", false)).thenReturn(false);

        boolean result = liveVariablesAnalysis.isArgumentsName(node);
        assertEquals(true, result);
    }
}
