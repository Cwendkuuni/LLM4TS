package org.apache.commons.math.special;

import static org.junit.Assert.*;

import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.Precision;

import org.junit.Test;

/**
 * JUnit 4 test file for the Gamma class.
 */
public class GammaTest {

    /**
     * Test for logGamma method.
     */
    @Test
    public void testLogGamma() {
        double result;

        // Test valid inputs
        result = Gamma.logGamma(1.5);
        assertEquals(0.4058251426953, result, Precision.TOLERANCE);
        result = Gamma.logGamma(2.0);
        assertEquals(Math.log(Math.PI), result, Precision.TOLERANCE);
        result = Gamma.logGamma(3.0);
        assertEquals(1.0633237550507, result, Precision.TOLERANCE);

        // Test invalid inputs
        result = Gamma.logGamma(-1.0);
        assertEquals(Double.NaN, result);
        result = Gamma.logGamma(0.0);
        assertEquals(Double.NaN, result);
        result = Gamma.logGamma(1.0);
        assertEquals(Double.NaN, result);
    }

    /**
     * Test for regularizedGammaP method with default epsilon and maxIterations.
     */
    @Test
    public void testRegularizedGammaP() {
        double result;

        // Test valid inputs
        result = Gamma.regularizedGammaP(1.5, 1.5);
        assertEquals(0.9713332618199, result, Precision.TOLERANCE);
        result = Gamma.regularizedGammaP(2.0, 2.0);
        assertEquals(0.6366197723676, result, Precision.TOLERANCE);
        result = Gamma.regularizedGammaP(3.0, 3.0);
        assertEquals(0.9999999999999, result, Precision.TOLERANCE);

        // Test invalid inputs
        result = Gamma.regularizedGammaP(-1.0, 1.5);
        assertEquals(Double.NaN, result);
        result = Gamma.regularizedGammaP(0.0, 1.5);
        assertEquals(Double.NaN, result);
        result = Gamma.regularizedGammaP(1.0, 1.5);
        assertEquals(Double.NaN, result);
        result = Gamma.regularizedGammaP(1.5, 0.0);
        assertEquals(Double.NaN, result);
    }

    /**
     * Test for regularizedGammaP method with specified epsilon and maxIterations.
     */
    @Test
    public void testRegularizedGammaPWithCustomEpsilonAndMaxIterations() {
        double result;

        // Test valid inputs
        result = Gamma.regularizedGammaP(1.5, 1.5, 0.00001, 10000);
        assertEquals(0.9713332618199, result, 0.00001);

        // Test invalid inputs - negative a
        try {
            result = Gamma.regularizedGammaP(-1.0, 1.5, 0.00001, 10000);
            fail();
        } catch (MathException e) {
            // Expected
        }

        // Test invalid inputs - negative x
        try {
            result = Gamma.regularizedGammaP(1.5, -1.0, 0.00001, 10000);
            fail();
        } catch (MathException e) {
            // Expected
        }

        // Test invalid inputs - a <= 0
        try {
            result = Gamma.regularizedGammaP(0.0, 1.5, 0.00001, 10000);
            fail();
        } catch (MathException e) {
            // Expected
        }

        // Test invalid inputs - x < 0
        try {
            result = Gamma.regularizedGammaP(1.5, 0.0, 0.00001, 10000);
            fail();
        } catch (MathException e) {
            // Expected
        }
    }

    /**
     * Test for regularizedGammaQ method with default epsilon and maxIterations.
     */
    @Test
    public void testRegularizedGammaQ() {
        double result;

        // Test valid inputs
        result = Gamma.regularizedGammaQ(1.5, 1.5);
        assertEquals(0.0286667381819, result, Precision.TOLERANCE);
        result = Gamma.regularizedGammaQ(2.0, 2.0);
        assertEquals(0.3633896537315, result, Precision.TOLERANCE);
        result = Gamma.regularizedGammaQ(3.0, 3.0);
        assertEquals(0.0, result, Precision.TOLERANCE);

        // Test invalid inputs
        result = Gamma.regularizedGammaQ(-1.0, 1.5);
        assertEquals(Double.NaN, result);
        result = Gamma.regularizedGammaQ(0.0, 1.5);
        assertEquals(Double.NaN, result);
        result = Gamma.regularizedGammaQ(1.0, 1.5);
        assertEquals(Double.NaN, result);
        result = Gamma.regularizedGammaQ(1.5, 0.0);
        assertEquals(Double.NaN, result);
    }

    /**
     * Test for regularizedGammaQ method with specified epsilon and maxIterations.
     */
    @Test
    public void testRegularizedGammaQWithCustomEpsilonAndMaxIterations() {
        double result;

        // Test valid inputs
        result = Gamma.regularizedGammaQ(1.5, 1.5, 0.00001, 10000);
        assertEquals(0.0286667381819, result, 0.00001);

        // Test invalid inputs - negative a
        try {
            result = Gamma.regularizedGammaQ(-1.0, 1.5, 0.00001, 10000);
            fail();
        } catch (MathException e) {
            // Expected
        }

        // Test invalid inputs - negative x
        try {
            result = Gamma.regularizedGammaQ(1.5, -1.0, 0.00001, 10000);
            fail();
        } catch (MathException e) {
            // Expected
        }

        // Test invalid inputs - a <= 0
        try {
            result = Gamma.regularizedGammaQ(0.0, 1.5, 0.00001, 10000);
            fail();
        } catch (MathException e) {
            // Expected
        }

        // Test invalid inputs - x < 0
        try {
            result = Gamma.regularizedGammaQ(1.5, 0.0, 0.00001, 10000);
            fail();
        } catch (MathException e) {
            // Expected
        }

        // Test maxIterationsExceededException
        try {
            result = Gamma.regularizedGammaQ(1.5, 1.5, 1e-20, 5);
            fail();
        } catch (MaxIterationsExceededException e) {
            // Expected
        }
    }
}