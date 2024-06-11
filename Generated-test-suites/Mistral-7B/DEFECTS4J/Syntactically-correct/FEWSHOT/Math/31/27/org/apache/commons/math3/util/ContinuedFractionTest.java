package org.apache.commons.math3.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.special.FresnelIntegrals;
import org.junit.Test;

/**
 * Test cases for ContinuedFraction class.
 */
public class ContinuedFractionTest {

    /**
     * Test for {@link ContinuedFraction#evaluate(double)}.
     */
    @Test
    public void testEvaluate() {
        final ContinuedFraction sqrtTest = new SqrtContinuedFraction();
        final ContinuedFraction sqrt2Test = new Sqrt2ContinuedFraction();
        final ContinuedFraction eTest = new EContinuedFraction();

        // Test with default parameters
        assertEquals(1.718281828459045, sqrtTest.evaluate(0.0), 1e-15);
        assertEquals(Math.E, eTest.evaluate(0.0), 1e-15);
        assertEquals(2.414213771102583, sqrt2Test.evaluate(0.0), 1e-15);

        // Test with custom epsilon and maxIterations
        assertEquals(1.718281828459045, sqrtTest.evaluate(0.0, 1e-20, 100), 1e-20);
        assertEquals(Math.E, eTest.evaluate(0.0, 1e-30, 200), 1e-30);
        assertEquals(2.414213771102583, sqrt2Test.evaluate(0.0, 1e-25, 50), 1e-25);

        // Test with maxIterations only
        assertEquals(1.718281828459045, sqrtTest.evaluate(0.0, 1e-15), Integer.MAX_VALUE);
        assertEquals(Math.E, eTest.evaluate(0.0, 1e-15), Integer.MAX_VALUE);
        assertEquals(2.414213771102583, sqrt2Test.evaluate(0.0, 1e-15), Integer.MAX_VALUE);

        // Test for convergence exception
        try {
            new SqrtNegativeContinuedFraction().evaluate(-1.0);
            assertFalse("Expected ConvergenceException", false);
        } catch (ConvergenceException e) {
            // Expected
        }

        try {
            new ENegativeContinuedFraction().evaluate(-1.0);
            assertFalse("Expected ConvergenceException", false);
        } catch (ConvergenceException e) {
            // Expected
        }

        try {
            new Sqrt2NegativeContinuedFraction().evaluate(0.0);
            assertFalse("Expected ConvergenceException", false);
        } catch (ConvergenceException e) {
            // Expected
        }

        try {
            new SqrtNegativeContinuedFraction().evaluate(1.0);
            assertFalse("Expected MaxCountExceededException", false);
        } catch (MaxCountExceededException e) {
            // Expected
        }

        try {
            new Sqrt2NegativeContinuedFraction().evaluate(1.0, 1e-15, 20);
            assertFalse("Expected MaxCountExceededException", false);
        } catch (MaxCountExceededException e) {
            // Expected
        }
    }

    /**
     * Test for {@link ContinuedFraction#getA(int, double)} and {@link ContinuedFraction#getB(int, double)}.
     */
    @Test
    public void testAAndB() {
        final SqrtContinuedFraction sqrtTest = new SqrtContinuedFraction();
        final Sqrt2ContinuedFraction sqrt2Test = new Sqrt2ContinuedFraction();

        // Test for getA
        assertEquals(2.0, sqrtTest.getA(1, 1.0), 1e-15);
        assertEquals(2.0, sqrtTest.getA(1, 4.0), 1e-15);
        assertEquals(2.0, sqrtTest.getA(1, 9.0), 1e-15);

        // Test for getB
        assertEquals(1.0, sqrtTest.getB(1, 1.0), 1e-15);
        assertEquals(1.0, sqrtTest.getB(1, 4.0), 1e-15);
        assertEquals(1.0, sqrtTest.getB(1, 9.0), 1e-15);

        // Test for getA with negative n
        assertNull(sqrtTest.getA(-1, 1.0));
        assertNull(sqrtTest.getA(-1, 4.0));
        assertNull(sqrtTest.getA(-1, 9.0));

        // Test for getB with negative n
        assertNull(sqrtTest.getB(-1, 1.0));
        assertNull(sqrtTest.getB(-1, 4.0));
        assertNull(sqrtTest.getB(-1, 9.0));

        // Test for sqrt2 continued fraction
        assertEquals(1.0, sqrt2Test.getA(1, 0.0), 1e-15);
        assertEquals(1.0, sqrt2Test.getA(2, 0.0), 1e-15);
        assertEquals(2.0, sqrt2Test.getA(3, 0.0), 1e-15);
        assertEquals(2.0, sqrt2Test.getA(4, 0.0), 1e-15);
        assertEquals(1.0, sqrt2Test.getA(5, 0.0), 1e-15);
        assertEquals(1.0, sqrt2Test.getA(6, 0.0), 1e-15);
        assertEquals(2.0, sqrt2Test.getA(7, 0.0), 1e-15);
        assertEquals(2.0, sqrt2Test.getA(8, 0.0), 1e-15);
        assertEquals(1.0, sqrt2Test.getA(9, 0.0), 1e-15);

        assertEquals(1.0, sqrt2Test.getB(1, 0.0), 1e-15);
        assertEquals(1.0, sqrt2Test.getB(2, 0.0), 1e-15);
        assertEquals(1.0, sqrt2Test.getB(3, 0.0), 1e-15);
        assertEquals(2.0, sqrt2Test.getB(4, 0.0), 1e-15);
        assertEquals(2.0, sqrt2Test.getB(5, 0.0), 1e-15);
        assertEquals(1.0, sqrt2Test.getB(6, 0.0), 1e-15);
        assertEquals(1.0, sqrt2Test.getB(7, 0.0), 1e-15);
        assertEquals(1.0, sqrt2Test.getB(8, 0.0), 1e-15);
        assertEquals(2.0, sqrt2Test.getB(9, 0.0), 1e-15);

        // Test for Fresnel integrals continued fraction
        final FresnelIntegrals fresnel = new FresnelIntegrals();
        final FresnelContinuedFraction fresnelCf = new FresnelContinuedFraction();

        // Test for getA
        assertArrayEquals(new double[] { 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0 },
                fresnelCf.getA(new double[] { 0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0, 1.1, 1.2, 1.3, 1.4, 1.5 },
                        11, 1.0), 1e-15);

        // Test for getB
        assertArrayEquals(new double[] { 0.0, 0.1, 0.1, 0.2, 0.2, 0.3, 0.4, 0.4, 0.5, 0.6, 0.6, 0.7, 0.8, 0.8, 0.9, 0.9 },
                fresnelCf.getB(new double[] { 0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0, 1.1, 1.2, 1.3, 1.4, 1.5 },
                        11, 1.0), 1e-15);
    }
}

