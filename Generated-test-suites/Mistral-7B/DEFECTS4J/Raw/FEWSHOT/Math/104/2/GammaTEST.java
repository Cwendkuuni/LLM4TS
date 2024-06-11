package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNaN;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.special.Gamma;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        assertEquals(0.0, Gamma.logGamma(0.0), 1e-20);
        assertEquals(-Math.log(2.0), Gamma.logGamma(1.0), 1e-20);
        assertEquals(Math.log(2.4), Gamma.logGamma(2.5), 1e-20);
        assertEquals(Math.log(3.0) + Math.log(2.0), Gamma.logGamma(3.0), 1e-20);
        assertEquals(Math.log(3.0) + Math.log(3.5), Gamma.logGamma(3.5), 1e-20);
        assertEquals(Math.log(5.5) + Math.log(3.5), Gamma.logGamma(5.5), 1e-20);
        assertNaN(Gamma.logGamma(-1.0));
        assertNaN(Gamma.logGamma(0.5));
        assertFalse(Double.isFinite(Gamma.logGamma(1.5)));
    }

    @Test
    public void testRegularizedGammaP() {
        assertEquals(0.5, Gamma.regularizedGammaP(0.5, 0.5), 1e-10);
        assertEquals(0.6666666666666666, Gamma.regularizedGammaP(1.5, 2.5), 1e-10);
        assertEquals(0.2312939119588923, Gamma.regularizedGammaP(2.5, 3.5), 1e-10);
        assertEquals(0.01735158522433521, Gamma.regularizedGammaP(3.5, 4.5), 1e-10);
        assertEquals(0.3678794411412243, Gamma.regularizedGammaP(4.5, 5.5), 1e-10);
        assertEquals(0.9182889223511288, Gamma.regularizedGammaP(5.5, 6.5), 1e-10);
        assertEquals(0.1213269365876159, Gamma.regularizedGammaP(6.5, 7.5), 1e-10);
        assertEquals(0.8888888888888889, Gamma.regularizedGammaP(7.5, 8.5), 1e-10);
        assertEquals(1.0, Gamma.regularizedGammaP(8.5, 9.5), 1e-10);
        assertNaN(Gamma.regularizedGammaP(-1.0, 2.5));
        assertNaN(Gamma.regularizedGammaP(1.5, 1.5));
        assertFalse(Double.isFinite(Gamma.regularizedGammaP(2.5, 2.5)));
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaPConvergenceFailure() {
        Gamma.regularizedGammaP(1.5, 1.5, 1e-20, 100);
    }

    @Test
    public void testRegularizedGammaQ() {
        assertEquals(0.5, Gamma.regularizedGammaQ(0.5, 0.5), 1e-10);
        assertEquals(0.3333333333333333, Gamma.regularizedGammaQ(1.5, 2.5), 1e-10);
        assertEquals(0.768706097566561, Gamma.regularizedGammaQ(2.5, 3.5), 1e-10);
        assertEquals(0.9826763245825111, Gamma.regularizedGammaQ(3.5, 4.5), 1e-10);
        assertEquals(0.632050295695243, Gamma.regularizedGammaQ(4.5, 5.5), 1e-10);
        assertEquals(0.1111111111111112, Gamma.regularizedGammaQ(5.5, 6.5), 1e-10);
        assertEquals(0.08213237864483777, Gamma.regularizedGammaQ(6.5, 7.5), 1e-10);
        assertEquals(0.1111111111111111, Gamma.regularizedGammaQ(7.5, 8.5), 1e-10);
        assertEquals(0.0, Gamma.regularizedGammaQ(8.5, 9.5), 1e-10);
        assertNaN(Gamma.regularizedGammaQ(-1.0, 2.5));
        assertNaN(Gamma.regularizedGammaQ(1.5, 1.5));
        assertFalse(Double.isFinite(Gamma.regularizedGammaQ(2.5, 2.5)));
    }

    @Test
    public void testRegularizedGammaQComparison() {
        double epsilon = 1e-15;
        double maxIterations = 100;

        assertEquals(1.0 - Gamma.regularizedGammaP(1.5, 2.5), 
                     Gamma.regularizedGammaQ(1.5, 2.5), epsilon);
        assertEquals(Gamma.regularizedGammaP(1.5, 2.5) + 
                     Gamma.regularizedGammaQ(1.5, 2.5), 2.0, epsilon);
    }
}