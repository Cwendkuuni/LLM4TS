package org.apache.commons.cli.optimization.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValuesPointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentOptimizer;
import org.junit.Test;

/**
 * Test cases for BrentOptimizer.
 */
public class BrentOptimizerTest {
    /**
     * Test the constructor.
     */
    @Test
    public void testConstructor() {
        final BrentOptimizer opt = new BrentOptimizer();
        assertEquals(Integer.MAX_VALUE, opt.getMaxEvaluations());
        assertEquals(100, opt.getMaximalIterationCount());
        assertEquals(1E-10, opt.getAbsoluteAccuracy(), 0.0);
        assertEquals(1.0e-14, opt.getRelativeAccuracy(), 0.0);
    }

    /**
     * Test optimization with a simple quadratic function.
     */
    @Test
    public void testQuadraticFunction() {
        final double x = 2.0;
        final double a = 1.0;
        final double b = -3.0;
        final double c = 4.0;
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double arg0) {
                return a * arg0 * arg0 + b * arg0 + c;
            }
        };

        final GoalType goalType = GoalType.MINIMIZE;
        final double min = Double.NEGATIVE_INFINITY;
        final double max = Double.POSITIVE_INFINITY;
        final double startValue = x;

        final UnivariateOptimizer optimizer = new BrentOptimizer();
        final PointValuePair result = optimizer.optimize(f, goalType, min, max, startValue);
        final double optimum = result.getPoint();

        assertEquals(x, optimum, 1E-12);
        assertArrayEquals(new double[] { 0.0 }, optimizer.getIterations());
    }

    /**
     * Test optimization with a simple quadratic function and different
     * initial point.
     */
    @Test
    public void testQuadraticFunctionDifferentStart() {
        final double x = 4.0;
        final double a = 1.0;
        final double b = -3.0;
        final double c = 4.0;
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double arg0) {
                return a * arg0 * arg0 + b * arg0 + c;
            }
        };

        final GoalType goalType = GoalType.MINIMIZE;
        final double min = Double.NEGATIVE_INFINITY;
        final double max = Double.POSITIVE_INFINITY;
        final double startValue = x;

        final UnivariateOptimizer optimizer = new BrentOptimizer();
        final PointValuePair result = optimizer.optimize(f, goalType, min, max, startValue);
        final double optimum = result.getPoint();

        assertEquals(x - 2.0, optimum, 1E-12);
        assertArrayEquals(new double[] { 0.0 }, optimizer.getIterations());
    }

    /**
     * Test optimization with a simple quadratic function and different
     * accuracy settings.
     */
    @Test
    public void testQuadraticFunctionDifferentAccuracy() {
        final double x = 2.0;
        final double a = 1.0;
        final double b = -3.0;
        final double c = 4.0;
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double arg0) {
                return a * arg0 * arg0 + b * arg0 + c;
            }
        };

        final GoalType goalType = GoalType.MINIMIZE;
        final double min = Double.NEGATIVE_INFINITY;
        final double max = Double.POSITIVE_INFINITY;
        final double startValue = x;

        final UnivariateOptimizer optimizer1 = new BrentOptimizer();
        optimizer1.setAbsoluteAccuracy(1E-9);
        optimizer1.setRelativeAccuracy(1.0e-8);
        final PointValuePair result1 = optimizer1.optimize(f, goalType, min, max, startValue);
        final double optimum1 = result1.getPoint();

        final UnivariateOptimizer optimizer2 = new BrentOptimizer();
        optimizer2.setAbsoluteAccuracy(1E-12);
        optimizer2.setRelativeAccuracy(1.0e-13);
        final PointValuePair result2 = optimizer2.optimize(f, goalType, min, max, startValue);
        final double optimum2 = result2.getPoint();

        assertEquals(x, optimum1, 1E-9);
        assertEquals(x, optimum2, 1E-12);
        assertTrue(Math.abs(optimum1 - optimum2) < 1E-13);
    }

    /**
     * Test optimization with a non-quadratic function.
     */
    @Test
    public void testNonQuadraticFunction() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double arg0) {
                return Math.sin(arg0);
            }
        };

        final GoalType goalType = GoalType.MINIMIZE;
        final double min = -Math.PI;
        final double max = Math.PI;
        final double startValue = 1.0;

        final UnivariateOptimizer optimizer = new BrentOptimizer();
        final PointValuePair result = optimizer.optimize(f, goalType, min, max, startValue);
        final double optimum = result.getPoint();

        assertEquals(-Math.PI, optimum, 1E-12);
        assertArrayEquals(new double[] { 0.0 }, optimizer.getIterations());
    }

    /**
     * Test optimization with a non-quadratic function and different
     * accuracy settings.
     */
    @Test
    public void testNonQuadraticFunctionDifferentAccuracy() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double arg0) {
                return Math.sin(arg0);
            }
        };

        final GoalType goalType = GoalType.MINIMIZE;
        final double min = -Math.PI;
        final double max = Math.PI;
        final double startValue = 1.0;

        final UnivariateOptimizer optimizer1 = new BrentOptimizer();
        optimizer1.setAbsoluteAccuracy(1E-9);
        optimizer1.setRelativeAccuracy(1.0e-8);
        final PointValuePair result1 = optimizer1.optimize(f, goalType, min, max, startValue);
        final double optimum1 = result1.getPoint();

        final UnivariateOptimizer optimizer2 = new BrentOptimizer();
        optimizer2.setAbsoluteAccuracy(1E-12);
        optimizer2.setRelativeAccuracy(1.0e-13);
        final PointValuePair result2 = optimizer2.optimize(f, goalType, min, max, startValue);
        final double optimum2 = result2.getPoint();

        assertEquals(-Math.PI, optimum1, 1E-9);
        assertEquals(-Math.PI, optimum2, 1E-12);
        assertTrue(Math.abs(optimum1 - optimum2) < 1E-13);
    }

    /**
     * Test optimization with a non-quadratic function and different
     * bounds.
     */
    @Test
    public void testNonQuadraticFunctionDifferentBounds() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double arg0) {
                return Math.sin(arg0);
            }
        };

        final GoalType goalType = GoalType.MINIMIZE;
        final double min = -Math.PI / 2;
        final double max = Math.PI / 2;
        final double startValue = 1.0;

        final UnivariateOptimizer optimizer = new BrentOptimizer();
        final PointValuePair result = optimizer.optimize(f, goalType, min, max, startValue);
        final double optimum = result.getPoint();

        assertEquals(-Math.PI / 2, optimum, 1E-12);
        assertArrayEquals(new double[] { 0.0 }, optimizer.getIterations());
    }

    /**
     * Test optimization with a non-quadratic function and different
     * starting point.
     */
    @Test
    public void testNonQuadraticFunctionDifferentStartPoint() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double arg0) {
                return Math.sin(arg0);
            }
        };

        final GoalType goalType = GoalType.MINIMIZE;
        final double min = -Math.PI;
        final double max = Math.PI;
        final double startValue = 2.0;

        final UnivariateOptimizer optimizer = new BrentOptimizer();
        final PointValuePair result = optimizer.optimize(f, goalType, min, max, startValue);
        final double optimum = result.getPoint();

        assertEquals(-Math.PI, optimum, 1E-12);
        assertArrayEquals(new double[] { 0.0 }, optimizer.getIterations());
    }

    /**
     * Test optimization with a non-quadratic function and a maximum number
     * of iterations.
     */
    @Test
    public void testNonQuadraticFunctionMaxIterations() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double arg0) {
                return Math.sin(arg0);
            }
        };

        final GoalType goalType = GoalType.MINIMIZE;
        final double min = -Math.PI;
        final double max = Math.PI;
        final double startValue = 1.0;

        final UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaximalIterationCount(1);
        assertThrows(MaxIterationsExceededException.class, () -> optimizer.optimize(f, goalType, min, max, startValue));
    }

    /**
     * Test optimization with a non-quadratic function and a relative
     * tolerance that is too small.
     */
    @Test
    public void testNonQuadraticFunctionSmallRelativeTolerance() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double arg0) {
                return Math.sin(arg0);
            }
        };

        final GoalType goalType = GoalType.MINIMIZE;
        final double min = -Math.PI;
        final double max = Math.PI;
        final double startValue = 1.0;

        final UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.setRelativeAccuracy(1.0e-18);
        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(f, goalType, min, max, startValue));
    }

    /**
     * Test optimization with a non-quadratic function and an absolute
     * tolerance that is too small.
     */
    @Test
    public void testNonQuadraticFunctionSmallAbsoluteTolerance() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double arg0) {
                return Math.sin(arg0);
            }
        };

        final GoalType goalType = GoalType.MINIMIZE;
        final double min = -Math.PI;
        final double max = Math.PI;
        final double startValue = 1.0;

        final UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.setAbsoluteAccuracy(1.0e-19);
        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(f, goalType, min, max, startValue));
    }
}