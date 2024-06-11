import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealPointValuePair;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        // Test case 1: Tableau with all positive coefficients
        SimplexTableau tableau1 = new SimplexTableau();
        tableau1.setEntry(0, 0, 1);
        tableau1.setEntry(0, 1, 2);
        tableau1.setEntry(0, 2, 3);
        Integer pivotColumn1 = SimplexSolver.getPivotColumn(tableau1);
        Assert.assertEquals(Integer.valueOf(-1), pivotColumn1);

        // Test case 2: Tableau with negative coefficients
        SimplexTableau tableau2 = new SimplexTableau();
        tableau2.setEntry(0, 0, -1);
        tableau2.setEntry(0, 1, -2);
        tableau2.setEntry(0, 2, -3);
        Integer pivotColumn2 = SimplexSolver.getPivotColumn(tableau2);
        Assert.assertEquals(Integer.valueOf(2), pivotColumn2);

        // Test case 3: Tableau with one negative and two positive coefficients
        SimplexTableau tableau3 = new SimplexTableau();
        tableau3.setEntry(0, 0, -1);
        tableau3.setEntry(0, 1, 2);
        tableau3.setEntry(0, 2, 3);
        Integer pivotColumn3 = SimplexSolver.getPivotColumn(tableau3);
        Assert.assertEquals(Integer.valueOf(0), pivotColumn3);
    }

    @Test
    public void testGetPivotRow() {
        // Test case 1: Tableau with valid entries
        SimplexTableau tableau1 = new SimplexTableau();
        tableau1.setEntry(1, 0, 1);
        tableau1.setEntry(1, 1, 2);
        tableau1.setEntry(1, 2, 3);
        tableau1.setEntry(1, 3, 4);
        Integer pivotRow1 = SimplexSolver.getPivotRow(2, tableau1);
        Assert.assertEquals(Integer.valueOf(1), pivotRow1);

        // Test case 2: Tableau with all negative entries
        SimplexTableau tableau2 = new SimplexTableau();
        tableau2.setEntry(1, 0, -1);
        tableau2.setEntry(1, 1, -2);
        tableau2.setEntry(1, 2, -3);
        tableau2.setEntry(1, 3, -4);
        Integer pivotRow2 = SimplexSolver.getPivotRow(2, tableau2);
        Assert.assertNull(pivotRow2);

        // Test case 3: Tableau with one negative and three positive entries
        SimplexTableau tableau3 = new SimplexTableau();
        tableau3.setEntry(1, 0, -1);
        tableau3.setEntry(1, 1, 2);
        tableau3.setEntry(1, 2, 3);
        tableau3.setEntry(1, 3, 4);
        Integer pivotRow3 = SimplexSolver.getPivotRow(2, tableau3);
        Assert.assertEquals(Integer.valueOf(1), pivotRow3);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoIteration_UnboundedSolution() throws OptimizationException {
        // Test case: Tableau with no pivot row
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(0, 1, 2);
        tableau.setEntry(0, 2, 3);
        SimplexSolver.doIteration(tableau);
    }

    @Test
    public void testIsPhase1Solved() {
        // Test case 1: Tableau with all positive coefficients
        SimplexTableau tableau1 = new SimplexTableau();
        tableau1.setEntry(0, 0, 1);
        tableau1.setEntry(0, 1, 2);
        tableau1.setEntry(0, 2, 3);
        Assert.assertTrue(SimplexSolver.isPhase1Solved(tableau1));

        // Test case 2: Tableau with negative coefficients
        SimplexTableau tableau2 = new SimplexTableau();
        tableau2.setEntry(0, 0, -1);
        tableau2.setEntry(0, 1, -2);
        tableau2.setEntry(0, 2, -3);
        Assert.assertFalse(SimplexSolver.isPhase1Solved(tableau2));

        // Test case 3: Tableau with one negative and two positive coefficients
        SimplexTableau tableau3 = new SimplexTableau();
        tableau3.setEntry(0, 0, -1);
        tableau3.setEntry(0, 1, 2);
        tableau3.setEntry(0, 2, 3);
        Assert.assertFalse(SimplexSolver.isPhase1Solved(tableau3));
    }

    @Test
    public void testIsOptimal() {
        // Test case 1: Tableau with all positive coefficients
        SimplexTableau tableau1 = new SimplexTableau();
        tableau1.setEntry(0, 0, 1);
        tableau1.setEntry(0, 1, 2);
        tableau1.setEntry(0, 2, 3);
        Assert.assertTrue(SimplexSolver.isOptimal(tableau1));

        // Test case 2: Tableau with negative coefficients
        SimplexTableau tableau2 = new SimplexTableau();
        tableau2.setEntry(0, 0, -1);
        tableau2.setEntry(0, 1, -2);
        tableau2.setEntry(0, 2, -3);
        Assert.assertFalse(SimplexSolver.isOptimal(tableau2));

        // Test case 3: Tableau with one negative and two positive coefficients
        SimplexTableau tableau3 = new SimplexTableau();
        tableau3.setEntry(0, 0, -1);
        tableau3.setEntry(0, 1, 2);
        tableau3.setEntry(0, 2, 3);
        Assert.assertFalse(SimplexSolver.isOptimal(tableau3));
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1_NoFeasibleSolution() throws OptimizationException {
        // Test case: Tableau with non-zero W coefficient
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(0, 1, 2);
        tableau.setEntry(0, 2, 3);
        SimplexSolver.solvePhase1(tableau);
    }

    @Test
    public void testDoOptimize() throws OptimizationException {
        // Test case: Simplex solver with valid input
        SimplexSolver solver = new SimplexSolver();
        RealPointValuePair solution = solver.doOptimize();
        Assert.assertNotNull(solution);
    }

}