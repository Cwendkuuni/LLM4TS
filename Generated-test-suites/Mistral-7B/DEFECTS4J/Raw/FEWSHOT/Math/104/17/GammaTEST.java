package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.special.MaxIterationsExceededException;
import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        assertEquals(0.0, Gamma.logGamma(0.0), 1e-10);
        assertEquals(0.0, Gamma.logGamma(1.0), 1e-10);
        assertEquals(-0.405465122332031, Gamma.logGamma(0.5), 1e-10);
        assertEquals(0.577215664901532, Gamma.logGamma(1.0 / 2.0), 1e-10);
        assertEquals(1.12265625, Gamma.logGamma(2.0), 1e-10);
        assertEquals(1.178097245115216, Gamma.logGamma(3.0), 1e-10);
        assertEquals(1.402009379066596, Gamma.logGamma(4.0), 1e-10);

        // test negative x
        assertTrue(Double.isNaN(Gamma.logGamma(-1.0)));
        assertTrue(Double.isNaN(Gamma.logGamma(-0.5)));
        assertTrue(Double.isNaN(Gamma.logGamma(-2.0)));
        assertTrue(Double.isNaN(Gamma.logGamma(-3.0)));

        // test large x
        assertEquals(5.781478984656423, Gamma.logGamma(100.0), 1e-10);

        // test NaN
        assertEquals(Double.NaN, Gamma.logGamma(Double.NaN), 1e-10);
        assertEquals(Double.NaN, Gamma.logGamma(Double.POSITIVE_INFINITY), 1e-10);
        assertEquals(Double.NaN, Gamma.logGamma(Double.NEGATIVE_INFINITY), 1e-10);
    }

    @Test
    public void testRegularizedGammaP() throws MathException {
        assertEquals(0.0, Gamma.regularizedGammaP(0.0, 0.0), 1e-10);
        assertEquals(1.0, Gamma.regularizedGammaP(1.0, 1.0), 1e-10);
        assertEquals(0.5, Gamma.regularizedGammaP(0.5, 1.0), 1e-10);
        assertEquals(0.666666666666667, Gamma.regularizedGammaP(1.3, 2.0), 1e-7);
        assertEquals(0.422784335087136, Gamma.regularizedGammaP(1.3, 3.0), 1e-7);
        assertEquals(0.333333333333333, Gamma.regularizedGammaP(1.3, 4.0), 1e-7);
        assertEquals(0.259520228635943, Gamma.regularizedGammaP(1.3, 5.0), 1e-7);
    }

    @Test
    public void testRegularizedGammaQ() throws MathException {
        assertEquals(1.0, Gamma.regularizedGammaQ(0.0, 0.0), 1e-10);
        assertEquals(0.0, Gamma.regularizedGammaQ(1.0, 1.0), 1e-10);
        assertEquals(0.5, Gamma.regularizedGammaQ(0.5, 1.0), 1e-10);
        assertEquals(0.333333333333333, Gamma.regularizedGammaQ(1.3, 2.0), 1e-7);
        assertEquals(0.666666666666667, Gamma.regularizedGammaQ(1.3, 3.0), 1e-7);
        assertEquals(0.740476190453913, Gamma.regularizedGammaQ(1.3, 4.0), 1e-7);
        assertEquals(0.800867748348522, Gamma.regularizedGammaQ(1.3, 5.0), 1e-7);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaPException() throws MathException {
        Gamma.regularizedGammaP(0.5, -1.0);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaQException() throws MathException {
        Gamma.regularizedGammaQ(-0.5, 1.0);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testRegularizedGammaMaxIterations() throws MathException {
        Gamma.regularizedGammaP(1.0, 1.0, 1e-15, 1);
    }
}