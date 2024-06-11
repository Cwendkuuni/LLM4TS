package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Test;

/**
 * Test cases for ContinuedFraction class.
 */
public class ContinuedFractionTest {

    /**
     * Test evaluate method with default arguments.
     */
    @Test
    public void testEvaluateDefault() {
        ContinuedFraction continuedFraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return (n % 2 == 0) ? 1.0 : 1.0 / (2.0 * x + n);
            }

            @Override
            protected double getB(int n, double x) {
                return (n % 2 == 1) ? 1.0 : 1.0 / (2.0 * x + n + 1);
            }
        };

        assertEquals(3.0, continuedFraction.evaluate(1.0), 1e-10);
    }

    /**
     * Test evaluate method with specified epsilon.
     */
    @Test
    public void testEvaluateEpsilon() {
        ContinuedFraction continuedFraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return (n % 2 == 0) ? 1.0 : 1.0 / (2.0 * x + n);
            }

            @Override
            protected double getB(int n, double x) {
                return (n % 2 == 1) ? 1.0 : 1.0 / (2.0 * x + n + 1);
            }
        };

        assertEquals(3.0, continuedFraction.evaluate(1.0, 1e-11), 1e-12);
    }

    /**
     * Test evaluate method with specified maxIterations.
     */
    @Test
    public void testEvaluateMaxIterations() {
        ContinuedFraction continuedFraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return (n % 2 == 0) ? 1.0 : 1.0 / (2.0 * x + n);
            }

            @Override
            protected double getB(int n, double x) {
                return (n % 2 == 1) ? 1.0 : 1.0 / (2.0 * x + n + 1);
            }
        };

        assertEquals(3.0, continuedFraction.evaluate(1.0, 1e-10, 10), 1e-11);
    }

    /**
     * Test evaluate method with epsilon smaller than default.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEvaluateEpsilonSmallerThanDefault() {
        ContinuedFraction continuedFraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return (n % 2 == 0) ? 1.0 : 1.0 / (2.0 * x + n);
            }

            @Override
            protected double getB(int n, double x) {
                return (n % 2 == 1) ? 1.0 : 1.0 / (2.0 * x + n + 1);
            }
        };

        continuedFraction.evaluate(1.0, 1e-15);
    }

    /**
     * Test evaluate method with maxIterations equal to zero.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEvaluateMaxIterationsZero() {
        ContinuedFraction continuedFraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return (n % 2 == 0) ? 1.0 : 1.0 / (2.0 * x + n);
            }

            @Override
            protected double getB(int n, double x) {
                return (n % 2 == 1) ? 1.0 : 1.0 / (2.0 * x + n + 1);
            }
        };

        continuedFraction.evaluate(1.0, 1e-10, 0);
    }

    /**
     * Test evaluate method with epsilon larger than default.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEvaluateEpsilonLargerThanDefault() {
        ContinuedFraction continuedFraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return (n % 2 == 0) ? 1.0 : 1.0 / (2.0 * x + n);
            }

            @Override
            protected double getB(int n, double x) {
                return (n % 2 == 1) ? 1.0 : 1.0 / (2.0 * x + n + 1);
            }
        };

        continuedFraction.evaluate(1.0, 1e11);
    }

    /**
     * Test evaluate method with infinite x.
     */
    @Test(expected = ConvergenceException.class)
    public void testEvaluateInfiniteX() {
        ContinuedFraction continuedFraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return (n % 2 == 0) ? 1.0 : 1.0 / (2.0 * x + n);
            }

            @Override
            protected double getB(int n, double x) {
                return (n % 2 == 1) ? 1.0 : 1.0 / (2.0 * x + n + 1);
            }
        };

        continuedFraction.evaluate(Double.POSITIVE_INFINITY);
    }

    /**
     * Test evaluate method with negative x.
     */
    @Test(expected = ConvergenceException.class)
    public void testEvaluateNegINGSX() {
        ContinuedFraction continuedFraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return (n % 2 == 0) ? 1.0 : 1.0 / (2.0 * x + n);
            }

            @Override
            protected double getB(int n, double x) {
                return (n % 2 == 1) ? 1.0 : 1.0 / (2.0 * x + n + 1);
            }
        };

        continuedFraction.evaluate(-1.0);
    }

    /**
     * Test convergence exception.
     */
    @Test
    public void testConvergenceException() {
        ContinuedFraction continuedFraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return Double.POSITIVE_INFINITY;
            }

            @Override
            protected double getB(int n, double x) {
                return Double.POSITIVE_INFINITY;
            }
        };

        assertThrows(ConvergenceException.class, () -> continuedFraction.evaluate(1.0));
    }

    /**
     * Test max count exceeded exception.
     */
    @Test
    public void testMaxCountExceededException() {
        ContinuedFraction continuedFraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return (n % 2 == 0) ? 1.0 : -1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return (n % 2 == 1) ? 1.0 : 1.0;
            }
        };

        assertThrows(MaxCountExceededException.class, () -> continuedFraction.evaluate(1.0, 1e-10, 1));
    }

}