import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LiveVariablesAnalysisTest {

    private LiveVariablesAnalysis liveVariablesAnalysis;

    @Before
    public void setUp() {
        // Set up the LiveVariablesAnalysis instance
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        Scope scope = new Scope();
        Compiler compiler = new Compiler();
        liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, scope, compiler);
    }

    
    @Test
    public void testGetEscapedLocals() {
        // Test the getEscapedLocals method
        assertNotNull(liveVariablesAnalysis.getEscapedLocals());
    }

    @Test
    public void testGetVarIndex() {
        // Test the getVarIndex method
        assertEquals(0, liveVariablesAnalysis.getVarIndex("var1"));
        assertEquals(1, liveVariablesAnalysis.getVarIndex("var2"));
    }
    
}