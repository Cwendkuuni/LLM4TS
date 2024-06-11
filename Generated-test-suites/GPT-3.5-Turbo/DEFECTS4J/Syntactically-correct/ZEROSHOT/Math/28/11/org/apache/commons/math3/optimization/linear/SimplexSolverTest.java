import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.apache.commons.math3.optimization.linear.NoFeasibleSolutionException;
import org.apache.commons.math3.optimization.linear.UnboundedSolutionException;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    private SimplexSolver solver;

    @Before
    public void setUp() {
        solver = new SimplexSolver();
    }

    @Test
    public void testGetPivotColumn() {
        double[][] tableauData = {
            { 1.0, 2.0, 3.0 },
            { 4.0, -5.0, 6.0 },
            { 7.0, 8.0, -9.0 },
        };
        SimplexTableau tableau = new SimplexTableau(tableauData);
        Integer pivotColumn = solver.getPivotColumn(tableau);
        assertEquals(pivotColumn, new Integer(1));
    }

    @Test
    public void testGetPivotRow() {
        double[][] tableauData = {
            { 1.0, 2.0, 3.0 },
            { 4.0, -5.0, 6.0 },
            { 7.0, 8.0, -9.0 },
        };
        SimplexTableau tableau = new SimplexTableau(tableauData);
        Integer pivotRow = solver.getPivotRow(tableau, 1);
        assertEquals(pivotRow, new Integer(1));
    }

    @Test
    public void testDoIteration() throws MaxCountExceededException, UnboundedSolutionException {
        double[][] tableauData = {
            { 1.0, -2.0, 3.0, 4.0 },
            { 5.0, 6.0, 7.0, -8.0 },
            { -9.0, 10.0, 11.0, 12.0 },
        };
        SimplexTableau tableau = new SimplexTableau(tableauData);
        solver.doIteration(tableau);
        double[][] expectedData = {
            { 1.0, 0.0, -1.0, 4.0 },
            { 0.0, 6.0, 12.0, -8.0 },
            { 0.0, 10.0, -98.0, 12.0 },
        };
        assertArrayEquals(tableau.getData(), expectedData);
    }

    @Test
    public void testSolvePhase1() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        double[][] tableauData = {
            { 1.0, -2.0, 3.0, 4.0 },
            { 5.0, 6.0, 7.0, -8.0 },
            { -9.0, 10.0, -11.0, 12.0 },
        };
        SimplexTableau tableau = new SimplexTableau(tableauData);
        solver.solvePhase1(tableau);
        double[][] expectedData = {
            { 0.0, -2.0, 3.0, 4.0 },
            { 1.0, 6.0, 7.0, -8.0 },
            { 0.0, 10.0, -11.0, 12.0 },
        };
        assertArrayEquals(tableau.getData(), expectedData);
    }

    @Test
    public void testDoOptimize() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        // TODO: write test case for doOptimize()
    }

}