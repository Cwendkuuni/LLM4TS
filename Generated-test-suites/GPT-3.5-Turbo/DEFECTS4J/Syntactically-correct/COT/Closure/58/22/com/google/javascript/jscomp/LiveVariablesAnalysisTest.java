import org.junit.Test;
import static org.junit.Assert.*;

public class LiveVariablesAnalysisTest {

    @Test
    public void testIsLive() {
        LiveVariablesAnalysis.LiveVariableLattice lattice = new LiveVariablesAnalysis.LiveVariableLattice(5);
        lattice.liveSet.set(0); // Variable at index 0 is live
        lattice.liveSet.set(3); // Variable at index 3 is live
        assertTrue(lattice.isLive(0));
        assertTrue(lattice.isLive(3));
        assertFalse(lattice.isLive(1));
        assertFalse(lattice.isLive(2));
        assertFalse(lattice.isLive(4));
    }

    @Test
    public void testFlowThrough() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(5);
        input.liveSet.set(0); // Variable at index 0 is live
        input.liveSet.set(3); // Variable at index 3 is live
        Node node = new Node(Token.ADD, new Node(Token.NAME, "a"), new Node(Token.NAME, "b")); // Simple addition operation
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, new Scope(), new AbstractCompiler());
        LiveVariablesAnalysis.LiveVariableLattice result = liveVariablesAnalysis.flowThrough(node, input);
        assertTrue(result.isLive(0)); // Variable at index 0 should still be live
        assertTrue(result.isLive(1)); // Variable at index 1 should be set as live
        assertFalse(result.isLive(2)); // Variable at index 2 should still be dead
        assertTrue(result.isLive(3)); // Variable at index 3 should still be live
        assertFalse(result.isLive(4)); // Variable at index 4 should still be dead
    }

    @Test
    public void testComputeGenKill() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, new Scope(), new AbstractCompiler());
        BitSet gen = new BitSet(5);
        BitSet kill = new BitSet(5);
        Node varNode1 = new Node(Token.VAR, new Node(Token.NAME, "a_1")); // Variable declaration for a_1
        liveVariablesAnalysis.computeGenKill(varNode1, gen, kill, false);
        assertTrue(gen.get(1)); // Variable at index 1 should be set as live
        assertFalse(kill.get(1)); // Variable at index 1 should be dead

        Node varNode2 = new Node(Token.VAR, new Node(Token.NAME, "a_2"), new Node(Token.STRING, "hello")); // Variable declaration for a_2 with initialization
        liveVariablesAnalysis.computeGenKill(varNode2, gen, kill, true);
        assertTrue(gen.get(2)); // Variable at index 2 should be set as live
        assertFalse(kill.get(2)); // Variable at index 2 should be dead

        Node assignNode = new Node(Token.ASSIGN, new Node(Token.NAME, "a_3"), new Node(Token.NAME, "a_2")); // Variable assignment for a_3
        liveVariablesAnalysis.computeGenKill(assignNode, gen, kill, false);
        assertTrue(gen.get(3)); // Variable at index 3 should be set as live
        assertFalse(kill.get(3)); // Variable at index 3 should be dead

        Node addNode = new Node(Token.ADD, new Node(Token.NAME, "a_1"), new Node(Token.NAME, "a_3")); // Variable addition for a_1 and a_3
        liveVariablesAnalysis.computeGenKill(addNode, gen, kill, false);
        assertTrue(gen.get(1)); // Variable at index 1 should still be live
        assertTrue(gen.get(3)); // Variable at index 3 should still be live
        assertTrue(kill.get(1)); // Variable at index 1 should be set as dead
        assertFalse(kill.get(3)); // Variable at index 3 should still be dead
    }

    @Test
    public void testMarkAllParametersEscaped() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        Scope scope = new Scope();
        AbstractCompiler compiler = new AbstractCompiler();
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, scope, compiler);
        liveVariablesAnalysis.markAllParametersEscaped();
        Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();
        assertEquals(4, escapedLocals.size()); // Assuming there are four parameters, all should be marked as escaped
    }

    @Test
    public void testIsArgumentsName() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        Scope scope = new Scope();
        AbstractCompiler compiler = new AbstractCompiler();
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, scope, compiler);
        Node argumentsNode = new Node(Token.NAME, "arguments"); // Node representing the name 'arguments'
        assertTrue(liveVariablesAnalysis.isArgumentsName(argumentsNode)); // The node represents the name 'arguments' and is not declared, so it should return true
    }

    @Test
    public void testGetEscapedLocals() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        Scope scope = new Scope();
        AbstractCompiler compiler = new AbstractCompiler();
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, scope, compiler);
        liveVariablesAnalysis.markAllParametersEscaped();
        Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();
        assertEquals(4, escapedLocals.size()); // Assuming there are four parameters, all should be marked as escaped
    }

    @Test
    public void testGetVarIndex() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        Scope scope = new Scope();
        AbstractCompiler compiler = new AbstractCompiler();
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, scope, compiler);
        int varIndex = liveVariablesAnalysis.getVarIndex("variable"); // Assuming "variable" is a local variable
        assertEquals(0, varIndex); // Assuming "variable" is the first local variable, its index should be 0
    }

}