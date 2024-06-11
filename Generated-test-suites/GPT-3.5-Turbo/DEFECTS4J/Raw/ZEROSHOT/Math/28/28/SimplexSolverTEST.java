import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.optimization.PointValuePair;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    private SimplexSolver solver;

    @Before
    public void setUp() {
        solver = new SimplexSolver();
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 0.0, 10);

        assertNull(solver.getPivotColumn(tableau));

        tableau.setEntry(0, 0, -1.0);
        tableau.setEntry(0, 1, -2.0);
        tableau.setEntry(0, 2, -3.0);
        assertEquals(Integer.valueOf(2), solver.getPivotColumn(tableau));

        tableau.setEntry(0, 0, 2.0);
        tableau.setEntry(0, 1, 1.0);
        tableau.setEntry(0, 2, 3.0);
        assertEquals(Integer.valueOf(1), solver.getPivotColumn(tableau));
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 0.0, 10);

        assertNull(solver.getPivotRow(tableau, 0));

        tableau.setEntry(1, 0, 2.0);
        tableau.setEntry(2, 0, 4.0);
        tableau.setEntry(1, 1, 1.0);
        tableau.setEntry(2, 1, 2.0);
        tableau.setEntry(1, tableau.getWidth() - 1, 6.0);
        tableau.setEntry(2, tableau.getWidth() - 1, 12.0);
        assertEquals(Integer.valueOf(1), solver.getPivotRow(tableau, 0));
    }

    @Test
    public void testDoIteration() throws MaxCountExceededException, UnboundedSolutionException {
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 0.0, 10);

        tableau.setEntry(1, 0, 2.0);
        tableau.setEntry(2, 0, 4.0);
        tableau.setEntry(1, 1, 1.0);
        tableau.setEntry(2, 1, 2.0);
        tableau.setEntry(1, tableau.getWidth() - 1, 6.0);
        tableau.setEntry(2, tableau.getWidth() - 1, 12.0);
        solver.doIteration(tableau);

        assertEquals(1.0, tableau.getEntry(1, 0), 0.0);
        assertEquals(0.0, tableau.getEntry(2, 0), 0.0);
        assertEquals(-1.0, tableau.getEntry(1, tableau.getWidth() - 1), 0.0);
        assertEquals(-2.0, tableau.getEntry(2, tableau.getWidth() - 1), 0.0);
    }

    @Test
    public void testSolvePhase1() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 0.0, 10);

        try {
            solver.solvePhase1(tableau);
            fail("Expected NoFeasibleSolutionException to be thrown");
        } catch (NoFeasibleSolutionException e) {
            // expected
        }

        tableau.setEntry(0, 0, -2.0);
        tableau.setEntry(1, 0, 2.0);
        tableau.setEntry(2, 0, 4.0);
        tableau.setEntry(0, 1, -1.0);
        tableau.setEntry(1, 1, 1.0);
        tableau.setEntry(2, 1, 2.0);
        tableau.setEntry(0, tableau.getWidth() - 1, 4.0);
        tableau.setEntry(1, tableau.getWidth() - 1, 6.0);
        tableau.setEntry(2, tableau.getWidth() - 1, 12.0);
        solver.solvePhase1(tableau);

        assertEquals(1.0, tableau.getEntry(1, 0), 0.0);
        assertEquals(0.0, tableau.getEntry(2, 0), 0.0);
        assertEquals(3.0, tableau.getEntry(1, tableau.getWidth() - 1), 0.0);
        assertEquals(4.0, tableau.getEntry(2, tableau.getWidth() - 1), 0.0);
    }

    @Test
    public void testDoOptimize() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 0.0, 10);

        assertNull(solver.doOptimize(tableau));

        tableau.setEntry(0, 0, -2.0);
        tableau.setEntry(1, 0, 2.0);
        tableau.setEntry(2, 0, 4.0);
        tableau.setEntry(0, 1, -1.0);
        tableau.setEntry(1, 1, 1.0);
        tableau.setEntry(2, 1, 2.0);
        tableau.setEntry(0, tableau.getWidth() - 1, 4.0);
        tableau.setEntry(1, tableau.getWidth() - 1, 6.0);
        tableau.setEntry(2, tableau.getWidth() - 1, 12.0);
        PointValuePair solution = solver.doOptimize(tableau);

        assertEquals(1.0, solution.getPoint()[0], 0.0);
        assertEquals(2.0, solution.getPoint()[1], 0.0);
        assertEquals(6.0, solution.getValue(), 0.0);
    }

}