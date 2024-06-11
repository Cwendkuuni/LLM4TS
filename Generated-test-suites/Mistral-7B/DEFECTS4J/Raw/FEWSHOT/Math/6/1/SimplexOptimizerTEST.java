package org.apache.commons.cli.optimization.direct;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleValueChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.AbstractSimplex;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimplexMethod;

public class SimplexOptimizerTest {

    @Test
    public void testOptimize() {
        // Setup
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return FunctionUtils.pow(FunctionUtils.sum(x), 2);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1.0E-6, 1.0E-6));
        optimizer.setSimplex(new SimplexMethod(3));

        // Test
        PointValuePair result = optimizer.optimize(100, function, goalType, new double[]{2.0, 3.0, 4.0});

        // Verify
        assertEquals(0.0, result.getValue(), 1.0E-6);
        assertArrayEquals(new double[]{0.0, 0.0, 0.0}, result.getPoint(), 1.0E-6);
    }

    @Test
    public void testOptimizeWithInitialGuess() {
        // Setup
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return FunctionUtils.pow(FunctionUtils.sum(x), 2);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1.0E-6, 1.0E-6));
        optimizer.setSimplex(new SimplexMethod(3));
        double[] initialGuess = new double[]{1.0, 1.0, 1.0};

        // Test
        OptimizationData optimizationData = new InitialGuess(initialGuess);
        PointValuePair result = optimizer.optimize(100, function, goalType, optimizationData);

        // Verify
        assertEquals(0.0, result.getValue(), 1.0E-6);
        assertArrayEquals(initialGuess, result.getPoint(), 1.0E-6);
    }

    @Test(expected = NullArgumentException.class)
    public void testOptimizeNullFunction() {
        // Setup
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1.0E-6, 1.0E-6));
        optimizer.setSimplex(new SimplexMethod(3));

        // Test
        optimizer.optimize(100, null, GoalType.MINIMIZE, new double[]{2.0, 3.0, 4.0});

        // Verify - NullArgumentException is thrown
    }

    @Test(expected = NullArgumentException.class)
    public void testOptimizeNullGoalType() {
        // Setup
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return FunctionUtils.pow(FunctionUtils.sum(x), 2);
            }
        };
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1.0E-6, 1.0E-6));
        optimizer.setSimplex(new SimplexMethod(3));

        // Test
        optimizer.optimize(100, function, null, new double[]{2.0, 3.0, 4.0});

        // Verify - NullArgumentException is thrown
    }

    @Test(expected = NullArgumentException.class)
    public void testOptimizeNullOptimizationData() {
        // Setup
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return FunctionUtils.pow(FunctionUtils.sum(x), 2);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1.0E-6, 1.0E-6));
        optimizer.setSimplex(new SimplexMethod(3));

        // Test
        optimizer.optimize(100, function, goalType, null);

        // Verify - NullArgumentException is thrown
    }

    @Test(expected = IllegalStateException.class)
    public void testOptimizeWithoutSimplex() {
        // Setup
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return FunctionUtils.pow(FunctionUtils.sum(x), 2);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1.0E-6, 1.0E-6));

        // Test
        optimizer.optimize(100, function, goalType, new double[]{2.0, 3.0, 4.0});

        // Verify - IllegalStateException is thrown
    }
}