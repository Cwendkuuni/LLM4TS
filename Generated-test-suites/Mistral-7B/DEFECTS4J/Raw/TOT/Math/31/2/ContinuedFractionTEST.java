package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.ContinuedFraction;

/**
 * Test for {@link ContinuedFraction} class.
 */
public class ContinuedFractionTest {

    /**
     * Test evaluate method for simple continued fraction.
     * @throws Exception if an error occurs during testing.
     */
    @org.junit.Test
    public void testEvaluateSimple() throws Exception {
        ContinuedFraction continuedFraction = new SimpleContinuedFraction();
        assertEquals(1.0, continuedFraction.evaluate(1.0), 1e-10);
        assertEquals(2.0, continuedFraction.evaluate(2.0), 1e-10);
        assertEquals(3.0, continuedFraction.evaluate(3.0), 1e-10);
    }

    /**
     * Test evaluate method for sqrt(2) continued fraction.
     * @throws Exception if an error occurs during testing.
     */
    @org.junit.Test
    public void testEvaluateSqrt2() throws Exception {
        ContinuedFraction continuedFraction = new SqrtTwoContinuedFraction();
        assertEquals(1.5, continuedFraction.evaluate(1.0), 1e-10);
        assertEquals(2.0, continuedFraction.evaluate(2.0), 1e-10);
    }

    /**
     * Test evaluate method with epsilon smaller than default.
     * @throws Exception if an error occurs during testing.
     */
    @org.junit.Test
    public void testEvaluateEpsilonSmallerThanDefault() throws Exception {
        ContinuedFraction continuedFraction = new SimpleContinuedFraction();
        assertEquals(1.0, continuedFraction.evaluate(1.0, 1e-15), 1e-15);
    }

    /**
     * Test evaluate method with maxIterations smaller than default.
     * @throws Exception if an error occurs during testing.
     */
    @org.junit.Test
    public void testEvaluateMaxIterationsSmallerThanDefault() throws Exception {
        ContinuedFraction continuedFraction = new SimpleContinuedFraction();
        assertThrows(MaxCountExceededException.class, () -> continuedFraction.evaluate(1.0, 1));
    }

    /**
     * Test evaluate method with infinite values.
     * @throws Exception if an error occurs during testing.
     */
    @org.junit.Test
    public void testEvaluateWithInfinities() throws Exception {
        ContinuedFraction continuedFraction = new SimpleContinuedFraction();
        assertThrows(ConvergenceException.class, () -> continuedFraction.evaluate(Double.POSITIVE_INFINITY, 1e-15));
        assertThrows(ConvergenceException.class, () -> continuedFraction.evaluate(-Double.POSITIVE_INFINITY, 1e-15));
    }

    /**
     * Test evaluate method with NaN values.
     * @throws Exception if an error occurs during testing.
     */
    @org.junit.Test
    public void testEvaluateWithNaN() throws Exception {
        ContinuedFraction continuedFraction = new SimpleContinuedFraction();
        assertThrows(ConvergenceException.class, () -> continuedFraction.evaluate(Double.NaN, 1e-15));
    }
}

class SimpleContinuedFraction extends ContinuedFraction {
    protected double getA(int n, double x) {
        return 1.0;
    }

    protected double getB(int n, double x) {
        return n;
    }
}

class SqrtTwoContinuedFraction extends ContinuedFraction {
    protected double getA(int n, double x) {
        if (n % 2 == 0) {
            return 2.0;
        }
        return 1.0;
    }

    protected double getB(int n, double x) {
        if (n % 2 == 0) {
            return 1.0;
        }
        return 2.0;
    }
}