package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.FunctionEvaluationException;

/**
 * Test class for BrentOptimizer.
 */
public class BrentOptimizerTest {

    /**
     * Test constructor.
     */
    @org.junit.Test
    public void testConstructor() {
        BrentOptimizer optimizer = new BrentOptimizer();
        assertNotNull(optimizer);
    }

    /**
     * Test doOptimize method with exception.
     */
    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testDoOptimizeWithException() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.doOptimize();
    }

    /**
     * Test optimize method with a simple function and minimum goal.
     */
    @org.junit.Test
    public void testOptimizeMinimum() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        double min = 0.0;
        double result = optimizer.optimize(function, GoalType.MINIMIZE, min, min + 1.0, 0.5);
        assertEquals(0.0, result, 1e-8);
    }

    /**
     * Test optimize method with a simple function and maximum goal.
     */
    @org.junit.Test
    public void testOptimizeMaximum() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return -x * x;
            }
        };
        double min = 0.0;
        double max = 1.0;
        double result = optimizer.optimize(function, GoalType.MAXIMIZE, min, max, 0.5);
        assertEquals(1.0, result, 1e-8);
    }

    /**
     * Test optimize method with a simple function and bounds.
     */
    @org.junit.Test
    public void testOptimizeBounds() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        };
        double min = -3.0;
        double max = 3.0;
        double result = optimizer.optimize(function, GoalType.MINIMIZE, min, max);
        assertEquals(-1.273240245971, result, 1e-8);
    }

    /**
     * Test localMin method with a simple function and minimum goal.
     */
    @org.junit.Test
    public void testLocalMinMinimum() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        double isMinim = 1;
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        double lo = 0.0;
        double mid = 1.0;
        double hi = 2.0;
        double eps = 0.01;
        double t = 0.01;
        double result = optimizer.localMin(isMinim, function, null, lo, mid, hi, eps, t);
        assertEquals(1.0, result, 1e-8);
    }

    /**
     * Test localMin method with a simple function and maximum goal.
     */
    @org.junit.Test
    public void testLocalMinMaximum() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        double isMinim = 0;
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return -x * x;
            }
        };
        double lo = 0.0;
        double mid = 1.0;
        double hi = 2.0;
        double eps = 0.01;
        double t = 0.01;
        double result = optimizer.localMin(isMinim, function, null, lo, mid, hi, eps, t);
        assertEquals(2.0, result, 1e-8);
    }

    /**
     * Test localMin method with a function having a minimum
     * outside the interval bounds.
     */
    @org.junit.Test(expected = MaxIterationsExceededException.class)
    public void testLocalMinFailure() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        double isMinim = 1;
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x < 1.0) {
                    return Math.pow(x, 2);
                }
                return Double.POSITIVE_INFINITY;
            }
        };
        double lo = 0.0;
        double mid = 1.0;
        double hi = 2.0;
        double eps = 0.01;
        double t = 0.01;
        optimizer.localMin(isMinim, function, null, lo, mid, hi, eps, t);
    }

    /**
     * Test localMin method with a function having a relative accuracy
     * less than machine epsilon.
     */
    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMinNotPositive() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        double isMinim = 1;
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        double lo = 0.0;
        double mid = 1.0;
        double hi = 2.0;
        double eps = 1e-17;
        double t = 0.01;
        optimizer.localMin(isMinim, function, null, lo, mid, hi, eps, t);
    }

    /**
     * Test optimize method with a function throwing exception.
     */
    @org.junit.Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws Exception {
                throw new FunctionEvaluationException("Test exception");
            }
        };
        double min = 0.0;
        optimizer.optimize(function, null, min, min + 1.0, 0.5);
    }

    /**
     * Test optimize method with a function throwing other exception.
     */
    @org.junit.Test(expected = RuntimeException.class)
    public void testOtherException() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws Exception {
                throw new RuntimeException("Test exception");
            }
        };
        double min = 0.0;
        optimizer.optimize(function, null, min, min + 1.0, 0.5);
    }
}