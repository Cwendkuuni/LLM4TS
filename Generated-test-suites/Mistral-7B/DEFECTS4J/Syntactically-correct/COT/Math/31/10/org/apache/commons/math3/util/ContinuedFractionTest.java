package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;

/**
 * Test class for ContinuedFraction.
 */
public class ContinuedFractionTest {

    /**
     * Test method for {@link ContinuedFraction#getA(int, double)}.
     * @throws Exception
     */
    @org.junit.Test
    public void testGetA() throws Exception {
        ContinuedFraction testInstance = new ContinuedFraction() {
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
            double result = testInstance.getA(i, 0.0);
            if (i % 2 == 0) {
                assertEquals(1.0, result, 1e-10);
            } else {
                assertEquals(0.0, result, 1e-10);
            }
        }
    }

    /**
     * Test method for {@link ContinuedFraction#getB(int, double)}.
     * @throws Exception
     */
    @org.junit.Test
    public void testGetB() throws Exception {
        ContinuedFraction testInstance = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return (n % 2 == 0) ? 1.0 : x;
            }

            @Override
            protected double getB(int n, double x) {
                return n % 2 == 0 ? 1.0 : x;
            }
        };
        for (int i = 0; i < 10; i++) {
            double result = testInstance.getB(i, 0.0);
            if (i % 2 == 0) {
                assertEquals(1.0, result, 1e-10);
            } else {
                assertEquals(0.0, result, 1e-10);
            }
        }
    }

    /**
     * Test method for {@link ContinuedFraction#evaluate(double)}.
     * @throws Exception
     */
    @org.junit.Test
    public void testEvaluate() throws Exception {
        ContinuedFraction testInstance = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return (n % 2 == 0) ? 1.0 : x;
            }

            @Override
            protected double getB(int n, double x) {
                return 1.0;
            }
        };
        double result = testInstance.evaluate(0.0);
        assertEquals(1.0, result, 1e-10);
    }

    /**
     * Test method for {@link ContinuedFraction#evaluate(double, double)}.
     * @throws Exception
     */
    @org.junit.Test
    public void testEvaluateWithEpsilon() throws Exception {
        ContinuedFraction testInstance = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return (n % 2 == 0) ? 1.0 : x;
            }

            @Override
            protected double getB(int n, double x) {
                return 1.0;
            }
        };
        double result = testInstance.evaluate(0.0, 1e-15);
        assertEquals(1.0, result, 1e-15);
    }

    /**
     * Test method for {@link ContinuedFraction#evaluate(double, int)}.
     * @throws Exception
     */
    @org.junit.Test
    public void testEvaluateWithMaxIterations() throws Exception {
        ContinuedFraction testInstance = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return (n % 2 == 0) ? 1.0 : x;
            }

            @Override
            protected double getB(int n, double x) {
                return 1.0;
            }
        };
        double result = testInstance.evaluate(0.0, 10);
        assertEquals(1.0, result, 1e-10);
    }

    /**
     * Test method for {@link ContinuedFraction#evaluate(double, double, int)}.
     * @throws Exception
     */
    @org.junit.Test
    public void testEvaluateWithEpsilonAndMaxIterations() throws Exception {
        ContinuedFraction testInstance = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return (n % 2 == 0) ? 1.0 : x;
            }

            @Override
            protected double getB(int n, double x) {
                return 1.0;
            }
        };
        double result = testInstance.evaluate(0.0, 1e-15, 10);
        assertEquals(1.0, result, 1e-15);
    }

    /**
     * Test method for {@link ContinuedFraction#evaluate(double, double, int)}.
     * Convergence test with small epsilon value.
     * @throws Exception
     */
    @org.junit.Test
    public void testEvaluateWithSmallEpsilon() throws Exception {
        ContinuedFraction testInstance = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return Math.pow(2, n);
            }

            @Override
            protected double getB(int n, double x) {
                return (n % 2 == 0) ? 1.0 : (1.0 / Math.pow(2, n));
            }
        };
        try {
            double result = testInstance.evaluate(0.5, 5e-15, 1000);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // expected
        }
    }

    /**
     * Test method for {@link ContinuedFraction#evaluate(double, double, int)}.
     * Max count exceeded test.
     * @throws Exception
     */
    @org.junit.Test
    public void testEvaluateWithMaxIterationsExceeded() throws Exception {
        ContinuedFraction testInstance = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return Math.pow(2, n);
            }

            @Override
            protected double getB(int n, double x) {
                return 1.0;
            }
        };
        try {
            testInstance.evaluate(0.5, 1e-10, 5);
            fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // expected
        }
    }

}