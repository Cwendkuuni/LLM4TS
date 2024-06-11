import org.junit.Test;
import static org.junit.Assert.*;

public class LiveVariablesAnalysisTest {

    @Test
    public void testLiveVariableLatticeEquals() {
        LiveVariablesAnalysis.LiveVariableLattice lattice1 = new LiveVariablesAnalysis.LiveVariableLattice(5);
        lattice1.liveSet.set(1);
        lattice1.liveSet.set(3);
        
        LiveVariablesAnalysis.LiveVariableLattice lattice2 = new LiveVariablesAnalysis.LiveVariableLattice(5);
        lattice2.liveSet.set(1);
        lattice2.liveSet.set(3);
        
        assertTrue(lattice1.equals(lattice2));
        assertTrue(lattice2.equals(lattice1));
    }
    
    @Test
    public void testLiveVariableLatticeEqualsDifferentSize() {
        LiveVariablesAnalysis.LiveVariableLattice lattice1 = new LiveVariablesAnalysis.LiveVariableLattice(5);
        lattice1.liveSet.set(1);
        lattice1.liveSet.set(3);
        
        LiveVariablesAnalysis.LiveVariableLattice lattice2 = new LiveVariablesAnalysis.LiveVariableLattice(3);
        lattice2.liveSet.set(1);
        lattice2.liveSet.set(3);
        
        assertFalse(lattice1.equals(lattice2));
        assertFalse(lattice2.equals(lattice1));
    }
    
    @Test
    public void testLiveVariableLatticeEqualsDifferentValues() {
        LiveVariablesAnalysis.LiveVariableLattice lattice1 = new LiveVariablesAnalysis.LiveVariableLattice(5);
        lattice1.liveSet.set(1);
        lattice1.liveSet.set(3);
        
        LiveVariablesAnalysis.LiveVariableLattice lattice2 = new LiveVariablesAnalysis.LiveVariableLattice(5);
        lattice2.liveSet.set(1);
        lattice2.liveSet.set(4);
        
        assertFalse(lattice1.equals(lattice2));
        assertFalse(lattice2.equals(lattice1));
    }
    
    @Test
    public void testLiveVariableLatticeEqualsNull() {
        LiveVariablesAnalysis.LiveVariableLattice lattice1 = new LiveVariablesAnalysis.LiveVariableLattice(5);
        lattice1.liveSet.set(1);
        lattice1.liveSet.set(3);
        
        assertFalse(lattice1.equals(null));
    }
    
    @Test
    public void testLiveVariableLatticeToString() {
        LiveVariablesAnalysis.LiveVariableLattice lattice = new LiveVariablesAnalysis.LiveVariableLattice(5);
        lattice.liveSet.set(1);
        lattice.liveSet.set(3);
        
        assertEquals("{1, 3}", lattice.toString());
    }
    
    @Test
    public void testLiveVariableLatticeHashCode() {
        LiveVariablesAnalysis.LiveVariableLattice lattice1 = new LiveVariablesAnalysis.LiveVariableLattice(5);
        lattice1.liveSet.set(1);
        lattice1.liveSet.set(3);
        
        LiveVariablesAnalysis.LiveVariableLattice lattice2 = new LiveVariablesAnalysis.LiveVariableLattice(5);
        lattice2.liveSet.set(1);
        lattice2.liveSet.set(3);
        
        assertEquals(lattice1.hashCode(), lattice2.hashCode());
    }
    
    @Test
    public void testLiveVariableLatticeHashCodeDifferentSize() {
        LiveVariablesAnalysis.LiveVariableLattice lattice1 = new LiveVariablesAnalysis.LiveVariableLattice(5);
        lattice1.liveSet.set(1);
        lattice1.liveSet.set(3);
        
        LiveVariablesAnalysis.LiveVariableLattice lattice2 = new LiveVariablesAnalysis.LiveVariableLattice(3);
        lattice2.liveSet.set(1);
        lattice2.liveSet.set(3);
        
        assertNotEquals(lattice1.hashCode(), lattice2.hashCode());
    }
    
