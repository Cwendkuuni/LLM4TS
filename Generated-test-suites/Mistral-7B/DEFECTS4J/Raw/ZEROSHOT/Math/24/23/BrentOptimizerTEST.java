package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.UnivariateOptimizer;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.special.E;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link BrentOptimizer}.
 */
public class BrentOptimizerTest {

    /**
     * Test function.
     */
    private static class MyFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.exp(Math.sin(x)) - E.EulerNumber.doubleValue() * Math.pow(Math.abs(x), 2);
        }

        @Override
        public double derivative(double x) {
            return (Math.cos(x) * Math.exp(Math.sin(x)) * 2 + 2 * Math.abs(x));
        }
    }

    /**
     * Test optimizer.
     */
    private UnivariateOptimizer optimizer;

    /**
     * Sets up the test.
     *
     * @throws NotStrictlyPositiveException if the thresholds are not positive.
     */
    @Before
    public void setUp() throws NotStrictlyPositiveException {
        optimizer = new BrentOptimizer(1e-8, 1e-8);
    }

    /**
     * Tests that the optimizer works correctly with the default stopping criterion.
     */
    @Test
    public void testDefaultStoppingCriterion() {
        UnivariateFunction function = new MyFunction();
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, new MaxEval(100));
        assertNotNull(result);
        assertEquals(-0.772525599625209, result.getPoint(), 1e-8);
        assertEquals(-1.115593408145741, result.getValue(), 1e-8);
    }

    /**
     * Tests that the optimizer converges to a minimum with the specified stopping criterion.
     */
    @Test
    public void testStoppingCriterion() {
        UnivariateFunction function = new MyFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10);
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, new MaxEval(100));
        assertNotNull(result);
        assertEquals(-0.772525599625209, result.getPoint(), 1e-10);
        assertEquals(-1.115593408145741, result.getValue(), 1e-10);
    }

    /**
     * Tests that the optimizer converges to a maximum with the specified stopping criterion.
     */
    @Test
    public void testStoppingCriterionMax() {
        UnivariateFunction function = x -> -MyFunction.this.value(x);
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10);
        PointValuePair result = optimizer.optimize(function, GoalType.MAXIMIZE, new MaxEval(100));
        assertNotNull(result);
        assertEquals(0.772525599625209, result.getPoint(), 1e-10);
        assertEquals(1.115593408145741, result.getValue(), 1e-10);
    }

    /**
     * Tests that the optimizer returns {@code null} if the initial interval is empty.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyInterval() {
        UnivariateFunction function = x -> 0;
        optimizer.optimize(function, GoalType.MINIMIZE, null);
    }

    /**
     * Tests that the optimizer throws an exception if the relative threshold is too small.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testSmallRelativeThreshold() {
        optimizer = new BrentOptimizer(1e-15, 1e-8);
    }

    /**
     * Tests that the optimizer throws an exception if the absolute threshold is not positive.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeAbsoluteThreshold() {
        optimizer = new BrentOptimizer(1e-8, -1e-8);
    }

}