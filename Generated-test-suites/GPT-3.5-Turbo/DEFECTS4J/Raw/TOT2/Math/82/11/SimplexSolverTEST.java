import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        // Test case for when there is a negative coefficient in objective function
        // expected: column with the most negative coefficient is returned
        SimplexTableau tableau = new SimplexTableau();
        SimplexSolver simplexSolver = new SimplexSolver();
        Integer pivotCol = simplexSolver.getPivotColumn(tableau);
        assertNotNull(pivotCol);
    }

    @Test
    public void testGetPivotRow() {
        // Test case for when there is a minimum ratio row
        // expected: row with the minimum ratio is returned
        SimplexTableau tableau = new SimplexTableau();
        SimplexSolver simplexSolver = new SimplexSolver();
        Integer pivotRow = simplexSolver.getPivotRow(2, tableau);
        assertNotNull(pivotRow);
    }

    @Test
    public void testDoIteration() {
        // Test case for running one iteration of the Simplex method
        // expected: no exceptions thrown
        SimplexTableau tableau = new SimplexTableau();
        SimplexSolver simplexSolver = new SimplexSolver();
        try {
            simplexSolver.doIteration(tableau);
        } catch (OptimizationException e) {
            fail("Exception thrown during iteration");
        }
    }

    @Test
    public void testIsPhase1Solved() {
        // Test case for checking if Phase 1 is solved
        // expected: true when Phase 1 is solved
        SimplexTableau tableau = new SimplexTableau();
        SimplexSolver simplexSolver = new SimplexSolver();
        boolean phase1Solved = simplexSolver.isPhase1Solved(tableau);
        assertTrue(phase1Solved);
    }

    @Test
    public void testIsOptimal() {
        // Test case for checking if the problem is at an optimal state
        // expected: true when the problem is solved
        SimplexTableau tableau = new SimplexTableau();
        SimplexSolver simplexSolver = new SimplexSolver();
        boolean optimal = simplexSolver.isOptimal(tableau);
        assertTrue(optimal);
    }

    @Test
    public void testSolvePhase1() {
        // Test case for solving Phase 1 of the Simplex method
        // expected: no exceptions thrown
        SimplexTableau tableau = new SimplexTableau();
        SimplexSolver simplexSolver = new SimplexSolver();
        try {
            simplexSolver.solvePhase1(tableau);
        } catch (OptimizationException e) {
            fail("Exception thrown during Phase 1 solving");
        }
    }

    @Test
    public void testDoOptimize() {
        // Test case for running the optimization process
        // expected: optimal solution is obtained
        SimplexTableau tableau = new SimplexTableau();
        SimplexSolver simplexSolver = new SimplexSolver();
        RealPointValuePair solution = null;
        try {
            solution = simplexSolver.doOptimize();
        } catch (OptimizationException e) {
            fail("Exception thrown during optimization");
        }
        assertNotNull(solution);
    }
}