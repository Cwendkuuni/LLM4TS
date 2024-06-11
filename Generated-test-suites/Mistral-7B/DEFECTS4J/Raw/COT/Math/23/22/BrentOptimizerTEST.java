package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

/**
 * Test cases for BrentOptimizer class
 */
public class BrentOptimizerTest {

    private static final double RELATIVE_TOLERANCE = 1e-6;
    private static final double ABSOLUTE_TOLERANCE = 1e-8;
    private static final double MIN_FUNCTION_VALUE = 1e-15;
    private static final double MAX_FUNCTION_VALUE = 1e15;
    private static final double MAX_INTERVAL_LENGTH = 100;
    private static final double MINIMUM = 2.5;
    private static final double MAXIMUM = 7.5;
    private static final ConvergenceChecker<UnivariatePointValuePair> NO_OP_CHECKER = new ConvergenceChecker<UnivariatePointValuePair>() {
        @Override
        public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
            return false;
        }
    };

    private UnivariateFunction function;
    private BrentOptimizer optimizer;

    @Before
    public void setUp() {
        function = new SimpleFunction();
        optimizer = new BrentOptimizer(RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE, NO_OP_CHECKER);
    }

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testBrentOptimizerConstruction() {
        optimizer = new BrentOptimizer(RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE);
        Assert.assertNotNull(optimizer);
    }

    /**
     * Test constructor with invalid arguments.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerConstructionInvalidRelativeTolerance() {
        new BrentOptimizer(0, ABSOLUTE_TOLERANCE);
    }

    /**
     * Test constructor with invalid arguments.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerConstructionInvalidAbsoluteTolerance() {
        new BrentOptimizer(RELATIVE_TOLERANCE, 0);
    }

    /**
     * Test optimize method with valid arguments.
     */
    @Test
    public void testOptimizeValidArguments() {
        optimizer.optimize(MINIMUM, MAXIMUM);
        UnivariatePointValuePair result = optimizer.getResult();
        Assert.assertNotNull(result);
        Assert.assertTrue(result.getValue() <= MINIMUM);
        Assert.assertTrue(result.getValue() >= MIN_FUNCTION_VALUE);
    }

    /**
     * Test optimize method with relative tolerance less than minimum tolerance.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testOptimizeInvalidRelativeTolerance() {
        optimizer = new BrentOptimizer(1e-9, ABSOLUTE_TOLERANCE);
        optimizer.optimize(MINIMUM, MAXIMUM);
    }

    /**
     * Test optimize method with absolute tolerance less than zero.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizeInvalidAbsoluteTolerance() {
        optimizer = new BrentOptimizer(RELATIVE_TOLERANCE, -1);
        optimizer.optimize(MINIMUM, MAXIMUM);
    }

    /**
     * Test optimize method with invalid interval.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeInvalidInterval() {
        optimizer.optimize(MAXIMUM, MINIMUM);
    }

    /**
     * Test optimize method with partially overlapping interval.
     */
    @Test
    public void testOptimizePartiallyOverlappingInterval() {
        optimizer.optimize(MINIMUM + 1e-6, MAXIMUM);
        UnivariatePointValuePair result = optimizer.getResult();
        Assert.assertNotNull(result);
        Assert.assertTrue(result.getValue() <= MINIMUM + 1e-6);
        Assert.assertTrue(result.getValue() >= MIN_FUNCTION_VALUE);
    }

    /**
     * Test optimize method with a function that does not have a minimum.
     */
    @Test
    public void testOptimizeNoMinimum() {
        function = new SimpleFunctionNoMinimum();
        optimizer = new BrentOptimizer(RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE);
        optimizer.optimize(MINIMUM, MAXIMUM);
        UnivariatePointValuePair result = optimizer.getResult();
        Assert.assertNull(result);
    }

    /**
     * Test optimize method with a converging function.
     */
    @Test
    public void testOptimizeConvergingFunction() {
        optimizer.optimize(MINIMUM, MAXIMUM);
        UnivariatePointValuePair result = optimizer.getResult();
        Assert.assertNotNull(result);
        Assert.assertEquals(MINIMUM, result.getPoint(), 1e-6);
    }

    /**
     * Test optimize method with a function that converges slowly.
     */
    @Test
    public void testOptimizeSlowlyConvergingFunction() {
        optimizer = new BrentOptimizer(RELATIVE_TOLERANCE * 10, ABSOLUTE_TOLERANCE * 10);
        optimizer.optimize(MINIMUM, MAXIMUM);
        UnivariatePointValuePair result = optimizer.getResult();
        Assert.assertNotNull(result);
        Assert.assertEquals(MINIMUM, result.getPoint(), 1e-6);
    }

    /**
     * Simple function used for testing.
     */
    private static class SimpleFunction implements UnivariateFunction {

        @Override
        public double value(double x) {
            double value = Math.pow(x, 2) - 4 * x + 4;
            if (value < MIN_FUNCTION_VALUE) {
                value = MIN_FUNCTION_VALUE;
            }
            return value;
        }

        @Override
        public double derivative(double x) {
            return 2 * x - 4;
        }

        @Override
        public double[] gradient(double[] x) {
            return new double[]{2 * x[0] - 4};
        }
    }

    /**
     * Simple function used for testing. This function has no minimum.
     */
    private static class SimpleFunctionNoMinimum implements UnivariateFunction {

        @Override
        public double value(double x) {
            return x * x;
        }

        @Override
        public double derivative(double x) {
            return 2 * x;
        }

        @Override
        public double[] gradient(double[] x) {
            return new double[]{2 * x[0]};
        }
    }
}