package org.apache.commons.math.special.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.special.Gamma;

import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        assertEquals(0.0, Gamma.logGamma(0.0), 1e-10);
        assertEquals(0.0, Gamma.logGamma(1.0), 1e-10);
        assertEquals(Math.log(0.5), Gamma.logGamma(0.5), 1e-10);
        assertEquals(Math.log(1.2), Gamma.logGamma(1.2), 1e-10);
        assertEquals(Math.log(Math.PI), Gamma.logGamma(0.5), 1e-10);

        try {
            Gamma.logGamma(-1.0);
            fail();
        } catch (MathException e) {
            // expected
        }

        try {
            Gamma.logGamma(0.0);
            fail();
        } catch (MathException e) {
            // expected
        }
    }

    @Test
    public void testRegularizedGammaP() {
        assertEquals(0.0, Gamma.regularizedGammaP(0.0, 0.0), 1e-10);
        assertEquals(1.0, Gamma.regularizedGammaP(1.0, 0.0), 1e-10);
        assertEquals(0.5, Gamma.regularizedGammaP(0.5, 0.5), 1e-10);
        assertEquals(0.5, Gamma.regularizedGammaP(0.5, 1.0), 1e-10);
        assertEquals(0.5, Gamma.regularizedGammaP(1.0, 0.5), 1e-10);
        assertEquals(0.5, Gamma.regularizedGammaP(1.0, 1.0), 1e-10);

        try {
            Gamma.regularizedGammaP(0.0, -1.0);
            fail();
        } catch (MathException e) {
            // expected
        }

        try {
            Gamma.regularizedGammaP(-1.0, 0.0);
            fail();
        } catch (MathException e) {
            // expected
        }

        try {
            Gamma.regularizedGammaP(0.5, 0.0);
            fail();
        } catch (MathException e) {
            // expected
        }

        try {
            Gamma.regularizedGammaP(1.0, -1.0);
            fail();
        } catch (MathException e) {
            // expected
        }
    }

    @Test
    public void testRegularizedGammaQ() {
        assertEquals(1.0, Gamma.regularizedGammaQ(0.0, 0.0), 1e-10);
        assertEquals(0.0, Gamma.regularizedGammaQ(1.0, 0.0), 1e-10);
        assertEquals(0.5, Gamma.regularizedGammaQ(0.5, 0.5), 1e-10);
        assertEquals(0.5, Gamma.regularizedGammaQ(0.5, 1.0), 1e-10);
        assertEquals(0.5, Gamma.regularizedGammaQ(1.0, 0.5), 1e-10);
        assertEquals(0.5, Gamma.regularizedGammaQ(1.0, 1.0), 1e-10);

        try {
            Gamma.regularizedGammaQ(0.0, -1.0);
            fail();
        } catch (MathException e) {
            // expected
        }

        try {
            Gamma.regularizedGammaQ(-1.0, 0.0);
            fail();
        } catch (MathException e) {
            // expected
        }

        try {
            Gamma.regularizedGammaQ(0.5, 0.0);
            fail();
        } catch (MathException e) {
            // expected
        }

        try {
            Gamma.regularizedGammaQ(1.0, -1.0);
            fail();
        } catch (MathException e) {
            // expected
        }

        try {
            Gamma.regularizedGammaQ(1.5, 1.0);
            fail();
        } catch (MaxIterationsExceededException e) {
            // expected
        }
    }
}