import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LiveVariablesAnalysisTest {

    private LiveVariablesAnalysis analysis;

    @Before
    public void setup() {
        // Create a test scope with some variables
        Scope testScope = new Scope();
        Var var1 = new Var("var1", 0);
        Var var2 = new Var("var2", 1);
        Var var3 = new Var("var3", 2);
        testScope.addVar(var1);
        testScope.addVar(var2);
        testScope.addVar(var3);

        // Create a control flow graph for testing
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();

        // Construct the graph

        // ...

        // Initialize the LiveVariablesAnalysis with the test scope and CFG
        analysis = new LiveVariablesAnalysis(cfg, testScope, new AbstractCompiler());
    }

    @Test
    public void testGetEscapedLocals() {
        // Test that the getEscapedLocals() method returns the expected set of escaped variables

        // ...
    }

    @Test
    public void testGetVarIndex() {
        // Test the getVarIndex() method for retrieving the index of a variable

        // ...
    }

    @Test
    public void testCreateEntryLattice() {
        // Test the createEntryLattice() method for creating the entry lattice

        // ...
    }

    @Test
    public void testCreateInitialEstimateLattice() {
        // Test the createInitialEstimateLattice() method for creating the initial estimate lattice

        // ...
    }

    @Test
    public void testFlowThrough() {
        // Test the flowThrough() method for computing the live variables

        // ...
    }

    @Test
    public void testComputeGenKill() {
        // Test the computeGenKill() method for computing the gen and kill sets

        // ...
    }

    @Test
    public void testAddToSetIfLocal() {
        // Test the addToSetIfLocal() method for adding variables to the set if they are local

        // ...
    }

    @Test
    public void testMarkAllParametersEscaped() {
        // Test the markAllParametersEscaped() method for marking all parameters as escaped

        // ...
    }

    @Test
    public void testIsArgumentsName() {
        // Test the isArgumentsName() method for checking if a name is "arguments"

        // ...
    }

}