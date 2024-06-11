package org.apache.commons.math.special.GammaTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNaN;

import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.MathException;
import org.junit.Test;

/**
 * Test class for the Gamma class.
 */
public class GammaTest {

    /**
     * Test logGamma method.
     * @throws MathException if the method fails.
     */
    @Test
    public void testLogGamma() throws MathException {
        assertEquals(0.0, Gamma.logGamma(0.0), 1e-14);
        assertEquals(0.0, Gamma.logGamma(1.0), 1e-14);
        assertEquals(1.0, Gamma.logGamma(2.0), 1e-14);
        assertEquals(1.0, Gamma.logGamma(3.0), 1e-14);
        assertEquals(1.460396, Gamma.logGamma(5.5), 1e-14);
        assertEquals(Math.log(Math.PI), Gamma.logGamma(0.5), 1e-14);
        assertEquals(Math.log(Math.PI) + Math.log(2.0), Gamma.logGamma(1.0), 1e-14);
        assertEquals(-Math.log(Math.PI) + Math.log(2.0), Gamma.logGamma(-1.0), 1e-14);
        assertEquals(Math.log(Math.E) + Math.log(Math.PI), Gamma.logGamma(1.0), 1e-14);

        // Test for negative values
        assertNaN(Gamma.logGamma(-1.0), "Negative values should return NaN");
        assertNaN(Gamma.logGamma(-2.0), "Negative values should return NaN");
    }

    /**
     * Test regularizedGammaP method.
     * @throws MathException if the method fails.
     */
    @Test
    public void testRegularizedGammaP() throws MathException {
        assertEquals(0.0, Gamma.regularizedGammaP(0.0, 0.0), 1e-14);
        assertEquals(1.0, Gamma.regularizedGammaP(1.0, 1.0), 1e-14);
        assertEquals(0.666666666666667, Gamma.regularizedGammaP(3.0, 2.0), 1e-14);
        assertEquals(0.888888888888889, Gamma.regularizedGammaP(3.0, 3.0), 1e-14);
        assertEquals(0.624009288786497, Gamma.regularizedGammaP(2.5, 1.5), 1e-14);

        // Test for large values
        assertEquals(1.0, Gamma.regularizedGammaP(100.0, 100.0), 1e-14);

        // Test for negative a
        assertNaN(Gamma.regularizedGammaP(-1.0, 0.0), "Negative a values should return NaN");

        // Test for negative x
        assertNaN(Gamma.regularizedGammaP(1.0, -1.0), "Negative x values should return NaN");

        // Test for large x
        assertEquals(0.0, Gamma.regularizedGammaP(1.0, Double.POSITIVE_INFINITY), 1e-14);
    }

    /**
     * Test regularizedGammaQ method.
     * @throws MathException if the method fails.
     */
    @Test
    public void testRegularizedGammaQ() throws MathException {
        assertEquals(1.0, Gamma.regularizedGammaQ(0.0, 0.0), 1e-14);
        assertEquals(0.0, Gamma.regularizedGammaQ(1.0, 1.0), 1e-14);
        assertEquals(0.3626865, Gamma.regularizedGammaQ(3.0, 2.0), 1e-14);
        assertEquals(0.6373135, Gamma.regularizedGammaQ(3.0, 3.0), 1e-14);
        assertEquals(0.624009288786497, Gamma.regularizedGammaQ(2.5, 1.5), 1e-14);

        // Test for large values
        assertEquals(1.0, Gamma.regularizedGammaQ(100.0, 100.0), 1e-14);

        // Test for negative a
        assertNaN(Gamma.regularizedGammaQ(-1.0, 0.0), "Negative a values should return NaN");

        // Test for negative x
        assertNaN(Gamma.regularizedGammaQ(1.0, -1.0), "Negative x values should return NaN");

        // Test for large x
        assertEquals(1.0, Gamma.regularizedGammaQ(1.0, Double.POSITIVE_INFINITY), 1e-14);
    }

    /**
     * Test max iterations in regularizedGammaP method.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsRegularizedGammaP() throws MathException {
        // Test with default epsilon and max iterations
        Gamma.regularizedGammaP(1.0, 1.0, 1e-14, 0);

        // Test with epsilon smaller than default
        Gamma.regularizedGammaP(1.0, 1.0, 1e-15, Integer.MAX_VALUE);

        // Test with max iterations smaller than default
        Gamma.regularizedGammaP(1.0, 1.0, 1e-14, 1);
    }
}