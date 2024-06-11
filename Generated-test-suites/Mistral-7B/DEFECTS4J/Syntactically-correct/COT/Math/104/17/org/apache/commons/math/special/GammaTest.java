package org.apache.commons.math.special.GammaTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.SpecialFunctions;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        assertEquals(0.0, SpecialFunctions.logGamma(0.0), 1e-9);
        assertEquals(1.0, SpecialFunctions.logGamma(1.0), 1e-9);
        assertEquals(-0.042059, SpecialFunctions.logGamma(2.5), 1e-9);
        assertEquals(-1.105831, SpecialFunctions.logGamma(3.0), 1e-9);
        assertEquals(-1.251524, SpecialFunctions.logGamma(3.5), 1e-9);
        assertEquals(-1.386294, SpecialFunctions.logGamma(4.0), 1e-9);
        assertEquals(-1.570796, SpecialFunctions.logGamma(5.0), 1e-9);
        assertEquals(-1.755152, SpecialFunctions.logGamma(6.0), 1e-9);
        assertEquals(-2.011161, SpecialFunctions.logGamma(7.0), 1e-9);
        assertEquals(-2.248519, SpecialFunctions.logGamma(8.0), 1e-9);

        try {
            SpecialFunctions.logGamma(-1.0);
            assertTrue(false);
        } catch (MathIllegalArgumentException e) {
            // expected
        }

        try {
            SpecialFunctions.logGamma(0.5);
            assertTrue(false);
        } catch (MathRuntimeException e) {
            assertTrue(e.getCause() instanceof MathIllegalArgumentException);
        }
    }

    @Test
    public void testRegularizedGammaP() {
        assertEquals(0.0, SpecialFunctions.regularizedGammaP(0.5, 0.0), 1e-9);
        assertEquals(1.0, SpecialFunctions.regularizedGammaP(1.5, 1.0), 1e-9);
        assertEquals(0.5772156649, SpecialFunctions.regularizedGammaP(0.5, 1.0), 1e-9);
        assertEquals(0.8862269255, SpecialFunctions.regularizedGammaP(1.5, 2.0), 1e-9);
        assertEquals(0.9753586555, SpecialFunctions.regularizedGammaP(1.0, 3.0), 1e-9);
        assertEquals(0.9923536791, SpecialFunctions.regularizedGammaP(1.5, 3.0), 1e-9);
        assertEquals(0.9999999975, SpecialFunctions.regularizedGammaP(2.0, 5.0), 1e-9);
        assertEquals(0.9999999998, SpecialFunctions.regularizedGammaP(2.5, 5.0), 1e-9);

        try {
            SpecialFunctions.regularizedGammaP(-0.5, 1.0);
            assertTrue(false);
        } catch (MathIllegalArgumentException e) {
            // expected
        }

        try {
            SpecialFunctions.regularizedGammaP(0.5, -1.0);
            assertTrue(false);
        } catch (MathIllegalArgumentException e) {
            // expected
        }

        try {
            SpecialFunctions.regularizedGammaP(1.5, -1.0);
            assertTrue(false);
        } catch (MathIllegalArgumentException e) {
            // expected
        }

        try {
            SpecialFunctions.regularizedGammaP(1.5, 0.0);
            assertTrue(false);
        } catch (MathRuntimeException e) {
            assertTrue(e.getCause() instanceof MathIllegalArgumentException);
        }
    }

    @Test
    public void testRegularizedGammaQ() {
        assertEquals(1.0, SpecialFunctions.regularizedGammaQ(0.5, 0.0), 1e-9);
        assertEquals(0.5, SpecialFunctions.regularizedGammaQ(1.5, 1.0), 1e-9);
        assertEquals(1.0 - 0.5772156649, SpecialFunctions.regularizedGammaQ(0.5, 1.0), 1e-9);
        assertEquals(1.0 - 0.8862269255, SpecialFunctions.regularizedGammaQ(1.5, 2.0), 1e-9);
        assertEquals(1.0 - 0.9753586555, SpecialFunctions.regularizedGammaQ(1.0, 3.0), 1e-9);
        assertEquals(1.0 - 0.9923536791, SpecialFunctions.regularizedGammaQ(1.5, 3.0), 1e-9);
        assertEquals(1.0 - 0.9999999975, SpecialFunctions.regularizedGammaQ(2.0, 5.0), 1e-9);
        assertEquals(1.0 - 0.9999999998, SpecialFunctions.regularizedGammaQ(2.5, 5.0), 1e-9);

        try {
            SpecialFunctions.regularizedGammaQ(-0.5, 1.0);
            assertTrue(false);
        } catch (MathIllegalArgumentException e) {
            // expected
        }

        try {
            SpecialFunctions.regularizedGammaQ(0.5, -1.0);
            assertTrue(false);
        } catch (MathIllegalArgumentException e) {
            // expected
        }

        try {
            SpecialFunctions.regularizedGammaQ(1.5, -1.0);
            assertTrue(false);
        } catch (MathIllegalArgumentException e) {
            // expected
        }

        try {
            SpecialFunctions.regularizedGammaQ(1.5, 0.0);
            assertTrue(false);
        } catch (MathRuntimeException e) {
            assertTrue(e.getCause() instanceof MathIllegalArgumentException);
        }
    }

    @Test(expected = MathRuntimeException.class)
    public void testRegularizedGammaP_MaxIterations() {
        SpecialFunctions.regularizedGammaP(0.5, 0.0, 0.0, 1);
    }
}