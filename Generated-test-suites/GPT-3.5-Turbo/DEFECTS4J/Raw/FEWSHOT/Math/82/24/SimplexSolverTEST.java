import org.junit.Test;

import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        Integer pivotColumn = SimplexSolver.getPivotColumn(tableau);
        assertNotNull(pivotColumn);
        assertEquals(0, pivotColumn.intValue());
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        Integer pivotColumn = SimplexSolver.getPivotColumn(tableau);
        assertNotNull(pivotColumn);
        Integer pivotRow = SimplexSolver.getPivotRow(pivotColumn, tableau);
        assertNotNull(pivotRow);
        assertEquals(1, pivotRow.intValue());
    }

    @Test
    public void testDoIteration() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        try {
            SimplexSolver.doIteration(tableau);
        } catch (OptimizationException e) {
            fail(e.getMessage());
        }
        assertTrue(tableau.isOptimal());
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        boolean phase1Solved = SimplexSolver.isPhase1Solved(tableau);
        assertFalse(phase1Solved);
    }

    @Test
    public void testIsOptimal() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        boolean optimal = SimplexSolver.isOptimal(tableau);
        assertFalse(optimal);
    }

    @Test
    public void testSolvePhase1() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        try {
            SimplexSolver.solvePhase1(tableau);
        } catch (OptimizationException e) {
            fail(e.getMessage());
        }
        assertFalse(tableau.hasFeasibleSolution());
    }

    @Test
    public void testDoOptimize() {
        SimplexSolver simplexSolver = new SimplexSolver();
        try {
            RealPointValuePair result = simplexSolver.doOptimize();
            assertNotNull(result);
        } catch (OptimizationException e) {
            fail(e.getMessage());
        }
    }
}