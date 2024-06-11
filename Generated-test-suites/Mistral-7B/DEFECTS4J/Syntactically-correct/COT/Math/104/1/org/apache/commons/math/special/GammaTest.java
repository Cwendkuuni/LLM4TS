package org.apache.commons.math.special;

import static org.junit.Assert.*;

import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.junit.Test;

/**
 * JUnit test cases for the Gamma class.
 */
public class GammaTest {

    /**
     * Test logGamma method.
     * @throws MathException if the method fails.
     */
    @Test
    public void testLogGamma() throws MathException {
        double result = Gamma.logGamma(1.0);
        assertEquals("logGamma(1.0) should be 0.0", 0.0, result, 1e-12);

        result = Gamma.logGamma(2.5);
        assertEquals("logGamma(2.5) should be 0.781344", 0.781344, result, 1e-12);

        result = Gamma.logGamma(0.5);
        assertNotNaN("logGamma(0.5) should not be NaN", result);
        assertTrue("logGamma(0.5) should be positive", result > 0.0);

        result = Gamma.logGamma(-0.5);
        assertNotNaN("logGamma(-0.5) should not be NaN", result);
        assertTrue("logGamma(-0.5) should be complex", Double.isNaN(Math.real(result)) || Double.isNaN(Math.imag(result)));

        result = Gamma.logGamma(1.5);
        assertThrows(MathException.class, () -> Gamma.logGamma(1.5));
    }

    /**
     * Test logGamma method with edge cases.
     * @throws MathException if the method fails.
     */
    @Test
    public void testLogGammaEdgeCases() throws MathException {
        // Test logGamma with large positive values
        double result = Gamma.logGamma(1000.0);
        assertTrue("logGamma(1000.0) should be greater than 2000", result > 2000.0);

        // Test logGamma with large negative values
        result = Gamma.logGamma(-1000.0);
        assertNotNaN("logGamma(-1000.0) should not be NaN", result);
        assertTrue("logGamma(-1000.0) should be complex", Double.isNaN(Math.real(result)) || Double.isNaN(Math.imag(result)));

        // Test logGamma with very small positive values
        result = Gamma.logGamma(1e-308);
        assertNotNaN("logGamma(1e-308) should not be NaN", result);
        assertTrue("logGamma(1e-308) should be negative", result < 0.0);
    }

    /**
     * Test regularizedGammaP method.
     * @throws MathException if the method fails.
     */
    @Test
    public void testRegularizedGammaP() throws MathException {
        double result = Gamma.regularizedGammaP(1.0, 1.0);
        assertEquals("P(1.0, 1.0) should be 0.5", 0.5, result, 1e-12);

        result = Gamma.regularizedGammaP(2.5, 1.0);
        assertEquals("P(2.5, 1.0) should be 0.687274", 0.687274, result, 1e-12);

        result = Gamma.regularizedGammaP(1.0, 2.5);
        assertEquals("P(1.0, 2.5) should be 0.797885", 0.797885, result, 1e-12);

        result = Gamma.regularizedGammaP(0.5, 1.0);
        assertNotNaN("P(0.5, 1.0) should not be NaN", result);
        assertTrue("P(0.5, 1.0) should be positive", result > 0.0);

        result = Gamma.regularizedGammaP(1.5, 2.0);
        assertEquals("P(1.5, 2.0) should be 0.886227", 0.886227, result, 1e-12);

        result = Gamma.regularizedGammaP(2.5, 0.5);
        assertThrows(MathException.class, () -> Gamma.regularizedGammaP(2.5, 0.5));
    }

    /**
     * Test regularizedGammaP method with edge cases.
     * @throws MathException if the method fails.
     */
    @Test
    public void testRegularizedGammaPEdgeCases() throws MathException {
        // Test P(a, x) with large positive a
        result = Gamma.regularizedGammaP(1000.0, 1.0);
        assertTrue("P(1000.0, 1.0) should be close to 1.0", result > 0.9999999999999998);

        // Test P(a, x) with negative a
        result = Gamma.regularizedGammaP(-1.0, 1.0);
        assertNotNaN("P(-1.0, 1.0) should not be NaN", result);
        assertTrue("P(-1.0, 1.0) should be complex", Double.isNaN(Math.real(result)) || Double.isNaN(Math.imag(result)));

        // Test P(a, x) with large negative x
        result = Gamma.regularizedGammaP(1.0, -1.0);
        assertNotNaN("P(1.0, -1.0) should not be NaN", result);
        assertTrue("P(1.0, -1.0) should be complex", Double.isNaN(Math.real(result)) || Double.isNaN(Math.imag(result)));
    }

