
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.optimization.linear.*;

public class SimplexSolverTest {

    private SimplexSolver simplexSolver;
    
    @Before
    public void setUp() {
        simplexSolver = new SimplexSolver();
    }
    
    @Test
    public void testGetPivotColumn() {
        // TODO: Write test case
    }
    
    @Test
    public void testGetPivotRow() {
        // TODO: Write test case
    }
    
    @Test
    public void testDoIteration() {
        // TODO: Write test case
    }
    
    @Test(expected = UnboundedSolutionException.class)
    public void testDoIterationThrowsUnboundedSolutionException() {
        // TODO: Write test case
    }
    
    @Test
    public void testIsPhase1Solved() {
        // TODO: Write test case
    }
    
    @Test
    public void testIsOptimal() {
        // TODO: Write test case
    }
    
    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1ThrowsNoFeasibleSolutionException() {
        // TODO: Write test case
    }
    
    @Test
    public void testDoOptimize() {
        // TODO: Write test case
    }
}

