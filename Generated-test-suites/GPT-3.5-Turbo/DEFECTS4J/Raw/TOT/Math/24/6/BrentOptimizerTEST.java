package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Test if the algorithm returns the correct minimum value for a quadratic function
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10);
        UnivariateFunction function = new QuadraticFunction();
        OptimizationData[] data = {GoalType.MINIMIZE};
        optimizer.optimize(data, function);

        double expectedMinValue = 0;
        double actualMinValue = optimizer.getMin();

        Assert.assertEquals(expectedMinValue, actualMinValue, 1e-10);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNotStrictlyPositiveException() {
        // Test if an exception is thrown when the absolute threshold is not strictly positive
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testNumberIsTooSmallException() {
        // Test if an exception is thrown when the relative threshold is smaller than 2 * Math.ulp(1d)
        BrentOptimizer optimizer = new BrentOptimizer(1e-20, 1e-10);
    }

    class QuadraticFunction implements UnivariateFunction {
        public double value(double x) {
            return x * x;
        }
    }
}