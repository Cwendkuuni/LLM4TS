import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.junit.Before;

public class SimplexSolverTest {
    private SimplexSolver solver;
    
    @Before
    public void setUp() {
        solver = new SimplexSolver();
    }
    
    @Test
    public void testGetPivotColumn() {
        //create a tableau for testing
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 1.0e-6, 10);
        Integer pivotColumn = solver.getPivotColumn(tableau);
        assertNull(pivotColumn);
    }
    
    @Test
    public void testGetPivotRow() {
        //create a tableau for testing
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 1.0e-6, 10);
        Integer pivotRow = solver.getPivotRow(tableau, 0);
        assertNull(pivotRow);
    }
    
    @Test
    public void testDoIteration() throws MaxCountExceededException, UnboundedSolutionException {
        //create a tableau for testing
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 1.0e-6, 10);
        solver.doIteration(tableau);
        //assertions
        assertTrue(tableau.isOptimal());
    }
    
    @Test
    public void testSolvePhase1() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        //create a tableau for testing
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 1.0e-6, 10);
        solver.solvePhase1(tableau);
        //assertions
        assertTrue(tableau.isOptimal());
    }
    
    @Test
    public void testDoOptimize() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        //assertions
        assertNotNull(solver.doOptimize());
    }
}