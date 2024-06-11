import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import org.junit.Assert;
import org.junit.Test;

public class LiveVariablesAnalysisTest {

    @Test
    public void testGetEscapedLocals() {
        // Given
        Node rootNode = new Node(Token.ROOT);
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(rootNode);
        Scope jsScope = new Scope(rootNode);
        AbstractCompiler compiler = new AbstractCompiler();

        // When
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        Set<Var> escapedLocals = analysis.getEscapedLocals();

        // Then
        Assert.assertNotNull(escapedLocals);
        Assert.assertTrue(escapedLocals.isEmpty());
    }

    @Test
    public void testGetVarIndex() {
        // Given
        Node rootNode = new Node(Token.ROOT);
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(rootNode);
        Scope jsScope = new Scope(rootNode);
        AbstractCompiler compiler = new AbstractCompiler();
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        String var = "x";

        // When
        int varIndex = analysis.getVarIndex(var);

        // Then
        Assert.assertNotEquals(-1, varIndex);
    }

    @Test
    public void testIsForward() {
        // Given
        Node rootNode = new Node(Token.ROOT);
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(rootNode);
        Scope jsScope = new Scope(rootNode);
        AbstractCompiler compiler = new AbstractCompiler();
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

        // When
        boolean isForward = analysis.isForward();

        // Then
        Assert.assertFalse(isForward);
    }

    @Test
    public void testCreateEntryLattice() {
        // Given
        Node rootNode = new Node(Token.ROOT);
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(rootNode);
        Scope jsScope = new Scope(rootNode);
        AbstractCompiler compiler = new AbstractCompiler();
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

        // When
        LiveVariableLattice entryLattice = analysis.createEntryLattice();

        // Then
        Assert.assertNotNull(entryLattice);
        Assert.assertEquals(jsScope.getVarCount(), entryLattice.liveSet.size());
    }

    @Test
    public void testCreateInitialEstimateLattice() {
        // Given
        Node rootNode = new Node(Token.ROOT);
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(rootNode);
        Scope jsScope = new Scope(rootNode);
        AbstractCompiler compiler = new AbstractCompiler();
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

        // When
        LiveVariableLattice initialEstimateLattice = analysis.createInitialEstimateLattice();

        // Then
        Assert.assertNotNull(initialEstimateLattice);
        Assert.assertEquals(jsScope.getVarCount(), initialEstimateLattice.liveSet.size());
    }

    @Test
    public void testFlowThrough() {
        // Given
        Node rootNode = new Node(Token.ROOT);
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(rootNode);
        Scope jsScope = new Scope(rootNode);
        AbstractCompiler compiler = new AbstractCompiler();
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        Node node = new Node(Token.NAME, "x");

        // When
        LiveVariableLattice input = new LiveVariableLattice(jsScope.getVarCount());
        LiveVariableLattice result = analysis.flowThrough(node, input);

        // Then
        Assert.assertNotNull(result);
    }

    @Test
    public void testComputeGenKill() {
        // Given
        Node rootNode = new Node(Token.ROOT);
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(rootNode);
        Scope jsScope = new Scope(rootNode);
        AbstractCompiler compiler = new AbstractCompiler();
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        Node n = new Node(Token.NAME, "x");
        BitSet gen = new BitSet();
        BitSet kill = new BitSet();
        boolean conditional = false;

        // When
        analysis.computeGenKill(n, gen, kill, conditional);

        // Then
        Assert.assertNotNull(gen);
        Assert.assertNotNull(kill);
    }

    @Test
    public void testAddToSetIfLocal() {
        // Given
        Node rootNode = new Node(Token.ROOT);
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(rootNode);
        Scope jsScope = new Scope(rootNode);
        AbstractCompiler compiler = new AbstractCompiler();
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        Node node = new Node(Token.NAME, "x");
        BitSet set = new BitSet();

        // When
        analysis.addToSetIfLocal(node, set);

        // Then
        Assert.assertNotNull(set);
    }

    @Test
    public void testMarkAllParametersEscaped() {
        // Given
        Node rootNode = new Node(Token.ROOT);
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(rootNode);
        Scope jsScope = new Scope(rootNode);
        AbstractCompiler compiler = new AbstractCompiler();
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

        // When
        analysis.markAllParametersEscaped();

        // Then
        Assert.assertFalse(analysis.getEscapedLocals().isEmpty());
    }

    @Test
    public void testIsArgumentsName() {
        // Given
        Node rootNode = new Node(Token.ROOT);
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(rootNode);
        Scope jsScope = new Scope(rootNode);
        AbstractCompiler compiler = new AbstractCompiler();
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        Node n = new Node(Token.NAME, "arguments");

        // When
        boolean isArgumentsName = analysis.isArgumentsName(n);

        // Then
        Assert.assertTrue(isArgumentsName);
    }

}