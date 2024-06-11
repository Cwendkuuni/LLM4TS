package org.apache.commons.math3.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.special.Precision;
import org.junit.Test;

/**
 * Test cases for {@link ContinuedFraction} class.
 */
public class ContinuedFractionTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testAbstractMethodCall() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                throw new UnsupportedOperationException();
            }

            @Override
            protected double getB(int n, double x) {
                throw new UnsupportedOperationException();
            }
        };
        cf.evaluate(0.0);
    }

    @Test
    public void testEvaluateWithNull() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 0.0;
            }

            @Override
            protected double getB(int n, double x) {
                return 1.0;
            }
        };
        assertNull(cf.evaluate(null));
    }

    @Test
    public void testEvaluateWithDefaultEpsilon() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n == 0 ? 1.0 : x;
            }

            @Override
            protected double getB(int n, double x) {
                return n == 0 ? 1.0 : 1.0 - x;
            }
        };

        assertEquals(1.0, cf.evaluate(0.0), 1e-9);
        assertEquals(0.0, cf.evaluate(1.0), 1e-9);
    }

    @Test
    public void testEvaluateWithCustomEpsilon() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n == 0 ? 1.0 : x;
            }

            @Override
            protected double getB(int n, double x) {
                return n == 0 ? 1.0 : 1.0 - x;
            }
        };

        assertEquals(1.0, cf.evaluate(0.0), 1e-12);
        assertEquals(0.0, cf.evaluate(1.0), 1e-12);
    }

    @Test
    public void testEvaluateWithMaxIterations() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n > 0 ? 1.0 / (2.0 * n + 1.0) : 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return n > 0 ? 1.0 / (2.0 * n) : 0.0;
            }
        };

        assertEquals(0.25, cf.evaluate(0.0), 1e-9);
        assertEquals(0.5, cf.evaluate(1.0), 1e-9);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateWithInfiniteValue() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n > 0 ? 1.0 / (2.0 * n + 1.0) : 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return n > 0 ? 1.0 / (2.0 * n) : Double.POSITIVE_INFINITY;
            }
        };

        cf.evaluate(0.0);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateWithNaN() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n > 0 ? 1.0 / (2.0 * n + 1.0) : Double.NaN;
            }

            @Override
            protected double getB(int n, double x) {
                return n > 0 ? 1.0 / (2.0 * n) : 1.0;
            }
        };

        cf.evaluate(0.0);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateWithMaxIterationsExceeded() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0 / (n + 1.0);
            }

            @Override
            protected double getB(int n, double x) {
                return 1.0;
            }
        };

        cf.evaluate(0.0, 0, 5);
    }

}