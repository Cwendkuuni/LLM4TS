package org.apache.commons.math.special.test;

import org.apache.commons.math3.SpecialFunctions;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link Gamma} class.
 */
public class GammaTest {

    /**
     * Test log gamma method.
     */
    @Test
    public void testLogGamma() {
        double eps = 1e-15;

        for (double x = 0.0; x < 10.0; x += 1.0) {
            double logGamma = SpecialFunctions.logGamma(x + 1.0);
            double result = Gamma.logGamma(x + 1.0);
            Assert.assertEquals(logGamma, result, eps);
        }

        for (double x = 10.0; x < 20.0; x += 1.0) {
            double logGamma = SpecialFunctions.logGamma(x + 1.0);
            double result = Gamma.logGamma(x + 1.0);
            Assert.assertEquals(logGamma, result, eps);
        }

        for (double x = -1.0; x > -10.0; x -= 1.0) {
            try {
                Gamma.logGamma(x + 1.0);
                Assert.fail("Expected MathException for negative argument");
            } catch (MathException e) {
                // expected
            }
        }

        try {
            Gamma.logGamma(Double.NaN);
            Assert.fail("Expected MathException for NaN argument");
        } catch (MathException e) {
            // expected
        }
    }

    /**
     * Test regularized gamma P method.
     */
    @Test
    public void testRegularizedGammaPP() throws MathException {
        double eps = 1e-14;
        double x = 2.5;
        double a = 3.0;

        double p1 = SpecialFunctions.regularizedGammaP(a, x);
        double p2 = Gamma.regularizedGammaP(a, x);
        Assert.assertEquals(p1, p2, eps);

        x = 1.5;
        a = 2.0;
        p1 = SpecialFunctions.regularizedGammaP(a, x);
        p2 = Gamma.regularizedGammaP(a, x);
        Assert.assertEquals(p1, p2, eps);

        x = -0.5;
        a = 1.5;
        p1 = SpecialFunctions.regularizedGammaP(a, x);
        p2 = Gamma.regularizedGammaP(a, x);
        Assert.assertEquals(p1, p2, eps);

        x = 1.0;
        a = 0.5;
        p1 = SpecialFunctions.regularizedGammaP(a, x);
        p2 = Gamma.regularizedGammaP(a, x);
        Assert.assertEquals(p1, p2, eps);

        x = 0.0;
        a = 0.5;
        p1 = SpecialFunctions.regularizedGammaP(a, x);
        p2 = Gamma.regularizedGammaP(a, x);
        Assert.assertEquals(p1, p2, eps);

        x = 1.5;
        a = -1.0;
        try {
            p1 = SpecialFunctions.regularizedGammaP(a, x);
            Assert.fail("Expected MathException for negative a");
        } catch (MathException e) {
            // expected
        }

        try {
            p1 = SpecialFunctions.regularizedGammaP(a, Double.NaN);
            Assert.fail("Expected MathException for NaN x");
        } catch (MathException e) {
            // expected
        }

        try {
            p1 = SpecialFunctions.regularizedGammaP(Double.NaN, x);
            Assert.fail("Expected MathException for NaN a");
        } catch (MathException e) {
            // expected
        }

        try {
            p1 = Gamma.regularizedGammaP(a, x);
            Assert.fail("Expected MathException for negative a");
        } catch (MathException e) {
            // expected
        }

        try {
            p1 = Gamma.regularizedGammaP(Double.NaN, x);
            Assert.fail("Expected MathException for NaN a");
        } catch (MathException e) {
            // expected
        }

        try {
            p1 = Gamma.regularizedGammaP(a, Double.NaN);
            Assert.fail("Expected MathException for NaN x");
        } catch (MathException e) {
            // expected
        }

        try {
            p1 = Gamma.regularizedGammaP(Double.NaN, Double.NaN);
            Assert.fail("Expected MathException for NaN x and a");
        } catch (MathException e) {
            // expected
        }

        try {
            Gamma.regularizedGammaP(a, x, 0.0, 0);
            Assert.fail("Expected MathException for invalid arguments");
        } catch (MathException e) {
            // expected
        }
    }

    /**
     * Test regularized gamma Q method.
     */
    @Test
    public void testRegularizedGammaQ() throws MathException {
        double eps = 1e-14;
        double x = 2.5;
        double a = 3.0;

        double q1 = SpecialFunctions.regularizedGammaQ(a, x);
        double q2 = 1.0 - Gamma.regularizedGammaP(a, x);
        Assert.assertEquals(q1, q2, eps);

        x = 1.5;
        a = 2.0;
        q1 = SpecialFunctions.regularizedGammaQ(a, x);
        q2 = 1.0 - Gamma.regularizedGammaP(a, x);
        Assert.assertEquals(q1, q2, eps);

        x = -0.5;
        a = 1.5;
        q1 = SpecialFunctions.regularizedGammaQ(a, x);
        q2 = 1.0 - Gamma.regularizedGammaP(a, x);
        Assert.assertEquals(q1, q2, eps);

        x = 1.0;
        a = 0.5;
        q1 = SpecialFunctions.regularizedGammaQ(a, x);
        q2 = 1.0 - Gamma.regularizedGammaP(a, x);
        Assert.assertEquals(q1, q2, eps);

        x = 0.0;
        a = 0.5;
        q1 = SpecialFunctions.regularizedGammaQ(a, x);
        q2 = 1.0 - Gamma.regularizedGammaP(a, x);
        Assert.assertEquals(q1, q2, eps);

        x = 1.5;
        a = -1.0;
        try {
            q1 = SpecialFunctions.regularizedGammaQ(a, x);
            Assert.fail("Expected MathException for negative a");
        } catch (MathException e) {
            // expected
        }

        try {
            q1 = SpecialFunctions.regularizedGammaQ(a, Double.NaN);
            Assert.fail("Expected MathException for NaN x");
        } catch (MathException e) {
            // expected
        }

        try {
            q1 = SpecialFunctions.regularizedGammaQ(Double.NaN, x);
            Assert.fail("Expected MathException for NaN a");
        } catch (MathException e) {
            // expected
        }

        try {
            q1 = Gamma.regularizedGammaQ(a, x);
            Assert.fail("Expected MathException for negative a");
        } catch (MathException e) {
            // expected
        }

        try {
            q1 = Gamma.regularizedGammaQ(Double.NaN, x);
            Assert.fail("Expected MathException for NaN a");
        } catch (MathException e) {
            // expected
        }

        try {
            q1 = Gamma.regularizedGammaQ(a, Double.NaN);
            Assert.fail("Expected MathException for NaN x");
        } catch (MathException e) {
            // expected
        }

        try {
            q1 = Gamma.regularizedGammaQ(Double.NaN, Double.NaN);
            Assert.fail("Expected MathException for NaN x and a");
        } catch (MathException e) {
            // expected
        }

        try {
            Gamma.regularizedGammaQ(a, x, 0.0, 0);
            Assert.fail("Expected MathException for invalid arguments");
        } catch (MathException e) {
            // expected
        }
    }
}