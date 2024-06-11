package org.apache.commons.cli.optimization.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.junit.Test;

public class BrentOptimizerTest {
    private static final double DELTA = 1e-6;

    @Test
    public void testConstructor() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 1.0);
        assertEquals(0.1, optimizer.getRelativeThreshold(), DELTA);
        assertEquals(1.0, optimizer.getAbsoluteThreshold(), DELTA);

        optimizer = new BrentOptimizer(0.01, 0.0);
        assertTrue(optimizer.getRelativeThreshold() > 0.0);
        assertTrue(optimizer.getAbsoluteThreshold() > 0.0);

        try {
            BrentOptimizer optimizer1 = new BrentOptimizer(-1.0, 1.0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            BrentOptimizer optimizer2 = new BrentOptimizer(1.0, -1.0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            BrentOptimizer optimizer3 = new BrentOptimizer(0.0, -1.0);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }
    }

    @Test
    public void testBasic() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 1.0);
        UnivariateFunction f = (x) -> Math.pow(x, 2);
        UnivariatePointValuePair result = optimizer.optimize(f, -5.0, 5.0, null);
        UnivariatePointValuePair expected = new UnivariatePointValuePair(2.0, 4.0);
        assertEquals(expected, result, DELTA);
    }

    @Test
    public void testBasicWithConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 1.0, new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return FastMath.abs(current.getValue()) < 0.001;
            }
        });
        UnivariateFunction f = (x) -> Math.pow(x, 2);
        UnivariatePointValuePair result = optimizer.optimize(f, -5.0, 5.0, null);
        UnivariatePointValuePair expected = new UnivariatePointValuePair(2.0, 4.0);
        assertEquals(expected, result, DELTA);
        assertTrue(optimizer.getIterationCount() > 0);
    }

    @Test
    public void testMaximization() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 1.0, GoalType.MAXIMIZE);
        UnivariateFunction f = (x) -> Math.pow(x, 2);
        UnivariatePointValuePair result = optimizer.optimize(f, -5.0, 5.0, null);
        UnivariatePointValuePair expected = new UnivariatePointValuePair(-2.0, -4.0);
        assertEquals(expected, result, DELTA);
    }

    @Test
    public void testConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 1.0);
        UnivariateFunction f = (x) -> x * Math.sin(Math.PI * x);
        UnivariatePointValuePair result = optimizer.optimize(f, -5.0, 5.0, null);
        UnivariatePointValuePair expected = new UnivariatePointValuePair(-1.0, -1.0);
        assertEquals(expected, result, DELTA);

        optimizer = new BrentOptimizer(0.01, 0.1);
        result = optimizer.optimize(f, -5.0, 5.0, null);
        assertNull(result);

        optimizer = new BrentOptimizer(0.1, 1.0, new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return FastMath.abs(current.getValue() - 1.0) < 0.001;
            }
        });
        UnivariatePointValuePair result2 = optimizer.optimize(f, -5.0, 5.0, null);
        assertEquals(expected, result2, DELTA);

        optimizer = new BrentOptimizer(0.1, 1.0, new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return FastMath.abs(current.getValue() - 1.0) > 0.01;
            }
        });
        assertNull(optimizer.optimize(f, -5.0, 5.0, null));
    }

    @Test
    public void testBracketing() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 1.0);
        UnivariateFunction f = (x) -> Math.pow(x, 3) - 2 * Math.pow(x, 2) + x;

        UnivariatePointValuePair[] points = new UnivariatePointValuePair[] {
            new UnivariatePointValuePair(1.0, 2.0),
            new UnivariatePointValuePair(2.0, 3.0),
            new UnivariatePointValuePair(3.0, 5.0)
        };

        UnivariatePointValuePair result = optimizer.optimize(f, points, null);
        UnivariatePointValuePair expected = new UnivariatePointValuePair(1.0, 0.0);
        assertEquals(expected, result, DELTA);
    }

    @Test
    public void testBracketingWithConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 1.0, new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return FastMath.abs(current.getValue() - 0.1) < 0.001;
            }
        });
        UnivariateFunction f = (x) -> Math.pow(x, 3) - 2 * Math.pow(x, 2) + x;

        UnivariatePointValuePair[] points = new UnivariatePointValuePair[] {
            new UnivariatePointValuePair(1.0, 2.0),
            new UnivariatePointValuePair(2.0, 3.0),
            new UnivariatePointValuePair(3.0, 5.0)
        };

        UnivariatePointValuePair result = optimizer.optimize(f, points, null);
        UnivariatePointValuePair expected = new UnivariatePointValuePair(1.0, 0.0);
        assertEquals(expected, result, DELTA);
        assertTrue(optimizer.getIterationCount() > 0);
    }
}