/**
 * Subclass of ContinuedFraction for square root.
 */
class SqrtContinuedFraction extends ContinuedFraction {
    @Override
    protected double getA(int n, double x) {
        return x / (x + 2 * n * n);
    }

    @Override
    protected double getB(int n, double x) {
        return 2.0 * n / (x + 2 * n * n);
    }
}

/**
 * Subclass of ContinuedFraction for square root of 2.
 */
class Sqrt2ContinuedFraction extends ContinuedFraction {
    @Override
    protected double getA(int n, double x) {
        if (n == 0) {
            return 2.0;
        } else if (n % 2 == 0) {
            return 1.0;
        } else {
            return -x / (x + (2.0 * n + 1) * (2.0 * n + 1));
        }
    }

    @Override
    protected double getB(int n, double x) {
        if (n == 0) {
            return 1.0;
        } else if (n % 2 == 0) {
            return 2.0;
        } else {
            return (2.0 * n + 1) / (x + (2.0 * n + 1) * (2.0 * n + 1));
        }
    }
}

/**
 * Subclass of ContinuedFraction for square root of negative number.
 */
class SqrtNegativeContinuedFraction extends ContinuedFraction {
    @Override
    protected double getA(int n, double x) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected double getB(int n, double x) {
        throw new UnsupportedOperationException();
    }
}

/**
 * Subclass of ContinuedFraction for square root of 2 with negative arguments.
 */
class Sqrt2NegativeContinuedFraction extends ContinuedFraction {
    @Override
    protected double getA(int n, double x) {
        if (x < 0) {
            x = -x;
        }
        return -x / (x + (2.0 * n + 1) * (2.0 * n + 1));
    }

    @Override
    protected double getB(int n, double x) {
        if (x < 0) {
            x = -x;
        }
        return (2.0 * n + 1) / (x + (2.0 * n + 1) * (2.0 * n + 1));
    }
}

/**
 * Subclass of ContinuedFraction for e.
 */
class EContinuedFraction extends ContinuedFraction {
    @Override
    protected double getA(int n, double x) {
        return (n < 2) ? (1.0 / e(x)) : (e(x) - e(x - 1.0) / (n - 1));
    }

    @Override
    protected double getB(int n, double x) {
        return (n < 2) ? 0.0 : 1.0 / (n - 1);
    }

    private double e(double x) {
        return Math.exp(x);
    }
}

/**
 * Subclass of ContinuedFraction for e with negative arguments.
 */
class ENegativeContinuedFraction extends ContinuedFraction {
    @Override
    protected double getA(int n, double x) {
        x = -x;
        return (n < 2) ? (1.0 / e(x)) : (e(x) - e(x - 1.0) / (n - 1));
    }

    @Override
    protected double getB(int n, double x) {
        x = -x;
        return (n < 2) ? 0.0 : 1.0 / (n - 1);
    }

    private double e(double x) {
        return Math.exp(x);
    }
}

/**
 * Subclass of ContinuedFraction for Fresnel integrals.
 */
class FresnelContinuedFraction extends ContinuedFraction {
    private double[] a;
    private double[] b;

    public FresnelContinuedFraction(double[] a, double[] b) {
        this.a = a;
        this.b = b;
    }

    @Override
    protected double getA(int n, double x) {
        return a[n];
    }

    @Override
    protected double getB(int n, double x) {
        return b[n];
    }
}