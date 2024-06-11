import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class LiveVariablesAnalysisTest {

    private ControlFlowGraph<Node> cfg;
    private Scope jsScope;
    private AbstractCompiler compiler;

    @Before
    public void setUp() {
        // Initialize the objects required for testing
        compiler = new AbstractCompiler();
        jsScope = new Scope(compiler);
        cfg = new ControlFlowGraph<>(new Node(Token.BLOCK));
    }

    @Test
    public void testGetEscapedLocals() {
        // Test the getEscapedLocals() method
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        Set<Var> escapedLocals = analysis.getEscapedLocals();
        assertNotNull(escapedLocals);
        assertEquals(0, escapedLocals.size());
    }

    @Test
    public void testGetVarIndex() {
        // Test the getVarIndex() method
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        int varIndex = analysis.getVarIndex("varName");
        assertEquals(-1, varIndex);
    }

    @Test
    public void testIsForward() {
        // Test the isForward() method
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        assertFalse(analysis.isForward());
    }

    @Test
    public void testCreateEntryLattice() {
        // Test the createEntryLattice() method
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        LiveVariablesAnalysis.LiveVariableLattice entryLattice = analysis.createEntryLattice();
        assertNotNull(entryLattice);
        assertEquals(0, entryLattice.liveSet.cardinality());
    }

    @Test
    public void testCreateInitialEstimateLattice() {
        // Test the createInitialEstimateLattice() method
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        LiveVariablesAnalysis.LiveVariableLattice initialEstimateLattice = analysis.createInitialEstimateLattice();
        assertNotNull(initialEstimateLattice);
        assertEquals(0, initialEstimateLattice.liveSet.cardinality());
    }

    @Test
    public void testFlowThrough() {
        // Test the flowThrough() method
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        Node node = new Node(Token.EQ);
        LiveVariablesAnalysis.LiveVariableLattice input = analysis.createEntryLattice();
        LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(node, input);
        assertNotNull(result);
        assertNotSame(input, result);
    }

    @Test
    public void testMarkAllParametersEscaped() {
        // Test the markAllParametersEscaped() method
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        analysis.markAllParametersEscaped();
        Set<Var> escapedLocals = analysis.getEscapedLocals();
        assertNotNull(escapedLocals);
        assertTrue(escapedLocals.size() > 0);
    }

    @Test
    public void testIsArgumentsName() {
        // Test the isArgumentsName() method
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        Node argumentsNode = new Node(Token.NAME, "arguments");
        assertTrue(analysis.isArgumentsName(argumentsNode));
        Node nonArgumentsNode = new Node(Token.NAME, "nonArguments");
        assertFalse(analysis.isArgumentsName(nonArgumentsNode));
    }

}