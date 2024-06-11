import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.linear.NoFeasibleSolutionException;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.UnboundedSolutionException;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();

        Integer pivotColumn = solver.getPivotColumn(tableau);

        assertNull(pivotColumn);
    }

    @Test
    public void testGetPivotRow() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();

        Integer pivotRow = solver.getPivotRow(tableau, 0);

        assertNull(pivotRow);
    }

    @Test
    public void testDoIteration() throws MaxCountExceededException, UnboundedSolutionException {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();

        solver.doIteration(tableau);

        // TODO: Add assertions
    }

    @Test
    public void testSolvePhase1() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();

        solver.solvePhase1(tableau);

        // TODO: Add assertions
    }

    @Test
    public void testDoOptimize() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexSolver solver = new SimplexSolver();

        PointValuePair solution = solver.doOptimize();

        assertNotNull(solution);
    }

}