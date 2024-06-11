package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link SimplexSolver}.
 */
public class SimplexSolverTest {

    /**
     * Tests a simple optimization problem with a single objective function and no constraints.
     */
    @Test
    public void testSimpleOptimization() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{-1d, 2d}, 3d);
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(obj);
        assertEquals(PointValuePair.Pair.ZERO, solution, 0.0);
    }

    /**
     * Tests a simple optimization problem with a single objective function and a single constraint.
     */
    @Test
    public void testSimpleOptimizationWithConstraint() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{-1d, 2d}, 3d);
        LinearConstraint constraint = new LinearConstraint(new double[]{-1d, 1d}, 2d);
        LinearConstraintSet constraintSet = new LinearConstraintSet();
        constraintSet.addConstraint(constraint);
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(obj, constraintSet);
        assertEquals(PointValuePair.Pair.ZERO, solution, 0.0);
    }

    /**
     * Tests an optimization problem with multiple objective functions and no constraints.
     */
    @Test
    public void testMultipleObjectivesNoConstraints() {
        LinearObjectiveFunction obj1 = new LinearObjectiveFunction(new double[]{1d, 2d}, 3d);
        LinearObjectiveFunction obj2 = new LinearObjectiveFunction(new double[]{1d, -2d}, 4d);
        SimplexSolver solver = new SimplexSolver();
        PointValuePair[] solutions = solver.optimize(new LinearObjectiveFunction[]{obj1, obj2});
        PointValuePair solution1 = solutions[0];
        PointValuePair solution2 = solutions[1];
        assertEquals(PointValuePair.Pair.ZERO, solution1, 0.0);
        assertEquals(PointValuePair.Pair.ZERO, solution2, 0.0);
    }

    /**
     * Tests an optimization problem with multiple objective functions and a single constraint.
     */
    @Test
    public void testMultipleObjectivesWithConstraint() {
        LinearObjectiveFunction obj1 = new LinearObjectiveFunction(new double[]{1d, 2d}, 3d);
        LinearObjectiveFunction obj2 = new LinearObjectiveFunction(new double[]{1d, -2d}, 4d);
        LinearConstraint constraint = new LinearConstraint(new double[]{-1d, 1d}, 2d);
        LinearConstraintSet constraintSet = new LinearConstraintSet();
        constraintSet.addConstraint(constraint);
        SimplexSolver solver = new SimplexSolver();
        PointValuePair[] solutions = solver.optimize(new LinearObjectiveFunction[]{obj1, obj2}, constraintSet);
        PointValuePair solution1 = solutions[0];
        PointValuePair solution2 = solutions[1];
        assertEquals(PointValuePair.Pair.ZERO, solution1, 0.0);
        assertEquals(PointValuePair.Pair.ZERO, solution2, 0.0);
    }

    /**
     * Tests an optimization problem with multiple objective functions and multiple constraints.
     */
    @Test
    public void testMultipleObjectivesWithMultipleConstraints() {
        LinearObjectiveFunction obj1 = new LinearObjectiveFunction(new double[]{1d, 2d}, 3d);
        LinearObjectiveFunction obj2 = new LinearObjectiveFunction(new double[]{1d, -2d}, 4d);
        LinearConstraint constraint1 = new LinearConstraint(new double[]{-1d, 1d}, 2d);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{2d, 2d}, 3d);
        LinearConstraintSet constraintSet = new LinearConstraintSet();
        constraintSet.addConstraint(constraint1);
        constraintSet.addConstraint(constraint2);
        SimplexSolver solver = new SimplexSolver();
        PointValuePair[] solutions = solver.optimize(new LinearObjectiveFunction[]{obj1, obj2}, constraintSet);
        PointValuePair solution1 = solutions[0];
        PointValuePair solution2 = solutions[1];
        // There is no guaranteed solution for this test case, as the constraints might be incompatible.
        assertNull(solution1);
        assertNull(solution2);
    }

    /**
     * Tests an optimization problem with a single objective function and an unbounded constraint.
     */
    @Test
    public void testUnboundedConstraint() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{-1d, 2d}, 3d);
        LinearConstraint constraint = new LinearConstraint(new double[]{-1d, 1d}, Double.POSITIVE_INFINITY);
        LinearConstraintSet constraintSet = new LinearConstraintSet();
        constraintSet.addConstraint(constraint);
        SimplexSolver solver = new SimplexSolver();
        assertThrows(UnboundedSolutionException.class, () -> solver.optimize(obj, constraintSet));
    }

    /**
     * Tests an optimization problem with a single objective function and a inconsistent constraint set.
     */
    @Test
    public void testInconsistentConstraints() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{1d, 2d}, 3d);
        LinearConstraint constraint1 = new LinearConstraint(new double[]{-1d, 1d}, 2d);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{-1d, 1d}, 3d);
        LinearConstraintSet constraintSet = new LinearConstraintSet();
        constraintSet.addConstraint(constraint1);
        constraintSet.addConstraint(constraint2);
        SimplexSolver solver = new SimplexSolver();
        assertThrows(NoFeasibleSolutionException.class, () -> solver.optimize(obj, constraintSet));
    }

    /**
     * Tests an optimization problem with a single objective function and an empty constraint set.
     */
    @Test
    public void testEmptyConstraints() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{-1d, 2d}, 3d);
        LinearConstraintSet constraintSet = new LinearConstraintSet();
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(obj, constraintSet);
        assertEquals(PointValuePair.Pair.ZERO, solution, 0.0);
    }

    /**
     * Tests an optimization problem with a single objective function and a constraint set with no solution.
     */
    @Test
    public void testNoSolutionConstraints() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{-1d, 2d}, 3d);
        LinearConstraint constraint1 = new LinearConstraint(new double[]{-1d, 1d}, Double.NEGATIVE_INFINITY);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{-1d, 1d}, Double.POSITIVE_INFINITY);
        LinearConstraintSet constraintSet = new LinearConstraintSet();
        constraintSet.addConstraint(constraint1);
        constraintSet.addConstraint(constraint2);
        SimplexSolver solver = new SimplexSolver();
        assertThrows(UnboundedSolutionException.class, () -> solver.optimize(obj, constraintSet));
    }

    /**
     * Tests a optimization problem with a single objective function and an infinite number of iterations.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterations() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{-1d, 2d}, 3d);
        SimplexSolver solver = new SimplexSolver();
        solver.setMaxIterations(0);
        solver.optimize(obj);
    }

}