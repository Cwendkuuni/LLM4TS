package org.apache.commons.math.special.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.SpecialFunctions;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.junit.Test;

/**
 * Test cases for the Gamma class.
 */
public class GammaTest {

    /**
     * Test log gamma function.
     */
    @Test
    public void testLogGamma() {
        assertEquals(0.0, Gamma.logGamma(0.0), 1e-12); // 0! = 1
        assertEquals(-0.04201825634873254, Gamma.logGamma(0.5), 1e-12);
        assertEquals(0.17087271247461903, Gamma.logGamma(1.0), 1e-12);
        assertEquals(1.007257600016542, Gamma.logGamma(2.0), 1e-12);
        assertEquals(1.365648225041298, Gamma.logGamma(3.0), 1e-12);
        assertEquals(1.092219261034711, Gamma.logGamma(5.0), 1e-12);
        assertEquals(1.449284555439328, Gamma.logGamma(6.0), 1e-12);
        assertEquals(1.642646533924224, Gamma.logGamma(7.0), 1e-12);

        // negative values and complex numbers are not supported
        try {
            Gamma.logGamma(-1.0);
            fail();
        } catch (MathIllegalArgumentException e) {
            // expected
        }

        try {
            Gamma.logGamma(new Complex(1.0, 2.0));
            fail();
        } catch (MathIllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test regularized gamma function P.
     */
    @Test
    public void testRegularizedGammaP() {
        assertEquals(1.0, Gamma.regularizedGammaP(1.0, 1.0), 1e-12);
        assertEquals(0.5, Gamma.regularizedGammaP(0.5, 1.0), 1e-12);
        assertEquals(0.3626864, Gamma.regularizedGammaP(0.5, 2.0), 1e-12);
        assertEquals(0.2596966, Gamma.regularizedGammaP(0.5, 3.0), 1e-12);
        assertEquals(0.1946548, Gamma.regularizedGammaP(0.5, 4.0), 1e-12);
        assertEquals(0.1492797, Gamma.regularizedGammaP(0.5, 5.0), 1e-12);
        assertEquals(0.1201068, Gamma.regularizedGammaP(0.5, 6.0), 1e-12);
        assertEquals(0.1017344, Gamma.regularizedGammaP(0.5, 7.0), 1e-12);
        assertEquals(0.0851588, Gamma.regularizedGammaP(0.5, 8.0), 1e-12);

        // negative values are not supported
        try {
            Gamma.regularizedGammaP(-1.0, 1.0);
            fail();
        } catch (MathIllegalArgumentException e) {
            // expected
        }

        try {
            Gamma.regularizedGammaP(1.0, -1.0);
            fail();
        } catch (MathIllegalArgumentException e) {
            // expected
        }

        // very large values should work
        assertEquals(1.0, Gamma.regularizedGammaP(1000.0, 1000.0), 1e-12);
    }

    /**
     * Test regularized gamma function Q.
     */
    @Test
    public void testRegularizedGammaQ() {
        assertEquals(0.5, Gamma.regularizedGammaQ(0.5, 1.0), 1e-12);
        assertEquals(0.6389056, Gamma.regularizedGammaQ(0.5, 2.0), 1e-12);
        assertEquals(0.7278317, Gamma.regularizedGammaQ(0.5, 3.0), 1e-12);
        assertEquals(0.7888882, Gamma.regularizedGammaQ(0.5, 4.0), 1e-12);
        assertEquals(0.8317554, Gamma.regularizedGammaQ(0.5, 5.0), 1e-12);
        assertEquals(0.8599253, Gamma.regularizedGammaQ(0.5, 6.0), 1e-12);
        assertEquals(0.8754762, Gamma.regularizedGammaQ(0.5, 7.0), 1e-12);
        assertEquals(0.8862269, Gamma.regularizedGammaQ(0.5, 8.0), 1e-12);

        // negative values are not supported
        try {
            Gamma.regularizedGammaQ(-1.0, 1.0);
            fail();
        } catch (MathIllegalArgumentException e) {
            // expected
        }

        try {
            Gamma.regularizedGammaQ(1.0, -1.0);
            fail();
        } catch (MathIllegalArgumentException e) {
            // expected
        }

        // very large values should work
        assertEquals(0.999999999999832, Gamma.regularizedGammaQ(1000.0, 1000.0), 1e-12);
    }

    /**
     * Test regularized gamma function P with large a.
     */
    @Test
    public void testRegularizedGammaPLargeA() {
        double epsilon = 1e-12;
        double maxIterations = 100000;

        assertEquals(0.5, Gamma.regularizedGammaP(100.0, 1.0), epsilon);
        assertEquals(0.4896207, Gamma.regularizedGammaP(100.0, 2.0), epsilon);
        assertEquals(0.4598492, Gamma.regularizedGammaP(100.0, 3.0), epsilon);
        assertEquals(0.4243548, Gamma.regularizedGammaP(100.0, 4.0), epsilon);
        assertEquals(0.3834633, Gamma.regularizedGammaP(100.0, 5.0), epsilon);
        assertEquals(0.3384364, Gamma.regularizedGammaP(100.0, 6.0), epsilon);
        assertEquals(0.3005203, Gamma.regularizedGammaP(100.0, 7.0), epsilon);
        assertEquals(0.2683658, Gamma.regularizedGammaP(100.0, 8.0), epsilon);
        assertEquals(0.2399716, Gamma.regularizedGammaP(100.0, 9.0), epsilon);
        assertEquals(0.2152059, Gamma.regularizedGammaP(100.0, 10.0), epsilon);

        try {
            Gamma.regularizedGammaP(100.0, 0.0);
            fail();
        } catch (MathIllegalArgumentException e) {
            // expected
        }

        try {
            Gamma.regularizedGammaP(100.0, -1.0);
            fail();
        } catch (MathIllegalArgumentException e) {
            // expected
        }

        try {
            Gamma.regularizedGammaP(100.0, Integer.MAX_VALUE);
            fail();
        } catch (MaxIterationsExceededException e) {
            // expected
        }
    }

    /**
     * Test regularized gamma function Q with large a.
     */
    @Test
    public void testRegularizedGammaQLargeA() {
        double epsilon = 1e-12;
        double maxIterations = 100000;

        assertEquals(0.500000018314116, Gamma.regularizedGammaQ(100.0, 1.0), epsilon);
        assertEquals(0.539331599434975, Gamma.regularizedGammaQ(100.0, 2.0), epsilon);
        assertEquals(0.577329757449438, Gamma.regularizedGammaQ(100.0, 3.0), epsilon);
        assertEquals(0.613070657812871, Gamma.regularizedGammaQ(100.0, 4.0), epsilon);
        assertEquals(0.645270133269743, Gamma.regularizedGammaQ(100.0, 5.0), epsilon);
        assertEquals(0.673171569256204, Gamma.regularizedGammaQ(100.0, 6.0), epsilon);
        assertEquals(0.696632388070563, Gamma.regularizedGammaQ(100.0, 7.0), epsilon);
        assertEquals(0.715572346619022, Gamma.regularizedGammaQ(100.0, 8.0), epsilon);
        assertEquals(0.730013942406281, Gamma.regularizedGammaQ(100.0, 9.0), epsilon);
        assertEquals(0.740994051149438, Gamma.regularizedGammaQ(100.0, 10.0), epsilon);

        try {
            Gamma.regularizedGammaQ(100.0, 0.0);
            fail();
        } catch (MathIllegalArgumentException e) {
            // expected
        }

        try {
            Gamma.regularizedGammaQ(100.0, -1.0);
            fail();
        } catch (MathIllegalArgumentException e) {
            // expected
        }

        try {
            Gamma.regularizedGammaQ(100.0, Integer.MAX_VALUE);
            fail();
        } catch (MaxIterationsExceededException e) {
            // expected
        }
    }
}