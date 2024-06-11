package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNaN;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        assertEquals(0.0, Gamma.logGamma(0.0), 1e-10);
        assertEquals(Math.log(1.0), Gamma.logGamma(1.0), 1e-10);
        assertEquals(Math.log(2.0), Gamma.logGamma(2.0), 1e-10);
        assertEquals(Math.log(7.0 / 6.0), Gamma.logGamma(7.0 / 6.0), 1e-10);
        assertEquals(Math.log(1.5), Gamma.logGamma(1.5), 1e-10);

        assertNaN(Gamma.logGamma(Double.NaN));
        assertNaN(Gamma.logGamma(-1.0));
        assertNaN(Gamma.logGamma(0.0));
    }

    @Test(expected = MathException.class)
    public void testLogGammaException() {
        Gamma.logGamma(-1.5);
    }

    @Test
    public void testRegularizedGammaP() {
        assertEquals(0.5, Gamma.regularizedGammaP(0.5, 1.0), 1e-10);
        assertEquals(0.3626963555263967, Gamma.regularizedGammaP(1.2, 1.5), 1e-10);
        assertEquals(0.822105926113051, Gamma.regularizedGammaP(1.5, 2.5), 1e-10);
        assertEquals(1.0, Gamma.regularizedGammaP(2.0, 2.0), 1e-10);
        assertEquals(0.6872857356325132, Gamma.regularizedGammaP(2.5, 3.5), 1e-10);

        assertEquals(0.6666666666666665, Gamma.regularizedGammaP(1.5, 1.5), 1e-10);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaPException() {
        Gamma.regularizedGammaP(-1.0, 1.0);
        Gamma.regularizedGammaP(1.0, -1.0);
        Gamma.regularizedGammaP(0.0, 1.0);
    }

    @Test
    public void testRegularizedGammaQ() {
        assertEquals(0.5, 1.0 - Gamma.regularizedGammaP(0.5, 1.0), 1e-10);
        assertEquals(0.6373036445083223, 1.0 - Gamma.regularizedGammaP(1.2, 1.5), 1e-10);
        assertEquals(0.1778940738869488, 1.0 - Gamma.regularizedGammaP(1.5, 2.5), 1e-10);
        assertEquals(0.31271881825744084, 1.0 - Gamma.regularizedGammaP(2.0, 2.0), 1e-10);
        assertEquals(0.3126744345252947, 1.0 - Gamma.regularizedGammaP(2.5, 3.5), 1e-10);

        assertEquals(1.0, 1.0 - Gamma.regularizedGammaP(1.5, 1.5), 1e-10);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaQException() {
        Gamma.regularizedGammaQ(-1.0, 1.0);
        Gamma.regularizedGammaQ(1.0, -1.0);
        Gamma.regularizedGammaQ(0.0, 1.0);
    }

    @Test
    public void testRegularizedGammaQMaxIterations() {
        try {
            Gamma.regularizedGammaQ(1.5, 3.0, 1e-9, 10);
        } catch (MaxIterationsExceededException e) {
            // expected
        }
    }
}