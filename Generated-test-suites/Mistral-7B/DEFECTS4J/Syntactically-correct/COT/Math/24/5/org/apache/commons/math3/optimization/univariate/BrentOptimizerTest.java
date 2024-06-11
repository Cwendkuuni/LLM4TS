package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.test.AbstractMathTestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit 4 test cases for the {@link BrentOptimizer} class.
 */
public class BrentOptimizerTest extends AbstractMathTestCase {

    /**
     * Tested instance of {@link BrentOptimizer}.
     */
    private BrentOptimizer optimizer;

    /**
     * Tested {@link UnivariateFunction}.
     */
    private static final FunctionTest function = new FunctionTest();

    /**
     * Sets up the tested instance of {@link BrentOptimizer}.
     */
    @Before
    public void setUp() {
        optimizer = new BrentOptimizer(1e-5, 1e-5);
    }

    /**
     * Tests the constructor that takes a {@code ConvergenceChecker}.
     */
    @Test
    public void testBrentOptimizerWithConvergenceChecker() {
        optimizer = new BrentOptimizer(1e-5, 1e-5, null);
        assertNotNull(optimizer);
    }

    /**
     * Tests the constructor that takes only two arguments.
     */
    @Test
    public void testBrentOptimizerWithTwoArguments() {
        optimizer = new BrentOptimizer(1e-5, 1e-5);
        assertNotNull(optimizer);
    }

    /**
     * Throws an exception when the relative threshold is less than the minimal
     * value.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testIllegalRelativeThreshold() {
        new BrentOptimizer(0.0, 1e-5);
    }

    /**
     * Throws an exception when the absolute threshold is less than or equal to 0.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testIllegalAbsoluteThreshold() {
        new BrentOptimizer(1e-5, 0.0);
    }

    /**
     * Tests that the optimization method finds the minimum of a test function.
     */
    @Test
    public void testOptimize() {
        UnivariatePointValuePair result = optimizer.optimize(function, 0.5, 3.0, GoalType.MINIMIZE);
        UnivariatePointValuePair expected = new UnivariatePointValuePair(2.0, 0.0);
        Assert.assertEquals(expected, result);
    }

    /**
     * Tests that the optimization method finds the maximum of a test function.
     */
    @Test
    public void testOptimizeMaximum() {
        optimizer = new BrentOptimizer(1e-5, 1e-5, new MaximumTest());
        UnivariatePointValuePair result = optimizer.optimize(function, 0.5, 3.0, GoalType.MAXIMUM);
        UnivariatePointValuePair expected = new UnivariatePointValuePair(1.0, 1.0);
        Assert.assertEquals(expected, result);
    }

    /**
     * Tests the optimization of a function with a large range.
     */
    @Test
    public void testOptimizeLargeRange() {
        optimizer = new BrentOptimizer(1e-12, 1e-12);
        UnivariatePointValuePair result = optimizer.optimize(new LargeRangeFunction(), -1000.0, 1000.0, GoalType.MINIMUM);
        UnivariatePointValuePair expected = new UnivariatePointValuePair(-999.99999999999993, 1.234567890123d);
        Assert.assertEquals(expected, result, 1e-12);
    }

    /**
     * Tests that the optimization method converges to the expected result.
     */
    @Test
    public void testConvergence() {
        UnivariateFunction function = new FunctionTest();
        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return FastMath.abs(current.getValue() - previous.getValue()) < 1e-7;
            }
        };
        optimizer = new BrentOptimizer(1e-8, 1e-8, checker);

        UnivariatePointValuePair result = optimizer.optimize(function, 0.5, 3.0, GoalType.MINIMIZE);
        UnivariatePointValuePair expected = new UnivariatePointValuePair(2.0, 0.0);
        Assert.assertEquals(expected, result);
    }

    /**
     * Tests that the optimization method fails to converge.
     */
    @Test(expected = IllegalStateException.class)
    public void testNoConvergence() {
        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return false;
            }
        };
        optimizer = new BrentOptimizer(1e-8, 1e-8, checker);

        optimizer.optimize(new FunctionTest(), 0.5, 3.0, GoalType.MINIMIZE);
    }
}

/**
 * Test univariate function for {@link BrentOptimizer} test cases.
 */
class FunctionTest implements UnivariateFunction {
    @Override
    public double value(double x) {
        return Math.pow(x - 2.0, 2.0);
    }
}

/**
 * Test univariate function for maximum optimization test case.
 */
class MaximumTest implements UnivariateFunction {
    @Override
    public double value(double x) {
        return 1.0 / (1.0 + Math.exp(-10.0 * x));
    }
}

/**
 * Test univariate function for large range optimization test case.
 */
class LargeRangeFunction implements UnivariateFunction {
    @Override
    public double value(double x) {
        return Math.exp(Math.log(Math.random()) * 10.0) + x;
    }
}