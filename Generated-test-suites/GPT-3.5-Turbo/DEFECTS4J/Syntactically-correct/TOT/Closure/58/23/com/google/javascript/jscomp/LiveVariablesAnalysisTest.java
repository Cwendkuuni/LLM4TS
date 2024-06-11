import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

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
        assertNotNull(liveVariablesAnalysis.getEscapedLocals());
        assertTrue(liveVariablesAnalysis.getEscapedLocals().isEmpty());
    }

    @Test
    public void testGetVarIndex() {
        assertEquals(0, liveVariablesAnalysis.getVarIndex("var1"));
        assertEquals(1, liveVariablesAnalysis.getVarIndex("var2"));
    }

    @Test
    public void testFlowThrough() {
        // Test with empty input
        LiveVariablesAnalysis.LiveVariableLattice input1 = new LiveVariablesAnalysis.LiveVariableLattice(2);
        LiveVariablesAnalysis.LiveVariableLattice expectedOutput1 = new LiveVariablesAnalysis.LiveVariableLattice(2);
        assertEquals(expectedOutput1, liveVariablesAnalysis.flowThrough(null, input1));

        // Test with non-empty input
        LiveVariablesAnalysis.LiveVariableLattice input2 = new LiveVariablesAnalysis.LiveVariableLattice(3);
        input2.liveSet.set(0);
        input2.liveSet.set(1);
        LiveVariablesAnalysis.LiveVariableLattice expectedOutput2 = new LiveVariablesAnalysis.LiveVariableLattice(3);
        expectedOutput2.liveSet.set(1);
        expectedOutput2.liveSet.set(2);
        expectedOutput2.liveSet.set(3);
        assertEquals(expectedOutput2, liveVariablesAnalysis.flowThrough(null, input2));
    }

    @Test
    public void testComputGenKill() {
        // Test with different node types
        Node n1 = new Node(Token.SCRIPT);
        BitSet gen1 = new BitSet(2);
        BitSet kill1 = new BitSet(2);
        liveVariablesAnalysis.computeGenKill(n1, gen1, kill1, false);
        assertEquals(new BitSet(2), gen1);
        assertEquals(new BitSet(2), kill1);

        Node n2 = new Node(Token.VAR);
        n2.addChildToFront(new Node(Token.NAME, "var1"));
        BitSet gen2 = new BitSet(2);
        BitSet kill2 = new BitSet(2);
        liveVariablesAnalysis.computeGenKill(n2, gen2, kill2, false);
        assertEquals(new BitSet(2), gen2);
        assertEquals(new BitSet(2), kill2);

        Node n3 = new Node(Token.NAME, "var1");
        BitSet gen3 = new BitSet(2);
        BitSet kill3 = new BitSet(2);
        liveVariablesAnalysis.computeGenKill(n3, gen3, kill3, false);
        assertEquals(new BitSet(2), gen3);
        assertEquals(new BitSet(2), kill3);

        // Add more test cases for different node types

    }

    @Test
    public void testMarkAllParametersEscaped() {
        liveVariablesAnalysis.markAllParametersEscaped();
        assertTrue(liveVariablesAnalysis.getEscapedLocals().contains("param1"));
        assertTrue(liveVariablesAnalysis.getEscapedLocals().contains("param2"));
    }

    @Test
    public void testIsArgumentsName() {
        Node n1 = new Node(Token.NAME, "arguments");
        assertTrue(liveVariablesAnalysis.isArgumentsName(n1));

        Node n2 = new Node(Token.NAME, "args");
        assertFalse(liveVariablesAnalysis.isArgumentsName(n2));

        Node n3 = new Node(Token.FUNCTION);
        assertFalse(liveVariablesAnalysis.isArgumentsName(n3));
    }

}