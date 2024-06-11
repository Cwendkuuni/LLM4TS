package org.apache.commons.math.special.tests;

import junit.framework.TestCase;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.special.Gamma;

public class GammaTest extends TestCase {

    /**
     * Test of logGamma method, regular case.
     */
    public void testLogGamma() {
        double result = Gamma.logGamma(1.5);
        double expected = Math.log(Math.PI) + 0.5 * Math.log(1.5) - 0.0841;
        assertEquals("logGamma(1.5)", expected, result, 1e-9);
    }

    /**
     * Test of logGamma method, edge case: x < 0.
     */
    public void testLogGammaNegativeX() {
        try {
            Gamma.logGamma(-0.5);
            fail("Expected MathException for negative argument.");
        } catch (MathException e) {
            // expected
        }
    }

    /**
     * Test of logGamma method, edge case: x = 0.
     */
    public void testLogGammaZero() {
        try {
            Gamma.logGamma(0.0);
            fail("Expected MathException for zero argument.");
        } catch (MathException e) {
            // expected
        }
    }

    /**
     * Test of logGamma method, edge case: very large x.
     */
    public void testLogGammaLargeX() {
        double result = Gamma.logGamma(1000.0);
        double expected = Math.log(Math.PI) + 1000.5 * Math.log(1000.0) - 1665.4;
        assertEquals("logGamma(1000.0)", expected, result, 1e-9);
    }

    /**
     * Test of regularizedGammaP method, regular case.
     */
    public void testRegularizedGammaP() {
        double result = Gamma.regularizedGammaP(1.5, 1.5);
        double expected = 0.9331;
        assertEquals("regularizedGammaP(1.5, 1.5)", expected, result, 1e-5);
    }

    /**
     * Test of regularizedGammaP method, edge case: x <= 0.
     */
    public void testRegularizedGammaPNegativeX() {
        try {
            Gamma.regularizedGammaP(0.5, 0.0);
            fail("Expected MathException for negative argument.");
        } catch (MathException e) {
            // expected
        }
    }

    /**
     * Test of regularizedGammaP method, edge case: x = 0.
     */
    public void testRegularizedGammaPZero() {
        try {
            Gamma.regularizedGammaP(0.5, 0.0);
            fail("Expected MathException for zero argument.");
        } catch (MathException e) {
            // expected
        }
    }

    /**
     * Test of regularizedGammaP method, edge case: very large x.
     */
    public void testRegularizedGammaPLargeX() {
        double result = Gamma.regularizedGammaP(1000.5, 1001.0);
        double expected = 0.0003;
        assertEquals("regularizedGammaP(1000.5, 1001.0)", expected, result, 1e-6);
    }

    /**
     * Test of regularizedGammaQ method, regular case.
     */
    public void testRegularizedGammaQ() {
        double result = Gamma.regularizedGammaQ(1.5, 1.5);
        double expected = 0.0669;
        assertEquals("regularizedGammaQ(1.5, 1.5)", expected, result, 1e-5);
    }

    /**
     * Test of regularizedGammaQ method, edge case: x <= 0.
     */
    public void testRegularizedGammaQNegativeX() {
        try {
            Gamma.regularizedGammaQ(0.5, 0.0);
            fail("Expected MathException for negative argument.");
        } catch (MathException e) {
            // expected
        }
    }

    /**
     * Test of regularizedGammaQ method, edge case: x = 0.
     */
    public void testRegularizedGammaQZero() {
        try {
            Gamma.regularizedGammaQ(0.5, 0.0);
            fail("Expected MathException for zero argument.");
        } catch (MathException e) {
            // expected
            return;
        }

        double result = Gamma.regularizedGammaQ(0.5, 0.0);
        double expected = 1.0;
        assertEquals("regularizedGammaQ(0.5, 0.0)", expected, result, 1e-9);
    }

    /**
     * Test of regularizedGammaQ method, edge case: very large x.
     */
    public void testRegularizedGammaQLargeX() {
        double result = Gamma.regularizedGammaQ(1000.5, 1001.0);
        double expected = 0.9997;
        assertEquals("regularizedGammaQ(1000.5, 1001.0)", expected, result, 1e-6);
    }
}