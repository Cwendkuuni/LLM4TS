package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link BrentOptimizer} class.
 */
public class BrentOptimizerTest {
    private BrentOptimizer optimizer;
    private UnivariateFunction function;

    /**
     * Sets up the test environment.
     */
    @Before
    public void setUp() {
        function = new MyFunction();
        optimizer = new BrentOptimizer(1e-5, 1e-5);
    }

    /**
     * Tests optimization of a simple function.
     */
    @Test
    public void testSimpleFunction() {
        optimizer.setFunction(function);
        optimizer.optimize();

        double expected = 1.0;
        UnivariatePointValuePair actual = optimizer.getResult();

        Assert.assertNotNull(actual);
        Assert.assertEquals(expected, actual.getPoint(), 1e-6);
        Assert.assertEquals(expected, actual.getValue(), 1e-6);
    }

    /**
     * Tests optimization of a function with multiple local minima.
     */
    @Test
    public void testMultipleMinima() {
        optimizer.setFunction(new MyFunctionWithMultipleMinima());
        optimizer.optimize();

        double expectedX = 2.5;
        double expectedF = 0.25;
        UnivariatePointValuePair actual = optimizer.getResult();

        Assert.assertNotNull(actual);
        Assert.assertEquals(expectedX, actual.getPoint(), 1e-6);
        Assert.assertEquals(expectedF, actual.getValue(), 1e-6);
    }

    /**
     * Tests optimization with custom convergence checker.
     */
    @Test
    public void testCustomConvergenceChecker() {
        optimizer = new BrentOptimizer(1e-5, 1e-5, new MyConvergenceChecker());
        optimizer.setFunction(function);
        optimizer.optimize();

        UnivariatePointValuePair actual = optimizer.getResult();
        Assert.assertNotNull(actual);
        Assert.assertNotNull(optimizer.getLastIteration());
    }

    /**
     * Tests optimization with wrong input parameters.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testWrongInputParameters() {
        optimizer = new BrentOptimizer(-1e-5, 1e-5);
    }

    /**
     * Tests optimization with wrong input parameters.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testWrongRelativeThreshold() {
        optimizer = new BrentOptimizer(0, 1e-5);
    }

    /**
     * Tests optimization with wrong input parameters.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testWrongConvergenceChecker() {
        optimizer = new BrentOptimizer(1e-5, 1e-5, null);
    }

    /**
     * Test function for optimization.
     */
    private static class MyFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return FastMath.pow(x, 3) - 2 * FastMath.pow(x, 2) + x;
        }

        @Override
        public GoalType getGoalType() {
            return GoalType.MINIMIZE;
        }
    }

    /**
     * Test function for optimization with multiple local minima.
     */
    private static class MyFunctionWithMultipleMinima implements UnivariateFunction {
        @Override
        public double value(double x) {
            return FastMath.pow(x - 2.5, 2) * FastMath.exp(-FastMath.pow(x - 2.5, 2));
        }

        @Override
        public GoalType getGoalType() {
            return GoalType.MINIMIZE;
        }
    }

    /**
     * Test convergence checker for optimization.
     */
    private static class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
        @Override
        public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
            return FastMath.abs(current.getValue()) < 1e-8;
        }
    }
}