package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearObjectiveFunction.LinearType;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearScalarValue;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.SimplexSolver;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair.*;

public class BrentOptimizerTest {

    @Test
    public void testValidConstruction() {
        // Test default constructor
        BrentOptimizer optimizer = new BrentOptimizer(null);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(Double.POSITIVE_INFINITY, optimizer.getMin(), 0);
        assertEquals(Double.NEGATIVE_INFINITY, optimizer.getMax(), 0);
        assertNull(optimizer.getConvergenceChecker());

        // Test default constructor with absolute and relative tolerances
        double relTol = 1e-4;
        double absTol = 1e-6;
        BrentOptimizer optimizer1 = new BrentOptimizer(relTol, absTol, null);
        assertEquals(GoalType.MINIMIZE, optimizer1.getGoalType());
        assertEquals(Double.POSITIVE_INFINITY, optimizer1.getMin(), 0);
        assertEquals(Double.NEGATIVE_INFINITY, optimizer1.getMax(), 0);
        assertEquals(relTol, optimizer1.getRelativeThreshold(), 0);
        assertEquals(absTol, optimizer1.getAbsoluteThreshold(), 0);
        assertNull(optimizer1.getConvergenceChecker());

        // Test constructor with additional parameter
        ConvergenceChecker<PointValuePair> convergenceChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return current.getValue() < 1e-6;
            }
        };
        BrentOptimizer optimizer2 = new BrentOptimizer(relTol, absTol, convergenceChecker);
        assertEquals(GoalType.MINIMIZE, optimizer2.getGoalType());
        assertEquals(Double.POSITIVE_INFINITY, optimizer2.getMin(), 0);
        assertEquals(Double.NEGATIVE_INFINITY, optimizer2.getMax(), 0);
        assertEquals(relTol, optimizer2.getRelativeThreshold(), 0);
        assertEquals(absTol, optimizer2.getAbsoluteThreshold(), 0);
        assertSame(convergenceChecker, optimizer2.getConvergenceChecker());
    }

    @Test
    public void testInvalidConstruction() {
        assertThrows(NotStrictlyPositiveException.class, () -> new BrentOptimizer(-1, 0));
        assertThrows(NumberIsTooSmallException.class, () -> new BrentOptimizer(0, 0));
        assertThrows(NotStrictlyPositiveException.class, () -> new BrentOptimizer(0, -1));
    }

    @Test
    public void testOptimize() {
        // Test optimization with no constraints
        ObjectiveFunction<Double> f = x -> Math.pow(x, 2);
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, null);
        PointValuePair result = optimizer.optimize(f, 1.0, 2.0, null);
        assertEquals(1.0, result.getPoint(), 1e-5);
        assertEquals(0.0, result.getValue(), 1e-5);

        // Test optimization with linear constraints
        LinearConstraintSet constraints = new LinearConstraintSet();
        LinearConstraint c1 = new LinearConstraint(new double[] {1.0}, 1.0);
        LinearConstraint c2 = new LinearConstraint(new double[] {1.0}, -1.0);
        constraints.addConstraint(c1);
        constraints.addConstraint(c2);
        LinearObjectiveFunction f2 = new LinearObjectiveFunction(new double[] {-2.0}, LinearType.CONSTANT);
        optimizer = new BrentOptimizer(1e-5, 1e-5, null);
        result = optimizer.optimize(f2, constraints, null);
        assertEquals(1.0, result.getPoint(), 1e-5);
        assertEquals(0.0, result.getValue(), 1e-5);

        // Test optimization with non-linear function and linear constraints
        ObjectiveFunction<Double> g = x -> Math.exp(-x) - Math.exp(-2.0) * x;
        SimplexSolver solver = new SimplexSolver();
        LinearSolver linearSolver = solver;
        InitialGuess<Double> initialGuess = null;
        result = optimizer.optimize(g, 0.0, 10.0, constraints, linearSolver, initialGuess);
        assertEquals(3.896312274948272, result.getPoint(), 1e-5);
        assertEquals(1.116324464293218e-16, result.getValue(), 1e-16);

        // Test optimization with non-linear function and non-linear constraints
        ObjectiveFunction<Double> h = x -> Math.pow(Math.sin(x), 2);
        result = optimizer.optimize(h, 0.0, Math.PI, null);
        assertEquals(1.273239544735162, result.getPoint(), 1e-5);
        assertEquals(0.4624508948471044, result.getValue(), 1e-5);

        // Test optimization with non-linear function and bounds
        ObjectiveFunction<Double> i = x -> Math.log(Math.abs(x) + 1.0) + Math.sin(x);
        result = optimizer.optimize(i, -10.0, 10.0, null);
        assertEquals(-0.573512525241251, result.getPoint(), 1e-5);
        assertEquals(0.4287442043988028, result.getValue(), 1e-5);

        // Test optimization with bound violation
        ObjectiveFunction<Double> j = x -> Math.pow(x, 2) - 4.0;
        result = optimizer.optimize(j, -10.0, -2.0, null);
        assertEquals(-2.0, result.getPoint(), 1e-5);
        assertEquals(4.0, result.getValue(), 1e-5);

        // Test optimization with non-convergence
        ObjectiveFunction<Double> k = x -> 1.0 / (x * x);
        result = optimizer.optimize(k, 1.0, 10.0, null);
        assertNull(result);
    }
}