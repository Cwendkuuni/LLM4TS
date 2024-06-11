import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        
        // Test when all values are positive
        Integer pivotColumn = simplexSolver.getPivotColumn(tableau);
        assertNull(pivotColumn);

        // Test when there is a negative value
        tableau.setEntry(0, 0, -1);
        pivotColumn = simplexSolver.getPivotColumn(tableau);
        assertEquals(0, pivotColumn.intValue());
    }

    @Test
    public void testGetPivotRow() {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        
        // Test when all values are positive
        Integer pivotRow = simplexSolver.getPivotRow(0, tableau);
        assertNull(pivotRow);

        // Test when there is a negative value
        tableau.setEntry(1, 0, -1);
        pivotRow = simplexSolver.getPivotRow(0, tableau);
        assertEquals(1, pivotRow.intValue());
        
    }

    @Test
    public void testDoIteration() {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        
        // Test when doIteration is called
        try {
            simplexSolver.doIteration(tableau);
        } catch (OptimizationException e) {
            fail("Exception should not be thrown for valid inputs");
        }
        
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        
        // Test when Phase 1 is solved
        boolean phase1Solved = simplexSolver.isPhase1Solved(tableau);
        assertTrue(phase1Solved);

        // Test when Phase 1 is not solved
        tableau.setEntry(0, 1, -1);
        phase1Solved = simplexSolver.isPhase1Solved(tableau);
        assertFalse(phase1Solved);
    }

    @Test
    public void testIsOptimal() {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        
        // Test when model is optimal
        boolean optimal = simplexSolver.isOptimal(tableau);
        assertTrue(optimal);

        // Test when model is not optimal
        tableau.setEntry(0, 1, -1);
        optimal = simplexSolver.isOptimal(tableau);
        assertFalse(optimal);
    }

    @Test
    public void testSolvePhase1() {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        
        // Test when solving Phase 1
        try {
            simplexSolver.solvePhase1(tableau);
        } catch (OptimizationException e) {
            fail("Exception should not be thrown for valid inputs");
        }
    }

    @Test
    public void testDoOptimize() {
        SimplexSolver simplexSolver = new SimplexSolver();
        
        // Test when optimizing
        try {
            RealPointValuePair result = simplexSolver.doOptimize();
            assertNotNull(result);
        } catch (OptimizationException e) {
            fail("Exception should not be thrown for valid inputs");
        }
    }
}