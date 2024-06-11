package org.apache.commons.math3.optimization.univariate.test;

import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.special.E;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstructor() {
        // Test constructor with relative and absolute thresholds and ConvergenceChecker
        double rel = 1e-6;
        double abs = 1e-8;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, null);
        Assert.assertEquals(rel, optimizer.getRelativeThreshold(), 0.0001);
        Assert.assertEquals(abs, optimizer.getAbsoluteThreshold(), 0.0001);
        Assert.assertNull(optimizer.getConvergenceChecker());

        // Test constructor with relative and absolute thresholds only
        optimizer = new BrentOptimizer(rel, abs);
        Assert.assertEquals(rel, optimizer.getRelativeThreshold(), 0.0001);
        Assert.assertEquals(abs, optimizer.getAbsoluteThreshold(), 0.0001);
        Assert.assertNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testDoOptimizeBasic() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-8);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(function, new UnivariatePointValuePair(1.0), GoalType.MINIMIZE);
        Assert.assertEquals(0.0, result.getX(), 0.0001);
        Assert.assertEquals(0.0, result.getValue(), 0.0001);
    }

    @Test
    public void testDoOptimizeEdgeCases() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-8);

        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.abs(x), 2) * (Math.sin(Math.abs(x)) + 0.5) * Math.exp(-Math.abs(x));
            }
        };

        // Test with lo < hi
        UnivariatePointValuePair result = optimizer.optimize(function, new UnivariatePointValuePair(-1.0), GoalType.MINIMIZE);
        Assert.assertTrue(result != null);

        // Test with lo > hi
        result = optimizer.optimize(function, new UnivariatePointValuePair(1.0), GoalType.MINIMIZE);
        Assert.assertTrue(result != null);

        // Test with lo = hi
        result = optimizer.optimize(function, new UnivariatePointValuePair(0.0), GoalType.MINIMIZE);
        Assert.assertTrue(result != null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testDoOptimizerNegativeAbsoluteThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, -1e-8, null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testDoOptimizerNegativeRelativeThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(-1e-6, 1e-8, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDoOptimizerNullFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-8, null);
        optimizer.optimize(null, null, GoalType.MINIMIZE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDoOptimizerNullInitialGuess() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-8, null);
        optimizer.optimize(new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        }, null, GoalType.MINIMIZE);
    }

    @Test
    public void testDoOptimizerConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-8, new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iter, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return current.getValue() <= 1e-10 && previous.getValue() > 1e-10;
            }
        });

        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(-Math.abs(x)) * Math.sin(Math.abs(x));
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(function, new UnivariatePointValuePair(1.0), GoalType.MINIMIZE);
        Assert.assertEquals(0.0, result.getX(), 0.0001);
        Assert.assertEquals(0.0, result.getValue(), 0.0001);
        Assert.assertEquals(1, optimizer.getIterations());
    }
}