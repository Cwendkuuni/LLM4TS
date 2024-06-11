import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
public class LiveVariablesAnalysisTest {

    @Test
    public void testCreateEntryLattice() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.createEntryLattice();
        assertEquals(0, lattice.liveSet.cardinality()); // No live variables initially
    }

    @Test
    public void testCreateInitialEstimateLattice() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.createInitialEstimateLattice();
        assertEquals(0, lattice.liveSet.cardinality()); // No live variables initially
    }

    @Test
    public void testIsForward() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        assertFalse(analysis.isForward()); // Flow analysis is not forward
    }

    @Test
    public void testGetEscapedLocals() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        assertTrue(analysis.getEscapedLocals().isEmpty()); // No escaped locals initially
    }

    @Test
    public void testGetVarIndex() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        analysis.jsScope = new Scope(null);
        analysis.jsScope.addLocal("x", null);
        analysis.jsScope.addLocal("y", null);
        assertEquals(0, analysis.getVarIndex("x"));
        assertEquals(1, analysis.getVarIndex("y"));
    }

    @Test
    public void testFlowThrough() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(2);
        input.liveSet.set(0);
        input.liveSet.clear(1);
        Node node = new Node(Token.NAME, new Node(Token.VAR, new Node(Token.NAME, new Node(Token.STRING))));
        LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(node, input);
        assertFalse(result.liveSet.get(0));
        assertTrue(result.liveSet.get(1));
    }

    @Test
    public void testComputeGenKill() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        Node node = new Node(Token.NAME, new Node(Token.VAR, new Node(Token.NAME, new Node(Token.STRING))));
        BitSet gen = new BitSet();
        BitSet kill = new BitSet();
        analysis.computeGenKill(node, gen, kill, false);
        assertFalse(gen.get(0));
        assertTrue(kill.get(0));
    }

    @Test
    public void testAddToSetIfLocal() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        analysis.jsScope = new Scope(null);
        analysis.jsScope.addLocal("x", null);
        analysis.jsScope.addLocal("y", null);
        analysis.escaped.add(new Var(null, "y", null, null, null));
        Node node = new Node(Token.NAME, new Node(Token.VAR, new Node(Token.NAME, new Node(Token.STRING))));
        BitSet set = new BitSet();
        analysis.addToSetIfLocal(node, set);
        assertTrue(set.get(0));
        assertFalse(set.get(1));
    }

    @Test
    public void testMarkAllParametersEscaped() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        analysis.jsScope = new Scope(null);
        Node functionNode = new Node(Token.FUNCTION);
        Node lp = new Node(Token.PARAM_LIST);
        Node param1 = new Node(Token.NAME, new Node(Token.STRING));
        Node param2 = new Node(Token.NAME, new Node(Token.STRING));

        lp.addChildToBack(param1);
        lp.addChildToBack(param2);
        functionNode.addChildToBack(lp);
        analysis.jsScope.setRootNode(functionNode);

        analysis.markAllParametersEscaped();
        assertTrue(analysis.escaped.contains(analysis.jsScope.getVar(param1.getString())));
        assertTrue(analysis.escaped.contains(analysis.jsScope.getVar(param2.getString())));
    }

    @Test
    public void testIsArgumentsName() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        Node node = new Node(Token.NAME, new Node(Token.STRING, "arguments"));
        assertTrue(analysis.isArgumentsName(node));
    }

}