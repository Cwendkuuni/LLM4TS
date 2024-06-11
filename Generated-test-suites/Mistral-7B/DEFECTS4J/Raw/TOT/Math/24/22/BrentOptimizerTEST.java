package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.SimpleBounds;
import org.apache.commons.math3.optimization.BrentOptimizer;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class BrentOptimizerTest {

    private static final double RELATIVE_TOLERANCE = 1e-6;
    private static final double ABSOLUTE_TOLERANCE = 1e-8;

    @Test
    public void testSimpleMin() {
        UnivariateFunction func = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE);
        UnivariatePointValuePair result = optimizer.optimize(func, new SimpleBounds(0, 4), GoalType.MINIMIZE);
        assertEquals(-2, result.getValue(), RELATIVE_TOLERANCE);
        assertEquals(2.0, result.getPoint(), RELATIVE_TOLERANCE);
    }

    @Test
    public void testSimpleMax() {
        UnivariateFunction func = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE);
        UnivariatePointValuePair result = optimizer.optimize(func, new SimpleBounds(0, 4), GoalType.MAXIMIZE);
        assertEquals(4, result.getValue(), RELATIVE_TOLERANCE);
        assertEquals(2.0, result.getPoint(), RELATIVE_TOLERANCE);
    }

    @Test
    public void testSimpleMinWithBounds() {
        UnivariateFunction func = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE);
        UnivariatePointValuePair result = optimizer.optimize(func, new SimpleBounds(-5, 5), GoalType.MINIMIZE);
        assertEquals(-2, result.getValue(), RELATIVE_TOLERANCE);
        assertEquals(-2.0, result.getPoint(), RELATIVE_TOLERANCE);
    }

    @Test
    public void testSimpleMaxWithBounds() {
        UnivariateFunction func = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE);
        UnivariatePointValuePair result = optimizer.optimize(func, new SimpleBounds(-5, 5), GoalType.MAXIMIZE);
        assertEquals(25, result.getValue(), RELATIVE_TOLERANCE);
        assertEquals(5.0, result.getPoint(), RELATIVE_TOLERANCE);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testRelativeThresholdTooSmall() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, ABSOLUTE_TOLERANCE);
        assertThrows(NumberIsTooSmallException.class, () -> optimizer.optimize(new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x;
            }
        }, new SimpleBounds(0, 1), GoalType.MINIMIZE));
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testAbsoluteThresholdNegative() {
        BrentOptimizer optimizer = new BrentOptimizer(RELATIVE_TOLERANCE, -1e-8);
        assertThrows(NumberIsTooSmallException.class, () -> optimizer.optimize(new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x;
            }
        }, new SimpleBounds(0, 1), GoalType.MINIMIZE));
    }
}