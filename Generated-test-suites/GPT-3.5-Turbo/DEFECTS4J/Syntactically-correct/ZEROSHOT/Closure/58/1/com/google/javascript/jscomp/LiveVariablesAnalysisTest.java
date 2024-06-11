import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.graph.DiGraph.DiGraphEdge;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.BitSet;
import java.util.List;
import java.util.Set;

public class LiveVariablesAnalysisTest {
    
    private LiveVariablesAnalysis liveVariablesAnalysis;
    
    @Before
    public void setUp() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        Scope jsScope = new Scope();
        AbstractCompiler compiler = new AbstractCompiler();
        liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    }
    
    @Test
    public void testGetEscapedLocals() {
        Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();
        assertNotNull(escapedLocals);
    }
    
    @Test
    public void testGetVarIndex() {
        int varIndex = liveVariablesAnalysis.getVarIndex("var");
        assertEquals(0, varIndex);
    }
    
    @Test
    public void testIsForward() {
        boolean isForward = liveVariablesAnalysis.isForward();
        assertFalse(isForward);
    }
    
    @Test
    public void testCreateEntryLattice() {
        LiveVariablesAnalysis.LiveVariableLattice entryLattice = liveVariablesAnalysis.createEntryLattice();
        assertNotNull(entryLattice);
    }
    
    @Test
    public void testCreateInitialEstimateLattice() {
        LiveVariablesAnalysis.LiveVariableLattice initialEstimateLattice = liveVariablesAnalysis.createInitialEstimateLattice();
        assertNotNull(initialEstimateLattice);
    }
    
    @Test
    public void testFlowThrough() {
        Node node = new Node(null, null);
        LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(10);
        LiveVariablesAnalysis.LiveVariableLattice result = liveVariablesAnalysis.flowThrough(node, input);
        assertNotNull(result);
    }
    
    @Test
    public void testComputeGenKill() {
        Node node = new Node(null, null);
        BitSet gen = new BitSet();
        BitSet kill = new BitSet();
        boolean conditional = false;
        liveVariablesAnalysis.computeGenKill(node, gen, kill, conditional);
    }
    
    @Test
    public void testAddToSetIfLocal() {
        Node node = new Node(Token.NAME, "var");
        BitSet set = new BitSet();
        liveVariablesAnalysis.addToSetIfLocal(node, set);
    }
    
    @Test
    public void testMarkAllParametersEscaped() {
        liveVariablesAnalysis.markAllParametersEscaped();
    }
    
    @Test
    public void testIsArgumentsName() {
        Node node = new Node(Token.NAME, "arguments");
        boolean isArgumentsName = liveVariablesAnalysis.isArgumentsName(node);
        assertTrue(isArgumentsName);
    }
}