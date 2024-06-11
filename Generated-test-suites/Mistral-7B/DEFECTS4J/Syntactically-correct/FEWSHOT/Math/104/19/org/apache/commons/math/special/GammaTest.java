package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.exception.MathException;
import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        assertEquals(-Math.log(2.0), Gamma.logGamma(0.0), 1e-9);
        assertEquals(0.0, Gamma.logGamma(1.0), 1e-9);
        assertEquals(Math.log(2.0), Gamma.logGamma(2.0), 1e-9);
        assertEquals(Math.log(Math.PI) + Math.log(Math.SQRT12), Gamma.logGamma(1.5), 1e-5);
        assertEquals(Math.log(Math.PI) + Math.log(Math.SQRT12) + Math.log(3.0 / 2.0), Gamma.logGamma(3.5), 1e-5);
        assertEquals(Math.log(Math.PI) + Math.log(Math.SQRT12) + Math.log(3.0 / 2.0) + Math.log(5.0 / 2.0), Gamma.logGamma(5.5), 1e-5);
        try {
            assertEquals(Double.NaN, Gamma.logGamma(-1.0), 1e-9);
        } catch (MathException e) {
            // expected
        }
        try {
            assertEquals(Double.NaN, Gamma.logGamma(0.0), 1e-9);
        } catch (MathException e) {
            // expected
        }
    }

    @Test
    public void testRegularizedGammaP() {
        assertEquals(1.0, Gamma.regularizedGammaP(1.0, 1.0), 1e-9);
        assertEquals(Math.E, Gamma.regularizedGammaP(1.0, 2.0), 1e-6);
        assertEquals(Math.PI * Math.SQRT18, Gamma.regularizedGammaP(1.5, 2.0), 1e-5);
        assertEquals(Math.PI * Math.SQRT18 * Math.SQRT2, Gamma.regularizedGammaP(1.5, 3.0), 1e-5);
        assertEquals(Math.PI * Math.SQRT18 * Math.SQRT2 / 3.0, Gamma.regularizedGammaP(1.5, 4.0), 1e-5);
        assertEquals(Math.PI * Math.SQRT18 * Math.SQRT2 / 6.0, Gamma.regularizedGammaP(1.5, 5.0), 1e-5);
        try {
            assertEquals(1.0, Gamma.regularizedGammaP(0.0, 1.0), 1e-9);
        } catch (MathException e) {
            // expected
        }
        try {
            assertEquals(Double.NaN, Gamma.regularizedGammaP(-1.0, 1.0), 1e-9);
        } catch (MathException e) {
            // expected
        }
        try {
            assertEquals(Double.NaN, Gamma.regularizedGammaP(1.0, 0.0), 1e-9);
        } catch (MathException e) {
            // expected
        }
        try {
            assertEquals(Double.NaN, Gamma.regularizedGammaP(1.0, -1.0), 1e-9);
        } catch (MathException e) {
            // expected
        }
    }

    @Test
    public void testRegularizedGammaQ() {
        assertEquals(1.0 - Math.E, Gamma.regularizedGammaQ(1.0, 1.0), 1e-9);
        assertEquals(1.0 - Math.E, Gamma.regularizedGammaQ(1.0, 2.0), 1e-9);
        assertEquals(1.0 - Math.PI * Math.SQRT18 / 2.0, Gamma.regularizedGammaQ(1.5, 2.0), 1e-5);
        assertEquals(1.0 - Math.PI * Math.SQRT18 / 2.0 * Math.SQRT2 / 3.0, Gamma.regularizedGammaQ(1.5, 3.0), 1e-5);
        assertEquals(1.0 - Math.PI * Math.SQRT18 / 2.0 * Math.SQRT2 / 6.0, Gamma.regularizedGammaQ(1.5, 4.0), 1e-5);
        assertEquals(1.0 - Math.PI * Math.SQRT18 / 2.0 * Math.SQRT2 / 12.0, Gamma.regularizedGammaQ(1.5, 5.0), 1e-5);
        try {
            assertEquals(0.0, Gamma.regularizedGammaQ(0.0, 1.0), 1e-9);
        } catch (MathException e) {
            // expected
        }
        try {
            assertEquals(Double.NaN, Gamma.regularizedGammaQ(-1.0, 1.0), 1e-9);
        } catch (MathException e) {
            // expected
        }
        try {
            assertEquals(Double.NaN, Gamma.regularizedGammaQ(1.0, 0.0), 1e-9);
        } catch (MathException e) {
            // expected
        }
        try {
            assertEquals(Double.NaN, Gamma.regularizedGammaQ(1.0, -1.0), 1e-9);
        } catch (MathException e) {
            // expected
        }
    }
}