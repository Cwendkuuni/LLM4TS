import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.optimization.linear.SimplexSolver;
import org.junit.Test;

public class SimplexSolverTest {
    
    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        Integer pivotColumn = Util.getPivotColumn(tableau);
        assertNotNull(pivotColumn);
        // add more assertions here
    }
    
    @Test(expected = UnboundedSolutionException.class)
    public void testDoIterationThrowsException() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        Util.doIteration(tableau);
    }
    
    @Test
    public void testIsOptimal() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        boolean isOptimal = Util.isOptimal(tableau);
        // add assertion here
    }
    
    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1ThrowsException() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        Util.solvePhase1(tableau);
    }
    
    @Test
    public void testDoOptimize() throws OptimizationException {
        SimplexSolver solver = new SimplexSolver();
        RealPointValuePair result = solver.doOptimize();
        assertNotNull(result);
        // add more assertions here
    }
    
}