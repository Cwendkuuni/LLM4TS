package org.apache.commons.math.special;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNaN;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.util.Precision;

import org.apache.commons.math.MathException;
import org.apache.commons.math.special.Gamma;

/**
 * Test class for Gamma utility class.
 */
public class GammaTest {

    /**
     * Test log gamma method with positive arguments.
     */
    @org.junit.Test
    public void testLogGammaPositive() {
        double result;

        result = Gamma.logGamma(1.0);
        assertEquals("logGamma(1.0) should be 0.0", 0.0, result, 1e-12);

        result = Gamma.logGamma(2.5);
        assertEquals("logGamma(2.5) should be 0.69314718056", 0.69314718056, result, 1e-12);

        result = Gamma.logGamma(5.5);
        assertEquals("logGamma(5.5) should be 1.51232028955", 1.51232028955, result, 1e-12);
    }

    /**
     * Test log gamma method with negative arguments.
     */
    @org.junit.Test
    public void testLogGammaNegative() {
        double result;

        result = Gamma.logGamma(-0.5);
        assertFalse("logGamma(-0.5) should be NaN", Double.isNaN(result));

        result = Gamma.logGamma(-1.5);
        assertFalse("logGamma(-1.5) should be NaN", Double.isNaN(result));

        result = Gamma.logGamma(-3.5);
        assertFalse("logGamma(-3.5) should be NaN", Double.isNaN(result));
    }

    /**
     * Test log gamma method with non-numeric arguments.
     */
    @org.junit.Test
    public void testLogGammaNaN() {
        double result;

        result = Gamma.logGamma(Double.NaN);
        assertNaN("logGamma(NaN) should be NaN", result);

        result = Gamma.logGamma("string");
        assertNaN("logGamma(\"string\") should be NaN", result);
    }

    /**
     * Test log gamma method with zero argument.
     */
    @org.junit.Test
    public void testLogGammaZero() {
        double result;

        result = Gamma.logGamma(0.0);
        assertFalse("logGamma(0.0) should be NaN", Double.isNaN(result));
    }

    /**
     * Test regularized Gamma function P with positive arguments.
     */
    @org.junit.Test
    public void testRegularizedGammaPPPositive() {
        double result;

        result = Gamma.regularizedGammaP(1.0, 1.0);
        assertEquals("regularizedGammaP(1.0, 1.0) should be 0.5", 0.5, result, 1e-12);

        result = Gamma.regularizedGammaP(2.5, 0.5);
        assertEquals("regularizedGammaP(2.5, 0.5) should be 0.72340232859", 0.72340232859, result, 1e-12);

        result = Gamma.regularizedGammaP(5.5, 1.5);
        assertEquals("regularizedGammaP(5.5, 1.5) should be 0.99999995469", 0.99999995469, result, 1e-12);
    }

    /**
     * Test regularized Gamma function P with negative arguments.
     */
    @org.junit.Test
    public void testRegularizedGammaPNegative() {
        double result;

        result = Gamma.regularizedGammaP(-0.5, 1.0);
        assertFalse("regularizedGammaP(-0.5, 1.0) should be NaN", Double.isNaN(result));

        result = Gamma.regularizedGammaP(-1.5, 0.5);
        assertFalse("regularizedGammaP(-1.5, 0.5) should be NaN", Double.isNaN(result));

        result = Gamma.regularizedGammaP(-3.5, 1.5);
        assertFalse("regularizedGammaP(-3.5, 1.5) should be NaN", Double.isNaN(result));
    }

    /**
     * Test regularized Gamma function P with non-numeric arguments.
     */
    @org.junit.Test
    public void testRegularizedGammaPNonNumeric() {
        double result;

        result = Gamma.regularizedGammaP(Double.NaN, 1.0);
        assertNaN("regularizedGammaP(NaN, 1.0) should be NaN", result);

        result = Gamma.regularizedGammaP("string", 1.0);
        assertNaN("regularizedGammaP(\"string\", 1.0) should be NaN", result);
    }

