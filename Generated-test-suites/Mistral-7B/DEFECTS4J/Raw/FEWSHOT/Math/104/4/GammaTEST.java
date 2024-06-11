package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        assertEquals(0.0, Gamma.logGamma(0.0), 1e-10);
        assertEquals(0.0, Gamma.logGamma(1.0), 1e-10);
        assertEquals(-0.03535533905965562, Gamma.logGamma(1.5), 1e-10);
        assertEquals(1.202056903159594, Gamma.logGamma(2.0), 1e-10);
        assertEquals(1.702737753487133, Gamma.logGamma(3.0), 1e-10);
        assertEquals(2.078801367360924, Gamma.logGamma(4.0), 1e-10);
        assertEquals(3.012556665754445, Gamma.logGamma(5.0), 1e-10);
        assertEquals(3.624557225125137, Gamma.logGamma(6.0), 1e-10);
        assertEquals(4.201855330493941, Gamma.logGamma(7.0), 1e-10);
        assertEquals(4.771130427519938, Gamma.logGamma(8.0), 1e-10);

        // Test with negative number
        assertNaN(Gamma.logGamma(-1.0));
        assertNaN(Gamma.logGamma(-2.0));

        // Test with large number
        assertEquals(Math.log(1.252960e+64), Gamma.logGamma(1.0e+31), 1e-10);
    }

    @Test(expected = MathException.class)
    public void testLogGammaException() {
        Gamma.logGamma(0.0);
    }

    @Test
    public void testRegularizedGammaP() {
        assertEquals(0.0, Gamma.regularizedGammaP(1.0, 0.5), 1e-10);
        assertEquals(0.1713244923235647, Gamma.regularizedGammaP(1.0, 1.0), 1e-10);
        assertEquals(0.4227843350985654, Gamma.regularizedGammaP(1.0, 2.0), 1e-10);
        assertEquals(0.6621101107228071, Gamma.regularizedGammaP(1.0, 3.0), 1e-10);
        assertEquals(0.8862271555554575, Gamma.regularizedGammaP(1.0, 4.0), 1e-10);
        assertEquals(1.110223023661205, Gamma.regularizedGammaP(1.0, 5.0), 1e-10);
        assertEquals(1.3389530508491355, Gamma.regularizedGammaP(1.0, 6.0), 1e-10);
        assertEquals(1.566668236032737, Gamma.regularizedGammaP(1.0, 7.0), 1e-10);
        assertEquals(1.793122785629239, Gamma.regularizedGammaP(1.0, 8.0), 1e-10);

        // Test with negative number
        assertEquals(Double.NaN, Gamma.regularizedGammaP(-1.0, 1.0), 1e-10);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(-2.0, 1.0), 1e-10);

        // Test with large number
        assertEquals(1.252960e+64, Gamma.regularizedGammaP(1.0e+31, 1.0), 1e-10);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaPException() {
        Gamma.regularizedGammaP(0.0, 0.5);
    }

    @Test
    public void testRegularizedGammaQ() {
        assertEquals(1.0, Gamma.regularizedGammaQ(1.0, 0.5), 1e-10);
        assertEquals(0.828838225982335, Gamma.regularizedGammaQ(1.0, 1.0), 1e-10);
        assertEquals(0.6521451353240486, Gamma.regularizedGammaQ(1.0, 2.0), 1e-10);
        assertEquals(0.514213691329272, Gamma.regularizedGammaQ(1.0, 3.0), 1e-10);
        assertEquals(0.4198860709356082, Gamma.regularizedGammaQ(1.0, 4.0), 1e-10);
        assertEquals(0.346118904316411, Gamma.regularizedGammaQ(1.0, 5.0), 1e-10);
        assertEquals(0.2870362837057675, Gamma.regularizedGammaQ(1.0, 6.0), 1e-10);
        assertEquals(0.2380347161670933, Gamma.regularizedGammaQ(1.0, 7.0), 1e-10);
        assertEquals(0.201893133704222, Gamma.regularizedGammaQ(1.0, 8.0), 1e-10);

        // Test with negative number
        assertEquals(1.0, Gamma.regularizedGammaQ(-1.0, 1.0), 1e-10);

        // Test with non-positive x
        assertEquals(1.0, Gamma.regularizedGammaQ(1.0, 0.0), 1e-10);
        assertEquals(1.0, Gamma.regularizedGammaQ(1.0, -1.0), 1e-10);

        // Test with large number
        assertEquals(1.252960e+64, Gamma.regularizedGammaQ(1.0e+31, 1.0), 1e-10);
    }

    private void assertNaN(double value) {
        assertEquals(Double.NaN, value, 1e-10);
    }
}