    /**
     * Test regularizedGammaQ method.
     * @throws MathException if the method fails.
     */
    @Test
    public void testRegularizedGammaQ() throws MathException {
        double result = Gamma.regularizedGammaQ(1.0, 1.0);
        assertEquals("Q(1.0, 1.0) should be 0.5", 0.5, result, 1e-12);

        result = Gamma.regularizedGammaQ(2.5, 1.0);
        assertEquals("Q(2.5, 1.0) should be 0.312726", 0.312726, result, 1e-12);

        result = Gamma.regularizedGammaQ(1.0, 2.5);
        assertEquals("Q(1.0, 2.5) should be 0.202115", 0.202115, result, 1e-12);

        result = Gamma.regularizedGammaQ(0.5, 1.0);
        assertNotNaN("Q(0.5, 1.0) should not be NaN", result);
        assertTrue("Q(0.5, 1.0) should be positive", result > 0.0);

        result = Gamma.regularizedGammaQ(1.5, 2.0);
        assertEquals("Q(1.5, 2.0) should be 0.113773", 0.113773, result, 1e-12);

        result = Gamma.regularizedGammaQ(2.5, 0.5);
        assertThrows(MathException.class, () -> Gamma.regularizedGammaQ(2.5, 0.5));
    }

    /**
     * Test regularizedGammaQ method with edge cases.
     * @throws MathException if the method fails.
     */
    @Test
    public void testRegularizedGammaQEdgeCases() throws MathException {
        // Test Q(a, x) with large positive a
        result = Gamma.regularizedGammaQ(1000.0, 1.0);
        assertTrue("Q(1000.0, 1.0) should be close to 0.5", Math.abs(result - 0.5) < 0.000001);

        // Test Q(a, x) with negative a
        result = Gamma.regularizedGammaQ(-1.0, 1.0);
        assertNotNaN("Q(-1.0, 1.0) should not be NaN", result);
        assertTrue("Q(-1.0, 1.0) should be complex", Double.isNaN(Math.real(result)) || Double.isNaN(Math.imag(result)));

        // Test Q(a, x) with large negative x
        result = Gamma.regularizedGammaQ(1.0, -1.0);
        assertNotNaN("Q(1.0, -1.0) should not be NaN", result);
        assertTrue("Q(1.0, -1.0) should be complex", Double.isNaN(Math.real(result)) || Double.isNaN(Math.imag(result)));

        // Test Q(a, x) with a = x
        result = Gamma.regularizedGammaQ(1.0, 1.0);
        assertEquals("Q(1.0, 1.0) should be 0", 0.0, result, 1e-12);
    }

    /**
     * Test regularizedGamma method.
     * @throws MathException if the method fails.
     */
    @Test
    public void testRegularizedGamma() throws MathException {
        double result = Gamma.regularizedGamma(1.0, 1.0);
        assertEquals("gamma(1.0, 1.0) should be 0.5", 0.5, result, 1e-12);

        result = Gamma.regularizedGamma(2.5, 1.0);
        assertEquals("gamma(2.5, 1.0) should be 0.687274", 0.687274, result, 1e-12);

        result = Gamma.regularizedGamma(1.0, 2.5);
        assertEquals("gamma(1.0, 2.5) should be 0.797885", 0.797885, result, 1e-12);

        result = Gamma.regularizedGamma(0.5, 1.0);
        assertNotNaN("gamma(0.5, 1.0) should not be NaN", result);
        assertTrue("gamma(0.5, 1.0) should be positive", result > 0.0);

        result = Gamma.regularizedGamma(1.5, 2.0);
        assertEquals("gamma(1.5, 2.0) should be 0.886227", 0.886227, result, 1e-12);

        result = Gamma.regularizedGamma(2.5, 0.5);
        assertThrows(MathException.class, () -> Gamma.regularizedGamma(2.5, 0.5));
    }

    /**
     * Test regularizedGamma method with edge cases.
     * @throws MathException if the method fails.
     */
    @Test
    public void testRegularizedGammaEdgeCases() throws MathException {
        // Test gamma(a, x) with large positive a
        result = Gamma.regularizedGamma(1000.0, 1.0);
        assertTrue("gamma(1000.0, 1.0) should be close to 1.0", Math.abs(result - 1.0) < 0.000001);

        // Test gamma(a, x) with negative a
        result = Gamma.regularizedGamma(-1.0, 1.0);
        assertThrows(MathException.class, () -> Gamma.regularizedGamma(-1.0, 1.0));

        // Test gamma(a, x) with large negative x
        result = Gamma.regularizedGamma(1.0, -1.0);
        assertThrows(MathException.class, () -> Gamma.regularizedGamma(1.0, -1.0));
    }

    /**
     * Test regularizedGamma method with exception handling.
     * @throws MathException if the method fails.
     */
    @Test(expected = MathException.class)
    public void testRegularizedGammaExceptionHandling() throws MathException {
        Gamma.regularizedGamma(-1.0, -1.0);
    }

    /**
     * Test regularizedGamma method with exception handling and edge cases.
     * @throws MathException if the method fails.
     */
    @Test(expected = MathException.class)
    public void testRegularizedGammaExceptionHandlingEdgeCases() throws MathException {
        Gamma.regularizedGamma(1.0, -1000.0);
    }

    /**
     * Test regularizedGamma method with exception handling and large values.
     * @throws MathException if the method fails.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testRegularizedGammaMaxIterations() throws MathException {
        result = Gamma.regularizedGamma(1.0, 1000000.0);
    }
}