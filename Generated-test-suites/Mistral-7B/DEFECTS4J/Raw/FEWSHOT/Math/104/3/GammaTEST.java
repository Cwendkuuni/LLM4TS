package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.math3.special.Gamma;

import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        // Test with valid inputs
        assertEquals(-0.01293389242712447, Gamma.logGamma(0.5), 1e-8);
        assertEquals(0.0, Gamma.logGamma(1.0), 1e-8);
        assertEquals(1.421010583138233, Gamma.logGamma(2.0), 1e-8);
        assertEquals(2.078794268324374, Gamma.logGamma(3.0), 1e-8);
        assertEquals(3.625182253305772, Gamma.logGamma(4.0), 1e-8);

        // Test with invalid inputs
        assertNaN(Gamma.logGamma(-1.0));
        assertNaN(Gamma.logGamma(0.0));
        assertNaN(Gamma.logGamma(1.414213562373095E-8));
    }

    @Test(expected = org.apache.commons.math3.MathException.class)
    public void testLogGammaMaxIterations() {
        // Test with max iterations exceeded
        Gamma.logGamma(0.5, 10000);
    }

    @Test(expected = org.apache.commons.math3.MaxIterationsExceededException.class)
    public void testLogGammaMaxIterationsThrows() {
        // Test with max iterations exceeded and throw an exception
        try {
            Gamma.logGamma(0.5, 10000, 1);
        } catch (MaxIterationsExceededException e) {
            // expected
            return;
        }
        fail();
    }

    @Test
    public void testRegularizedGammaP() {
        // Test with valid inputs
        assertEquals(0.665677886278944, Gamma.regularizedGammaP(0.5, 0.5), 1e-8);
        assertEquals(0.5, Gamma.regularizedGammaP(0.5, 1.0), 1e-8);
        assertEquals(0.886226925452758, Gamma.regularizedGammaP(1.0, 0.5), 1e-8);
        assertEquals(0.632120558825143, Gamma.regularizedGammaP(2.0, 0.5), 1e-8);
        assertEquals(0.636945542277952, Gamma.regularizedGammaP(3.0, 0.5), 1e-8);

        // Test with invalid inputs
        assertNaN(Gamma.regularizedGammaP(-1.0, 0.5));
        assertNaN(Gamma.regularizedGammaP(0.5, 1.414213562373095E-8));
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaPConvergenceFailure() {
        // Test with convergence failure
        Gamma.regularizedGammaP(1.0, 1.5, 1e-8, 1000);
    }

    @Test
    public void testRegularizedGammaQ() {
        // Test with valid inputs
        assertEquals(0.334326592131056, Gamma.regularizedGammaQ(0.5, 0.5), 1e-8);
        assertEquals(1.0, Gamma.regularizedGammaQ(1.0, 1.0), 1e-8);
        assertEquals(0.113695861778262, Gamma.regularizedGammaQ(1.0, 2.0), 1e-8);
        assertEquals(0.113695861778262, Gamma.regularizedGammaQ(2.0, 1.0), 1e-8);
        assertEquals(0.113695861778262, Gamma.regularizedGammaQ(3.0, 2.0), 1e-8);

        // Test with invalid inputs
        assertNaN(Gamma.regularizedGammaQ(-1.0, 0.5));
        assertNaN(Gamma.regularizedGammaQ(0.5, 1.414213562373095E-8));
    }
}