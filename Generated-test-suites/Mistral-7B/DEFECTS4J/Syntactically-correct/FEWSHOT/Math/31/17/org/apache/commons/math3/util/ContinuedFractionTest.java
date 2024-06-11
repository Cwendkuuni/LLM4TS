package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

public class ContinuedFractionTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testAbstractMethods() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                throw new UnsupportedOperationException("getA not implemented");
            }

            @Override
            protected double getB(int n, double x) {
                throw new UnsupportedOperationException("getB not implemented");
            }
        };
        cf.evaluate(1.0);
    }

    @Test
    public void testEvaluateDefaultEpsilon() {
        ContinuedFraction underTest = new SquareRootContinuedFraction();
        assertEquals(1.4142135623730955, underTest.evaluate(2.0), 0.000000001);
    }

    @Test
    public void testEvaluateCustomEpsilon() {
        ContinuedFraction underTest = new SquareRootContinuedFraction();
        assertEquals(1.4142135623730955, underTest.evaluate(2.0, 0.001), 0.00000001);
    }

    @Test
    public void testEvaluateMaxIterations() {
        ContinuedFraction underTest = new SineContinuedFraction();
        assertEquals(0.9466025713133582, underTest.evaluate(Math.PI / 2, 10), 0.00001);
        try {
            underTest.evaluate(Math.PI / 2, 5);
            fail();
        } catch (MaxCountExceededException e) {
            // expected
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateInfinityDivergence() {
        ContinuedFraction underTest = new CotangentContinuedFraction();
        underTest.evaluate(0.0);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateNaN() {
        ContinuedFraction underTest = new CotangentContinuedFraction();
        underTest.evaluate(Double.NaN);
    }

    @Test
    public void testGetA() {
        ContinuedFraction underTest = new SquareRootContinuedFraction();
        assertEquals(0.5, underTest.getA(0, 1.0), 0.001);
        assertEquals(-0.5, underTest.getA(1, 1.0), 0.001);
        assertEquals(2.0, underTest.getA(2, 1.0), 0.001);
    }

    @Test
    public void testGetB() {
        ContinuedFraction underTest = new SquareRootContinuedFraction();
        assertEquals(2.0, underTest.getB(0, 1.0), 0.001);
        assertEquals(1.0, underTest.getB(1, 1.0), 0.001);
        assertEquals(1.0, underTest.getB(2, 1.0), 0.001);
    }

    @Test
    public void testEvaluateWithNull() {
        ContinuedFraction underTest = new SquareRootContinuedFraction();
        assertNull(underTest.evaluate(null));
    }

    @Test
    public void testEvaluateWithZero() {
        ContinuedFraction underTest = new SquareRootContinuedFraction();
        assertEquals(0.0, underTest.evaluate(0.0), 0.001);
    }

    @Test
    public void testEvaluateWithNegative() {
        ContinuedFraction underTest = new SquareRootContinuedFraction();
        assertEquals(-1.0, underTest.evaluate(-1.0), 0.001);
    }

    @Test
    public void testEvaluateWithLargeValue() {
        ContinuedFraction underTest = new SquareRootContinuedFraction();
        double largeValue = 1e300;
        assertEquals(Math.sqrt(largeValue), underTest.evaluate(largeValue), 0.001);
    }

    @Test
    public void testEvaluateWithVerySmallValue() {
        ContinuedFraction underTest = new SquareRootContinuedFraction();
        double verySmallValue = 1e-300;
        assertEquals(Math.sqrt(verySmallValue), underTest.evaluate(verySmallValue), 0.001);
    }

    @Test
    public void testEvaluateWithLargeError() {
        ContinuedFraction underTest = new SquareRootContinuedFraction();
        double largeError = 1e10;
        assertEquals(Math.sqrt(1.0) + largeError, underTest.evaluate(1.0, largeError), 0.001);
    }

    @Test
    public void testEvaluateWithSmallError() {
        ContinuedFraction underTest = new SquareRootContinuedFraction();
        double smallError = 1e-12;
        assertEquals(Math.sqrt(1.0), underTest.evaluate(1.0, smallError), 0.001);
    }

    @Test
    public void testEvaluateWithMaxIterations() {
        ContinuedFraction underTest = new SineContinuedFraction();
        double maxIterations = 5;
        assertEquals(0.9466025713133582, underTest.evaluate(Math.PI / 2, maxIterations), 0.00001);
    }

    @Test
    public void testEvaluateWithDefaultEpsilonAndMaxIterations() {
        ContinuedFraction underTest = new SineContinuedFraction();
        double maxIterations = 10;
        assertEquals(0.9466025713133582, underTest.evaluate(Math.PI / 2, maxIterations), 0.00001);
    }

    @Test
    public void testEvaluateWithSmallEpsilonAndMaxIterations() {
        ContinuedFraction underTest = new SineContinuedFraction();
        double epsilon = 0.00001;
        double maxIterations = 10;
        assertEquals(0.9466025713133582, underTest.evaluate(Math.PI / 2, epsilon, maxIterations), 0.00001);
    }

    @Test
    public void testEvaluateWithLargeEpsilonAndMaxIterations() {
        ContinuedFraction underTest = new SineContinuedFraction();
        double epsilon = 1.0;
        double maxIterations = 10;
        assertEquals(0.9466025713133582 + epsilon * 0.01, underTest.evaluate(Math.PI / 2, epsilon, maxIterations), 0.00001);
    }

    @Test
    public void testEvaluateWithMaxIterationsAndNegativeEpsilon() {
        ContinuedFraction underTest = new SineContinuedFraction();
        double maxIterations = 10;
        double epsilon = -0.001;
        assertEquals(0.9466025713133582, underTest.evaluate(Math.PI / 2, epsilon, maxIterations), 0.00001);
        assertTrue(FastMath.abs(underTest.evaluate(Math.PI / 2, epsilon, maxIterations) - 0.9466025713133582) < epsilon);
    }

    @Test
    public void testEvaluateWithMaxIterationsAndZeroEpsilon() {
        ContinuedFraction underTest = new SineContinuedFraction();
        double maxIterations = 10;
        double epsilon = 0.0;
        assertEquals(0.9466025713133582, underTest.evaluate(Math.PI / 2, epsilon, maxIterations), 0.00001);
    }

    @Test
    public void testEvaluateWithMaxIterationsAndNegativeMaxIterations() {
        ContinuedFraction underTest = new SineContinuedFraction();
        double maxIterations = -10;
        try {
            underTest.evaluate(Math.PI / 2, 0.001, maxIterations);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testEvaluateWithMaxIterationsAndNaN() {
        ContinuedFraction underTest = new CotangentContinuedFraction();
        double maxIterations = 10;
        try {
            underTest.evaluate(Double.NaN, 0.001, maxIterations);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testEvaluateWithMaxIterationsAndInfinity() {
        ContinuedFraction underTest = new CotangentContinuedFraction();
        double maxIterations = 10;
        try {
            underTest.evaluate(Double.POSITIVE_INFINITY, 0.001, maxIterations);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testEvaluateWithMaxIterationsAndNegativeX() {
        ContinuedFraction underTest = new CotangentContinuedFraction();
        double maxIterations = 10;
        try {
            underTest.evaluate(-1.0, 0.001, maxIterations);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    static class SquareRootContinuedFraction extends ContinuedFraction {
        @Override
        protected double getA(int n, double x) {
            return x / (x + 1.0) / 2.0;
        }

        @Override
        protected double getB(int n, double x) {
            return 1.0;
        }
    }

    static class SineContinuedFraction extends ContinuedFraction {
        @Override
        protected double getA(int n, double x) {
            return (n & 1) == 0 ? 1.0 / (2.0 * Math.PI * x) : -Math.cos(x) / (2.0 * Math.PI * x);
        }

        @Override
        protected double getB(int n, double x) {
            return (n & 1) == 0 ? Math.sin(x) / (2.0 * Math.PI * x) : 1.0 / (2.0 * Math.PI * x);
        }
    }

    static class CotangentContinuedFraction extends ContinuedFraction {
        @Override
        protected double getA(int n, double x) {
            return -1.0 / (2.0 * x + ((n + 1) & 1) * 2.0);
        }

        @Override
        protected double getB(int n, double x) {
            return 1.0 / (2.0 * x + ((n + 1) & 1) * 2.0);
        }
    }
}