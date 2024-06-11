import org.apache.commons.math.optimization.linear.SimplexSolver;
import org.apache.commons.math.optimization.linear.SimplexTableau;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        // Test case 1: Tableau with only one negative coefficient
        SimplexTableau tableau1 = new SimplexTableau();
        tableau1.setEntry(0, 0, -1.0);
        Integer expected1 = 0;
        Integer actual1 = SimplexSolver.getPivotColumn(tableau1);
        assertEquals(expected1, actual1);

        // Test case 2: Tableau with multiple negative coefficients
        SimplexTableau tableau2 = new SimplexTableau();
        tableau2.setEntry(0, 0, -1.0);
        tableau2.setEntry(0, 1, -2.5);
        tableau2.setEntry(0, 2, -3.7);
        tableau2.setEntry(0, 3, -0.5);
        Integer expected2 = 3;
        Integer actual2 = SimplexSolver.getPivotColumn(tableau2);
        assertEquals(expected2, actual2);

        // Test case 3: Tableau with no negative coefficients
        SimplexTableau tableau3 = new SimplexTableau();
        tableau3.setEntry(0, 0, 1.0);
        tableau3.setEntry(0, 1, 2.5);
        tableau3.setEntry(0, 2, 3.7);
        tableau3.setEntry(0, 3, 0.5);
        Integer expected3 = null;
        Integer actual3 = SimplexSolver.getPivotColumn(tableau3);
        assertEquals(expected3, actual3);
    }

    @Test
    public void testGetPivotRow() {
        // Test case 1: Tableau with one ratio less than max value
        SimplexTableau tableau1 = new SimplexTableau();
        tableau1.setWidth(4);
        tableau1.setHeight(3);
        tableau1.setEntry(0, 0, 1.0);
        tableau1.setEntry(1, 0, 2.5);
        tableau1.setEntry(1, 1, -2.0);
        tableau1.setEntry(2, 0, 3.7);
        tableau1.setEntry(2, 2, -1.5);
        Integer expected1 = 2;
        Integer actual1 = SimplexSolver.getPivotRow(0, tableau1);
        assertEquals(expected1, actual1);

        // Test case 2: Tableau with all ratios greater than max value
        SimplexTableau tableau2 = new SimplexTableau();
        tableau2.setWidth(4);
        tableau2.setHeight(3);
        tableau2.setEntry(0, 0, 1.0);
        tableau2.setEntry(1, 0, 2.5);
        tableau2.setEntry(1, 1, -2.0);
        tableau2.setEntry(2, 0, 3.7);
        tableau2.setEntry(2, 2, 1.5);
        Integer expected2 = null;
        Integer actual2 = SimplexSolver.getPivotRow(0, tableau2);
        assertEquals(expected2, actual2);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoIteration_UnboundedSolutionException() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, -1.0);
        SimplexSolver.doIteration(tableau);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1_NoFeasibleSolutionException() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, 1.0);
        SimplexSolver.solvePhase1(tableau);
    }

    @Test
    public void testIsPhase1Solved() {
        // Test case 1: Tableau with no artificial variables
        SimplexTableau tableau1 = new SimplexTableau();
        tableau1.setWidth(4);
        tableau1.setNumArtificialVariables(0);
        tableau1.setEntry(0, 0, 1.0);
        tableau1.setEntry(0, 1, 2.5);
        tableau1.setEntry(0, 2, 3.7);
        tableau1.setEntry(0, 3, 0.5);
        assertTrue(SimplexSolver.isPhase1Solved(tableau1));

        // Test case 2: Tableau with non-zero entry in objective function row
        SimplexTableau tableau2 = new SimplexTableau();
        tableau2.setWidth(4);
        tableau2.setNumArtificialVariables(2);
        tableau2.setEntry(0, 0, 1.0);
        tableau2.setEntry(0, 1, 2.5);
        tableau2.setEntry(0, 2, 3.7);
        tableau2.setEntry(0, 3, -0.5);
        assertFalse(SimplexSolver.isPhase1Solved(tableau2));
    }

    @Test
    public void testIsOptimal() {
        // Test case 1: Tableau with no negative coefficients
        SimplexTableau tableau1 = new SimplexTableau();
        tableau1.setWidth(4);
        tableau1.setNumArtificialVariables(0);
        tableau1.setEntry(0, 0, 1.0);
        tableau1.setEntry(0, 1, 2.5);
        tableau1.setEntry(0, 2, 3.7);
        tableau1.setEntry(0, 3, 0.5);
        assertTrue(SimplexSolver.isOptimal(tableau1));

        // Test case 2: Tableau with negative coefficient
        SimplexTableau tableau2 = new SimplexTableau();
        tableau2.setWidth(4);
        tableau2.setNumArtificialVariables(0);
        tableau2.setEntry(0, 0, 1.0);
        tableau2.setEntry(0, 1, -2.5);
        tableau2.setEntry(0, 2, 3.7);
        tableau2.setEntry(0, 3, 0.5);
        assertFalse(SimplexSolver.isOptimal(tableau2));
    }

    @Test
    public void testDoOptimize() throws OptimizationException {
        // Test case 1: Tableau with feasible solution
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau1 = new SimplexTableau();
        tableau1.setWidth(4);
        tableau1.setHeight(3);
        tableau1.setNumArtificialVariables(0);
        tableau1.setEntry(0, 0, 1.0);
        tableau1.setEntry(0, 1, 2.5);
        tableau1.setEntry(0, 2, 3.7);
        tableau1.setEntry(0, 3, 0.5);
        RealPointValuePair expected1 = new RealPointValuePair(new double[]{0, 0, 0, 0}, 0);
        RealPointValuePair actual1 = simplexSolver.doOptimize(tableau1);
        assertArrayEquals(expected1.getPoint(), actual1.getPoint(), 0.0);
        assertEquals(expected1.getValue(), actual1.getValue(), 0.0);

        // Test case 2: Tableau with infeasible solution
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau2 = new SimplexTableau();
        tableau2.setWidth(4);
        tableau2.setHeight(3);
        tableau2.setNumArtificialVariables(1);
        tableau2.setEntry(0, 0, 1.0);
        tableau2.setEntry(0, 1, 2.5);
        tableau2.setEntry(0, 2, 3.7);
        tableau2.setEntry(0, 3, -0.5);
        RealPointValuePair expected2 = null;
        RealPointValuePair actual2 = simplexSolver.doOptimize(tableau2);
        assertEquals(expected2, actual2);
    }
}