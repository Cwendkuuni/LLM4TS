package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RoundingMode;
import org.apache.commons.math3.util.UnsupportedOperationException;
import org.junit.Test;

/**
 * Test class for {@link ContinuedFraction}.
 */
public class ContinuedFractionTest {

    /**
     * Test method for {@link ContinuedFraction#getA(int, double)}.
     * @throws Exception
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetA() throws Exception {
        ContinuedFraction c = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                throw new UnsupportedOperationException();
            }
        };
        c.getA(0, 0.0);
    }

    /**
     * Test method for {@link ContinuedFraction#getB(int, double)}.
     * @throws Exception
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetB() throws Exception {
        ContinuedFraction c = new ContinuedFraction() {
            @Override
            protected double getB(int n, double x) {
                throw new UnsupportedOperationException();
            }
        };
        c.getB(0, 0.0);
    }

    /**
     * Test method for {@link ContinuedFraction#evaluate(double)}.
     */
    @Test
    public void testEvaluateDouble() {
        ContinuedFraction c = new MyContinuedFraction();
        double result = c.evaluate(0.5);
        double expected = 1.648051265168627471;
        assertEquals(expected, result, 1e-10);
    }

    /**
     * Test method for {@link ContinuedFraction#evaluate(double, double)}.
     */
    @Test
    public void testEvaluateDoubleDouble() {
        ContinuedFraction c = new MyContinuedFraction();
        double result = c.evaluate(0.5, 1e-12);
        double expected = 1.648051265168627471;
        assertEquals(expected, result, 1e-13);
    }

    /**
     * Test method for {@link ContinuedFraction#evaluate(double, int)}.
     */
    @Test
    public void testEvaluateDoubleInt() {
        ContinuedFraction c = new MyContinuedFraction();
        double result = c.evaluate(0.5, 100);
        double expected = 1.648051265168627471;
        assertEquals(expected, result, 1e-14);
    }

    /**
     * Test method for {@link ContinuedFraction#evaluate(double, double, int)}.
     */
    @Test
    public void testEvaluateDoubleDoubleInt() {
        ContinuedFraction c = new MyContinuedFraction();
        double result = c.evaluate(0.5, 1e-12, 100);
        double expected = 1.648051265168627471;
        assertEquals(expected, result, 1e-15);
    }

    /**
     * Test method for {@link ContinuedFraction#evaluate(double, double, int)} with
     * maxIterations set to 0.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEvaluateDoubleDoubleIntIllegalArg() {
        ContinuedFraction c = new MyContinuedFraction();
        c.evaluate(0.5, 1e-12, 0);
    }

    /**
     * Test method for {@link ContinuedFraction#evaluate(double, double, int)} with
     * epsilon set to negative value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEvaluateDoubleDoubleIntIllegalArgNegativeEpsilon() {
        ContinuedFraction c = new MyContinuedFraction();
        c.evaluate(0.5, -1e-12, 100);
    }

    /**
     * Test method for {@link ContinuedFraction#evaluate(double, double, int)} with
     * maxIterations set to negative value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEvaluateDoubleDoubleIntIllegalArgNegativeMaxIterations() {
        ContinuedFraction c = new MyContinuedFraction();
        c.evaluate(0.5, 1e-12, -100);
    }

    /**
     * Test method for {@link ContinuedFraction#evaluate(double, double, int)} with
     * epsilon and maxIterations set to zero.
     */
    @Test
    public void testEvaluateDoubleDoubleIntZero() {
        ContinuedFraction c = new MyContinuedFraction();
        double result = c.evaluate(0.5, 0.0, 0);
        double expected = 0.0;
        assertEquals(expected, result, 1e-14);
    }

    /**
     * Test method for {@link ContinuedFraction#evaluate(double, double, int)} with
     * epsilon set to very small value.
     */
    @Test
    public void testEvaluateDoubleDoubleIntSmallEpsilon() {
        ContinuedFraction c = new MyContinuedFraction();
        double result = c.evaluate(0.5, 1e-308, 100);
        double expected = 1.648051265168627471;
        assertEquals(expected, result, 1e-308);
    }

    /**
     * Test method for {@link ContinuedFraction#evaluate(double, double, int)} with
     * maxIterations set to very large value.
     */
    @Test
    public void testEvaluateDoubleDoubleIntLargeMaxIterations() {
        ContinuedFraction c = new MyContinuedFraction();
        double result = c.evaluate(0.5, 1e-12, Integer.MAX_VALUE);
        double expected = 1.648051265168627471;
        assertEquals(expected, result, 1e-15);
    }

    /**
     * Test method for {@link ContinuedFraction#evaluate(double, double, int)}
     * with ConvergenceException.
     */
    @Test(expected = ConvergenceException.class)
    public void testEvaluateDoubleDoubleIntConvergenceException() {
        ContinuedFraction c = new MyContinuedFractionWithException();
        c.evaluate(0.5, 1e-12, 100);
    }

    /**
     * Test method for {@link ContinuedFraction#evaluate(double, double, int)}
     * with MaxCountExceededException.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateDoubleDoubleIntMaxCountExceededException() {
        ContinuedFraction c = new MyContinuedFractionWithMaxCountExceededException();
        c.evaluate(0.5, 1e-12, 1);
    }

    private static class MyContinuedFraction extends ContinuedFraction {
        @Override
        protected double getA(int n, double x) {
            if (n % 2 == 0) {
                return Math.sqrt(x);
            } else {
                return Math.cos(x);
            }
        }

        @Override
        protected double getB(int n, double x) {
            if (n % 2 == 0) {
                return Math.sin(x);
            } else {
                return Math.sqrt(x + 1);
            }
        }
    }

    private static class MyContinuedFractionWithException extends ContinuedFraction {
        @Override
        protected double getA(int n, double x) {
            if (n == 1) {
                throw new ConvergenceException(LocalizedFormats.CONTINUED_FRACTION_INFINITY_DIVERGENCE, x);
            } else {
                return Math.sqrt(x);
            }
        }

        @Override
        protected double getB(int n, double x) {
            if (n == 1) {
                throw new MaxCountExceededException(LocalizedFormats.NON_CONVERGENT_CONTINUED_FRACTION, 1, x);
            } else {
                return Math.sin(x);
            }
        }
    }

}