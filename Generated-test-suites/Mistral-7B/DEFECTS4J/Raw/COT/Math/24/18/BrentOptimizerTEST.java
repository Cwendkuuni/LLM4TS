package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.MinEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.nonlinear.SimpleBounds;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.special.E;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * JUnit 4 test file for the BrentOptimizer class.
 */
public class BrentOptimizerTest {
    /**
     * Test constructor with both rel and abs set.
     */
    @Test
    public void testConstructorWithRelAndAbs() {
        double rel = 1e-5;
        double abs = 1e-6;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, null);
        assertEquals(rel, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(abs, optimizer.getAbsoluteThreshold(), 0.0);
    }

    /**
     * Test constructor with only rel set.
     */
    @Test
    public void testConstructorWithRelOnly() {
        double rel = 1e-5;
        BrentOptimizer optimizer = new BrentOptimizer(rel, 0.0);
        assertEquals(rel, optimizer.getRelativeThreshold(), 0.0);
        assertNull(optimizer.getAbsoluteThreshold());
    }

    /**
     * Test constructor with only abs set.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithAbsOnlyNegative() {
        double abs = -1e-6;
        new BrentOptimizer(0.0, abs, null);
    }

    /**
     * Test constructor with only abs set.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithAbsOnlyZero() {
        double abs = 0.0;
        new BrentOptimizer(0.0, abs, null);
    }

    /**
     * Test optimizing a function with known minimum.
     */
    @Test
    public void testOptimizeFunctionWithKnownMinimum() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 0.0);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return -Math.pow(Math.sin(x), 2);
            }
        };
        PointValuePair result = optimizer.optimize(function, new SimpleBounds(-Math.PI, Math.PI));
        assertEquals(-Math.PI / 2, result.getPoint(), 1e-6);
        assertEquals(-1, result.getValue(), 1e-6);
    }

    /**
     * Test optimizing a function with known maximum.
     */
    @Test
    public void testOptimizeFunctionWithKnownMaximum() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 0.0, new MaxEval());
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        };
        PointValuePair result = optimizer.optimize(function, new SimpleBounds(-Math.PI, Math.PI));
        assertEquals(Math.PI / 2, result.getPoint(), 1e-6);
        assertEquals(1, result.getValue(), 1e-6);
    }

    /**
     * Test optimizing a function with known infimum and supremum.
     */
    @Test
    public void testOptimizeFunctionWithInfAndSup() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(-x) - E.E;
            }
        };
        PointValuePair result = optimizer.optimize(function, new SimpleBounds(0.0, Double.POSITIVE_INFINITY));
        assertNotNull(result);
        assertTrue(result.getValue() < 0);
        assertTrue(result.getPoint() > 0);
    }
}