package org.apache.commons.cli;

import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.GoalType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;
    private UnivariateFunction function;

    @Before
    public void setUp() {
        function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2 * x * Math.pow(Math.E, x);
            }
        };

        optimizer = new BrentOptimizer(1e-5, 1e-5);
    }

    @Test
    public void testBrentOptimizer_minimal() {
        UnivariatePointValuePair result = optimizer.optimize(function, -3, 3);
        Assert.assertNotNull(result);
        Assert.assertEquals(0.3626865, result.getX(), 1e-5);
        Assert.assertEquals(-0.7122123, result.getValue(), 1e-5);
    }

    @Test
    public void testBrentOptimizer_maximal() {
        optimizer = new BrentOptimizer(1e-5, 1e-5, new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iter, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return Math.abs(previous.getValue() - current.getValue()) < 1e-5;
            }
        });

        UnivariatePointValuePair result = optimizer.optimize(function.negate(), -3, 3);
        Assert.assertNotNull(result);
        Assert.assertEquals(-0.3626865, result.getX(), 1e-5);
        Assert.assertEquals(0.7122123, result.getValue(), 1e-5);
    }

    @Test
    public void testBrentOptimizer_goalType() {
        optimizer = new BrentOptimizer(1e-5, 1e-5, new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iter, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return Math.abs(previous.getValue() - current.getValue()) < 1e-5;
            }
        });

        UnivariatePointValuePair result = optimizer.optimize(function.negate(), -3, 3, GoalType.MAXIMIZE);
        Assert.assertNotNull(result);
        Assert.assertEquals(-0.3626865, result.getX(), 1e-5);
        Assert.assertEquals(0.7122123, result.getValue(), 1e-5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizer_illegalArgument() {
        optimizer = new BrentOptimizer(-1, 1e-5);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizer_illegalArgument2() {
        optimizer = new BrentOptimizer(1e-6, 1e-5);
    }
}

class UnivariateFunction {
    double value(double x) {
        return 0;
    }
}