import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.optimization.linear.SimplexSolver;
import org.apache.commons.math.optimization.linear.SimplexTableau;
import org.apache.commons.math.optimization.linear.NoFeasibleSolutionException;
import org.apache.commons.math.optimization.linear.UnboundedSolutionException;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.junit.Before;

public class SimplexSolverTest {
    
    private SimplexSolver solver;

    @Before
    public void setUp() {
        solver = new SimplexSolver();
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau();
        Integer pivotColumn = solver.getPivotColumn(tableau);
        assertNull(pivotColumn);
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau();
        Integer pivotRow = solver.getPivotRow(0, tableau);
        assertNull(pivotRow);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoIterationWithUnboundedSolution() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau();
        solver.doIteration(tableau);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1WithNoFeasibleSolution() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau();
        solver.solvePhase1(tableau);
    }

    @Test
    public void testIsOptimal() {
        SimplexTableau tableau = new SimplexTableau();
        assertFalse(solver.isOptimal(tableau));
    }

    @Test
    public void testDoOptimize() throws OptimizationException {
        RealPointValuePair result = solver.doOptimize();
        assertNotNull(result);
    }

}