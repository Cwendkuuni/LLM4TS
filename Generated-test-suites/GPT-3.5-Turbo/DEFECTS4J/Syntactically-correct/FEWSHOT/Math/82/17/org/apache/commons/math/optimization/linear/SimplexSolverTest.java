import static org.junit.Assert.*;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        
        // Test when the tableau has only one column
        assertEquals(null, getPivotColumn(tableau));

        // Test when the tableau has multiple columns
        tableau.setEntry(0, 0, -5);
        tableau.setEntry(0, 1, 10);
        tableau.setEntry(0, 2, -15);
        assertEquals(0, getPivotColumn(tableau));

        tableau.setEntry(0, 0, -10);
        tableau.setEntry(0, 1, -5);
        tableau.setEntry(0, 2, -20);
        assertEquals(2, getPivotColumn(tableau));

        tableau.setEntry(0, 0, 0);
        tableau.setEntry(0, 1, 0);
        tableau.setEntry(0, 2, 0);
        assertEquals(null, getPivotColumn(tableau));
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        
        // Test when the tableau has only one row
        assertEquals(null, getPivotRow(0, tableau));

        // Test when the tableau has multiple rows
        tableau.setEntry(1, 0, -5);
        tableau.setEntry(1, 1, 10);
        tableau.setEntry(1, 2, -15);
        assertEquals(1, getPivotRow(0, tableau));

        tableau.setEntry(1, 0, -10);
        tableau.setEntry(1, 1, -5);
        tableau.setEntry(1, 2, -20);
        assertEquals(1, getPivotRow(2, tableau));

        tableau.setEntry(1, 0, 0);
        tableau.setEntry(1, 1, 0);
        tableau.setEntry(1, 2, 0);
        assertEquals(null, getPivotRow(0, tableau));
    }

    @Test
    public void testDoIteration() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        
        // Test when the tableau is empty
        doIteration(tableau);

        tableau.setEntry(0, 0, -5);
        tableau.setEntry(0, 1, 10);
        tableau.setEntry(0, 2, -15);
        doIteration(tableau);
        assertEquals(1.0, tableau.getEntry(0, 0), epsilon);
        assertEquals(1.0, tableau.getEntry(0, 1), epsilon);
        assertEquals(0.0, tableau.getEntry(0, 2), epsilon);

        tableau.setEntry(0, 0, -10);
        tableau.setEntry(0, 1, -5);
        tableau.setEntry(0, 2, -20);
        doIteration(tableau);
        assertEquals(1.0, tableau.getEntry(0, 0), epsilon);
        assertEquals(0.0, tableau.getEntry(0, 1), epsilon);
        assertEquals(-10.0, tableau.getEntry(0, 2), epsilon);

        tableau.setEntry(0, 0, 0);
        tableau.setEntry(0, 1, 0);
        tableau.setEntry(0, 2, 0);
        doIteration(tableau);
        assertEquals(0.0, tableau.getEntry(0, 0), epsilon);
        assertEquals(0.0, tableau.getEntry(0, 1), epsilon);
        assertEquals(0.0, tableau.getEntry(0, 2), epsilon);

    }

    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        
        // Test when there are no artificial variables
        assertTrue(isPhase1Solved(tableau));

        // Test when there are artificial variables but all coefficients are non-zero
        tableau.setEntry(0, 0, -5);
        tableau.setEntry(0, 1, 10);
        tableau.setEntry(0, 2, -15);
        assertFalse(isPhase1Solved(tableau));

        // Test when there are artificial variables and some coefficients are zero
        tableau.setEntry(0, 0, -10);
        tableau.setEntry(0, 1, -5);
        tableau.setEntry(0, 2, -20);
        assertFalse(isPhase1Solved(tableau));

        // Test when there are only artificial variables with zero coefficients
        tableau.setEntry(0, 0, 0);
        tableau.setEntry(0, 1, 0);
        tableau.setEntry(0, 2, 0);
        assertTrue(isPhase1Solved(tableau));

    }

    @Test
    public void testIsOptimal() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        
        // Test when all coefficients are non-zero
        tableau.setEntry(0, 0, -5);
        tableau.setEntry(0, 1, 10);
        tableau.setEntry(0, 2, -15);
        assertFalse(isOptimal(tableau));

        // Test when some coefficients are zero
        tableau.setEntry(0, 0, -10);
        tableau.setEntry(0, 1, -5);
        tableau.setEntry(0, 2, -20);
        assertFalse(isOptimal(tableau));

        // Test when all coefficients are zero
        tableau.setEntry(0, 0, 0);
        tableau.setEntry(0, 1, 0);
        tableau.setEntry(0, 2, 0);
        assertTrue(isOptimal(tableau));

    }

    @Test
    public void testSolvePhase1() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        
        // Test when there are no artificial variables
        solvePhase1(tableau);
        assertTrue(isPhase1Solved(tableau));

        // Test when there are artificial variables but no feasible solution
        tableau.setEntry(0, 0, -10);
        tableau.setEntry(0, 1, -5);
        tableau.setEntry(0, 2, -20);
        try {
            solvePhase1(tableau);
            fail("Expected NoFeasibleSolutionException");
        } catch (NoFeasibleSolutionException e) {
            // Expected exception
        }

        // Test when there are artificial variables with feasible solution
        tableau.setEntry(0, 0, 0);
        tableau.setEntry(0, 1, 0);
        tableau.setEntry(0, 2, 0);
        solvePhase1(tableau);
        assertTrue(isPhase1Solved(tableau));

    }

    @Test
    public void testDoOptimize() {
        SimplexSolver solver = new SimplexSolver();
        
        // Test when there is no feasible solution
        SimplexTableau tableau1 = new SimplexTableau(f, constraints1, goalType, restrictToNonNegative, epsilon);
        try {
            solver.doOptimize();
            fail("Expected NoFeasibleSolutionException");
        } catch (NoFeasibleSolutionException e) {
            // Expected exception
        }

        // Test when there is a feasible solution
        SimplexTableau tableau2 = new SimplexTableau(f, constraints2, goalType, restrictToNonNegative, epsilon);
        RealPointValuePair result = solver.doOptimize();
        double[] expectedPoint = {1.0, 2.0};
        assertEquals(expectedPoint, result.getPoint());
        assertEquals(5.0, result.getValue(), epsilon);
    }
}