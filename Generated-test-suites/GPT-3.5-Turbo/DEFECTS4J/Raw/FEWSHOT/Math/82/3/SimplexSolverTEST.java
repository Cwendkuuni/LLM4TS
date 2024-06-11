import org.apache.commons.math.optimization.linear.SimplexTableau;
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
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(0, 1, -2);
        tableau.setEntry(0, 2, 3);
        tableau.setEntry(0, 3, -4);

        assertEquals(new Integer(3), solver.getPivotColumn(tableau));
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(1, 0, 1);
        tableau.setEntry(2, 0, 1);
        tableau.setEntry(0, 1, 2);
        tableau.setEntry(1, 1, 4);
        tableau.setEntry(2, 1, 6);
        tableau.setEntry(0, 2, -1);
        tableau.setEntry(1, 2, -2);
        tableau.setEntry(2, 2, -3);
        tableau.setEntry(0, 3, 8);
        tableau.setEntry(1, 3, 16);
        tableau.setEntry(2, 3, 24);

        assertEquals(new Integer(2), solver.getPivotRow(0, tableau));
    }

    @Test
    public void testDoIteration() {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, 2);
        tableau.setEntry(0, 1, -1);
        tableau.setEntry(0, 2, 1);
        tableau.setEntry(0, 3, 0);
        tableau.setEntry(0, 4, -8);
        
        solver.doIteration(tableau);

        assertEquals(0, tableau.getEntry(1, 0), 0.000001);
        assertEquals(0, tableau.getEntry(1, 1), 0.000001);
        assertEquals(1, tableau.getEntry(1, 2), 0.000001);
        assertEquals(0, tableau.getEntry(1, 3), 0.000001);
        assertEquals(-8, tableau.getEntry(1, 4), 0.000001);
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(0, 1, -2);
        tableau.setEntry(0, 2, 3);
        tableau.setEntry(0, 3, -4);

        assertFalse(solver.isPhase1Solved(tableau));

        tableau.setEntry(0, 0, 0);
        tableau.setEntry(0, 1, 0);
        tableau.setEntry(0, 2, 0);
        tableau.setEntry(0, 3, 0);

        assertTrue(solver.isPhase1Solved(tableau));
    }

    @Test
    public void testIsOptimal() {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(0, 1, -2);
        tableau.setEntry(0, 2, 3);
        tableau.setEntry(0, 3, -4);

        assertFalse(solver.isOptimal(tableau));

        tableau.setEntry(0, 0, 0);
        tableau.setEntry(0, 1, 0);
        tableau.setEntry(0, 2, 0);
        tableau.setEntry(0, 3, 0);

        assertTrue(solver.isOptimal(tableau));
    }

    @Test
    public void testSolvePhase1() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(0, 1, 1);
        tableau.setEntry(0, 2, 1);
        tableau.setEntry(0, 3, 0);
        tableau.setEntry(0, 4, 8);
        tableau.setEntry(1, 0, -1);
        tableau.setEntry(1, 1, -2);
        tableau.setEntry(1, 2, -3);
        tableau.setEntry(1, 3, 0);
        tableau.setEntry(1, 4, -24);
        tableau.setEntry(2, 0, 1);
        tableau.setEntry(2, 1, 2);
        tableau.setEntry(2, 2, 3);
        tableau.setEntry(2, 3, 0);
        tableau.setEntry(2, 4, 24);

        solver.solvePhase1(tableau);

        assertEquals(0, tableau.getEntry(0, 0), 0.000001);
        assertEquals(0, tableau.getEntry(0, 1), 0.000001);
        assertEquals(0, tableau.getEntry(0, 2), 0.000001);
        assertEquals(0, tableau.getEntry(0, 3), 0.000001);
        assertEquals(0, tableau.getEntry(0, 4), 0.000001);
        assertEquals(0, tableau.getEntry(1, 0), 0.000001);
        assertEquals(0, tableau.getEntry(1, 1), 0.000001);
        assertEquals(0, tableau.getEntry(1, 2), 0.000001);
        assertEquals(1, tableau.getEntry(1, 3), 0.000001);
        assertEquals(-8, tableau.getEntry(1, 4), 0.000001);
        assertEquals(1, tableau.getEntry(2, 0), 0.000001);
        assertEquals(2, tableau.getEntry(2, 1), 0.000001);
        assertEquals(3, tableau.getEntry(2, 2), 0.000001);
        assertEquals(0, tableau.getEntry(2, 3), 0.000001);
        assertEquals(24, tableau.getEntry(2, 4), 0.000001);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1WithNoFeasibleSolution() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(0, 1, 1);
        tableau.setEntry(0, 2, 1);
        tableau.setEntry(0, 3, 0);
        tableau.setEntry(0, 4, 5);
        tableau.setEntry(1, 0, -1);
        tableau.setEntry(1, 1, -2);
        tableau.setEntry(1, 2, -3);
        tableau.setEntry(1, 3, 0);
        tableau.setEntry(1, 4, -6);

        solver.solvePhase1(tableau);
    }

    @Test
    public void testDoOptimize() throws OptimizationException {
        //TODO: Add test case for doOptimize method
    }

    // Additional test cases can be added here

}