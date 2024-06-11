package org.apache.commons.cli.optimization.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.junit.Test;

public class BrentOptimizerTest {
    @Test
    public void testBrentOptimizerConstruction() {
        BrentOptimizer optimizer = new BrentOptimizer();
        assertNotNull(optimizer);
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
        assertEquals(100, optimizer.getMaximalIterationCount());
        assertEquals(1E-10, optimizer.getAbsoluteAccuracy(), 0.0);
        assertEquals(1.0e-14, optimizer.getRelativeAccuracy(), 0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.doOptimize();
    }

    @Test
    public void testOptimizeMinimum() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer();

        double min = optimizer.optimize(f, GoalType.MINIMIZE, -5.0, 5.0, 0.0);
        assertEquals(Math.PI / 2, min, 1e-5);
    }

    @Test
    public void testOptimizeMaximum() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return -Math.pow(Math.sin(x), 2);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer();

        double max = optimizer.optimize(f, GoalType.MAXIMIZE, -5.0, 5.0, 0.0);
        assertEquals(-Math.PI / 2, max, 1e-5);
    }

    @Test
    public void testOptimizeMinimumWithInitialGuess() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer();

        double min = optimizer.optimize(f, GoalType.MINIMIZE, -5.0, 5.0, 1.0);
        assertTrue(min > 1.0);
        assertEquals(Math.PI / 2, min, 1e-5);
    }

    @Test
    public void testOptimizeMaximumWithInitialGuess() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return -Math.pow(Math.sin(x), 2);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer();

        double max = optimizer.optimize(f, GoalType.MAXIMIZE, -5.0, 5.0, 1.0);
        assertTrue(max < 1.0);
        assertEquals(-Math.PI / 2, max, 1e-5);
    }

    @Test
    public void testOptimizeMinimumWithBounds() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                if (x < 0) {
                    return x * x;
                } else {
                    return -Math.pow(x, 2);
                }
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer();

        double min = optimizer.optimize(f, GoalType.MINIMIZE, -3.0, 3.0);
        assertEquals(-1.0, min, 1e-5);
    }

    @Test
    public void testOptimizeMaximumWithBounds() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                if (x < 0) {
                    return -x * x;
                } else {
                    return x * x;
                }
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer();

        double max = optimizer.optimize(f, GoalType.MAXIMIZE, -3.0, 3.0);
        assertEquals(1.0, max, 1e-5);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testOptimizeMaxIterationsExceeded() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                // A function that converges very slowly.
                return 1.0 / (x + 1);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaximalIterationCount(1);

        optimizer.optimize(f, GoalType.MINIMIZE, -5.0, 5.0, 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizeNotStrictlyPositiveBounds() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer();

        optimizer.optimize(f, GoalType.MINIMIZE, 0.0, -5.0, 0.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testOptimizeNumberIsTooSmallAccuracy() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer();

        optimizer.setAbsoluteAccuracy(1e-6);
        optimizer.optimize(f, GoalType.MINIMIZE, -5.0, 5.0, 0.0);
    }
}