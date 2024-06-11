package org.apache.commons.math.optimization.linear;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunctionSet;
import org.apache.commons.math3.optim.linear.SimpleTableau;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link SimplexSolver}.
 */
public class SimplexSolverTest {

    private SimplexSolver solver;
    private LinearObjectiveFunctionSet objectiveFunctionSet;
    private LinearConstraintSet constraintSet;
    private PointValuePair solution;

    @Before
    public void setUp() {
        solver = new SimplexSolver();
        objectiveFunctionSet = new LinearObjectiveFunctionSet(2);
        constraintSet = new LinearConstraintSet(2);
    }

    /**
     * Test for the case when the problem has a bounded solution.
     */
    @Test
    public void testBoundedSolution() {
        LinearObjectiveFunction f1 = new LinearObjectiveFunction(new double[]{-1, 1}, 0.5);
        LinearObjectiveFunction f2 = new LinearObjectiveFunction(new double[]{1, 1}, 2.0);
        LinearConstraint c1 = new LinearConstraint(new double[]{1, 1}, 2.0, GoalType.LEQ, null);
        LinearConstraint c2 = new LinearConstraint(new double[]{1, -1}, 1.0, GoalType.LEQ, null);

        objectiveFunctionSet.addObjectiveFunction(f1);
        objectiveFunctionSet.addObjectiveFunction(f2);
        constraintSet.addConstraint(c1);
        constraintSet.addConstraint(c2);

        try {
            solution = solver.doOptimize();
            Assert.assertEquals(new double[]{0.25, 0.75}, solution.getPoint(), 0.001);
            Assert.assertEquals(-2.25, solution.getValue(), 0.001);
        } catch (OptimizationException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test for the case when the problem has no feasible solution.
     */
    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() {
        LinearObjectiveFunction f1 = new LinearObjectiveFunction(new double[]{-1, 1}, 0.5);
        LinearObjectiveFunction f2 = new LinearObjectiveFunction(new double[]{1, 1}, 2.0);
        LinearConstraint c1 = new LinearConstraint(new double[]{1, 1}, 2.0, GoalType.LEQ, null);
        LinearConstraint c2 = new LinearConstraint(new double[]{1, -1}, 1.0, GoalType.LEQ, null);
        LinearConstraint c3 = new LinearConstraint(new double[]{1, 1}, 1.0, GoalType.EQ, null);

        objectiveFunctionSet.addObjectiveFunction(f1);
        objectiveFunctionSet.addObjectiveFunction(f2);
        constraintSet.addConstraint(c1);
        constraintSet.addConstraint(c2);
        constraintSet.addConstraint(c3);

        try {
            solver.doOptimize();
            Assert.fail("Unexpected solution");
        } catch (OptimizationException e) {
            // expected
        }
    }

    /**
     * Test for the case when the problem has an unbounded solution.
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() {
        LinearObjectiveFunction f1 = new LinearObjectiveFunction(new double[]{-1, 1}, 0.5);
        LinearObjectiveFunction f2 = new LinearObjectiveFunction(new double[]{1, 1}, 2.0);
        LinearConstraint c1 = new LinearConstraint(new double[]{1, 1}, 2.0, GoalType.LEQ, null);
        LinearConstraint c2 = new LinearConstraint(new double[]{1, -1}, Double.NEGATIVE_INFINITY, GoalType.NO_BOUNDS,
                null);

        objectiveFunctionSet.addObjectiveFunction(f1);
        objectiveFunctionSet.addObjectiveFunction(f2);
        constraintSet.addConstraint(c1);
        constraintSet.addConstraint(c2);

        try {
            solver.doOptimize();
            Assert.fail("Unexpected solution");
        } catch (OptimizationException e) {
            // expected
        }
    }

    /**
     * Test for the case when the problem cannot be solved after a
     * specified maximum number of iterations.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() {
        LinearObjectiveFunction f1 = new LinearObjectiveFunction(new double[]{-1, 1}, 0.5);
        LinearObjectiveFunction f2 = new LinearObjectiveFunction(new double[]{1, 1}, 2.0);
        LinearConstraint c1 = new LinearConstraint(new double[]{1, 1}, 2.0, GoalType.LEQ, null);
        LinearConstraint c2 = new LinearConstraint(new double[]{1, -1}, Double.NEGATIVE_INFINITY, GoalType.NO_BOUNDS,
                null);

        objectiveFunctionSet.addObjectiveFunction(f1);
        objectiveFunctionSet.addObjectiveFunction(f2);
        constraintSet.addConstraint(c1);
        constraintSet.addConstraint(c2);

        SimplexSolver solver = new SimplexSolver(0.0);

        try {
            solver.doOptimize();
            Assert.fail("Unexpected solution");
        } catch (OptimizationException e) {
            // expected
        }
    }

    /**
     * Test for the case when the problem is at an optimal state.
     */
    @Test
    public void testOptimalState() {
        LinearObjectiveFunction f1 = new LinearObjectiveFunction(new double[]{-1, 1}, 0.5);
        LinearObjectiveFunction f2 = new LinearObjectiveFunction(new double[]{1, 1}, 2.0);
        LinearConstraint c1 = new LinearConstraint(new double[]{1, 1}, 2.0, GoalType.LEQ, null);
        LinearConstraint c2 = new LinearConstraint(new double[]{1, -1}, 1.0, GoalType.EQ, null);

        objectiveFunctionSet.addObjectiveFunction(f1);
        objectiveFunctionSet.addObjectiveFunction(f2);
        constraintSet.addConstraint(c1);
        constraintSet.addConstraint(c2);

        try {
            solution = solver.doOptimize();
            Assert.assertEquals(new double[]{0.25, 0.75}, solution.getPoint(), 0.001);
            Assert.assertEquals(-2.25, solution.getValue(), 0.001);
            Assert.assertTrue(solver.isOptimal(new SimpleTableau(f1, f2, constraintSet.getConstraints(), GoalType.MINIMIZE,
                    1.0e-6)));
        } catch (OptimizationException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }
}