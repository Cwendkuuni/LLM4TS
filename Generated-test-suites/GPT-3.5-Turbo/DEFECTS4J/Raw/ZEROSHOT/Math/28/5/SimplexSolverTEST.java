import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, 1.0);
        tableau.setEntry(0, 1, -2.0);
        tableau.setEntry(0, 2, 3.0);
        tableau.setEntry(0, 3, -4.0);

        Integer pivotColumn = solver.getPivotColumn(tableau);

        assertEquals(Integer.valueOf(3), pivotColumn);
    }

    @Test
    public void testGetPivotRow() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(1, 0, 1.0);
        tableau.setEntry(2, 0, 2.0);
        tableau.setEntry(3, 0, 3.0);
        tableau.setEntry(4, 0, 4.0);

        Integer pivotRow = solver.getPivotRow(tableau, 0);

        assertEquals(Integer.valueOf(4), pivotRow);
    }

    @Test
    public void testDoIteration() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, 1.0);
        tableau.setEntry(1, 0, 2.0);
        tableau.setEntry(1, 1, -1.0);

        try {
            solver.doIteration(tableau);
            double expectedEntry00 = tableau.getEntry(0, 0);
            double expectedEntry10 = tableau.getEntry(1, 0);
            double expectedEntry11 = tableau.getEntry(1, 1);

            assertEquals(1.0, expectedEntry00, 0.000001);
            assertEquals(0.0, expectedEntry10, 0.000001);
            assertEquals(0.0, expectedEntry11, 0.000001);
        } catch (MaxCountExceededException ex) {
            fail("MaxCountExceededException thrown");
        } catch (UnboundedSolutionException ex) {
            fail("UnboundedSolutionException thrown");
        }
    }

    @Test
    public void testSolvePhase1() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, 1.0);
        tableau.setEntry(1, 0, -1.0);
        tableau.setEntry(2, 0, 1.0);
        tableau.setEntry(2, 1, -1.0);

        try {
            solver.solvePhase1(tableau);
            double expectedEntry00 = tableau.getEntry(0, 0);
            double expectedEntry10 = tableau.getEntry(1, 0);
            double expectedEntry11 = tableau.getEntry(1, 1);
            double expectedEntry20 = tableau.getEntry(2, 0);
            double expectedEntry21 = tableau.getEntry(2, 1);

            assertEquals(1.0, expectedEntry00, 0.000001);
            assertEquals(0.0, expectedEntry10, 0.000001);
            assertEquals(0.0, expectedEntry11, 0.000001);
            assertEquals(0.0, expectedEntry20, 0.000001);
            assertEquals(0.0, expectedEntry21, 0.000001);
        } catch (MaxCountExceededException ex) {
            fail("MaxCountExceededException thrown");
        } catch (UnboundedSolutionException ex) {
            fail("UnboundedSolutionException thrown");
        } catch (NoFeasibleSolutionException ex) {
            fail("NoFeasibleSolutionException thrown");
        }
    }

    @Test
    public void testDoOptimize() {
        SimplexSolver solver = new SimplexSolver();
        try {
            PointValuePair result = solver.doOptimize();

            assertNotNull(result);
        } catch (MaxCountExceededException ex) {
            fail("MaxCountExceededException thrown");
        } catch (UnboundedSolutionException ex) {
            fail("UnboundedSolutionException thrown");
        } catch (NoFeasibleSolutionException ex) {
            fail("NoFeasibleSolutionException thrown");
        }
    }

}