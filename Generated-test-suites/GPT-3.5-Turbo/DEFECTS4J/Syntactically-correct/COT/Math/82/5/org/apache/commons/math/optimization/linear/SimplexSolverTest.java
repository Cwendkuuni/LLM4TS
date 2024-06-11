
import static org.junit.Assert.*;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau();
        // Test when all coefficients are positive 
        // It should return the column with the smallest negative coefficient
        assertEquals(2, simplexSolver.getPivotColumn(tableau).intValue());

        // Test when all coefficients are negative
        // It should return the column with the smallest negative coefficient
        assertEquals(0, simplexSolver.getPivotColumn(tableau).intValue());

        // Test when some coefficients are positive and some are negative
        // It should return the column with the smallest negative coefficient
        assertEquals(2, simplexSolver.getPivotColumn(tableau).intValue());

        // Test when all coefficients are zero
        // It should return null
        assertNull(simplexSolver.getPivotColumn(tableau));
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau();
        int col = 1;
        // Test when all ratios are positive
        // It should return the row with the minimum ratio
        assertEquals(3, simplexSolver.getPivotRow(col, tableau).intValue());

        // Test when all ratios are negative
        // It should return null
        assertNull(simplexSolver.getPivotRow(col, tableau));

        // Test when some ratios are positive and some are negative
        // It should return the row with the minimum ratio
        assertEquals(3, simplexSolver.getPivotRow(col, tableau).intValue());

        // Test when all ratios are zero
        // It should return null
        assertNull(simplexSolver.getPivotRow(col, tableau));
    }

    @Test
    public void testDoIteration() {
        SimplexTableau tableau = new SimplexTableau();
        // Test when the tableau has a pivot column and row
        // It should update the tableau according to the simplex method
        simplexSolver.doIteration(tableau);
        assertEquals(1, tableau.getEntry(0, 0), epsilon);
        assertEquals(0, tableau.getEntry(1, 0), epsilon);
        assertEquals(3, tableau.getEntry(1, 1), epsilon);
        assertEquals(0, tableau.getEntry(2, 0), epsilon);
        assertEquals(4, tableau.getEntry(2, 1), epsilon);

        // Test when the tableau has no pivot column
        // It should throw an UnboundedSolutionException
        simplexSolver.doIteration(tableau);
        assertEquals(UnboundedSolutionException.class, ExceptionUtils.getRootCause(throwable).getClass());

        // Test when the tableau has no pivot row
        // It should throw an UnboundedSolutionException
        simplexSolver.doIteration(tableau);
        assertEquals(UnboundedSolutionException.class, ExceptionUtils.getRootCause(throwable).getClass());
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = new SimplexTableau();
        // Test when the tableau has no artificial variables
        // It should return true
        assertTrue(simplexSolver.isPhase1Solved(tableau));

        // Test when the tableau has artificial variables but all coefficients are non-negative
        // It should return true
        assertTrue(simplexSolver.isPhase1Solved(tableau));

        // Test when the tableau has artificial variables and some coefficients are negative
        // It should return false
        assertFalse(simplexSolver.isPhase1Solved(tableau));
    }

    @Test
    public void testIsOptimal() {
        SimplexTableau tableau = new SimplexTableau();
        // Test when all coefficients are non-negative
        // It should return true
        assertTrue(simplexSolver.isOptimal(tableau));

        // Test when all coefficients are zero
        // It should return true
        assertTrue(simplexSolver.isOptimal(tableau));

        // Test when some coefficients are negative
        // It should return false
        assertFalse(simplexSolver.isOptimal(tableau));
    }

    @Test
    public void testSolvePhase1() {
        SimplexTableau tableau = new SimplexTableau();
        // Test when the tableau has no artificial variables
        // It should not modify the tableau
        simplexSolver.solvePhase1(tableau);
        assertEquals(0, tableau.getWidth());

        // Test when the tableau has artificial variables and has a feasible solution
        // It should update the tableau according to Phase 1 of the simplex method
        simplexSolver.solvePhase1(tableau);
        assertEquals(0, ExceptionUtils.getThrowables(throwable).size());

        // Test when the tableau has artificial variables and has no feasible solution
        // It should throw a NoFeasibleSolutionException
        simplexSolver.solvePhase1(tableau);
        assertEquals(NoFeasibleSolutionException.class, ExceptionUtils.getRootCause(throwable).getClass());
    }

    @Test
    public void testDoOptimize() {
        // Test when the model has a feasible solution and is optimal
        // It should return the optimized solution
        assertEquals(0, simplexSolver.doOptimize(), epsilon);

        // Test when the model has a feasible solution but is not optimal
        // It should return the optimized solution
        assertEquals(0, simplexSolver.doOptimize(), epsilon);

        // Test when the model has no feasible solution
        // It should throw a NoFeasibleSolutionException
        simplexSolver.doOptimize();
        assertEquals(NoFeasibleSolutionException.class, ExceptionUtils.getRootCause(throwable).getClass());
    }

}

