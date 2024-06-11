package org.apache.commons.math.special.test;

import junit.framework.TestCase;
import org.apache.commons.math3.special.Gamma;

public class GammaTest extends TestCase {

    public void testLogGamma() {
        assertEquals(0.0, Gamma.logGamma(0.0), 1e-12); // positive integer
        assertEquals(1.0, Gamma.logGamma(1.0), 1e-12); // positive integer
        assertEquals(Math.log(1.0 / Math.SQRT12), Gamma.logGamma(0.5), 1e-12); // positive semidefinite
        assertEquals(Math.log(Math.PI) + Math.log(Math.SQRT(2.0)), Gamma.logGamma(1.0), 1e-12); // positive
        assertEquals(Double.NEGATIVE_INFINITY, Gamma.logGamma(-1.0), 0.0); // negative integer
        assertEquals(Double.NaN, Gamma.logGamma(0.0), 0.0); // zero
        assertEquals(Double.NaN, Gamma.logGamma(1.0 / 0.0), 0.0); // complex

        try {
            Gamma.logGamma(-0.5);
            fail("Expected MathException");
        } catch (MathException e) {
            // expected
        }
    }

    public void testRegularizedGammaP() {
        assertEquals(1.0, Gamma.regularizedGammaP(0.5, 0.5), 1e-12); // positive semidefinite
        assertEquals(1.0 / Math.sqrt(Math.PI), Gamma.regularizedGammaP(0.5, 0.5, 1e-8), 1e-3); // positive semidefinite
        assertEquals(1.0 / Math.PI, Gamma.regularizedGammaP(1.0, 1.0), 1e-12); // positive
        assertEquals(0.0, Gamma.regularizedGammaP(1.0, 1.0, 1e-12), 1e-12); // positive
        assertEquals(1.0 / Math.PI, Gamma.regularizedGammaP(1.0, 1.0, 1e-3), 1e-3); // positive
        assertEquals(Math.exp(-Math.PI / 2.0), Gamma.regularizedGammaP(0.5, 1.0), 1e-12); // positive
        assertEquals(Math.exp(-1.0), Gamma.regularizedGammaP(1.0, 0.0), 1e-12); // positive

        try {
            Gamma.regularizedGammaP(-1.0, 1.0);
            fail("Expected MathException");
        } catch (MathException e) {
            // expected
        }

        try {
            Gamma.regularizedGammaP(1.0, -1.0);
            fail("Expected MathException");
        } catch (MathException e) {
            // expected
        }
    }

    public void testRegularizedGammaQ() {
        assertEquals(1.0, Gamma.regularizedGammaQ(0.5, 0.5), 1e-12); // positive semidefinite
        assertEquals(1.0 - 1.0 / (Math.sqrt(Math.PI) * 2.0), Gamma.regularizedGammaQ(0.5, 0.5, 1e-8), 1e-3); // positive semidefinite
        assertEquals(1.0, Gamma.regularizedGammaQ(1.0, 1.0), 1e-12); // positive
        assertEquals(0.0, Gamma.regularizedGammaQ(1.0, 1.0, 1e-12), 1e-12); // positive
        assertEquals(1.0 - 1.0 / (Math.PI * 2.0), Gamma.regularizedGammaQ(1.0, 1.0, 1e-3), 1e-3); // positive
        assertEquals(1.0 - Math.exp(-Math.PI / 2.0), Gamma.regularizedGammaQ(0.5, 1.0), 1e-12); // positive
        assertEquals(1.0, Gamma.regularizedGammaQ(1.0, 0.0), 1e-12); // positive

        try {
            Gamma.regularizedGammaQ(-1.0, 1.0);
            fail("Expected MathException");
        } catch (MathException e) {
            // expected
        }

        try {
            Gamma.regularizedGammaQ(1.0, -1.0);
            fail("Expected MathException");
        } catch (MathException e) {
            // expected
        }
    }
}