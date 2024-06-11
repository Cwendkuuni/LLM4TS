package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        assertEquals(0.0, Gamma.logGamma(0.0), 1e-10);
        assertEquals(Math.log(1.0), Gamma.logGamma(1.0), 1e-10);
        assertEquals(Math.log(2.0), Gamma.logGamma(2.0), 1e-10);
        assertEquals(-Math.log(2.0) + Math.log(Math.PI), Gamma.logGamma(1.5), 1e-6);
        assertEquals(-Math.log(2.0) + Math.log(Math.PI) + Math.log(2.0), Gamma.logGamma(2.0), 1e-6);

        // Test for negative inputs
        assertNaN(Gamma.logGamma(-0.5));
        assertNaN(Gamma.logGamma(-1.0));
        assertNaN(Gamma.logGamma(Double.NEGATIVE_INFINITY));

        // Test for very large inputs
        assertEquals(Gamma.logGamma(100.0), Double.POSITIVE_INFINITY, 1e-10);
        assertEquals(Gamma.logGamma(Double.POSITIVE_INFINITY), Double.POSITIVE_INFINITY, 1e-10);
    }

    @Test
    public void testRegularizedGammaP() throws MathException {
        assertEquals(1.0, Gamma.regularizedGammaP(0.5, 0.5), 1e-10);
        assertEquals(1.0 / Math.sqrt(Math.PI), Gamma.regularizedGammaP(0.5, 1.0), 1e-6);
        assertEquals(Math.E / (Math.sqrt(Math.PI) * 2.0), Gamma.regularizedGammaP(1.0, 1.5), 1e-6);
        assertEquals(1.0 - Math.E / (Math.sqrt(Math.PI) * 2.0), Gamma.regularizedGammaP(1.0, 2.0), 1e-6);
        assertEquals(1.0, Gamma.regularizedGammaP(2.0, 2.0), 1e-10);

        // Test for negative inputs
        assertNaN(Gamma.regularizedGammaP(-0.5, 0.5));
        assertNaN(Gamma.regularizedGammaP(-0.5, 1.5));
        assertNaN(Gamma.regularizedGammaP(-0.5, Double.POSITIVE_INFINITY));
        assertNaN(Gamma.regularizedGammaP(0.5, Double.NEGATIVE_INFINITY));
        assertNaN(Gamma.regularizedGammaP(0.5, -1.5));

        // Test for very large inputs
        assertEquals(1.0, Gamma.regularizedGammaP(100.0, 100.0), 1e-10);
        assertNaN(Gamma.regularizedGammaP(100.0, 101.0));

        // Test for very small x
        assertEquals(1.0, Gamma.regularizedGammaP(0.0, 0.0), 1e-10);
    }

    @Test
    public void testRegularizedGammaQ() throws MathException {
        assertEquals(0.0, Gamma.regularizedGammaQ(0.5, 0.5), 1e-10);
        assertEquals(1.0 - 1.0 / (Math.sqrt(Math.PI) * 2.0), Gamma.regularizedGammaQ(0.5, 1.0), 1e-6);
        assertEquals(1.0 - Math.E / (Math.sqrt(Math.PI) * 2.0), Gamma.regularizedGammaQ(1.0, 1.5), 1e-6);
        assertEquals(1.0 - 1.0, Gamma.regularizedGammaQ(1.0, 2.0), 1e-10);
        assertEquals(0.0, Gamma.regularizedGammaQ(2.0, 2.0), 1e-10);

        // Test for negative inputs
        assertNaN(Gamma.regularizedGammaQ(-0.5, 0.5));
        assertNaN(Gamma.regularizedGammaQ(-0.5, 1.5));
        assertNaN(Gamma.regularizedGammaQ(-0.5, Double.POSITIVE_INFINITY));
        assertNaN(Gamma.regularizedGammaQ(0.5, Double.NEGATIVE_INFINITY));
        assertNaN(Gamma.regularizedGammaQ(0.5, -1.5));

        // Test for very large inputs
        assertEquals(0.0, Gamma.regularizedGammaQ(100.0, 100.0), 1e-10);
        assertNaN(Gamma.regularizedGammaQ(100.0, 101.0));

        // Test for very small x
        assertEquals(1.0, Gamma.regularizedGammaQ(0.0, 0.0), 1e-10);
    }

    @Test
    public void testRegularizedGammaWithMaxIterations() throws MathException, InterruptedException {
        assertEquals(1.0, Gamma.regularizedGammaP(0.5, 0.5, 1e-30, 1000000), 1e-10);
        assertEquals(1.0 / Math.sqrt(Math.PI), Gamma.regularizedGammaP(0.5, 1.0, 1e-12, 1000000), 1e-6);
        assertEquals(Math.E / (Math.sqrt(Math.PI) * 2.0), Gamma.regularizedGammaP(1.0, 1.5, 1e-12, 1000000), 1e-6);
        assertEquals(1.0 - Math.E / (Math.sqrt(Math.PI) * 2.0), Gamma.regularizedGammaP(1.0, 2.0, 1e-12, 1000000), 1e-6);
        assertEquals(1.0, Gamma.regularizedGammaP(2.0, 2.0, 1e-10, 1000000), 1e-10);

        // Test for very large inputs
        assertEquals(1.0, Gamma.regularizedGammaP(100.0, 100.0, 1e-10, 1000000), 1e-10);
        assertThrows(MaxIterationsExceededException.class, () -> Gamma.regularizedGammaP(100.0, 101.0, 1e-12, 1000));

        // Test for very small x
        assertEquals(1.0, Gamma.regularizedGammaP(0.0, 0.0, 1e-10, 1000000), 1e-10);

        assertEquals(0.0, Gamma.regularizedGammaQ(0.5, 0.5, 1e-30, 1000000), 1e-10);
        assertEquals(1.0 - 1.0 / (Math.sqrt(Math.PI) * 2.0), Gamma.regularizedGammaQ(0.5, 1.0, 1e-12, 1000000), 1e-6);
        assertEquals(1.0 - Math.E / (Math.sqrt(Math.PI) * 2.0), Gamma.regularizedGammaQ(1.0, 1.5, 1e-12, 1000000), 1e-6);
        assertEquals(1.0 - 1.0, Gamma.regularizedGammaQ(1.0, 2.0, 1e-12, 1000000), 1e-10);
        assertEquals(0.0, Gamma.regularizedGammaQ(2.0, 2.0, 1e-10, 1000000), 1e-10);

        // Test for very large inputs
        assertEquals(0.0, Gamma.regularizedGammaQ(100.0, 100.0, 1e-10, 1000000), 1e-10);
        assertThrows(MaxIterationsExceededException.class, () -> Gamma.regularizedGammaQ(100.0, 101.0, 1e-12, 1000));

        // Test for very small x
        assertEquals(1.0, Gamma.regularizedGammaQ(0.0, 0.0, 1e-10, 1000000), 1e-10);
    }

    private void assertNaN(double result) {
        assertNotNull(result);
        assertEquals(Double.NaN, result, 0.0);
    }

    private void assertThrows(Class<? extends Throwable> expected, Runnable runnable) {
        try {
            runnable.run();
            fail();
        } catch (Throwable e) {
            if (expected.isInstance(e)) {
                return;
            }
            throw new AssertionError("Unexpected exception " + e.getClass());
        }
    }

    private void assertNotNull(Object obj) {
        if (obj == null) {
            fail("Expected object not null");
        }
    }
}