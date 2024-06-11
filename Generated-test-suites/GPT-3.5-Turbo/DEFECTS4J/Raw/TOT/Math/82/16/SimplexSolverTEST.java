import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau();
        // Test case for an empty tableau
        assertNull(tableau.getPivotColumn());

        // Test case for a tableau with a single column and negative coefficient
        tableau.setEntry(0, 0, -1);
        assertEquals((Integer) 0, tableau.getPivotColumn());

        // Test case for a tableau with a single column and positive coefficient
        tableau.setEntry(0, 0, 1);
        assertNull(tableau.getPivotColumn());

        // Test case for a tableau with multiple columns and negative coefficient
        tableau.setEntry(0, 1, -2);
        tableau.setEntry(0, 2, -3);
        assertEquals((Integer) 2, tableau.getPivotColumn());

        // Test case for a tableau with multiple columns and all positive coefficients
        tableau.setEntry(0, 1, 2);
        tableau.setEntry(0, 2, 3);
        assertNull(tableau.getPivotColumn());
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau();
        // Test case for an empty tableau
        assertNull(tableau.getPivotRow(0));

        // Test case for a tableau with a single row and non-zero ratio
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(0, 1, 2);
        assertEquals((Integer) 0, tableau.getPivotRow(1));

        // Test case for a tableau with a single row and zero ratio
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(0, 1, 0);
        assertNull(tableau.getPivotRow(1));

        // Test case for a tableau with multiple rows and non-zero ratio
        tableau.setEntry(1, 0, 1);
        tableau.setEntry(1, 1, 2);
        tableau.setEntry(2, 0, 1);
        tableau.setEntry(2, 1, 3);
        assertEquals((Integer) 2, tableau.getPivotRow(1));

        // Test case for a tableau with multiple rows and zero ratio
        tableau.setEntry(1, 0, 0);
        tableau.setEntry(1, 1, 2);
        tableau.setEntry(2, 0, 3);
        tableau.setEntry(2, 1, 0);
        assertNull(tableau.getPivotRow(1));
    }

    @Test
    public void testDoIteration() {
        SimplexTableau tableau = new SimplexTableau();
        // Test case for a tableau with a single row and column
        tableau.setEntry(0, 0, 1);
        tableau.doIteration();
        assertEquals(1, tableau.getEntry(0, 0), 0);

        // Test case for a tableau with multiple rows and columns
        tableau = new SimplexTableau();
        tableau.setNumColumns(2);
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(0, 1, -2);
        tableau.setEntry(1, 0, 3);
        tableau.setEntry(1, 1, 4);
        tableau.doIteration();
        assertEquals(0, tableau.getEntry(0, 0), 0);
        assertEquals(1, tableau.getEntry(1, 1), 0);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoIterationUnboundedSolutionException() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(1, 0, 1);
        tableau.doIteration();
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = new SimplexTableau();
        // Test case for a tableau with no artificial variables
        assertTrue(tableau.isPhase1Solved());

        // Test case for a tableau with all positive coefficients
        tableau.setEntry(0, 0, 1);
        assertTrue(tableau.isPhase1Solved());

        // Test case for a tableau with a negative coefficient
        tableau.setEntry(0, 0, -1);
        assertFalse(tableau.isPhase1Solved());
    }

    @Test
    public void testIsOptimal() {
        SimplexTableau tableau = new SimplexTableau();
        // Test case for a tableau with no negative coefficients
        assertTrue(tableau.isOptimal());

        // Test case for a tableau with a negative coefficient
        tableau.setEntry(0, 0, -1);
        assertFalse(tableau.isOptimal());
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1NoFeasibleSolutionException() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, 1);
        tableau.solvePhase1();
    }

    @Test
    public void testDoOptimize() {
        SimplexSolver solver = new SimplexSolver();
        // Test case for a problem with a feasible solution
        assertTrue(solver.doOptimize().isSolutionFeasible());

        // Test case for a problem with an infeasible solution
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, 1);
        solver = new SimplexSolver();
        assertFalse(solver.doOptimize().isSolutionFeasible());
    }
}