    @Test
    public void testLiveVariableLatticeHashCodeDifferentValues() {
        LiveVariablesAnalysis.LiveVariableLattice lattice1 = new LiveVariablesAnalysis.LiveVariableLattice(5);
        lattice1.liveSet.set(1);
        lattice1.liveSet.set(3);
        
        LiveVariablesAnalysis.LiveVariableLattice lattice2 = new LiveVariablesAnalysis.LiveVariableLattice(5);
        lattice2.liveSet.set(1);
        lattice2.liveSet.set(4);
        
        assertNotEquals(lattice1.hashCode(), lattice2.hashCode());
    }
    
    @Test
    public void testIsForward() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        
        assertFalse(analysis.isForward());
    }
    
    @Test
    public void testCreateEntryLattice() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        
        LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.createEntryLattice();
        
        assertNotNull(lattice);
    }
    
    @Test
    public void testCreateInitialEstimateLattice() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        
        LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.createInitialEstimateLattice();
        
        assertNotNull(lattice);
    }
    
    @Test
    public void testFlowThrough() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(5);
        input.liveSet.set(1);
        input.liveSet.set(3);
        
        LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(null, input);
        
        assertNotNull(result);
        assertEquals("{1, 3}", result.toString());
    }
    
    @Test
    public void testComputeGenKill() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        Node node = new Node(Token.EXPR_RESULT);
        node.addChildToBack(Node.newString(Token.NAME, "var1"));
        node.addChildToBack(Node.newNumber(5));
        LiveVariablesAnalysis.LiveVariableLattice gen = new LiveVariablesAnalysis.LiveVariableLattice(5);
        LiveVariablesAnalysis.LiveVariableLattice kill = new LiveVariablesAnalysis.LiveVariableLattice(5);
        
        analysis.computeGenKill(node, gen.liveSet, kill.liveSet, false);
        
        assertEquals("{1}", gen.toString());
        assertEquals("{1}", kill.toString());
    }
    
    @Test
    public void testComputeGenKillConditional() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        Node node = new Node(Token.EXPR_RESULT);
        node.addChildToBack(Node.newString(Token.NAME, "var1"));
        node.addChildToBack(Node.newNumber(5));
        LiveVariablesAnalysis.LiveVariableLattice gen = new LiveVariablesAnalysis.LiveVariableLattice(5);
        LiveVariablesAnalysis.LiveVariableLattice kill = new LiveVariablesAnalysis.LiveVariableLattice(5);
        
        analysis.computeGenKill(node, gen.liveSet, kill.liveSet, true);
        
        assertEquals("{1}", gen.toString());
        assertEquals("{}", kill.toString());
    }
    
    @Test
    public void testAddToSetIfLocal() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        Node node = Node.newString(Token.NAME, "var1");
        LiveVariablesAnalysis.LiveVariableLattice set = new LiveVariablesAnalysis.LiveVariableLattice(5);
        
        analysis.addToSetIfLocal(node, set.liveSet);
        
        assertEquals("{1}", set.toString());
    }
    
    @Test
    public void testAddToSetIfLocalNoLocal() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        Node node = Node.newString(Token.NAME, "var1");
        LiveVariablesAnalysis.LiveVariableLattice set = new LiveVariablesAnalysis.LiveVariableLattice(5);
        
        analysis.jsScope = new Scope(null);
        
        analysis.addToSetIfLocal(node, set.liveSet);
        
        assertEquals("{}", set.toString());
    }
    
    @Test
    public void testMarkAllParametersEscaped() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        
        analysis.markAllParametersEscaped();
        
        assertNotNull(analysis.getEscapedLocals());
    }
    
    @Test
    public void testGetVarIndex() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        
        analysis.jsScope = new Scope(null);
        
        int index = analysis.getVarIndex("var1");
        
        assertEquals(-1, index);
    }
    
    @Test
    public void testGetEscapedLocals() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        
        assertNotNull(analysis.getEscapedLocals());
    }
}