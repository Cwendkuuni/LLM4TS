import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math.optimization.linear.*;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        // Typical case
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 1.0e-6);
        tableau.setEntry(0, 0, -2.0);
        tableau.setEntry(0, 1, -1.0);
        Integer expectedResult = 1;
        Integer actualResult = solver.getPivotColumn(tableau);
        Assert.assertEquals(expectedResult, actualResult);

        // Edge case: all positive coefficients
        tableau.setEntry(0, 0, 2.0);
        tableau.setEntry(0, 1, 1.0);
        expectedResult = null;
        actualResult = solver.getPivotColumn(tableau);
        Assert.assertEquals(expectedResult, actualResult);

        // Error scenario: empty tableau
        tableau.clear();
        expectedResult = null;
        actualResult = solver.getPivotColumn(tableau);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetPivotRow() {
        // Typical case
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 1.0e-6);
        tableau.setEntry(2, 0, 2.0);
        tableau.setEntry(2, 1, 1.0);
        tableau.setEntry(2, tableau.getWidth() - 1, 5.0);
        int col = 1;
        Integer expectedResult = 2;
        Integer actualResult = solver.getPivotRow(col, tableau);
        Assert.assertEquals(expectedResult, actualResult);

        // Edge case: all negative or zero ratios
        tableau.setEntry(1, 0, 1.0);
        tableau.setEntry(1, 1, -1.0);
        tableau.setEntry(1, tableau.getWidth() - 1, -5.0);
        expectedResult = null;
        actualResult = solver.getPivotRow(col, tableau);
        Assert.assertEquals(expectedResult, actualResult);

        // Error scenario: empty tableau
        tableau.clear();
        expectedResult = null;
        actualResult = solver.getPivotRow(col, tableau);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testDoIteration() {
        // Typical case
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 1.0e-6);
        tableau.setEntry(1, 0, 1.0);
        tableau.setEntry(1, 1, -1.0);
        tableau.setEntry(1, tableau.getWidth() - 1, -5.0);
        solver.doIteration(tableau);
        double expectedResult = 1.0;
        double actualResult = tableau.getEntry(1, 1);
        Assert.assertEquals(expectedResult, actualResult, 1.0e-6);

        // Edge case: pivot row equals 0
        tableau = new SimplexTableau(null, null, null, false, 1.0e-6);
        solver.doIteration(tableau);

        // Error scenario: empty tableau
        tableau.clear();
        solver.doIteration(tableau);
    }

    @Test
    public void testIsPhase1Solved() {
        // Typical case: not solved
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 1.0e-6);
        tableau.setEntry(0, 0, -2.0);
        tableau.setEntry(0, 1, -1.0);
        boolean expectedResult = false;
        boolean actualResult = solver.isPhase1Solved(tableau);
        Assert.assertEquals(expectedResult, actualResult);

        // Typical case: solved
        tableau.setEntry(0, 0, 2.0);
        tableau.setEntry(0, 1, 1.0);
        expectedResult = true;
        actualResult = solver.isPhase1Solved(tableau);
        Assert.assertEquals(expectedResult, actualResult);

        // Edge case: no artificial variables
        tableau.clear();
        expectedResult = true;
        actualResult = solver.isPhase1Solved(tableau);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsOptimal() {
        // Typical case: not optimal
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 1.0e-6);
        boolean expectedResult = false;
        boolean actualResult = solver.isOptimal(tableau);
        Assert.assertEquals(expectedResult, actualResult);

        // Typical case: optimal
        tableau.setEntry(0, 0, 2.0);
        tableau.setEntry(0, 1, 1.0);
        expectedResult = true;
        actualResult = solver.isOptimal(tableau);
        Assert.assertEquals(expectedResult, actualResult);

        // Edge case: all positive coefficients
        tableau.setEntry(0, 0, -2.0);
        tableau.setEntry(0, 1, -1.0);
        expectedResult = true;
        actualResult = solver.isOptimal(tableau);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testSolvePhase1() {
        // Typical case: infeasible solution
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 1.0e-6);
        tableau.setEntry(0, tableau.getRhsOffset(), 5.0);
        try {
            solver.solvePhase1(tableau);
            Assert.fail("Expected NoFeasibleSolutionException");
        } catch (NoFeasibleSolutionException e) {
            // Test passed
        } catch (OptimizationException e) {
            Assert.fail("Unexpected exception");
        }

        // Typical case: feasible solution
        tableau.setEntry(0, tableau.getRhsOffset(), 0.0);
        try {
            solver.solvePhase1(tableau);
        } catch (NoFeasibleSolutionException e) {
            Assert.fail("Unexpected NoFeasibleSolutionException");
        } catch (OptimizationException e) {
            Assert.fail("Unexpected exception");
        }

        // Edge case: no artificial variables
        tableau.clear();
        try {
            solver.solvePhase1(tableau);
        } catch (NoFeasibleSolutionException e) {
            Assert.fail("Unexpected NoFeasibleSolutionException");
        } catch (OptimizationException e) {
            Assert.fail("Unexpected exception");
        }
    }

    @Test
    public void testDoOptimize() {
        // Typical case
        SimplexSolver solver = new SimplexSolver();
        solver.setGoalType(GoalType.MAXIMIZE);
        solver.setRestrictToNonNegative(true);
        double[] f = {-3.0, -2.0, -1.0};
        LinearConstraint[] constraints = {new LinearConstraint(new double[]{2.0, 1.0, 1.0}, Relationship.LEQ, 5.0)};
        RealPointValuePair expectedResult = new RealPointValuePair(new double[]{1.0, 2.0, 0.0}, -9.0);
        try {
            RealPointValuePair actualResult = solver.doOptimize();
            Assert.assertEquals(expectedResult, actualResult);
        } catch (OptimizationException e) {
            Assert.fail("Unexpected exception");
        }

        // Edge case: empty objective function and constraints
        solver.setLinearObjectiveFunction(null);
        solver.setConstraints(new LinearConstraint[0]);
        expectedResult = null;
        try {
            RealPointValuePair actualResult = solver.doOptimize();
            Assert.assertEquals(expectedResult, actualResult);
        } catch (OptimizationException e) {
            Assert.fail("Unexpected exception");
        }
    }

}