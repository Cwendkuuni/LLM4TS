package org.apache.commons.math.optimization.linear;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link SimplexSolver} class.
 */
public class SimplexSolverTest {

    /**
     * Test for solver with default settings.
     */
    @Test
    public void testDefaultSolver() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-3.0, -2.0, -1.0}, new double[]{1.0, 1.0, 1.0});
        Constraint[] constraints = new Constraint[]{
                new LinearConstraint(new double[]{1.0, 1.0}, Operator.LEQUAL, 2.0),
                new LinearConstraint(new double[]{-1.0, 1.0}, Operator.LEQUAL, 1.0),
                new LinearConstraint(new double[]{1.0, -1.0}, Operator.LEQUAL, 1.0),
                new LinearConstraint(new double[]{-1.0, -1.0}, Operator.LEQUAL, 0.0)};
        GoalType goalType = GoalType.MINIMIZE;
        boolean restrictToNonNegative = false;

        SimplexSolver solver = new SimplexSolver();
        RealPointValuePair solution = null;
        try {
            solution = solver.optimize(f, constraints, goalType, restrictToNonNegative);
        } catch (OptimizationException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        Assert.assertNotNull(solution);
        PointValuePair expected = new PointValuePair(new double[]{-0.666666666666667, -0.666666666666667, 1.0}, -2.0);
        Assert.assertEquals(expected.getValue(), solution.getValue(), 1e-6);
        Assert.assertArrayEquals(expected.getPoint(), solution.getPoint(), 1e-6);
    }

    /**
     * Test for solver with custom epsilon.
     */
    @Test
    public void testCustomSolver() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-3.0, -2.0, -1.0}, new double[]{1.0, 1.0, 1.0});
        Constraint[] constraints = new Constraint[]{
                new LinearConstraint(new double[]{1.0, 1.0}, Operator.LEQUAL, 2.0),
                new LinearConstraint(new double[]{-1.0, 1.0}, Operator.LEQUAL, 1.0),
                new LinearConstraint(new double[]{1.0, -1.0}, Operator.LEQUAL, 1.0),
                new LinearConstraint(new double[]{-1.0, -1.0}, Operator.LEQUAL, 0.0)};
        GoalType goalType = GoalType.MINIMIZE;
        boolean restrictToNonNegative = false;

        SimplexSolver solver = new SimplexSolver(1e-7);
        RealPointValuePair solution = null;
        try {
            solution = solver.optimize(f, constraints, goalType, restrictToNonNegative);
        } catch (OptimizationException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        Assert.assertNotNull(solution);
        PointValuePair expected = new PointValuePair(new double[]{-0.666666666666667, -0.666666666666667, 1.0}, -2.0);
        Assert.assertEquals(expected.getValue(), solution.getValue(), 1e-7);
        Assert.assertArrayEquals(expected.getPoint(), solution.getPoint(), 1e-7);
    }

    /**
     * Test for solver with unbounded solution.
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{0.0}, new double[]{1.0});
        Constraint[] constraints = new Constraint[0];
        GoalType goalType = GoalType.MINIMIZE;
        boolean restrictToNonNegative = false;

        SimplexSolver solver = new SimplexSolver();
        solver.optimize(f, constraints, goalType, restrictToNonNegative);
    }

    /**
     * Test for solver with no feasible solution.
     */
    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{0.0}, new double[]{1.0});
        Constraint[] constraints = new Constraint[]{
                new LinearConstraint(new double[0], Operator.LEQUAL, 0.0),
                new LinearConstraint(new double[0], Operator.LEQUAL, 0.0)};
        GoalType goalType = GoalType.MINIMIZE;
        boolean restrictToNonNegative = false;

        SimplexSolver solver = new SimplexSolver();
        solver.optimize(f, constraints, goalType, restrictToNonNegative);
    }

    /**
     * Test for solver with maximization goal.
     */
    @Test
    public void testMaximizationGoal() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-3.0, -2.0, -1.0}, new double[]{1.0, 1.0, 1.0});
        Constraint[] constraints = new Constraint[]{
                new LinearConstraint(new double[]{1.0, 1.0}, Operator.LEQUAL, 2.0),
                new LinearConstraint(new double[]{-1.0, 1.0}, Operator.LEQUAL, 1.0),
                new LinearConstraint(new double[]{1.0, -1.0}, Operator.LEQUAL, 1.0),
                new LinearConstraint(new double[]{-1.0, -1.0}, Operator.LEQUAL, 0.0)};
        GoalType goalType = GoalType.MAXIMIZE;
        boolean restrictToNonNegative = false;

        SimplexSolver solver = new SimplexSolver();
        RealPointValuePair solution = null;
        try {
            solution = solver.optimize(f, constraints, goalType, restrictToNonNegative);
        } catch (OptimizationException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        Assert.assertNotNull(solution);
        PointValuePair expected = new PointValuePair(new double[]{0.666666666666667, 0.666666666666667, 1.0}, 2.0);
        Assert.assertEquals(expected.getValue(), solution.getValue(), 1e-6);
        Assert.assertArrayEquals(expected.getPoint(), solution.getPoint(), 1e-6);
    }

    /**
     * Test for solver with restrictions to non-negative values.
     */
    @Test
    public void testRestrictedToNonNegativeValues() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-3.0, -2.0, 2.0}, new double[]{1.0, 1.0, 1.0});
        Constraint[] constraints = new Constraint[]{
                new LinearConstraint(new double[]{1.0, 1.0}, Operator.LEQUAL, 2.0),
                new LinearConstraint(new double[]{-1.0, 1.0}, Operator.LEQUAL, 1.0),
                new LinearConstraint(new double[]{1.0, -1.0}, Operator.LEQUAL, 1.0),
                new LinearConstraint(new double[]{-1.0, -1.0}, Operator.LEQUAL, 0.0)};
        GoalType goalType = GoalType.MINIMIZE;
        boolean restrictToNonNegative = true;

        SimplexSolver solver = new SimplexSolver();
        RealPointValuePair solution = null;
        try {
            solution = solver.optimize(f, constraints, goalType, restrictToNonNegative);
        } catch (OptimizationException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        Assert.assertNotNull(solution);
        PointValuePair expected = new PointValuePair(new double[]{0.0, 1.0, 1.0}, 0.0);
        Assert.assertEquals(expected.getValue(), solution.getValue(), 1e-6);
        Assert.assertArrayEquals(expected.getPoint(), solution.getPoint(), 1e-6);
    }

    /**
     * Test for solver with maximal iteration count.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-3.0, -2.0, -1.0}, new double[]{1.0, 1.0, 1.0});
        Constraint[] constraints = new Constraint[]{
                new LinearConstraint(new double[]{1.0, 1.0}, Operator.LEQUAL, 2.0),
                new LinearConstraint(new double[]{-1.0, 1.0}, Operator.LEQUAL, 1.0),
                new LinearConstraint(new double[]{1.0, -1.0}, Operator.LEQUAL, 1.0),
                new LinearConstraint(new double[]{-1.0, -1.0}, Operator.LEQUAL, 0.0)};
        GoalType goalType = GoalType.MINIMIZE;
        boolean restrictToNonNegative = false;

        SimplexSolver solver = new SimplexSolver(1);
        solver.optimize(f, constraints, goalType, restrictToNonNegative);
    }
}