import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, -1);
        tableau.setEntry(0, 1, -2);
        tableau.setEntry(0, 2, -3);
        tableau.setEntry(0, 3, -4);
        Integer pivotColumn = solver.getPivotColumn(tableau);
        assertEquals(Integer.valueOf(3), pivotColumn);
    }

    @Test
    public void testGetPivotRow() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(1, 0, 1);
        tableau.setEntry(2, 0, 2);
        tableau.setEntry(1, 1, 3);
        tableau.setEntry(2, 1, 4);
        tableau.setEntry(1, 2, 5);
        tableau.setEntry(2, 2, 6);
        Integer pivotRow = solver.getPivotRow(tableau, 2);
        assertEquals(Integer.valueOf(1), pivotRow);
    }

    @Test
    public void testDoIteration() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(1, 0, 2);
        tableau.setEntry(2, 0, 3);
        tableau.setEntry(0, 1, 4);
        tableau.setEntry(1, 1, 5);
        tableau.setEntry(2, 1, 6);
        solver.doIteration(tableau);
        assertEquals(0.0, tableau.getEntry(0, 0), 0.001);
        assertEquals(1.0, tableau.getEntry(1, 0), 0.001);
        assertEquals(2.0, tableau.getEntry(2, 0), 0.001);
        assertEquals(0.0, tableau.getEntry(0, 1), 0.001);
        assertEquals(1.0, tableau.getEntry(1, 1), 0.001);
        assertEquals(2.0, tableau.getEntry(2, 1), 0.001);
    }

    @Test
    public void testSolvePhase1() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 1, -1);
        tableau.setEntry(0, 2, -2);
        tableau.setEntry(1, 0, 1);
        tableau.setEntry(1, 1, 2);
        tableau.setEntry(1, 2, 3);
        try {
            solver.solvePhase1(tableau);
            fail("NoFeasibleSolutionException not thrown.");
        } catch (NoFeasibleSolutionException e) {
            // pass
        } catch (Exception e) {
            fail("Unexpected exception thrown.");
        }
    }

    @Test
    public void testDoOptimize() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(0, 1, 2);
        tableau.setEntry(1, 0, 3);
        tableau.setEntry(1, 1, 4);
        try {
            PointValuePair result = solver.doOptimize();
            assertNotNull(result);
        } catch (Exception e) {
            fail("Unexpected exception thrown.");
        }
    }

}