package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.nonlinear.SimpleGoalType;
import org.apache.commons.math3.optim.nonlinear.linear.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.linear.LinearConstraints;
import org.apache.commons.math3.optim.nonlinear.linear.LinearObjectiveFunctionImpl;
import org.apache.commons.math3.optim.nonlinear.linear.LinearPointValuePair;
import org.apache.commons.math3.optim.nonlinear.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.linear.LinearObjectiveFunctionSet;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SimplexSolverTest {

    @Test
    public void testEmptyConstraintSet() throws Exception {
        LinearObjectiveFunction obj = new LinearObjectiveFunctionImpl(new double[]{-1}, new double[]{1.0});
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(obj, null);
        assertNull(solution);
    }

    @Test
    public void testSimpleExample() throws Exception {
        LinearObjectiveFunction obj = new LinearObjectiveFunctionImpl(new double[]{-2, 3}, new double[]{1.0, -1.0});
        LinearConstraint c1 = new LinearConstraint(new double[]{1.0}, 2.0);
        LinearConstraint c2 = new LinearConstraint(new double[]{-1.0, 1.0}, 0.0);
        LinearConstraintSet constraints = new LinearConstraintSet(new LinearConstraints(c1, c2));
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(obj, constraints);
        LinearPointValuePair expectedSolution = new LinearPointValuePair(new double[]{1.0 / 3.0, 2.0 / 3.0}, -1.0 / 3.0);
        assertEquals(expectedSolution, solution);
    }

    @Test
    public void testUnboundedSolution() throws Exception {
        LinearObjectiveFunction obj = new LinearObjectiveFunctionImpl(new double[]{-1}, new double[]{0.0});
        LinearConstraint c1 = new LinearConstraint(new double[]{1.0}, 2.0);
        LinearConstraintSet constraints = new LinearConstraintSet(new LinearConstraints(c1));
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.optimize(obj, constraints);
            fail("Expected UnboundedSolutionException");
        } catch (UnboundedSolutionException e) {
            // expected
        }
    }

    @Test
    public void testNoFeasibleSolution() throws Exception {
        LinearObjectiveFunction obj = new LinearObjectiveFunctionImpl(new double[]{-1}, new double[]{0.0});
        LinearConstraint c1 = new LinearConstraint(new double[]{1.0, 0.0}, 0.0);
        LinearConstraintSet constraints = new LinearConstraintSet(new LinearConstraints(c1));
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.optimize(obj, constraints);
            fail("Expected NoFeasibleSolutionException");
        } catch (NoFeasibleSolutionException e) {
            // expected
        }
    }

    @Test
    public void testMaxIterations() throws Exception {
        LinearObjectiveFunction obj = new LinearObjectiveFunctionImpl(new double[]{-1}, new double[]{1.0});
        LinearConstraint c1 = new LinearConstraint(new double[]{1.0}, 2.0);
        LinearConstraintSet constraints = new LinearConstraintSet(new LinearConstraints(c1));
        SimplexSolver solver = new SimplexSolver(1, 1);
        try {
            solver.optimize(obj, constraints);
            fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // expected
        }
    }

    @Test
    public void testGoalType() throws Exception {
        LinearObjectiveFunction obj = new LinearObjectiveFunctionImpl(new double[]{-1}, new double[]{1.0});
        LinearConstraint c1 = new LinearConstraint(new double[]{1.0}, 2.0);
        LinearConstraintSet constraints = new LinearConstraintSet(new LinearConstraints(c1));
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(obj, constraints, SimpleGoalType.MINIMIZE);
        LinearPointValuePair expectedSolution = new LinearPointValuePair(new double[]{1.0 / 3.0, 2.0 / 3.0}, -1.0 / 3.0);
        assertEquals(expectedSolution, solution);

        solver = new SimplexSolver();
        PointValuePair solution2 = solver.optimize(obj, constraints, SimpleGoalType.MAXIMIZE);
        LinearPointValuePair expectedSolution2 = new LinearPointValuePair(new double[]{2.0 / 3.0, 1.0 / 3.0}, 1.0 / 3.0);
        assertEquals(expectedSolution2, solution2);
    }

    @Test
    public void testRestrictToNonNegative() throws Exception {
        LinearObjectiveFunction obj = new LinearObjectiveFunctionImpl(new double[]{1.0, -1.0}, new double[]{0.0, 1.0});
        LinearConstraint c1 = new LinearConstraint(new double[]{1.0, 1.0}, 2.0);
        LinearConstraintSet constraints = new LinearConstraintSet(new LinearConstraints(c1));
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(obj, constraints, null, true);
        LinearPointValuePair expectedSolution = new LinearPointValuePair(new double[]{1.0, 0.0}, 0.0);
        assertEquals(expectedSolution, solution);

        solver = new SimplexSolver();
        try {
            solver.optimize(obj, constraints, null, false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testObjectiveFunctionSet() throws Exception {
        LinearObjectiveFunction obj1 = new LinearObjectiveFunctionImpl(new double[]{-1, 2}, new double[]{1.0, -1.0});
        LinearObjectiveFunction obj2 = new LinearObjectiveFunctionImpl(new double[]{1, -1}, new double[]{1.0, -1.0});
        LinearConstraint c1 = new LinearConstraint(new double[]{1.0, 0.0}, 1.0);
        LinearConstraintSet constraints = new LinearConstraintSet(new LinearConstraints(c1));
        LinearObjectiveFunctionSet objectiveFunctions = new LinearObjectiveFunctionSet(new LinearObjectiveFunction[] {obj1, obj2});
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(objectiveFunctions, constraints);
        LinearPointValuePair expectedSolution = new LinearPointValuePair(new double[]{1.0 / 3.0, 2.0 / 3.0}, -(1.0 / 3.0 + 1.0 / 3.0) / 3.0);
        assertEquals(expectedSolution, solution);
    }

    @Test
    public void testConstraintSet() throws Exception {
        LinearObjectiveFunction obj = new LinearObjectiveFunctionImpl(new double[]{-1, 2}, new double[]{1.0, -1.0});
        LinearConstraint c1 = new LinearConstraint(new double[]{1.0, 0.0}, 1.0);
        LinearConstraint c2 = new LinearConstraint(new double[]{0.0, 1.0}, 1.0);
        LinearConstraintSet constraints = new LinearConstraintSet(new LinearConstraints(c1, c2));
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(obj, constraints);
        LinearPointValuePair expectedSolution = new LinearPointValuePair(new double[]{1.0 / 2.0, 1.0 / 2.0}, -1.0 / 2.0);
        assertEquals(expectedSolution, solution);
    }
}