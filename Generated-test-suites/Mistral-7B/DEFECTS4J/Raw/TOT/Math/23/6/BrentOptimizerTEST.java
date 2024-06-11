package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;

    @Before
    public void setUp() {
        try {
            optimizer = new BrentOptimizer(1e-5, 1e-5);
        } catch (NotStrictlyPositiveException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testMinimization() {
        Function f = new Function() {
            @Override
            public double value(double x) {
                return x * x - 4 * x + 4;
            }
        };

        UnivariateOptimizerResult result = optimizer.optimize(f, GoalType.MINIMIZE, null, null);
        UnivariatePointValuePair expected = new UnivariatePointValuePair(2.0, 0.0);
        Assert.assertEquals(expected.getValue(), result.getMin(), 1e-7);
        Assert.assertEquals(expected.getPoint(), result.getMinX(), 1e-7);
    }

    @Test
    public void testMinimizationWithInitialGuess() {
        Function f = new Function() {
            @Override
            public double value(double x) {
                return x * x - 4 * x + 4;
            }
        };

        UnivariateOptimizerResult result = optimizer.optimize(f, GoalType.MINIMIZE, 3.0, null);
        UnivariatePointValuePair expected = new UnivariatePointValuePair(2.0, 0.0);
        Assert.assertEquals(expected.getValue(), result.getMin(), 1e-7);
        Assert.assertEquals(expected.getPoint(), result.getMinX(), 1e-7);
    }

    @Test
    public void testMinimizationWithConvergenceChecker() {
        Function f = new Function() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        };

        ConvergenceChecker<UnivariatePointValuePair> checker = (iter, previous, current) -> {
            double currentValue = current.getValue();
            double previousValue = previous.getValue();
            return Math.abs(currentValue - previousValue) < 1e-6;
        };

        UnivariateOptimizerResult result = optimizer.optimize(f, GoalType.MINIMIZE, null, checker);
        UnivariatePointValuePair expected = new UnivariatePointValuePair(0.0, 0.0);
        Assert.assertEquals(expected.getValue(), result.getMin(), 1e-7);
        Assert.assertEquals(expected.getPoint(), result.getMinX(), 1e-7);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testExceptionWithInvalidAbsThreshold() {
        optimizer = new BrentOptimizer(-1e-5, 1e-5);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testExceptionWithInvalidRelThreshold() {
        optimizer = new BrentOptimizer(0.0, 1e-5);
    }

}