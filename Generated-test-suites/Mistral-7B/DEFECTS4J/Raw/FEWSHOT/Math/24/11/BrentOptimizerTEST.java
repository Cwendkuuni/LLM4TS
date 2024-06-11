package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.SimpleBounds;
import org.apache.commons.math3.optimization.minimization.BrentOptimizer;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstructor() {
        double rel = 1e-6;
        double abs = 1e-8;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(rel, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(abs, optimizer.getAbsoluteThreshold(), 0.0);
        assertNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testBrentOptimizerConstructorWithChecker() {
        double rel = 1e-6;
        double abs = 1e-8;
        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iter, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return previous.getValue() == current.getValue();
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(rel, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(abs, optimizer.getAbsoluteThreshold(), 0.0);
        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testBrentOptimizerConstructorWithRelAndAbs() {
        double rel = 1e-6;
        double abs = 1e-8;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(rel, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(abs, optimizer.getAbsoluteThreshold(), 0.0);
        assertNull(optimizer.getConvergenceChecker());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerNegativeAbsoluteThreshold() {
        double rel = 1e-6;
        double abs = -1e-8;
        new BrentOptimizer(rel, abs);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerTooSmallRelativeThreshold() {
        double rel = 1e-10;
        double abs = 1e-8;
        new BrentOptimizer(rel, abs);
    }

    @Test
    public void testBrentOptimizerOptimizeSimpleFunction() {
        double rel = 1e-6;
        double abs = 1e-8;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        UnivariateFunction function = x -> x * x;
        SimpleBounds bounds = new SimpleBounds(0.0, 5.0);
        UnivariatePointValuePair result = optimizer.optimize(function, bounds);
        assertEquals(2.0, result.getPoint(), 0.0);
        assertEquals(0.0, result.getValue(), 0.0);
    }

    @Test
    public void testBrentOptimizerOptimizeFunctionWithLimit() {
        double rel = 1e-6;
        double abs = 1e-8;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        UnivariateFunction function = x -> x * x * Math.sin(Math.PI * x);
        SimpleBounds bounds = new SimpleBounds(-2.0, 2.0);
        UnivariatePointValuePair result = optimizer.optimize(function, bounds);
        assertEquals(0.0, result.getPoint(), 0.0);
        assertEquals(0.0, result.getValue(), 0.0);
    }

    @Test
    public void testBrentOptimizerOptimizeFunctionWithChecker() {
        double rel = 1e-6;
        double abs = 1e-8;
        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iter, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return previous.getValue() - current.getValue() < 1e-8;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        UnivariateFunction function = x -> Math.pow(Math.E, -x);
        SimpleBounds bounds = new SimpleBounds(0.0, 5.0);
        UnivariatePointValuePair result = optimizer.optimize(function, bounds);
        assertTrue(result.getValue() < 0.5);
    }

    @Test
    public void testBrentOptimizerOptimizeFunctionWithLimitAndCheck() {
        double rel = 1e-6;
        double abs = 1e-8;
        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iter, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return previous.getValue() - current.getValue() < 1e-8 && iter > 10;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        UnivariateFunction function = x -> Math.pow(Math.E, -x);
        SimpleBounds bounds = new SimpleBounds(0.0, 5.0);
        UnivariatePointValuePair result = optimizer.optimize(function, bounds);
        assertTrue(result.getValue() < 0.5 && iter > 10);
    }

    @Test
    public void testBrentOptimizerOptimizeFunctionWithLimitAndChecker() {
        double rel = 1e-6;
        double abs = 1e-8;
        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iter, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return previous.getValue() - current.getValue() < 1e-8;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        UnivariateFunction function = x -> Math.pow(Math.E, -x);
        SimpleBounds bounds = new SimpleBounds(0.0, 5.0);
        UnivariatePointValuePair result = optimizer.optimize(function, bounds);
        assertTrue(result.getValue() < 0.5);
    }

    @Test
    public void testBrentOptimizerOptimizeFunctionWithLimitAndLimitReached() {
        double rel = 1e-6;
        double abs = 1e-8;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        UnivariateFunction function = x -> Math.abs(Math.sin(x) - 1);
        SimpleBounds bounds = new SimpleBounds(-10.0, 10.0);
        UnivariatePointValuePair result = optimizer.optimize(function, bounds);
        assertEquals(0.0, result.getPoint(), 0.0);
        assertEquals(0.0, result.getValue(), 0.0);
    }

    @Test
    public void testBrentOptimizerOptimizeFunctionWithLimitAndCheckerLimitReached() {
        double rel = 1e-6;
        double abs = 1e-8;
        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iter, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return false; // doesn't converge
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        UnivariateFunction function = x -> Math.abs(Math.sin(x) - 1);
        SimpleBounds bounds = new SimpleBounds(-10.0, 10.0);
        UnivariatePointValuePair result = optimizer.optimize(function, bounds);
        assertNull(result);
    }

    @Test
    public void testBrentOptimizerOptimizeFunctionWithLimitAndLimitReachedAndChecker() {
        double rel = 1e-6;
        double abs = 1e-8;
        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iter, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return false; // doesn't converge
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        UnivariateFunction function = x -> Math.abs(Math.sin(x) - 1);
        SimpleBounds bounds = new SimpleBounds(-10.0, 10.0);
        UnivariatePointValuePair result = optimizer.optimize(function, bounds);
        assertNull(result);
        assertFalse(optimizer.isConverged());
    }

    @Test
    public void testBrentOptimizerOptimizeFunctionWithLimitAndLimitReachedAndCheckerIterations() {
        double rel = 1e-6;
        double abs = 1e-8;
        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iter, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return iter > 100;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        UnivariateFunction function = x -> Math.abs(Math.sin(x) - 1);
        SimpleBounds bounds = new SimpleBounds(-10.0, 10.0);
        UnivariatePointValuePair result = optimizer.optimize(function, bounds);
        assertNull(result);
        assertTrue(optimizer.isConverged());
    }

    @Test
    public void testBrentOptimizerBestPoints() {
        double rel = 1e-6;
        double abs = 1e-8;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);

        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 1.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 4.0);
        UnivariatePointValuePair c = new UnivariatePointValuePair(3.0, 9.0);

        assertEquals(a, optimizer.best(null, b, true));
        assertEquals(b, optimizer.best(a, c, false));
        assertEquals(a, optimizer.best(a, c, true));
    }

    @Test
    public void testBrentOptimizerIterations() {
        double rel = 1e-6;
        double abs = 1e-8;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        UnivariateFunction function = x -> Math.pow(Math.E, -x) * Math.sin(Math.PI * x);
        SimpleBounds bounds = new SimpleBounds(0.0, 5.0);
        UnivariatePointValuePair result = optimizer.optimize(function, bounds);
        assertEquals(14, optimizer.getIterations());
    }
}