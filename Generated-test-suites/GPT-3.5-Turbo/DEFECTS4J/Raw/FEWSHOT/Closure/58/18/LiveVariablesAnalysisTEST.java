import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class LiveVariablesAnalysisTest {

    private ControlFlowGraph<Node> cfg;
    private Scope jsScope;
    private AbstractCompiler compiler;

    @Before
    public void setUp() {
        cfg = new ControlFlowGraph<Node>();
        jsScope = new Scope();
        compiler = new AbstractCompiler();
    }

    @Test
    public void testConstructor() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        Set<Var> escaped = analysis.getEscapedLocals();
        assertNotNull(escaped);
        assertTrue(escaped.isEmpty());
        assertEquals(0, analysis.getVarIndex("var1"));
        assertEquals(1, analysis.getVarIndex("var2"));
    }

    @Test
    public void testFlowThrough() {
        LiveVariableLattice input = new LiveVariableLattice(2);
        assertFalse(input.isLive(0));
        assertFalse(input.isLive(1));

        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        Node node = new Node(Token.VAR);
        node.addChildToBack(new Node(Token.NAME, "var1"));
        node.addChildToBack(new Node(Token.NAME, "var2"));
        LiveVariableLattice result = analysis.flowThrough(node, input);
        assertFalse(result.isLive(0));
        assertFalse(result.isLive(1));

        node = new Node(Token.NAME);
        node.addChildToBack(new Node(Token.NAME, "var1"));
        node.addChildToBack(new Node(Token.NUMBER, "0"));
        result = analysis.flowThrough(node, result);
        assertTrue(result.isLive(0));
        assertFalse(result.isLive(1));
    }

    @Test
    public void testComputeGenKill() {
        BitSet gen = new BitSet(2);
        BitSet kill = new BitSet(2);
        boolean conditional = false;

        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        Node node = new Node(Token.VAR);
        node.addChildToBack(new Node(Token.NAME, "var1"));
        node.addChildToBack(new Node(Token.NAME, "var2"));
        analysis.computeGenKill(node, gen, kill, conditional);
        assertFalse(gen.get(0));
        assertFalse(gen.get(1));
        assertFalse(kill.get(0));
        assertFalse(kill.get(1));

        node = new Node(Token.NAME);
        node.addChildToBack(new Node(Token.NAME, "var1"));
        node.addChildToBack(new Node(Token.NUMBER, "0"));
        analysis.computeGenKill(node, gen, kill, conditional);
        assertTrue(gen.get(0));
        assertFalse(gen.get(1));
        assertFalse(kill.get(0));
        assertFalse(kill.get(1));

        Node assignNode = new Node(Token.ASSIGN);
        assignNode.addChildToBack(new Node(Token.NAME, "var1"));
        assignNode.addChildToBack(new Node(Token.NUMBER, "1"));
        node = new Node(Token.EXPR_RESULT, assignNode);
        analysis.computeGenKill(node, gen, kill, conditional);
        assertFalse(gen.get(0));
        assertFalse(gen.get(1));
        assertFalse(kill.get(0));
        assertTrue(kill.get(1));
    }

    @Test
    public void testAddToSetIfLocal() {
        BitSet set = new BitSet(2);
        boolean conditional = false;

        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        Node node = new Node(Token.VAR);
        node.addChildToBack(new Node(Token.NAME, "var1"));
        node.addChildToBack(new Node(Token.NAME, "var2"));
        analysis.addToSetIfLocal(node.getFirstChild(), set);
        assertFalse(set.get(0));
        assertTrue(set.get(1));

        set.clear();
        analysis.addToSetIfLocal(node.getLastChild(), set);
        assertFalse(set.get(0));
        assertFalse(set.get(1));
    }

    @Test
    public void testMarkAllParametersEscaped() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        analysis.markAllParametersEscaped();
        Set<Var> escaped = analysis.getEscapedLocals();
        assertNotNull(escaped);
        assertFalse(escaped.isEmpty());
        assertEquals(2, escaped.size());
    }
}