import org.apache.commons.math3.optimization.linear.*;
import org.apache.commons.math3.exception.*;
import org.junit.Assert;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testPivotColumn() {
        // Test case where all coefficients are zero
        SimplexTableau tableau1 = new SimplexTableau(new double[]{0, 0, 0});

        // The pivot column should be null
        Assert.assertNull(tableau1.getPivotColumn());

        // Test case where one coefficient is negative
        SimplexTableau tableau2 = new SimplexTableau(new double[]{0, -1, 0});

        // The pivot column should be 1
        Assert.assertEquals(Integer.valueOf(1), tableau2.getPivotColumn());

        // Test case where all coefficients are zero except one negative coefficient
        SimplexTableau tableau3 = new SimplexTableau(new double[]{0, 0, -1, 0});

        // The pivot column should be 2
        Assert.assertEquals(Integer.valueOf(2), tableau3.getPivotColumn());
    }

    @Test
    public void testPivotRow() {
        // Test case where all entries are positive
        SimplexTableau tableau1 = new SimplexTableau(new double[][]{{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}});

        // The pivot row should be null
        Assert.assertNull(tableau1.getPivotRow(0));

        // Test case where some entries are negative
        SimplexTableau tableau2 = new SimplexTableau(new double[][]{{1, -2, 3, 4}, {5, 6, -7, 8}, {9, 10, 11, 12}});

        // The pivot row should be 1
        Assert.assertEquals(Integer.valueOf(1), tableau2.getPivotRow(2));

        // Test case where some entries are zero
        SimplexTableau tableau3 = new SimplexTableau(new double[][]{{1, -2, 0, 4}, {5, 0, -7, 8}, {0, 10, 11, 12}});

        // The pivot row should be 2
        Assert.assertEquals(Integer.valueOf(2), tableau3.getPivotRow(1));
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoIterationUnbounded() throws MaxCountExceededException, UnboundedSolutionException {
        // Test case where the model is found not to have a bounded solution
        SimplexTableau tableau = new SimplexTableau(new double[][]{{1, -1, 1}, {-1, 1, -1}});
        SimplexSolver simplexSolver = new SimplexSolver();
        simplexSolver.doIteration(tableau);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1NoFeasibleSolution() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        // Test case where there is no feasible solution
        SimplexTableau tableau = new SimplexTableau(new double[][]{{-1, -2, -3, 1}, {-4, -5, -6, -1}});
        SimplexSolver simplexSolver = new SimplexSolver();
        simplexSolver.solvePhase1(tableau);
    }

    @Test
    public void testDoOptimize() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        // Test typical use cases

        // Test case where there is a feasible solution
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-1, -2, -3}, 0);
        LinearConstraintSet constraints = new LinearConstraintSet(new LinearConstraint[]{new LinearConstraint(new double[]{4, 5, 6}, Relationship.GEQ, 1)});
        SimplexSolver simplexSolver1 = new SimplexSolver();
        PointValuePair solution1 = simplexSolver1.optimize(f, constraints, GoalType.MINIMIZE, false);
        double[] expectedSolution1 = new double[]{0, 0, 1};
        Assert.assertArrayEquals(expectedSolution1, solution1.getPoint(), 0);

        // Test case where there is no feasible solution
        LinearConstraintSet constraints2 = new LinearConstraintSet(new LinearConstraint[]{new LinearConstraint(new double[]{1, 2, 3}, Relationship.GEQ, 1)});
        SimplexSolver simplexSolver2 = new SimplexSolver();
        try {
            simplexSolver2.optimize(f, constraints2, GoalType.MINIMIZE, false);
            Assert.fail("NoFeasibleSolutionException not thrown");
        } catch (NoFeasibleSolutionException e) {
            // Expected exception
        }
    }

    @Test(timeout=1000)
    public void testDoOptimizeMaxIterationsExceeded() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        // Test case where the maximum iteration count is exceeded
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{1, 1, 1, 1, 1}, 0);
        LinearConstraintSet constraints = new LinearConstraintSet(new LinearConstraint[]{new LinearConstraint(new double[]{1, 0, 0, 0, 0}, Relationship.GEQ, 1)});
        SimplexSolver simplexSolver = new SimplexSolver();
        simplexSolver.setMaxIterations(1);
        try {
            simplexSolver.optimize(f, constraints, GoalType.MINIMIZE, false);
            Assert.fail("MaxCountExceededException not thrown");
        } catch (MaxCountExceededException e) {
            // Expected exception
        }
    }

    @Test
    public void testDoOptimizeUnboundedSolution() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        // Test case where the model is found not to have a bounded solution
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-1, -2, -3}, 0);
        LinearConstraintSet constraints = new LinearConstraintSet(new LinearConstraint[]{new LinearConstraint(new double[]{1, 2, 3}, Relationship.LEQ, -1)});
        SimplexSolver simplexSolver = new SimplexSolver();
        try {
            simplexSolver.optimize(f, constraints, GoalType.MINIMIZE, false);
            Assert.fail("UnboundedSolutionException not thrown");
        } catch (UnboundedSolutionException e) {
            // Expected exception
        }
    }

    @Test
    public void testDoOptimizeNonNegativeRestriction() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        // Test case where the solver is restricted to non-negative solutions

        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{1, 1}, 0);
        LinearConstraintSet constraints = new LinearConstraintSet(new LinearConstraint[]{new LinearConstraint(new double[]{-1, -1}, Relationship.LEQ, -2)});
        SimplexSolver simplexSolver = new SimplexSolver();
        PointValuePair solution = simplexSolver.optimize(f, constraints, GoalType.MINIMIZE, true);
        double[] expectedSolution = new double[]{0, 2};
        Assert.assertArrayEquals(expectedSolution, solution.getPoint(), 0);
    }

}