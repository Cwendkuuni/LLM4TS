package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.LinearConstraint;
import org.apache.commons.math3.optimization.PointVectorValue;
import org.apache.commons.math3.optimization.SimplexTableau;
import org.apache.commons.math3.optimization.goals.MaximizationGoal;
import org.apache.commons.math3.optimization.linear.LinearConstraintSet;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;

import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testDoOptimize() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{
                {1.0, 2.0},
                {3.0, 4.0}}, new double[]{5.0, 6.0});

        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(2.0, new double[]{1.0, 1.0}, 0.0));
        constraints.addConstraint(new LinearConstraint(-1.0, new double[]{2.0, 3.0}, 0.0));

        MaximizationGoal goal = new MaximizationGoal();

        SimplexSolver solver = new SimplexSolver();

        PointValuePair result = solver.doOptimize();
        assertEquals(new PointVectorValue(new double[]{3.0 / 5.0, 4.0 / 5.0}), result.getPoint());
        assertEquals(5.0, result.getValue(), 1e-6);
    }

    @Test
    public void testDoOptimizeWithFeasibleSolution() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{
                {1.0, 2.0},
                {3.0, 4.0}}, new double[]{5.0, 6.0});

        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(0.0, new double[]{1.0, 1.0}, 0.0));
        constraints.addConstraint(new LinearConstraint(0.0, new double[]{2.0, 3.0}, 0.0));

        MaximizationGoal goal = new MaximizationGoal();

        SimplexSolver solver = new SimplexSolver();

        PointValuePair result = solver.doOptimize();
        assertEquals(new PointVectorValue(new double[]{1.0, 1.0}), result.getPoint());
        assertEquals(5.0, result.getValue(), 1e-6);
    }

    @Test
    public void testDoOptimizeWithUnboundedSolution() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{
                {1.0, 1.0},
                {1.0, 1.0}}, new double[]{0.0, 0.0});

        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(Double.NEGATIVE_INFINITY, new double[]{1.0, 1.0}, 0.0));

        MaximizationGoal goal = new MaximizationGoal();

        SimplexSolver solver = new SimplexSolver();

        assertThrows(UnboundedSolutionException.class, () -> solver.doOptimize());
    }

    @Test
    public void testDoOptimizeWithMaxIterations() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{
                {1.0, 2.0},
                {3.0, 4.0}}, new double[]{5.0, 6.0});

        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(2.0, new double[]{1.0, 1.0}, 0.0));
        constraints.addConstraint(new LinearConstraint(-1.0, new double[]{2.0, 3.0}, 0.0));

        MaximizationGoal goal = new MaximizationGoal();

        SimplexSolver solver = new SimplexSolver(1, 1);

        assertThrows(MaxCountExceededException.class, () -> solver.doOptimize());
    }

    @Test
    public void testDoOptimizeWithNoFeasibleSolution() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{
                {1.0, 2.0},
                {3.0, 4.0}}, new double[]{5.0, 6.0});

        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(3.0, new double[]{1.0, 1.0}, 0.0));

        MaximizationGoal goal = new MaximizationGoal();

        SimplexSolver solver = new SimplexSolver();

        assertThrows(NoFeasibleSolutionException.class, () -> solver.doOptimize());
    }

    @Test
    public void testDoOptimizeWithDifferentConstraints() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{
                {1.0, 2.0},
                {3.0, 4.0}}, new double[]{5.0, 6.0});

        LinearConstraintSet constraints1 = new LinearConstraintSet();
        constraints1.addConstraint(new LinearConstraint(2.0, new double[]{1.0, 1.0}, 0.0));
        constraints1.addConstraint(new LinearConstraint(-1.0, new double[]{2.0, 3.0}, 0.0));

        LinearConstraintSet constraints2 = new LinearConstraintSet();
        constraints2.addConstraint(new LinearConstraint(0.0, new double[]{1.0, 1.0}, 0.0));
        constraints2.addConstraint(new LinearConstraint(0.0, new double[]{2.0, 3.0}, 0.0));

        MaximizationGoal goal = new MaximizationGoal();

        SimplexSolver solver1 = new SimplexSolver();
        PointValuePair result1 = solver1.doOptimize(f, constraints1, goal);
        assertEquals(new PointVectorValue(new double[]{3.0 / 5.0, 4.0 / 5.0}), result1.getPoint());
        assertEquals(5.0, result1.getValue(), 1e-6);

        SimplexSolver solver2 = new SimplexSolver();
        PointValuePair result2 = solver2.doOptimize(f, constraints2, goal);
        assertEquals(new PointVectorValue(new double[]{1.0, 1.0}), result2.getPoint());
        assertEquals(5.0, result2.getValue(), 1e-6);
    }
}