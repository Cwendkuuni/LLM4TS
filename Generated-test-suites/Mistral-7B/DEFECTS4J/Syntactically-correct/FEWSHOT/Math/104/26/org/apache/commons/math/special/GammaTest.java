package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNaN;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.special.Gamma;
import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        assertEquals(0.0, Gamma.logGamma(0.0), 1e-14);
        assertEquals(0.0, Gamma.logGamma(1.0), 1e-14);
        assertEquals(-Math.log(2.0), Gamma.logGamma(0.5), 1e-14);

        assertNaN(Gamma.logGamma(1.5));
        assertNaN(Gamma.logGamma(-0.5));
        assertNaN(Gamma.logGamma(1.5, new MathException()));
    }

    @Test
    public void testRegularizedGammaP() {
        assertEquals(0.0, Gamma.regularizedGammaP(0.0), 1e-14);
        assertEquals(1.0 / Math.sqrt(Math.PI), Gamma.regularizedGammaP(0.5), 1e-14);
        assertEquals(1.0 / Math.E, Gamma.regularizedGammaP(1.0), 1e-14);

        assertNaN(Gamma.regularizedGammaP(-0.5));
        assertNaN(Gamma.regularizedGammaP(1.5));
        assertNaN(Gamma.regularizedGammaP(1.5, new MathException()));

        assertThrows(MathException.class, () -> Gamma.regularizedGammaP(1.0, -1.0));
    }

    @Test
    public void testRegularizedGammaQ() {
        assertEquals(1.0, Gamma.regularizedGammaQ(0.0), 1e-14);
        assertEquals(1.0 - 1.0 / Math.sqrt(Math.PI), Gamma.regularizedGammaQ(0.5), 1e-14);
        assertEquals(1.0 - 1.0 / Math.E, Gamma.regularizedGammaQ(1.0), 1e-14);

        assertNaN(Gamma.regularizedGammaQ(-0.5));
        assertNaN(Gamma.regularizedGammaQ(1.5));
        assertNaN(Gamma.regularizedGammaQ(1.5, new MathException()));

        assertThrows(MathException.class, () -> Gamma.regularizedGammaQ(1.0, -1.0));
    }
}