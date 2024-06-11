package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.math3.special.Gamma;

public class GammaTest {

    @Test
    public void testLogGamma() {
        assertEquals(0.0, Gamma.logGamma(0.0), 1e-10);
        assertEquals(-Math.log(2.0), Gamma.logGamma(1.0), 1e-10);
        assertEquals(Math.log(45.0), Gamma.logGamma(5.5), 1e-10);
        assertEquals(Math.log(15629.6326234), Gamma.logGamma(7), 1e-10);
        assertEquals(Math.log(640320.0), Gamma.logGamma(6), 1e-10);
        assertEquals(Double.NaN, Gamma.logGamma(-1.0), 1e-10);
        assertEquals(Double.NaN, Gamma.logGamma(1.5), 1e-10);
        assertEquals(Double.NaN, Gamma.logGamma(0.5), 1e-10);
    }

    @Test(expected = MathException.class)
    public void testLogGammaDiverges() {
        Gamma.logGamma(0.0);
    }

    @Test
    public void testRegularizedGammaP() {
        assertEquals(0.0, Gamma.regularizedGammaP(0.0, 0.0), 1e-10);
        assertEquals(1.0, Gamma.regularizedGammaP(1.0, 1.0), 1e-10);
        assertEquals(0.781186873739658, Gamma.regularizedGammaP(1.5, 1.0), 1e-6);
        assertEquals(1.212764921149719, Gamma.regularizedGammaP(2.5, 1.0), 1e-6);
        assertEquals(0.888184745651732, Gamma.regularizedGammaP(2.0, 2.0), 1e-6);
        assertEquals(0.664469719613333, Gamma.regularizedGammaP(1.5, 2.0), 1e-6);
        assertEquals(1.8212846261665935e-15, Gamma.regularizedGammaP(0.5, 1.0), 1e-13);
        assertEquals(0.6666666666666667, Gamma.regularizedGammaP(1.5, 1.5), 1e-6);
        assertEquals(0.5, Gamma.regularizedGammaP(0.5, 1.0), 1e-6);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaPConvergenceFailure() {
        Gamma.regularizedGammaP(2.0, 1.0, 1e-20, 10);
    }

    @Test
    public void testRegularizedGammaQ() {
        assertEquals(1.0, Gamma.regularizedGammaQ(1.0, 1.0), 1e-10);
        assertEquals(0.295652225321741, Gamma.regularizedGammaQ(1.5, 1.0), 1e-6);
        assertEquals(0.723125373226225, Gamma.regularizedGammaQ(2.5, 1.0), 1e-6);
        assertEquals(0.886226925452758, Gamma.regularizedGammaQ(2.0, 2.0), 1e-6);
        assertEquals(0.687283938604203, Gamma.regularizedGammaQ(1.5, 2.0), 1e-6);
        assertEquals(1.320913361624353, Gamma.regularizedGammaQ(0.5, 1.0), 1e-6);
        assertEquals(0.5, Gamma.regularizedGammaQ(1.5, 1.5), 1e-6);
        assertEquals(1.5, Gamma.regularizedGammaQ(0.5, 1.0), 1e-6);
    }

    @Test
    public void testRegularizedGammaQNegativeParameter() {
        assertEquals(1.0, Gamma.regularizedGammaQ(-1.0, 1.0), 1e-10);
        assertEquals(1.0, Gamma.regularizedGammaQ(0.0, 1.0), 1e-10);
        assertEquals(1.0, Gamma.regularizedGammaQ(1.0, 0.0), 1e-10);
    }
}