package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;

/**
 * Test cases for ContinuedFraction class.
 */
public class ContinuedFractionTest {
    /**
     * Test case for sqrt of 2 continued fraction.
     */
    public static class SqrtTwoContinuedFraction extends ContinuedFraction {
        @Override
        protected double getA(int n, double x) {
            if (n == 0) {
                return 2.0;
            }
            if (n % 2 == 0) {
                return -2.0;
            }
            return 1.0;
        }

        @Override
        protected double getB(int n, double x) {
            if (n == 0) {
                return 1.0;
            }
            if (n % 2 == 0) {
                return 2.0;
            }
            return 1.0;
        }
    }

    /**
     * Test case for pi continued fraction.
     */
    public static class PiContinuedFraction extends ContinuedFraction {
        @Override
        protected double getA(int n, double x) {
            if (n == 0) {
                return 0.0;
            }
            if (n % 2 == 0) {
                return 1.0 / (2.0 * n - 1.0);
            }
            return 1.0 / n;
        }

        @Override
        protected double getB(int n, double x) {
            if (n == 0) {
                return 1.0;
            }
            if (n % 2 == 0) {
                return -1.0 / (2.0 * n);
            }
            return 1.0 / n;
        }
    }

    @Test
    public void testSqrtTwoContinuedFraction() {
        ContinuedFraction continuedFraction = new SqrtTwoContinuedFraction();
        assertEquals(1.5, continuedFraction.evaluate(0.0), 0.0001);
        assertEquals(1.4142135623730955, continuedFraction.evaluate(1.0), 0.0001);
        assertEquals(1.414213741054273, continuedFraction.evaluate(2.0), 0.0001);
    }

    @Test
    public void testPiContinuedFraction() {
        ContinuedFraction continuedFraction = new PiContinuedFraction();
        assertEquals(3.141592653589793, continuedFraction.evaluate(0.0), 0.0001);
        assertEquals(3.141592653589793, continuedFraction.evaluate(1.0), 0.0001);
        assertEquals(3.141592653589793, continuedFraction.evaluate(2.0), 0.0001);
    }

    @Test
    public void testEvaluateWithZeroAsArgument() {
        ContinuedFraction continuedFraction = new SqrtTwoContinuedFraction();
        assertEquals(1.0, continuedFraction.evaluate(0.0), 0.0);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateWithDivergentContinuedFraction() {
        ContinuedFraction continuedFraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n + 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return -n;
            }
        };
        continuedFraction.evaluate(0.0);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateWithNonConvergentContinuedFraction() {
        ContinuedFraction continuedFraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return -n;
            }

            @Override
            protected double getB(int n, double x) {
                return n + 1.0;
            }
        };
        continuedFraction.evaluate(0.0);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateWithMaxIterationsReached() {
        ContinuedFraction continuedFraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n + 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return -n;
            }
        };
        continuedFraction.evaluate(0.0, 10, 10);
    }
}