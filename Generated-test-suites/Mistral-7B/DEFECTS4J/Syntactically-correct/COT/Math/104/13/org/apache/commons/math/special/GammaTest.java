package org.apache.commons.math.special;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNaN;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.junit.Test;

/**
 * Tests for the Gamma class.
 */
public class GammaTest {

    @Test
    public void testLogGamma() {
        double result;

        // Test valid inputs
        result = Gamma.logGamma(0.5);
        assertEquals(0.5 * Math.log(Math.PI) + 0.5 * Math.log(2.0), result, 1e-5);

        result = Gamma.logGamma(1.0);
        assertEquals(Math.log(Math.PI) + Math.EULER_CONSTANT, result, 1e-5);

        result = Gamma.logGamma(2.0);
        assertEquals(Math.log(Math.PI) + 2.0 * Math.log(2.0) - 1.0, result, 1e-5);

        // Test negative input
        assertNaN(Gamma.logGamma(-1.0));

        // Test zero input
        assertNaN(Gamma.logGamma(0.0));

        // Test non-numeric input
        assertNaN(Gamma.logGamma("invalid"));

        // Test exception handling
        assertThrows(MathIllegalArgumentException.class, () -> Gamma.logGamma(Double.NEGATIVE_INFINITY));
        assertThrows(MathRuntimeException.class, () -> Gamma.logGamma(Double.POSITIVE_INFINITY));
    }

    @Test
    public void testRegularizedGammaP() throws MathException {
        double result;

        // Test valid inputs
        result = Gamma.regularizedGammaP(0.5, 0.5);
        assertEquals(Math.sqrt(Math.PI) / (2.0 * Math.sqrt(2.0)), result, 1e-5);

        result = Gamma.regularizedGammaP(1.0, 1.0);
        assertEquals(1.0, result, 1e-5);

        result = Gamma.regularizedGammaP(3.0, 2.0);
        assertEquals(0.543573764412461, result, 1e-5);

        result = Gamma.regularizedGammaP(3.0, 3.0);
        assertEquals(1.0, result, 1e-5);

        result = Gamma.regularizedGammaP(5.0, 1.0);
        assertEquals(0.017723858520582, result, 1e-5);

        // Test edge cases
        result = Gamma.regularizedGammaP(-1.0, 1.0);
        assertNaN(result);

        result = Gamma.regularizedGammaP(0.0, 1.0);
        assertNaN(result);

        result = Gamma.regularizedGammaP(0.0, 0.0);
        assertNaN(result);

        result = Gamma.regularizedGammaP(1.0, 0.0);
        assertEquals(1.0, result, 1e-5);

        // Test exception handling
        assertThrows(MathIllegalArgumentException.class, () -> Gamma.regularizedGammaP(-1.0, 1.0));
        assertThrows(MathException.class, () -> Gamma.regularizedGammaP(1.5, 1.0));
    }

    @Test
    public void testRegularizedGammaQ() throws MathException {
        double result;

        // Test valid inputs
        result = Gamma.regularizedGammaQ(0.5, 0.5);
        assertEquals(1.0 - Math.sqrt(Math.PI) / (2.0 * Math.sqrt(2.0)), result, 1e-5);

        result = Gamma.regularizedGammaQ(1.0, 1.0);
        assertEquals(0.0, result, 1e-5);

        result = Gamma.regularizedGammaQ(3.0, 2.0);
        assertEquals(0.456426235865539, result, 1e-5);

        result = Gamma.regularizedGammaQ(3.0, 3.0);
        assertEquals(0.0, result, 1e-5);

        result = Gamma.regularizedGammaQ(5.0, 1.0);
        assertEquals(0.982276141479418, result, 1e-5);

        // Test edge cases
        result = Gamma.regularizedGammaQ(-1.0, 1.0);
        assertNaN(result);

        result = Gamma.regularizedGammaQ(0.0, 1.0);
        assertNaN(result);

        result = Gamma.regularizedGammaQ(0.0, 0.0);
        assertNaN(result);

        result = Gamma.regularizedGammaQ(1.0, 0.0);
        assertEquals(1.0, result, 1e-5);

        // Test exception handling
        assertThrows(MathIllegalArgumentException.class, () -> Gamma.regularizedGammaQ(-1.0, 1.0));
        assertThrows(MathException.class, () -> Gamma.regularizedGammaQ(1.5, 1.0));
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testRegularizedGammaPWithMaxIterations() throws MathException {
        Gamma.regularizedGammaP(1.5, 1.0, 1e-5, 1);
    }
}