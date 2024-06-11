package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.UnivariatePointValuePair;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * JUnit test cases for BrentOptimizer class.
 */
@RunWith(JUnit4.class)
public class BrentOptimizerTest {

    /**
     * Test constructor with given relative and absolute thresholds.
     * @throws NotStrictlyPositiveException
     */
    @Test
    public void testConstructorWithThresholds() {
        try {
            new BrentOptimizer(0.0, 0.0);
            Assert.fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        new BrentOptimizer(2.0, 0.0);
        new BrentOptimizer(0.0, 1.0);
        new BrentOptimizer(0.01, 1.0);
    }

    /**
     * Test constructor with given relative threshold.
     */
    @Test
    public void testConstructorWithRelativeThreshold() {
        new BrentOptimizer(0.0);
        new BrentOptimizer(0.1);
        new BrentOptimizer(1.0);
    }

    /**
     * Test optimize method with a simple function and given bounds.
     */
    @Test
    public void testOptimizeSimpleFunction() {
        UnivariateFunction function = f -> Math.pow(f, 2);
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 1.0);
        UnivariatePointValuePair result = optimizer.optimize(function, -5.0, 5.0);
        Assert.assertEquals(0.0, result.getPoint(), 0.001);
        Assert.assertEquals(0.0, result.getValue(), 0.001);
    }

    /**
     * Test optimize method with a simple function and given start value.
     */
    @Test
    public void testOptimizeSimpleFunctionWithStartValue() {
        UnivariateFunction function = f -> Math.pow(f, 2);
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 1.0);
        UnivariatePointValuePair result = optimizer.optimize(function, 3.0);
        Assert.assertEquals(1.0, result.getPoint(), 0.001);
        Assert.assertEquals(1.0, result.getValue(), 0.001);
    }

    /**
     * Test optimize method with a more complex function and given bounds.
     */
    @Test
    public void testOptimizeComplexFunction() {
        UnivariateFunction function = f -> Math.pow(Math.sin(f), 2) + 1;
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 1.0);
        UnivariatePointValuePair result = optimizer.optimize(function, -5.0, 5.0);
        Assert.assertEquals(-1.175201193643801, result.getPoint(), 0.001);
        Assert.assertEquals(0.0, result.getValue(), 0.001);
    }

    /**
     * Test optimize method with a more complex function and given start value.
     */
    @Test
    public void testOptimizeComplexFunctionWithStartValue() {
        UnivariateFunction function = f -> Math.pow(Math.sin(f), 2) + 1;
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 1.0);
        UnivariatePointValuePair result = optimizer.optimize(function, 2.0);
        Assert.assertEquals(0.8813736877573367, result.getPoint(), 0.001);
        Assert.assertEquals(1.118033988749895, result.getValue(), 0.001);
    }
}