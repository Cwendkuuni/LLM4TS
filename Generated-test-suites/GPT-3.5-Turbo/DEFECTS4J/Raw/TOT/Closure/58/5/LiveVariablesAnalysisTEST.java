import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LiveVariablesAnalysisTest {

    // Test for isLive method
    @Test
    public void testIsLive() {
        LiveVariablesAnalysis.LiveVariableLattice lattice = new LiveVariablesAnalysis.LiveVariableLattice(5);

        lattice.liveSet.set(1);
        assertTrue(lattice.isLive(1));

        lattice.liveSet.set(3);
        assertTrue(lattice.isLive(3));

        lattice.liveSet.clear(1);
        assertTrue(!lattice.isLive(1));
    }

    // Test for getEscapedLocals method
    @Test
    public void testGetEscapedLocals() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        assertTrue(analysis.getEscapedLocals().isEmpty());

        analysis.getEscapedLocals().add(new Var("var1"));
        assertTrue(!analysis.getEscapedLocals().isEmpty());
    }

    // Test for getVarIndex method
    @Test
    public void testGetVarIndex() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        analysis.getVarIndex("var1");
        assertEquals(0, analysis.getVarIndex("var1"));

        analysis.getVarIndex("var2");
        assertEquals(1, analysis.getVarIndex("var2"));
    }

    // Test for createEntryLattice method
    @Test
    public void testCreateEntryLattice() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.createEntryLattice();
        assertTrue(lattice.liveSet.isEmpty());

        lattice.liveSet.set(2);
        assertTrue(!lattice.liveSet.isEmpty());
    }

    // Test for createInitialEstimateLattice method
    @Test
    public void testCreateInitialEstimateLattice() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.createInitialEstimateLattice();
        assertTrue(lattice.liveSet.isEmpty());

        lattice.liveSet.set(4);
        assertTrue(!lattice.liveSet.isEmpty());
    }

    // Test for flowThrough method
    @Test
    public void testFlowThrough() {
        LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(3);
        LiveVariablesAnalysis.LiveVariableLattice result;

        input.liveSet.set(2);
        result = analysis.flowThrough(NodeUtil.createName("var1"), input);
        assertTrue(result.liveSet.isEmpty());

        input.liveSet.set(0);
        result = analysis.flowThrough(NodeUtil.createAssign("var2", "var3"), input);
        assertTrue(result.liveSet.isEmpty());

        input.liveSet.set(1);
        result = analysis.flowThrough(NodeUtil.createIf(NodeUtil.createName("var4")), input);
        assertTrue(result.liveSet.isEmpty());

        input.liveSet.set(0);
        result = analysis.flowThrough(NodeUtil.createFunctionCall("func1"), input);
        assertTrue(result.liveSet.isEmpty());
    }

    // Test for computeGenKill method
    @Test
    public void testComputeGenKill() {
        BitSet gen = new BitSet();
        BitSet kill = new BitSet();
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        boolean conditional = false;

        Node n1 = NodeUtil.createAssign("var1", "var2");
        Node n2 = NodeUtil.createVarDec("var3");
        Node n3 = NodeUtil.createName("var4");
        Node n4 = NodeUtil.createIf(NodeUtil.createName("var5"));

        analysis.computeGenKill(n1, gen, kill, conditional);
        assertTrue(!gen.isEmpty());
        assertTrue(!kill.isEmpty());
        gen.clear();
        kill.clear();

        analysis.computeGenKill(n2, gen, kill, conditional);
        assertTrue(!gen.isEmpty());
        assertTrue(!kill.isEmpty());
        gen.clear();
        kill.clear();

        analysis.computeGenKill(n3, gen, kill, conditional);
        assertTrue(!gen.isEmpty());
        assertTrue(kill.isEmpty());
        gen.clear();
        kill.clear();

        analysis.computeGenKill(n4, gen, kill, conditional);
        assertTrue(!gen.isEmpty());
        assertTrue(!kill.isEmpty());
        gen.clear();
        kill.clear();
    }

    // Test for markAllParametersEscaped method
    @Test
    public void testMarkAllParametersEscaped() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        analysis.markAllParametersEscaped();
        assertTrue(!analysis.getEscapedLocals().isEmpty());
    }

    // Test for isArgumentsName method
    @Test
    public void testIsArgumentsName() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        Node n1 = NodeUtil.createName("arguments");
        Node n2 = NodeUtil.createName("arg");
        Node n3 = NodeUtil.createName("Arguments");

        assertTrue(analysis.isArgumentsName(n1));
        assertTrue(!analysis.isArgumentsName(n2));
        assertTrue(!analysis.isArgumentsName(n3));
    }

}