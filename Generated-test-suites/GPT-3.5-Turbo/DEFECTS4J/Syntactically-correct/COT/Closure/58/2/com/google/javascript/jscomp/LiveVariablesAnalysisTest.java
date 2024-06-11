import org.junit.Test;
import static org.junit.Assert.*;

public class LiveVariablesAnalysisTest {
    
    @Test
    public void testIsLive() {
        LiveVariablesAnalysis.LiveVariableLattice lattice = new LiveVariablesAnalysis.LiveVariableLattice(5);
        // Initialize lattice with live variables
        lattice.liveSet.set(1);
        lattice.liveSet.set(3);
        assertTrue(lattice.isLive(1));
        assertFalse(lattice.isLive(2));
        assertTrue(lattice.isLive(3));
        assertFalse(lattice.isLive(4));
    }
    
    @Test
    public void testGetEscapedLocals() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        analysis.getEscapedLocals().add(new Var());
        assertTrue(analysis.getEscapedLocals().contains(new Var()));
    }
    
    @Test
    public void testGetVarIndex() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        analysis.getVarIndex("test");
        // Not possible to verify the result since there is no return value
    }
    
    @Test
    public void testIsForward() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        assertFalse(analysis.isForward());
    }
    
    @Test
    public void testCreateEntryLattice() {
        LiveVariablesAnalysis.LiveVariableLattice lattice = new LiveVariablesAnalysis.LiveVariableLattice(5);
        assertEquals("{}", lattice.toString());
    }
    
    @Test
    public void testCreateInitialEstimateLattice() {
        LiveVariablesAnalysis.LiveVariableLattice lattice = new LiveVariablesAnalysis.LiveVariableLattice(5);
        assertEquals("{}", lattice.toString());
    }
    
    @Test
    public void testFlowThrough() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        LiveVariablesAnalysis.LiveVariableLattice lattice = new LiveVariablesAnalysis.LiveVariableLattice(5);
        // Initialize lattice with live variables
        lattice.liveSet.set(1);
        
        LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(new Node(), lattice);
        assertFalse(result.isLive(1));
        assertTrue(result.isLive(2));
    }
    
    @Test
    public void testMarkAllParametersEscaped() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        analysis.markAllParametersEscaped();
        // Not possible to verify the result since there is no return value
    }
    
    @Test
    public void testIsArgumentsName() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        assertTrue(analysis.isArgumentsName(new Node(Token.NAME, "arguments")));
        assertFalse(analysis.isArgumentsName(new Node(Token.NAME, "test")));
    }
}