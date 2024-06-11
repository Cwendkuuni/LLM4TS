package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Test;

/**
 * Test class for ContinuedFraction.
 */
public class ContinuedFractionTest {

    /**
     * Test method for {@link ContinuedFraction#getA(int, double)}.
     */
    @Test
    public void testGetA() {
        ContinuedFraction continuedFraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return (n % 2 == 0) ? 1.0 : x;
            }

            @Override
            protected double getB(int n, double x) {
                return 1.0;
            }
        };

        for (int i = 0; i < 10; i++) {
            double result = continuedFraction.getA(i, 1.0);
            if (i % 2 == 0) {
                assertEquals(1.0, result, 1e-9);
            } else {
                assertEquals(1.0, result, 1e-9);
            }
        }
    }

    /**
     * Test method for {@link ContinuedFraction#getB(int, double)}.
     */
    @Test
    public void testGetB() {
        ContinuedFraction continuedFraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return n % 2 == 0 ? 1.0 : 2.0;
            }
        };

        for (int i = 0; i < 10; i++) {
            double result = continuedFraction.getB(i, 1.0);
            if (i % 2 == 0) {
                assertEquals(1.0, result, 1e-9);
            } else {
                assertEquals(2.0, result, 1e-9);
            }
        }
    }

    /**
     * Test method for {@link ContinuedFraction#evaluate(double)}.
     */
    @Test
    public void testEvaluate() {
        ContinuedFraction continuedFraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return (n % 2 == 0) ? 1.0 : 2.0;
            }
        };

        double result = continuedFraction.evaluate(1.0);
        assertEquals(3.0, result, 1e-9);
    }

    /**
     * Test method for {@link ContinuedFraction#evaluate(double, double)}.
     */
    @Test
    public void testEvaluateWithEpsilon() {
        ContinuedFraction continuedFraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return (n % 2 == 0) ? 1.0 : 2.0;
            }
        };

        double result = continuedFraction.evaluate(1.0, 1e-8);
        assertEquals(3.0, result, 1e-8);
    }

    /**
     * Test method for {@link ContinuedFraction#evaluate(double, int)}.
     */
    @Test
    public void testEvaluateWithMaxIterations() {
        ContinuedFraction continuedFraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return (n % 2 == 0) ? 1.0 : 2.0;
            }
        };

        assertThrows(ConvergenceException.class, () -> continuedFraction.evaluate(1.0, 1));
    }

    /**
     * Test method for {@link ContinuedFraction#evaluate(double, double, int)}.
     */
    @Test
    public void testEvaluateWithEpsilonAndMaxIterations() {
        ContinuedFraction continuedFraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return (n % 2 == 0) ? 1.0 : 2.0;
            }
        };

        double result = continuedFraction.evaluate(1.0, 1e-8, 1);
        assertEquals(3.0, result, 1e-8);
    }

    /**
     * Test method for {@link ContinuedFraction#evaluate(double, int, int)}.
     */
    @Test
    public void testEvaluateWithMaxIterationsAndMaxCountExceeded() {
        ContinuedFraction continuedFraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return 1.0;
            }
        };

        assertThrows(MaxCountExceededException.class, () -> continuedFraction.evaluate(1.0, 1, 1));
    }
}