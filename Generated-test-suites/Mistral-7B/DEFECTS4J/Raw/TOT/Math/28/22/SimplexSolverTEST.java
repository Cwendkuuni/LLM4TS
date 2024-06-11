package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.PointValues;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearObjectiveFunctionSet;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link SimplexSolver} class.
 */
public class SimplexSolverTest {

    /**
     * Test for {@link SimplexSolver#SimplexSolver()} constructor.
     */
    @Test
    public void testDefaultConstructor() {
        SimplexSolver solver = new SimplexSolver();
        LinearObjectiveFunctionSet objFuncs = new LinearObjectiveFunctionSet();
        LinearConstraintSet constraints = new LinearConstraintSet();
        GoalType goalType = GoalType.MINIMIZE;
        PointValuePair solution = solver.doOptimize(objFuncs, constraints, goalType);
        Assert.assertNotNull(solution);
    }

    /**
     * Test for {@link SimplexSolver#SimplexSolver(double, int)} constructor.
     */
    @Test
    public void testCustomConstructor() {
        SimplexSolver solver = new SimplexSolver(1e-5, 5);
        LinearObjectiveFunctionSet objFuncs = new LinearObjectiveFunctionSet();
        LinearConstraintSet constraints = new LinearConstraintSet();
        GoalType goalType = GoalType.MINIMIZE;
        PointValuePair solution = solver.doOptimize(objFuncs, constraints, goalType);
        Assert.assertNotNull(solution);
    }

    /**
     * Test for {@link SimplexSolver#doOptimize()} method with simple example.
     */
    @Test
    public void testSimpleExample() {
        SimplexSolver solver = new SimplexSolver();

        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{1, 2}, new double[]{-3, -2});
        PointValues objectiveValues = new PointValues();
        objectiveValues.add(objFunc.value(new double[]{0, 0}));

        LinearConstraint constraint1 = new LinearConstraint(new double[]{1, 1}, 0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{1, -1}, 3);
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.add(constraint1);
        constraints.add(constraint2);

        GoalType goalType = GoalType.MINIMIZE;

        PointValuePair solution = solver.doOptimize(new LinearObjectiveFunctionSet(objFunc), constraints, goalType);

        Assert.assertEquals(-3, solution.getValue(), 1e-6);
        Assert.assertArrayEquals(new double[]{2/3, 1/3}, solution.getPoint(), 1e-6);
    }

    /**
     * Test for {@link SimplexSolver#doOptimize()} method with objective function and constraint with large coefficients.
     */
    @Test
    public void testLargeCoefficients() {
        SimplexSolver solver = new SimplexSolver();

        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{1e10, 1e10}, new double[]{-1e12, -1e12});
        PointValues objectiveValues = new PointValues();
        objectiveValues.add(objFunc.value(new double[]{0, 0}));

        LinearConstraint constraint1 = new LinearConstraint(new double[]{1e15, 1e15}, 0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{1e15, -1e15}, 3e15);
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.add(constraint1);
        constraints.add(constraint2);

        GoalType goalType = GoalType.MINIMIZE;

        try {
            PointValuePair solution = solver.doOptimize(new LinearObjectiveFunctionSet(objFunc), constraints, goalType);
            Assert.fail();
        } catch (MaxCountExceededException ex) {
            // expected
        }
    }

    /**
     * Test for {@link SimplexSolver#doOptimize()} method with unbounded solution.
     */
    @Test
    public void testUnboundedSolution() {
        SimplexSolver solver = new SimplexSolver();

        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{1, 2}, new double[]{1, 1});
        PointValues objectiveValues = new PointValues();
        objectiveValues.add(objFunc.value(new double[]{0, 0}));

        LinearConstraintSet constraints = new LinearConstraintSet();

        GoalType goalType = GoalType.MINIMIZE;

        try {
            PointValuePair solution = solver.doOptimize(new LinearObjectiveFunctionSet(objFunc), constraints, goalType);
            Assert.fail();
        } catch (UnboundedSolutionException ex) {
            // expected
        }
    }

    /**
     * Test for {@link SimplexSolver#doOptimize()} method with no feasible solution.
     */
    @Test
    public void testNoFeasibleSolution() {
        SimplexSolver solver = new SimplexSolver();

        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{1, 1}, new double[]{0, 1});
        PointValues objectiveValues = new PointValues();
        objectiveValues.add(objFunc.value(new double[]{0, 0}));

        LinearConstraint constraint1 = new LinearConstraint(new double[]{1, 1}, 0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{1, -1}, 1);
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.add(constraint1);
        constraints.add(constraint2);

        GoalType goalType = GoalType.MINIMIZE;

        try {
            PointValuePair solution = solver.doOptimize(new LinearObjectiveFunctionSet(objFunc), constraints, goalType);
            Assert.fail();
        } catch (NoFeasibleSolutionException ex) {
            // expected
        }
    }
}