import static org.junit.Assert.*;
import org.junit.Test;

public class SimplexSolverTest {

    // Test getPivotColumn method
    @Test
    public void testGetPivotColumn() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        // test case for empty tableau
        assertNull(solver.getPivotColumn(tableau));
        // add some entries to the tableau
        tableau.setEntry(0, 0, -1);
        tableau.setEntry(0, 1, -2);
        assertEquals(1, solver.getPivotColumn(tableau).intValue());
    }

    // Test getPivotRow method
    @Test
    public void testGetPivotRow() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        // test case for empty tableau
        assertNull(solver.getPivotRow(0, tableau));
        // add some entries to the tableau
        tableau.setEntry(1, 0, 1);
        tableau.setEntry(2, 0, 2);
        assertEquals(1, solver.getPivotRow(0, tableau).intValue());
    }

    // Test doIteration method
    @Test
    public void testDoIteration() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        // add some entries to the tableau
        tableau.setEntry(1, 1, 1);
        tableau.setEntry(2, 1, 2);
        // test if the iteration works without errors
        try {
            solver.doIteration(tableau);
        } catch (OptimizationException e) {
            fail("Exception thrown during iteration");
        }
    }

    // Test solvePhase1 method
    @Test
    public void testSolvePhase1() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        try {
            solver.solvePhase1(tableau);
        } catch (OptimizationException e) {
            fail("Exception thrown during Phase 1 solving");
        }
    }

    // Test isOptimal method
    @Test
    public void testIsOptimal() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        assertTrue(solver.isOptimal(tableau));
    }

    // Test doOptimize method
    @Test
    public void testDoOptimize() {
        SimplexSolver solver = new SimplexSolver();
        RealPointValuePair result = null;
        try {
            result = solver.doOptimize();
        } catch (OptimizationException e) {
            fail("Exception thrown during optimization");
        }
        assertNotNull(result);
    }
}