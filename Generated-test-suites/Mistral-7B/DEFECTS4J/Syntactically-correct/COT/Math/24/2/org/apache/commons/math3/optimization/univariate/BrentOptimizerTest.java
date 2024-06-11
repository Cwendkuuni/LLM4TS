package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.UnivariateOptimizer;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.SimpleBoundingMethod;
import org.apache.commons.math3.optimization.noDerivatives.BrentOptimizer;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test for BrentOptimizer class.
 */
public class BrentOptimizerTest {

    /**
     * Test constructor with relative and absolute threshold.
     * @throws NotStrictlyPositiveException
     */
    @Test
    public void testBrentOptimizer_relativeAndAbsoluteThreshold() throws NotStrictlyPositiveException {
        double relThreshold = 1e-6;
        double absThreshold = 1e-8;
        UnivariateOptimizer optimizer = new BrentOptimizer(relThreshold, absThreshold);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(relThreshold, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(absThreshold, optimizer.getAbsoluteThreshold(), 0.0);
    }

    /**
     * Test constructor with only relative threshold.
     * @throws NotStrictlyPositiveException
     */
    @Test
    public void testBrentOptimizer_relativeThreshold() throws NotStrictlyPositiveException {
        double relThreshold = 1e-6;
        UnivariateOptimizer optimizer = new BrentOptimizer(relThreshold);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(relThreshold, optimizer.getRelativeThreshold(), 0.0);
        assertNull(optimizer.getAbsoluteThreshold());
    }

    /**
     * Test constructor with only absolute threshold.
     * @throws NotStrictlyPositiveException
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizer_absoluteThreshold() throws NotStrictlyPositiveException {
        double absThreshold = 1e-8;
        new BrentOptimizer(0, absThreshold);
    }

    /**
     * Test optimization of a simple function.
     */
    @Test
    public void testFunctionMinimization() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-8);
        UnivariateFunction f = new SimpleBoundingMethod(Math::sin);

        UnivariatePointValuePair result = optimizer.optimize(f, 0, 2 * Math.PI);
        assertEquals(Math.PI / 2, result.getPoint(), 1e-6);
        assertEquals(-Math.sin(Math.PI / 2), result.getValue(), 1e-6);
    }

    /**
     * Test optimization of a non-differentiable function.
     */
    @Test
    public void testFunctionMinimizationWithNonDifferentiablePoint() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-8);
        UnivariateFunction f = x -> Math.pow(Math.abs(x - 0.5), 3) - Math.pow(x - 0.5, 2);

        UnivariatePointValuePair result = optimizer.optimize(f, -1, 1);
        assertEquals(0.5, result.getPoint(), 1e-6);
        assertEquals(0, result.getValue(), 1e-6);
    }

    /**
     * Test optimization with a function that never converges.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFunctionNeverConverging() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-8);
        UnivariateFunction f = x -> Math.sin(1 / x);

        optimizer.optimize(f, 0, 1);
    }
}