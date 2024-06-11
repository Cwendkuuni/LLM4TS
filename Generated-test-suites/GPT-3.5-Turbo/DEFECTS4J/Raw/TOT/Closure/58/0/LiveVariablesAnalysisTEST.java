import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LiveVariablesAnalysisTest {
    private LiveVariablesAnalysis liveVariablesAnalysis;
    private ControlFlowGraph<Node> controlFlowGraph;
    private Scope scope;
    private AbstractCompiler compiler;

    @Before
    public void setUp() {
        // Create a mock ControlFlowGraph, Scope, and AbstractCompiler
        controlFlowGraph = new ControlFlowGraph<>();
        scope = new Scope();
        compiler = new AbstractCompiler();
        liveVariablesAnalysis = new LiveVariablesAnalysis(controlFlowGraph, scope, compiler);
    }

    @Test
    public void testGetVarIndex() {
        int index = liveVariablesAnalysis.getVarIndex("var");
        Assert.assertEquals(0, index);
    }

    @Test
    public void testIsForward() {
        boolean forward = liveVariablesAnalysis.isForward();
        Assert.assertFalse(forward);
    }

    @Test
    public void testCreateEntryLattice() {
        LiveVariablesAnalysis.LiveVariableLattice entryLattice = liveVariablesAnalysis.createEntryLattice();
        Assert.assertNotNull(entryLattice);
    }

    @Test
    public void testCreateInitialEstimateLattice() {
        LiveVariablesAnalysis.LiveVariableLattice initialEstimateLattice = liveVariablesAnalysis.createInitialEstimateLattice();
        Assert.assertNotNull(initialEstimateLattice);
    }

    @Test
    public void testFlowThrough() {
        Node node = new Node(null);
        LiveVariablesAnalysis.LiveVariableLattice input = liveVariablesAnalysis.createEntryLattice();
        LiveVariablesAnalysis.LiveVariableLattice output = liveVariablesAnalysis.flowThrough(node, input);
        Assert.assertNotNull(output);
    }

    @Test
    public void testGetEscapedLocals() {
        Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();
        Assert.assertNotNull(escapedLocals);
        Assert.assertTrue(escapedLocals.isEmpty());
    }

    @Test
    public void testMarkAllParametersEscaped() {
        liveVariablesAnalysis.markAllParametersEscaped();
        Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();
        Assert.assertNotNull(escapedLocals);
        Assert.assertFalse(escapedLocals.isEmpty());
    }

    @Test
    public void testIsArgumentsName() {
        Node node = new Node(null);
        node.setType(Token.NAME);
        node.setString("arguments");

        boolean isArgumentsName = liveVariablesAnalysis.isArgumentsName(node);
        Assert.assertTrue(isArgumentsName);
    }
}