    /**
     * Test regularized Gamma function P with zero arguments.
     */
    @org.junit.Test
    public void testRegularizedGammaPZero() {
        double result;

        result = Gamma.regularizedGammaP(0.0, 1.0);
        assertEquals("regularizedGammaP(0.0, 1.0) should be 1.0", 1.0, result, 1e-12);
    }

    /**
     * Test regularized Gamma function P with large arguments.
     */
    @org.junit.Test
    public void testRegularizedGammaPLarge() {
        double result;

        result = Gamma.regularizedGammaP(1000.0, 1000.0);
        assertTrue("regularizedGammaP(1000.0, 1000.0) should be > 0.95", Precision.greater(result, 0.95));
    }

    /**
     * Test regularized Gamma function Q with positive arguments.
     */
    @org.junit.Test
    public void testRegularizedGammaQPositive() {
        double result;

        result = Gamma.regularizedGammaQ(1.0, 1.0);
        assertEquals("regularizedGammaQ(1.0, 1.0) should be 0.5", 0.5, result, 1e-12);

        result = Gamma.regularizedGammaQ(2.5, 0.5);
        assertEquals("regularizedGammaQ(2.5, 0.5) should be 1.27660464498", 1.27660464498, result, 1e-12);

        result = Gamma.regularizedGammaQ(5.5, 1.5);
        assertEquals("regularizedGammaQ(5.5, 1.5) should be 0.049999999928", 0.049999999928, result, 1e-12);
    }

    /**
     * Test regularized Gamma function Q with negative arguments.
     */
    @org.junit.Test
    public void testRegularizedGammaQNegative() {
        double result;

        result = Gamma.regularizedGammaQ(-0.5, 1.0);
        assertFalse("regularizedGammaQ(-0.5, 1.0) should be NaN", Double.isNaN(result));

        result = Gamma.regularizedGammaQ(-1.5, 0.5);
        assertFalse("regularizedGammaQ(-1.5, 0.5) should be NaN", Double.isNaN(result));

        result = Gamma.regularizedGammaQ(-3.5, 1.5);
        assertFalse("regularizedGammaQ(-3.5, 1.5) should be NaN", Double.isNaN(result));
    }

    /**
     * Test regularized Gamma function Q with non-numeric arguments.
     */
    @org.junit.Test
    public void testRegularizedGammaQNonNumeric() {
        double result;

        result = Gamma.regularizedGammaQ(Double.NaN, 1.0);
        assertNaN("regularizedGammaQ(NaN, 1.0) should be NaN", result);

        result = Gamma.regularizedGammaQ("string", 1.0);
        assertNaN("regularizedGammaQ(\"string\", 1.0) should be NaN", result);
    }

    /**
     * Test regularized Gamma function Q with zero arguments.
     */
    @org.junit.Test
    public void testRegularizedGammaQZero() {
        double result;

        result = Gamma.regularizedGammaQ(0.0, 1.0);
        assertEquals("regularizedGammaQ(0.0, 1.0) should be 1.0", 1.0, result, 1e-12);
    }

    /**
     * Test regularized Gamma function Q with large arguments.
     */
    @org.junit.Test
    public void testRegularizedGammaQLarge() {
        double result;

        result = Gamma.regularizedGammaQ(1000.0, 1000.0);
        assertTrue("regularizedGammaQ(1000.0, 1000.0) should be < 1.0", Precision.lessThan(result, 1.0));
    }

    /**
     * Test regularized Gamma function Q with equal arguments.
     */
    @org.junit.Test
    public void testRegularizedGammaQEqual() {
        double result;

        result = Gamma.regularizedGammaQ(1.0, 1.0);
        assertEquals("regularizedGammaQ(1.0, 1.0) should be 0.5", 0.5, result, 1e-12);

        result = Gamma.regularizedGammaQ(2.0, 2.0);
        assertEquals("regularizedGammaQ(2.0, 2.0) should be 0.66666668653", 0.66666668653, result, 1e-12);

        result = Gamma.regularizedGammaQ(3.5, 3.5);
        assertEquals("regularizedGammaQ(3.5, 3.5) should be 0.74612813292", 0.74612813292, result, 1e-12);
    }
}