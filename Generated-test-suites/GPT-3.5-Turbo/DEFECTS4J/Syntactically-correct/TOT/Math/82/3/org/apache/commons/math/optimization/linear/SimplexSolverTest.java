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

        // typical use case: all positive objective function coefficients
        SimplexTableau tableau1 = new SimplexTableau();
        tableau1.setEntry(0, 0, 2);
        tableau1.setEntry(0, 1, 4);
        tableau1.setEntry(0, 2, 6);
        tableau1.setEntry(0, 3, 8);
        assertEquals(Integer.valueOf(0), solver.getPivotColumn(tableau1));

        // edge case: all negative objective function coefficients
        SimplexTableau tableau2 = new SimplexTableau();
        tableau2.setEntry(0, 0, -2);
        tableau2.setEntry(0, 1, -4);
        tableau2.setEntry(0, 2, -6);
        tableau2.setEntry(0, 3, -8);
        assertEquals(Integer.valueOf(3), solver.getPivotColumn(tableau2));

        // error scenario: positive and negative objective function coefficients
        SimplexTableau tableau3 = new SimplexTableau();
        tableau3.setEntry(0, 0, -2);
        tableau3.setEntry(0, 1, 4);
        tableau3.setEntry(0, 2, -6);
        tableau3.setEntry(0, 3, 8);
        assertEquals(Integer.valueOf(0), solver.getPivotColumn(tableau3));
    }

    @Test
    public void testGetPivotRow() {

        // typical use case: all positive ratios
        SimplexTableau tableau1 = new SimplexTableau();
        tableau1.setEntry(1, 0, 2);
        tableau1.setEntry(2, 0, 4);
        tableau1.setEntry(3, 0, 6);
        tableau1.setEntry(4, 0, 8);
        assertEquals(Integer.valueOf(1), solver.getPivotRow(0, tableau1));

        // edge case: negative ratios and positive entries
        SimplexTableau tableau2 = new SimplexTableau();
        tableau2.setEntry(1, 0, -2);
        tableau2.setEntry(2, 0, -4);
        tableau2.setEntry(3, 0, -6);
        tableau2.setEntry(4, 0, -8);
        assertEquals(null, solver.getPivotRow(0, tableau2));

        // error scenario: positive ratios and negative entries
        SimplexTableau tableau3 = new SimplexTableau();
        tableau3.setEntry(1, 0, -2);
        tableau3.setEntry(2, 0, 4);
        tableau3.setEntry(3, 0, -6);
        tableau3.setEntry(4, 0, 8);
        assertEquals(Integer.valueOf(1), solver.getPivotRow(0, tableau3));
    }

    @Test
    public void testDoIteration() throws OptimizationException {

        // typical use case
        SimplexTableau tableau1 = new SimplexTableau();
        tableau1.setEntry(0, 0, 2);
        tableau1.setEntry(0, 1, 4);
        tableau1.setEntry(1, 0, 1);
        tableau1.setEntry(1, 1, 1);
        solver.doIteration(tableau1);
        assertEquals(0.0, tableau1.getEntry(1, 0), 0.0001);
        assertEquals(0.0, tableau1.getEntry(0, 1), 0.0001);

        // edge case: zero pivot value
        SimplexTableau tableau2 = new SimplexTableau();
        tableau2.setEntry(0, 0, 0);
        try {
            solver.doIteration(tableau2);
            fail("OptimizationException not thrown");
        } catch (OptimizationException e) {
            // expected behavior
        }

        // error scenario: unbounded solution
        SimplexTableau tableau3 = new SimplexTableau();
        tableau3.setEntry(0, 0, -2);
        tableau3.setEntry(0, 1, 4);
        tableau3.setEntry(1, 0, 1);
        tableau3.setEntry(1, 1, -1);
        try {
            solver.doIteration(tableau3);
            fail("OptimizationException not thrown");
        } catch (OptimizationException e) {
            // expected behavior
        }
    }

    @Test
    public void testIsPhase1Solved() {

        // typical use case: all positive coefficients
        SimplexTableau tableau1 = new SimplexTableau();
        tableau1.setEntry(0, 0, 2);
        tableau1.setEntry(0, 1, 4);
        assertTrue(solver.isPhase1Solved(tableau1));

        // edge case: all negative coefficients
        SimplexTableau tableau2 = new SimplexTableau();
        tableau2.setEntry(0, 0, -2);
        tableau2.setEntry(0, 1, -4);
        assertTrue(solver.isPhase1Solved(tableau2));

        // error scenario: both positive and negative coefficients
        SimplexTableau tableau3 = new SimplexTableau();
        tableau3.setEntry(0, 0, 2);
        tableau3.setEntry(0, 1, -4);
        assertFalse(solver.isPhase1Solved(tableau3));
    }

    @Test
    public void testIsOptimal() {

        // typical use case: all positive objective function coefficients
        SimplexTableau tableau1 = new SimplexTableau();
        tableau1.setEntry(0, 0, 2);
        tableau1.setEntry(0, 1, 4);
        assertTrue(solver.isOptimal(tableau1));

        // edge case: all negative objective function coefficients
        SimplexTableau tableau2 = new SimplexTableau();
        tableau2.setEntry(0, 0, -2);
        tableau2.setEntry(0, 1, -4);
        assertTrue(solver.isOptimal(tableau2));

        // error scenario: both positive and negative objective function coefficients
        SimplexTableau tableau3 = new SimplexTableau();
        tableau3.setEntry(0, 0, -2);
        tableau3.setEntry(0, 1, 4);
        assertFalse(solver.isOptimal(tableau3));
    }

    @Test
    public void testSolvePhase1() throws OptimizationException {

        // typical use case: feasible solution exists
        SimplexTableau tableau1 = new SimplexTableau();
        tableau1.setEntry(1, 0, -2);
        tableau1.setEntry(1, 1, 4);
        tableau1.setEntry(2, 0, 1);
        tableau1.setEntry(2, 1, 0);
        tableau1.setEntry(2, 2, -1);
        solver.solvePhase1(tableau1);
        assertEquals(0.0, tableau1.getEntry(0, tableau1.getRhsOffset()), 0.0001);

        // edge case: no feasible solution
        SimplexTableau tableau2 = new SimplexTableau();
        tableau2.setEntry(1, 0, -2);
        tableau2.setEntry(1, 1, 4);
        try {
            solver.solvePhase1(tableau2);
            fail("NoFeasibleSolutionException not thrown");
        } catch (NoFeasibleSolutionException e) {
            // expected behavior
        }

        // error scenario: all positive coefficients in objective function
        SimplexTableau tableau3 = new SimplexTableau();
        tableau3.setEntry(1, 0, 2);
        tableau3.setEntry(1, 1, 4);
        try {
            solver.solvePhase1(tableau3);
            fail("NoFeasibleSolutionException not thrown");
        } catch (NoFeasibleSolutionException e) {
            // expected behavior
        }
    }

    @Test
    public void testDoOptimize() throws OptimizationException {

        // typical use case
        SimplexTableau tableau1 = new SimplexTableau();
        tableau1.setEntry(0, 0, 2);
        tableau1.setEntry(0, 1, 4);
        solver.doOptimize(tableau1);
        // perform assertions on solution

        // edge case: no feasible solution
        SimplexTableau tableau2 = new SimplexTableau();
        tableau2.setEntry(1, 0, -2);
        tableau2.setEntry(1, 1, 4);
        try {
            solver.doOptimize(tableau2);
            fail("NoFeasibleSolutionException not thrown");
        } catch (NoFeasibleSolutionException e) {
            // expected behavior
        }
    }
}