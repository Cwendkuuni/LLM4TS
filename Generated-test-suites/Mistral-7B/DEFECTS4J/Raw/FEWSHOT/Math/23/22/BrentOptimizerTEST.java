package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.simple.AbstractUnivariateFunction;
import org.apache.commons.math3.optimization.simple.ScalarFunction;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-6);
        assertEquals(BrentOptimizer.class, optimizer.getClass());
        assertEquals(1e-5, optimizer.getRelativeThreshold(), 0.0001);
        assertEquals(1e-6, optimizer.getAbsoluteThreshold(), 0.0001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerNegativeAbsoluteThreshold() {
        new BrentOptimizer(1.0, -1.0, null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerTooSmallRelativeThreshold() {
        new BrentOptimizer(0.5 * FastMath.ulp(1d), 1e-6, null);
    }

    @Test
    public void testBrentOptimizerMinimization() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-6, null);

        ScalarFunction f = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(f, 0.0, 2.0, GoalType.MINIMIZE);
        assertEquals(1.0, result.getPoint(), 0.0001);
        assertEquals(1.0, result.getValue(), 0.0001);
    }

    @Test
    public void testBrentOptimizerMaximization() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-6, null);

        ScalarFunction f = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                return 1.0 / (x * x);
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(f, 0.0, 2.0, GoalType.MAXIMIZE);
        assertEquals(1.0 / Math.sqrt(2.0), result.getPoint(), 0.0001);
        assertEquals(1.0 / (2.0 * Math.sqrt(2.0)), result.getValue(), 0.0001);
    }

    @Test
    public void testBrentOptimizerWithConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-6, new MyConvergenceChecker());

        ScalarFunction f = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(f, 0.0, Math.PI, GoalType.MINIMIZE);
        assertEquals(Math.PI / 2.0, result.getPoint(), 0.0001);
        assertEquals(-1.0, result.getValue(), 0.0001);
    }

    @Test
    public void testBrentOptimizerWithCustomInitialGuess() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-6, null);

        ScalarFunction f = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(f, 2.0, 2.0, GoalType.MINIMIZE);
        assertEquals(1.0, result.getPoint(), 0.0001);
        assertEquals(1.0, result.getValue(), 0.0001);
    }

    @Test
    public void testBrentOptimizerWithEmptyInterval() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-6, null);

        ScalarFunction f = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };

        try {
            optimizer.optimize(f, Double.NaN, Double.NaN, GoalType.MINIMIZE);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testBrentOptimizerWithFunctionThrowsException() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-6, null);

        ScalarFunction f = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                throw new RuntimeException("Function failed.");
            }
        };

        try {
            optimizer.optimize(f, 0.0, 2.0, GoalType.MINIMIZE);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testBrentOptimizerWithPointsOrdered() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-6, null);

        ScalarFunction f = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2 * x * x;
            }
        };

        double[] expectedPoints = {1.5, 1.2, 1.0};
        double[] expectedValues = {-0.5874969696969697, -1.2324964351866293, -2.0};

        UnivariatePointValuePair result = optimizer.optimize(f, 0.0, 2.0, GoalType.MINIMIZE);

        UnivariatePointValuePair[] points = optimizer.getIterationHistory();
        double[] calculatedValues = new double[points.length];

        for (int i = 0; i < points.length; i++) {
            calculatedValues[i] = points[i].getValue();
        }

        assertArrayEquals(expectedPoints, new double[] {result.getPoint(), points[0].getPoint(), points[1].getPoint()}, 0.0001);
        assertArrayEquals(expectedValues, calculatedValues, 0.0001);
    }

    @Test
    public void testBrentOptimizerWithPointsNotOrdered() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-6, null);

        ScalarFunction f = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2 * x * x;
            }
        };

        double[] expectedPoints = {1.5, 1.0, 1.2};
        double[] expectedValues = {-0.5874969696969697, -2.0, -1.2324964351866293};

        UnivariatePointValuePair result = optimizer.optimize(f, 0.0, 2.0, GoalType.MINIMIZE);

        UnivariatePointValuePair[] points = optimizer.getIterationHistory();
        double[] calculatedValues = new double[points.length];

        for (int i = 0; i < points.length; i++) {
            calculatedValues[i] = points[i].getValue();
        }

        assertArrayEquals(expectedPoints, new double[] {result.getPoint(), points[1].getPoint(), points[0].getPoint()}, 0.0001);
        assertArrayEquals(expectedValues, calculatedValues, 0.0001);
    }

    @Test
    public void testBrentOptimizerWithPointsOrderedAndConverged() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-6, new MyConvergenceChecker());

        ScalarFunction f = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2 * x * x;
            }
        };

        double[] expectedPoints = {1.5, 1.2, 1.0};
        double[] expectedValues = {-0.5874969696969697, -1.2324964351866293, -2.0};

        UnivariatePointValuePair result = optimizer.optimize(f, 0.0, 2.0, GoalType.MINIMIZE);

        UnivariatePointValuePair[] points = optimizer.getIterationHistory();
        double[] calculatedValues = new double[points.length];

        for (int i = 0; i < points.length; i++) {
            calculatedValues[i] = points[i].getValue();
        }

        assertArrayEquals(expectedPoints, new double[] {result.getPoint(), points[0].getPoint(), points[1].getPoint()}, 0.0001);
        assertArrayEquals(expectedValues, calculatedValues, 0.0001);
        assertTrue(optimizer.isConverged());
    }

    private static class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
        @Override
        public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
            return FastMath.abs(previous.getValue() - current.getValue()) < 1e-7;
        }
    }
}