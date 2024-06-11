package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.initializer.AverageStartingPointInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.FibonacciSearchOptimizer;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.math3.BaseRegistry;
import org.apache.commons.math3.optim.BaseOptimizer;
import org.apache.commons.math3.optimization.BaseOptimizer.PAIR;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PAIR> optimizer = new FibonacciSearchOptimizer<>();
        assertEquals(MaxEval.UNLIMITED, optimizer.getMaxEvaluations());

        optimizer = new FibonacciSearchOptimizer<>(100);
        assertEquals(100, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PAIR> optimizer = new FibonacciSearchOptimizer<>();
        assertEquals(0, optimizer.getEvaluations());

        optimizer.optimize(new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }
        }, new SimpleBounds(0, 5), new AverageStartingPointInitializer(), new ConvergenceChecker() {
            @Override
            public boolean isConverged(PAIR current, PAIR previous) {
                return Math.abs(current.getPoint() - previous.getPoint()) < 0.001;
            }
        });

        assertEquals(1, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PAIR> optimizer = new FibonacciSearchOptimizer<>();

        ConvergenceChecker<PAIR> convergenceCheck = optimizer.getConvergenceChecker();
        assertNotNull(convergenceCheck);
        fail("Should not be able to access the optimizer's convergence checker directly.");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetConvergenceCheckerUnsupported() {
        BaseOptimizer<PAIR> optimizer = new FibonacciSearchOptimizer<>();

        optimizer.setConvergenceChecker(new ConvergenceChecker<PAIR>() {
            @Override
            public boolean isConverged(PAIR current, PAIR previous) {
                return false;
            }
        });

        ConvergenceChecker<PAIR> convergenceCheck = optimizer.getConvergenceChecker();
    }

    @Test
    public void testOptimizeWithBasicFunction() {
        BaseOptimizer<PAIR> optimizer = new FibonacciSearchOptimizer<>();
        OptimizationResult result = optimizeFunction(optimizer, Math::abs);

        assertEquals(1.0, result.getPoint(), 0.0);
        assertEquals(0.0, result.getValue(), 0.0);
    }

    @Test
    public void testOptimizeWithFunctionThrowsException() {
        BaseOptimizer<PAIR> optimizer = new FibonacciSearchOptimizer<>();
        OptimizationResult result = optimizeFunction(optimizer, Math::sqrt);

        fail("Expected IllegalArgumentException to be thrown.");
    }

    @Test
    public void testOptimizeWithFunctionReturnsNaN() {
        BaseOptimizer<PAIR> optimizer = new FibonacciSearchOptimizer<>();
        OptimizationResult result = optimizeFunction(optimizer, Math::log);

        assertEquals(Double.NaN, result.getValue(), 0.0);
    }

    @Test
    public void testOptimizeWithFunctionReturnsInfinity() {
        BaseOptimizer<PAIR> optimizer = new FibonacciSearchOptimizer<>();
        OptimizationResult result = optimizeFunction(optimizer, x -> 1.0 / x);

        assertEquals(Double.POSITIVE_INFINITY, result.getValue(), 0.0);
    }

    @Test
    public void testOptimizeWithFunctionReturnsMinusInfinity() {
        BaseOptimizer<PAIR> optimizer = new FibonacciSearchOptimizer<>();
        OptimizationResult result = optimizeFunction(optimizer, x -> -1.0 / x);

        assertEquals(Double.NEGATIVE_INFINITY, result.getValue(), 0.0);
    }

    private OptimizationResult optimizeFunction(BaseOptimizer<PAIR> optimizer, UnivariateFunction function) {
        return optimizer.optimize(function, new SimpleBounds(-10, 10), new AverageStartingPointInitializer(),
                BaseRegistry.getConvergenceChecker(1e-5));
    }
}