package org.apache.commons.math.special.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.special.Gamma;

import org.junit.Test;

/**
 * This class contains unit tests for the {@link Gamma} class.
 */
public class GammaTest {

    /**
     * Test for logGamma method.
     */
    @Test
    public void testLogGamma() {
        // Test with valid inputs
        double x = 1.0;
        double result = Gamma.logGamma(x);
        assertNotNull(result);
        double expected = 0.0; // log of gamma function for x = 1 is 0
        assertEquals(expected, result, 1e-12);

        x = 2.0;
        result = Gamma.logGamma(x);
        expected = Math.log(Math.PI) + Math.log(x) - 0.5 * Math.log(x) - 0.5;
        assertEquals(expected, result, 1e-12);

        x = 0.5;
        result = Gamma.logGamma(x);
        expected = Math.log(Math.PI) + Math.log(x) - 0.5 * Math.log(x) - 0.5 + 0.57721566490153286061;
        assertEquals(expected, result, 1e-12);

        // Test with invalid inputs
        x = Double.NEGATIVE_INFINITY;
        assertNull(Gamma.logGamma(x));

        x = Double.POSITIVE_INFINITY;
        result = Gamma.logGamma(x);
        assertFalse(Double.isNaN(result));
        assertEquals(Double.POSITIVE_INFINITY, result, 0.0);

        x = -0.5;
        assertNull(Gamma.logGamma(x));

        x = 0.0;
        assertNull(Gamma.logGamma(x));

        x = 1.5; // non-integer positive number
        assertNotNull(Gamma.logGamma(x));
    }

    /**
     * Test for regularizedGammaP method with valid inputs.
     */
    @Test
    public void testRegularizedGammaPValidInputs() {
        // Test with valid inputs
        double a = 1.0;
        double x = 1.0;
        double result = Gamma.regularizedGammaP(a, x);
        double expected = 1.0;
        assertEquals(expected, result, 1e-12);

        a = 3.0;
        x = 2.0;
        result = Gamma.regularizedGammaP(a, x);
        expected = Math.PI * Math.pow(2.0, a - 1.0) / (Math.Gamma(a) * Math.pow(2.0, a));
        assertEquals(expected, result, 1e-12);

        a = 0.5;
        x = 1.0;
        result = Gamma.regularizedGammaP(a, x);
        expected = Math.sqrt(Math.PI) / (Math.pow(2.0, a) * Math.Gamma(a + 0.5));
        assertEquals(expected, result, 1e-12);
    }

    /**
     * Test for regularizedGammaP method with invalid inputs.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testRegularizedGammaPInvalidInputs() {
        // Test with invalid inputs
        double a = Double.NEGATIVE_INFINITY;
        Gamma.regularizedGammaP(a, 1.0);

        a = Double.POSITIVE_INFINITY;
        Gamma.regularizedGammaP(a, 1.0);

        a = -0.5;
        Gamma.regularizedGammaP(a, 1.0);

        a = 0.0;
        Gamma.regularizedGammaP(a, 1.0);

        double x = Double.NEGATIVE_INFINITY;
        Gamma.regularizedGammaP(1.0, x);

        x = Double.POSITIVE_INFINITY;
        Gamma.regularizedGammaP(1.0, x);

        x = -1.0;
        Gamma.regularizedGammaP(1.0, x);
    }

    /**
     * Test for regularizedGammaQ method with valid inputs.
     */
    @Test
    public void testRegularizedGammaQValidInputs() {
        // Test with valid inputs
        double a = 1.0;
        double x = 1.0;
        double result = Gamma.regularizedGammaQ(a, x);
        double expected = 0.0;
        assertEquals(expected, result, 1e-12);

        a = 3.0;
        x = 2.0;
        result = Gamma.regularizedGammaQ(a, x);
        expected = 1.0 - Math.PI * Math.pow(2.0, a - 1.0) / (Math.Gamma(a) * Math.pow(2.0, a));
        assertEquals(expected, result, 1e-12);

        a = 0.5;
        x = 1.0;
        result = Gamma.regularizedGammaQ(a, x);
        expected = 1.0 - Math.sqrt(Math.PI) / (Math.pow(2.0, a) * Math.Gamma(a + 0.5));
        assertEquals(expected, result, 1e-12);
    }

    /**
     * Test for regularizedGammaQ method with invalid inputs.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testRegularizedGammaQInvalidInputs() {
        // Test with invalid inputs
        double a = Double.NEGATIVE_INFINITY;
        Gamma.regularizedGammaQ(a, 1.0);

        a = Double.POSITIVE_INFINITY;
        Gamma.regularizedGammaQ(a, 1.0);

        a = -0.5;
        Gamma.regularizedGammaQ(a, 1.0);

        a = 0.0;
        Gamma.regularizedGammaQ(a, 1.0);

        double x = Double.NEGATIVE_INFINITY;
        Gamma.regularizedGammaQ(1.0, x);

        x = Double.POSITIVE_INFINITY;
        Gamma.regularizedGammaQ(1.0, x);

        x = -1.0;
        Gamma.regularizedGammaQ(1.0, x);
    }
}