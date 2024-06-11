import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.LiveVariablesAnalysis;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import com.google.javascript.jscomp.ControlFlowGraph;

public class LiveVariablesAnalysisTest {
    
    private LiveVariablesAnalysis liveVariablesAnalysis;
    private ControlFlowGraph<Node> cfg;
    private Scope jsScope;
    
    @Before
    public void setUp() {
        cfg = new ControlFlowGraph<Node>();
        jsScope = new Scope(cfg);
        liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, null);
    }
    
    @Test
    public void testGetEscapedLocals() {
        assertNotNull(liveVariablesAnalysis.getEscapedLocals());
    }
    
    @Test
    public void testGetVarIndex() {
        assertEquals(0, liveVariablesAnalysis.getVarIndex("var1"));
    }
    
    @Test
    public void testIsForward() {
        assertFalse(liveVariablesAnalysis.isForward());
    }
    
    @Test
    public void testCreateEntryLattice() {
        assertNotNull(liveVariablesAnalysis.createEntryLattice());
    }
    
    @Test
    public void testCreateInitialEstimateLattice() {
        assertNotNull(liveVariablesAnalysis.createInitialEstimateLattice());
    }
    
    @Test
    public void testFlowThrough() {
        Node node = new Node();
        assertNotNull(liveVariablesAnalysis.flowThrough(node, null));
    }
    
    @Test
    public void testMarkAllParametersEscaped() {
        liveVariablesAnalysis.markAllParametersEscaped();
        assertNotNull(liveVariablesAnalysis.getEscapedLocals());
    }
    
    @Test
    public void testIsArgumentsName() {
        Node node = new Node();
        assertTrue(liveVariablesAnalysis.isArgumentsName(node));
    }
}