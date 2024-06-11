import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        // Typical use case: The tableau has a column with negative coefficients
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, -5.0);
        tableau.setEntry(0, 1, -3.0);
        tableau.setEntry(0, 2, 0.0);
        tableau.setEntry(0, 3, 2.0);
        tableau.setEntry(0, 4, -1.0);
        SimplexSolver solver = new SimplexSolver();
        Integer pivotColumn = solver.getPivotColumn(tableau);
        assertNotNull(pivotColumn);
        assertEquals(0, pivotColumn.intValue());

        // Edge case: The tableau only has non-negative coefficients
        tableau = new SimplexTableau();
        tableau.setEntry(0, 0, 1.0);
        tableau.setEntry(0, 1, 2.0);
        tableau.setEntry(0, 2, 3.0);
        tableau.setEntry(0, 3, 4.0);
        tableau.setEntry(0, 4, 5.0);
        solver = new SimplexSolver();
        pivotColumn = solver.getPivotColumn(tableau);
        assertNull(pivotColumn);
    }

    @Test
    public void testGetPivotRow() {
        // Typical use case: The tableau has a row with minimum ratio
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(1, 3, 3.0);
        tableau.setEntry(1, 4, 5.0);
        tableau.setEntry(1, 5, 7.0);
        tableau.setEntry(1, 6, 2.0);
        tableau.setEntry(1, 7, 4.0);
        SimplexSolver solver = new SimplexSolver();
        Integer pivotRow = solver.getPivotRow(tableau, 3);
        assertNotNull(pivotRow);
        assertEquals(1, pivotRow.intValue());

        // Edge case: The tableau only has non-positive ratios
        tableau = new SimplexTableau();
        tableau.setEntry(1, 3, 0.0);
        tableau.setEntry(1, 4, -1.0);
        tableau.setEntry(1, 5, -2.0);
        tableau.setEntry(1, 6, -3.0);
        tableau.setEntry(1, 7, -4.0);
        solver = new SimplexSolver();
        pivotRow = solver.getPivotRow(tableau, 3);
        assertNull(pivotRow);
    }

    @Test
    public void testDoIteration() {
        // Typical use case: Perform one iteration of the Simplex method
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(1, 2, 1.0);
        tableau.setEntry(1, 3, 2.0);
        tableau.setEntry(1, 4, 3.0);
        tableau.setEntry(1, 5, 4.0);
        tableau.setEntry(1, 6, 5.0);
        tableau.setEntry(1, 7, 6.0);
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.doIteration(tableau);
        } catch (MaxCountExceededException | UnboundedSolutionException e) {
            fail("An exception occurred: " + e.getMessage());
        }
        assertEquals(1.0, tableau.getEntry(1, 1), 0.0);
        assertEquals(0.0, tableau.getEntry(1, 2), 0.0);
        assertEquals(0.0, tableau.getEntry(1, 3), 0.0);
        assertEquals(0.0, tableau.getEntry(1, 4), 0.0);
        assertEquals(0.0, tableau.getEntry(1, 5), 0.0);
        assertEquals(1.0, tableau.getEntry(1, 6), 0.0);

        // Error scenario: The model is found to be unbounded
        tableau = new SimplexTableau();
        tableau.setEntry(1, 2, -1.0);
        tableau.setEntry(1, 3, -2.0);
        tableau.setEntry(1, 4, -3.0);
        tableau.setEntry(1, 5, -4.0);
        tableau.setEntry(1, 6, -5.0);
        tableau.setEntry(1, 7, -6.0);
        solver = new SimplexSolver();
        try {
            solver.doIteration(tableau);
            fail("An exception was expected but not thrown.");
        } catch (MaxCountExceededException e) {
            fail("A different exception occurred: " + e.getMessage());
        } catch (UnboundedSolutionException e) {
            // Exception is expected
        }
    }

    @Test
    public void testSolvePhase1() {
        // Typical use case: Solve Phase 1 of the Simplex method
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, -1.0);
        tableau.setEntry(1, 1, 1.0);
        tableau.setEntry(1, 2, 1.0);
        tableau.setEntry(1, 3, 1.0);
        tableau.setEntry(1, 4, 1.0);
        tableau.setEntry(1, 5, 1.0);
        tableau.setEntry(1, 6, 1.0);
        tableau.setEntry(1, 7, 1.0);
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.solvePhase1(tableau);
        } catch (MaxCountExceededException | UnboundedSolutionException | NoFeasibleSolutionException e) {
            fail("An exception occurred: " + e.getMessage());
        }

        // Error scenario: The model is found to have no feasible solution
        tableau = new SimplexTableau();
        tableau.setEntry(0, 0, -1.0);
        tableau.setEntry(1, 1, 1.0);
        tableau.setEntry(1, 2, 1.0);
        tableau.setEntry(1, 3, 1.0);
        tableau.setEntry(1, 4, 1.0);
        tableau.setEntry(1, 5, 1.0);
        tableau.setEntry(1, 6, 1.0);
        tableau.setEntry(1, 7, 2.0);
        solver = new SimplexSolver();
        try {
            solver.solvePhase1(tableau);
            fail("An exception was expected but not thrown.");
        } catch (MaxCountExceededException | UnboundedSolutionException e) {
            fail("A different exception occurred: " + e.getMessage());
        } catch (NoFeasibleSolutionException e) {
            // Exception is expected
        }
    }

}