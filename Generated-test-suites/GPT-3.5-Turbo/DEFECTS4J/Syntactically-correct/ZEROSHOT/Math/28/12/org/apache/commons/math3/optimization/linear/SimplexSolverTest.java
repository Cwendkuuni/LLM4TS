import org.junit.Test;

import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 0.0, 0);
        SimplexSolver solver = new SimplexSolver();
        Integer pivotColumn = solver.getPivotColumn(tableau);
        assertNull(pivotColumn);
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 0.0, 0);
        SimplexSolver solver = new SimplexSolver();
        Integer pivotRow = solver.getPivotRow(tableau, 0);
        assertNull(pivotRow);
    }

    @Test
    public void testDoIteration() throws MaxCountExceededException, UnboundedSolutionException {
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 0.0, 0);
        SimplexSolver solver = new SimplexSolver();
        solver.doIteration(tableau);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testSolvePhase1() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 0.0, 0);
        SimplexSolver solver = new SimplexSolver();
        solver.solvePhase1(tableau);
    }

    @Test
    public void testDoOptimize() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexSolver solver = new SimplexSolver();
        PointValuePair result = solver.doOptimize();
        assertNull(result);
    }
}