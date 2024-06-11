package org.apache.commons.math.special;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNaN;

import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.junit.Test;

/**
 * JUnit test file for Gamma class.
 */
public class GammaTest {

    /**
     * Test for logGamma method.
     * @throws MathException If the method fails.
     */
    @Test
    public void testLogGamma() throws MathException {
        assertEquals(0.4051255111764706, Gamma.logGamma(1.0), 1e-8);
        assertEquals(-Infinity, Gamma.logGamma(0.0), 1e-8);
        assertEquals(-Infinity, Gamma.logGamma(-1.0), 1e-8);
        assertEquals(Double.NaN, Gamma.logGamma(1.0, Double.NaN), 1e-8);
        assertEquals(Double.NaN, Gamma.logGamma(-1.0, Double.NaN), 1e-8);
        assertEquals(Double.NaN, Gamma.logGamma(0.5), 1e-8);
    }

    /**
     * Test for regularizedGammaP method.
     * @throws MathException If the method fails.
     */
    @Test
    public void testRegularizedGammaP() throws MathException {
        assertEquals(0.5, Gamma.regularizedGammaP(0.5, 1.0), 1e-8);
        assertEquals(0.5, Gamma.regularizedGammaP(0.5, 1.5), 1e-8);
        assertEquals(0.5, Gamma.regularizedGammaP(0.5, 2.0), 1e-8);
        assertEquals(1.0, Gamma.regularizedGammaP(1.5, 1.5), 1e-8);
        assertEquals(1.0, Gamma.regularizedGammaP(2.0, 2.0), 1e-8);
        assertEquals(1.0, Gamma.regularizedGammaP(3.0, 3.0), 1e-8);
        assertEquals(1.1275201372549084, Gamma.regularizedGammaP(3.0, 4.5), 1e-8);
        assertEquals(1.0, Gamma.regularizedGammaP(1.0, 1.0), 1e-8);
        assertEquals(0.0, Gamma.regularizedGammaP(1.0, 0.0), 1e-8);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(-1.0, 2.0), 1e-8);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(1.0, -2.0), 1e-8);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(1.0, Double.NaN), 1e-8);

        try {
            Gamma.regularizedGammaP(0.5, 0.0);
            assert (false); // Should throw an exception
        } catch (MathException e) {
            // Expected MathException
        }

        try {
            Gamma.regularizedGammaP(1.5, 0.0);
            assert (false); // Should throw an exception
        } catch (MathException e) {
            // Expected MathException
        }

        try {
            Gamma.regularizedGammaP(1.0, Double.POSITIVE_INFINITY);
            assert (false); // Should throw an exception
        } catch (MaxIterationsExceededException e) {
            // Expected MaxIterationsExceededException
        }
    }

    /**
     * Test for regularizedGammaQ method.
     * @throws MathException If the method fails.
     */
    @Test
    public void testRegularizedGammaQ() throws MathException {
        assertEquals(0.5, 1.0 - Gamma.regularizedGammaP(0.5, 1.0), 1e-8);
        assertEquals(0.5, 1.0 - Gamma.regularizedGammaP(0.5, 1.5), 1e-8);
        assertEquals(0.5, 1.0 - Gamma.regularizedGammaP(0.5, 2.0), 1e-8);
        assertEquals(0.5, 1.0 - Gamma.regularizedGammaP(1.5, 1.5), 1e-8);
        assertEquals(0.5, 1.0 - Gamma.regularizedGammaP(2.0, 2.0), 1e-8);
        assertEquals(0.5, 1.0 - Gamma.regularizedGammaP(3.0, 3.0), 1e-8);
        assertEquals(0.8724633928773851, 1.0 - Gamma.regularizedGammaP(3.0, 4.5), 1e-8);
        assertEquals(1.0, 1.0 - Gamma.regularizedGammaP(1.0, 1.0), 1e-8);
        assertEquals(1.0, 1.0 - Gamma.regularizedGammaP(1.0, 0.0), 1e-8);
        assertEquals(1.0, 1.0 - Gamma.regularizedGammaP(1.0, Double.NaN), 1e-8);
        assertEquals(1.0, 1.0 - Gamma.regularizedGammaP(-1.0, 2.0), 1e-8);
        assertEquals(1.0, 1.0 - Gamma.regularizedGammaP(1.0, -2.0), 1e-8);

        try {
            Gamma.regularizedGammaQ(0.5, 0.0);
            assert (false); // Should throw an exception
        } catch (MathException e) {
            // Expected MathException
        }

        try {
            Gamma.regularizedGammaQ(1.5, 0.0);
            assert (false); // Should throw an exception
        } catch (MathException e) {
            // Expected MathException
        }

        try {
            Gamma.regularizedGammaQ(1.0, Double.POSITIVE_INFINITY);
            assert (false); // Should throw an exception
        } catch (MaxIterationsExceededException e) {
            // Expected MaxIterationsExceededException
        }
    }
}