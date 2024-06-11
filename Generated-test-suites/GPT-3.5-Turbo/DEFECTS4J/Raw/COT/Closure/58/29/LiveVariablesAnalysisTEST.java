import com.google.common.collect.Sets;

import org.junit.Before;
import org.junit.Test;

import java.util.Set;

public class LiveVariablesAnalysisTest {
    
    private LiveVariablesAnalysis liveVariablesAnalysis;
    
    @Before
    public void setUp() {
        // Create a mock ControlFlowGraph and Scope
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        Scope jsScope = new Scope();
        AbstractCompiler compiler = new AbstractCompiler();
        liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    }
    
    @Test
    public void testGetEscapedLocals_emptyScope() {
        Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();
        assert escapedLocals.isEmpty();
    }
    
    @Test
    public void testGetEscapedLocals_nonEmptyScope() {
        // Set up a mock Scope with some escaped variables
        Set<Var> escapedVariables = Sets.newHashSet();
        Scope jsScope = new Scope();
        jsScope.setEscaped(escapedVariables);
        
        AbstractCompiler compiler = new AbstractCompiler();
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
        
        Set<Var> result = liveVariablesAnalysis.getEscapedLocals();
        assert result.equals(escapedVariables);
    }
    
    @Test
    public void testGetVarIndex_validVariable() {
        // Add a variable to the scope
        Var var = new Var("testVar", 0, null);
        liveVariablesAnalysis.jsScope.declare(var);
        
        int result = liveVariablesAnalysis.getVarIndex("testVar");
        assert result == 0;
    }
    
    @Test(expected = NullPointerException.class)
    public void testGetVarIndex_invalidVariable() {
        int result = liveVariablesAnalysis.getVarIndex("nonExistentVar");
    }
    
    @Test
    public void testIsForward() {
        boolean result = liveVariablesAnalysis.isForward();
        assert !result;
    }
    
    @Test
    public void testCreateEntryLattice() {
        LiveVariablesAnalysis.LiveVariableLattice result = liveVariablesAnalysis.createEntryLattice();
        
        // Verify that the live set is initially empty
        assert result.liveSet.isEmpty();
    }
    
    @Test
    public void testCreateInitialEstimateLattice() {
        LiveVariablesAnalysis.LiveVariableLattice result = liveVariablesAnalysis.createInitialEstimateLattice();
        
        // Verify that the live set is initially empty
        assert result.liveSet.isEmpty();
    }
    
    @Test
    public void testFlowThrough() {
        // Create a test node and input lattice
        Node node = new Node(Token.EMPTY);
        LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(5);
        
        // Add some elements to the input lattice's live set
        input.liveSet.set(1);
        input.liveSet.set(3);
        
        LiveVariablesAnalysis.LiveVariableLattice result = liveVariablesAnalysis.flowThrough(node, input);
        
        // Verify that the live set is updated correctly
        assert result.liveSet.get(1);
        assert result.liveSet.get(3);
    }
    
    @Test
    public void testComputeGenKill() {
        // Create a test node and sets
        Node node = new Node(Token.NAME, new Node(Token.STRING, "testVar"));
        BitSet gen = new BitSet(1);
        BitSet kill = new BitSet(1);
        boolean conditional = false;
        
        liveVariablesAnalysis.computeGenKill(node, gen, kill, conditional);
        
        // Verify that the gen set is updated correctly
        assert gen.get(0);
        
        // Verify that the kill set is not updated since the conditional flag is false
        assert kill.isEmpty();
    }
    
    @Test
    public void testAddToSetIfLocal_localVariable() {
        // Create a test node and set
        Node node = new Node(Token.NAME, new Node(Token.STRING, "testVar"));
        BitSet set = new BitSet(1);
        
        // Add a variable to the scope
        Var var = new Var("testVar", 0, null);
        liveVariablesAnalysis.jsScope.declare(var);
        
        liveVariablesAnalysis.addToSetIfLocal(node, set);
        
        // Verify that the set contains the variable index
        assert set.get(0);
    }
    
    @Test
    public void testAddToSetIfLocal_nonLocalVariable() {
        // Create a test node and set
        Node node = new Node(Token.NAME, new Node(Token.STRING, "testVar"));
        BitSet set = new BitSet(1);
        
        liveVariablesAnalysis.addToSetIfLocal(node, set);
        
        // Verify that the set is empty since the variable is not in the scope
        assert set.isEmpty();
    }
    
    @Test
    public void testMarkAllParametersEscaped() {
        // Add some variables to the scope
        Var var1 = new Var("param1", 0, null);
        Var var2 = new Var("param2", 0, null);
        liveVariablesAnalysis.jsScope.declare(var1);
        liveVariablesAnalysis.jsScope.declare(var2);
        
        liveVariablesAnalysis.markAllParametersEscaped();
        
        // Verify that both variables are in the escaped set
        Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();
        assert escapedLocals.contains(var1);
        assert escapedLocals.contains(var2);
    }
    
    @Test
    public void testIsArgumentsName_validArgumentsName() {
        Node node = new Node(Token.NAME, new Node(Token.STRING, "arguments"));
        
        boolean result = liveVariablesAnalysis.isArgumentsName(node);
        assert result;
    }
    
    @Test
    public void testIsArgumentsName_invalidArgumentsName() {
        Node node = new Node(Token.NAME, new Node(Token.STRING, "testVar"));
        
        boolean result = liveVariablesAnalysis.isArgumentsName(node);
        assert !result;
    }
}