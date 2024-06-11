import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    private static final double epsilon = 1.0e-6;

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau();
        
        // Test when there is a column with a negative coefficient
        tableau.setEntry(0, 0, -1.0);
        Integer expectedColumn = 0;
        Integer actualColumn = SimplexSolver.getPivotColumn(tableau);
        assertEquals(expectedColumn, actualColumn);

        // Test when there is no column with a negative coefficient
        tableau.setEntry(0, 0, 1.0);
        expectedColumn = null;
        actualColumn = SimplexSolver.getPivotColumn(tableau);
        assertEquals(expectedColumn, actualColumn);
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau();

        // Test when there is a row with a positive ratio
        tableau.setEntry(1, 1, 1.0);
        tableau.setEntry(1, 2, 2.0);
        tableau.setEntry(1, 3, 3.0);
        tableau.setEntry(1, 4, 4.0);
        tableau.setEntry(1, 5, 5.0);
        tableau.setEntry(1, 6, 6.0);
        tableau.setEntry(1, 7, 7.0);
        tableau.setEntry(1, 8, 8.0);
        Integer expectedRow = 1;
        Integer actualRow = SimplexSolver.getPivotRow(1, tableau);
        assertEquals(expectedRow, actualRow);

        // Test when there is no row with a positive ratio
        tableau.setEntry(1, 1, -1.0);
        tableau.setEntry(1, 2, -2.0);
        tableau.setEntry(1, 3, -3.0);
        tableau.setEntry(1, 4, -4.0);
        tableau.setEntry(1, 5, -5.0);
        tableau.setEntry(1, 6, -6.0);
        tableau.setEntry(1, 7, -7.0);
        tableau.setEntry(1, 8, -8.0);
        expectedRow = null;
        actualRow = SimplexSolver.getPivotRow(1, tableau);
        assertEquals(expectedRow, actualRow);
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = new SimplexTableau();

        // Test when there are artificial variables and all coefficients in the objective function row are positive
        tableau.setNumArtificialVariables(2);
        tableau.setEntry(0, 1, 1.0);
        tableau.setEntry(0, 2, 1.0);
        boolean expected = true;
        boolean actual = SimplexSolver.isPhase1Solved(tableau);
        assertEquals(expected, actual);

        // Test when there are artificial variables and there is a negative coefficient in the objective function row
        tableau.setEntry(0, 1, -1.0);
        expected = false;
        actual = SimplexSolver.isPhase1Solved(tableau);
        assertEquals(expected, actual);

        // Test when there are no artificial variables
        tableau.setNumArtificialVariables(0);
        expected = true;
        actual = SimplexSolver.isPhase1Solved(tableau);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsOptimal() {
        SimplexTableau tableau = new SimplexTableau();

        // Test when there are artificial variables and all coefficients in the objective function row are positive
        tableau.setNumArtificialVariables(2);
        tableau.setEntry(0, 1, 1.0);
        tableau.setEntry(0, 2, 1.0);
        boolean expected = false;
        boolean actual = SimplexSolver.isOptimal(tableau);
        assertEquals(expected, actual);

        // Test when there are artificial variables and there is a negative coefficient in the objective function row
        tableau.setEntry(0, 1, -1.0);
        expected = false;
        actual = SimplexSolver.isOptimal(tableau);
        assertEquals(expected, actual);

        // Test when there are no artificial variables and all coefficients in the objective function row are positive
        tableau.setNumArtificialVariables(0);
        tableau.setEntry(0, 1, 1.0);
        tableau.setEntry(0, 2, 1.0);
        expected = true;
        actual = SimplexSolver.isOptimal(tableau);
        assertEquals(expected, actual);

        // Test when there are no artificial variables and there is a negative coefficient in the objective function row
        tableau.setEntry(0, 1, -1.0);
        expected = false;
        actual = SimplexSolver.isOptimal(tableau);
        assertEquals(expected, actual);
    }

    @Test
    public void testDoIteration() {
        SimplexTableau tableau = new SimplexTableau();

        // Test when the pivot row is null
        tableau.setEntry(0, 1, 1.0);
        assertThrows(UnboundedSolutionException.class, () -> {
            SimplexSolver.doIteration(tableau);
        });

        // Test when the pivot row is not null
        tableau.setEntry(1, 1, 1.0);
        tableau.setEntry(1, 2, 2.0);
        tableau.setEntry(1, 3, 3.0);
        tableau.setEntry(1, 4, 4.0);
        tableau.setEntry(1, 5, 5.0);
        tableau.setEntry(1, 6, 6.0);
        tableau.setEntry(1, 7, 7.0);
        tableau.setEntry(1, 8, 8.0);
        tableau.setEntry(1, 0, -2.0);
        SimplexSolver.doIteration(tableau);
        double expected = 1.0;
        double actual = tableau.getEntry(1, 0);
        assertEquals(expected, actual, epsilon);
    }

    @Test
    public void testSolvePhase1() {
        SimplexTableau tableau = new SimplexTableau();

        // Test when there is a feasible solution
        tableau.setNumArtificialVariables(1);
        tableau.setEntry(0, 1, 1.0);
        tableau.setEntry(0, 2, 1.0);
        assertDoesNotThrow(() -> {
            SimplexSolver.solvePhase1(tableau);
        });

        // Test when there is no feasible solution
        tableau.setEntry(0, 1, -1.0);
        assertThrows(NoFeasibleSolutionException.class, () -> {
            SimplexSolver.solvePhase1(tableau);
        });
    }

    @Test
    public void testDoOptimize() {
        SimplexSolver solver = new SimplexSolver();

        // Test when there is a feasible solution
        SimplexTableau tableau1 = new SimplexTableau();
        tableau1.setNumArtificialVariables(1);
        tableau1.setEntry(0, 1, 1.0);
        tableau1.setEntry(0, 2, 1.0);
        RealPointValuePair expected1 = new RealPointValuePair(new double[]{0.0, 0.0, 0.0}, 0.0);
        RealPointValuePair actual1 = solver.doOptimize(tableau1);
        assertArrayEquals(expected1.getPoint(), actual1.getPoint(), epsilon);
        assertEquals(expected1.getValue(), actual1.getValue(), epsilon);

        // Test when there is no feasible solution
        SimplexTableau tableau2 = new SimplexTableau();
        tableau2.setEntry(0, 1, -1.0);
        assertThrows(NoFeasibleSolutionException.class, () -> {
            solver.doOptimize(tableau2);
        });
    }


}