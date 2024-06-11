import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimplexSolverTest {

    private SimplexSolver solver;

    @Before
    public void setUp() {
        solver = new SimplexSolver();
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau();
        Integer pivotColumn = solver.getPivotColumn(tableau);
        assertNull(pivotColumn);
        tableau.setEntry(0, 0, -1);
        tableau.setEntry(0, 1, -2);
        pivotColumn = solver.getPivotColumn(tableau);
        assertEquals(0, pivotColumn.intValue());
        tableau.setEntry(0, 1, 2);
        pivotColumn = solver.getPivotColumn(tableau);
        assertEquals(1, pivotColumn.intValue());
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau();
        Integer pivotRow = solver.getPivotRow(0, tableau);
        assertNull(pivotRow);
        tableau.setEntry(1, 0, 1);
        tableau.setEntry(2, 0, 2);
        pivotRow = solver.getPivotRow(0, tableau);
        assertEquals(1, pivotRow.intValue());
        tableau.setEntry(2, 0, -1);
        pivotRow = solver.getPivotRow(0, tableau);
        assertEquals(2, pivotRow.intValue());
    }

    @Test
    public void testDoIteration() {
        SimplexTableau tableau = new SimplexTableau();
        solver.doIteration(tableau);
        // Unable to test due to private methods used

    }

    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = new SimplexTableau();
        boolean phase1Solved = solver.isPhase1Solved(tableau);
        assertTrue(phase1Solved);
        tableau.setEntry(0, 0, -1);
        tableau.setEntry(0, 1, -2);
        phase1Solved = solver.isPhase1Solved(tableau);
        assertFalse(phase1Solved);
        tableau.setEntry(0, 1, 2);
        phase1Solved = solver.isPhase1Solved(tableau);
        assertTrue(phase1Solved);
        tableau.setNumArtificialVariables(1);
        phase1Solved = solver.isPhase1Solved(tableau);
        assertFalse(phase1Solved);
    }

    @Test
    public void testIsOptimal() {
        SimplexTableau tableau = new SimplexTableau();
        boolean optimal = solver.isOptimal(tableau);
        assertTrue(optimal);
        tableau.setEntry(0, 0, -1);
        tableau.setEntry(0, 1, -2);
        optimal = solver.isOptimal(tableau);
        assertFalse(optimal);
        tableau.setEntry(0, 1, 2);
        optimal = solver.isOptimal(tableau);
        assertTrue(optimal);
        tableau.setNumArtificialVariables(1);
        optimal = solver.isOptimal(tableau);
        assertFalse(optimal);
    }

    @Test
    public void testSolvePhase1() {
        SimplexTableau tableau = new SimplexTableau();
        try {
            solver.solvePhase1(tableau);
            fail("NoFeasibleSolutionException not thrown");
        } catch (NoFeasibleSolutionException ex) {
            // expected exception
        } catch (OptimizationException ex) {
            fail("Unexpected OptimizationException");
        }
    }

    @Test
    public void testDoOptimize() {
        RealPointValuePair solution = solver.doOptimize();
        assertNotNull(solution);
    }
}