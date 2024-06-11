import static org.junit.Assert.*;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        // Create a SimplexSolver object
        SimplexSolver simplexSolver = new SimplexSolver();

        // Create a SimplexTableau object for testing
        SimplexTableau tableau = new SimplexTableau();

        // Set up the tableau for testing
        tableau.setEntry(0, 0, -1);
        tableau.setEntry(0, 1, 2);
        tableau.setEntry(0, 2, 3);

        // Call the getPivotColumn method and assert the result
        assertEquals(0, simplexSolver.getPivotColumn(tableau));
    }

    @Test
    public void testGetPivotRow() {
        // Create a SimplexSolver object
        SimplexSolver simplexSolver = new SimplexSolver();

        // Create a SimplexTableau object for testing
        SimplexTableau tableau = new SimplexTableau();

        // Set up the tableau for testing
        tableau.setEntry(0, 0, -1);
        tableau.setEntry(1, 0, 2);
        tableau.setEntry(2, 0, 3);

        // Call the getPivotRow method and assert the result
        assertEquals(2, simplexSolver.getPivotRow(tableau, 0));
    }

    @Test
    public void testDoIteration() {
        // Create a SimplexSolver object
        SimplexSolver simplexSolver = new SimplexSolver();

        // Create a SimplexTableau object for testing
        SimplexTableau tableau = new SimplexTableau();

        // Set up the tableau for testing
        tableau.setEntry(0, 0, -1);
        tableau.setEntry(1, 0, 2);
        tableau.setEntry(1, 1, 1);
        tableau.setEntry(2, 0, 3);
        tableau.setEntry(2, 2, 2);

        // Call the doIteration method
        simplexSolver.doIteration(tableau);

        // Assert the result
        assertEquals(0, tableau.getEntry(0, 0), 0);
        assertEquals(0, tableau.getEntry(1, 0), 0);
        assertEquals(0, tableau.getEntry(2, 0), 0);
        assertEquals(1, tableau.getEntry(1, 1), 0);
        assertEquals(0, tableau.getEntry(2, 2), 0);
    }

    @Test
    public void testSolvePhase1() {
        // Create a SimplexSolver object
        SimplexSolver simplexSolver = new SimplexSolver();

        // Create a SimplexTableau object for testing
        SimplexTableau tableau = new SimplexTableau();

        // Set up the tableau for testing
        tableau.setEntry(0, 0, -1);
        tableau.setEntry(1, 0, 2);
        tableau.setEntry(2, 0, 3);

        // Call the solvePhase1 method
        simplexSolver.solvePhase1(tableau);

        // Assert the result
        assertEquals(0, tableau.getEntry(0, 0), 0);
        assertEquals(0, tableau.getEntry(1, 0), 0);
        assertEquals(0, tableau.getEntry(2, 0), 0);
    }

    @Test
    public void testDoOptimize() {
        // Create a SimplexSolver object
        SimplexSolver simplexSolver = new SimplexSolver();

        // Call the doOptimize method and assert the result
        assertThrows(MaxCountExceededException.class, () -> simplexSolver.doOptimize());
    }
}