
import static org.junit.Assert.*;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.junit.Test;

public class SimplexSolverTest {
    
    @Test
    public void testGetPivotColumn() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(new double[][]{{1, 2, 3}, {4, 5, 6}});
        Integer pivotColumn = solver.getPivotColumn(tableau);
        assertEquals(pivotColumn, Integer.valueOf(0));
    }
    
    @Test
    public void testGetPivotRow() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(new double[][]{{1, 2, 3}, {4, 5, 6}});
        Integer pivotRow = solver.getPivotRow(tableau, 0);
        assertEquals(pivotRow, Integer.valueOf(1));
    }
    
    @Test
    public void testDoIteration() throws MaxCountExceededException, UnboundedSolutionException {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(new double[][]{{1, 2, 3, 4}, {4, 5, 6, 7}});
        solver.doIteration(tableau);
        assertEquals(tableau.getEntry(1, 0), 0d, 0.000001);
    }
    
    @Test
    public void testSolvePhase1() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(new double[][]{{1, 2, 3, 4}, {4, 5, 6, 7}});
        solver.solvePhase1(tableau);
        assertFalse(tableau.isOptimal());
    }
    
    @Test
    public void testDoOptimize() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.doOptimize();
        assertNotNull(solution);
    }
}
