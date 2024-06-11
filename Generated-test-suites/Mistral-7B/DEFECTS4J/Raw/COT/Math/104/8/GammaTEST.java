package org.apache.commons.math.special;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNaN;

import org.apache.commons.math3.special.Gamma;
import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        // Test valid inputs
        assertEquals(0.0, Gamma.logGamma(0.0), 1e-9);
        assertEquals(Math.log(1.0), Gamma.logGamma(1.0), 1e-9);
        assertEquals(Math.log(2.0) + Math.log(Math.PI) - Math.log(2.0 / Math.SQRT12),
                Gamma.logGamma(1.5), 1e-9);
        assertEquals(Math.log(Math.PI) + Math.log(Math.SQRT18) - Math.log(Math.SQRT2),
                Gamma.logGamma(0.5), 1e-9);

        // Test invalid inputs
        assertNaN(Gamma.logGamma(-1.0));
        assertNaN(Gamma.logGamma(0.0));
        assertNaN(Gamma.logGamma(1.0, 0.0));
    }

    @Test(expected = org.apache.commons.math3.exception.MaxIterationsExceededException.class)
    public void testRegularizedGammaPMaxIterations() throws Exception {
        Gamma.regularizedGammaP(0.5, 1.0, 1e-16, 10);
    }

    @Test
    public void testRegularizedGammaPValid() throws Exception {
        assertEquals(1.04251968501936885357, Gamma.regularizedGammaP(0.5, 1.5), 1e-6);
        assertEquals(0.95538484605341953431, Gamma.regularizedGammaP(1.5, 2.5), 1e-6);
        assertEquals(0.57724993880027323077, Gamma.regularizedGammaP(1.0, 1.0), 1e-6);
        assertEquals(0.55124711364190424822, Gamma.regularizedGammaP(2.0, 1.0), 1e-6);
    }

    @Test
    public void testRegularizedGammaQValid() throws Exception {
        assertEquals(0.42131535423313254289, Gamma.regularizedGammaQ(0.5, 1.5), 1e-6);
        assertEquals(0.04468657529012894339, Gamma.regularizedGammaQ(1.5, 2.5), 1e-6);
        assertEquals(0.42261837373655683775, Gamma.regularizedGammaQ(1.0, 1.0), 1e-6);
        assertEquals(0.44978337035583423839, Gamma.regularizedGammaQ(2.0, 1.0), 1e-6);
    }

    @Test
    public void testRegularizedGammaInvalid() throws Exception {
        assertNaN(Gamma.regularizedGammaP(-1.0, 1.0));
        assertNaN(Gamma.regularizedGammaP(0.0, 1.0));
        assertNaN(Gamma.regularizedGammaP(1.0, 0.0));

        assertNaN(Gamma.regularizedGammaQ(-1.0, 1.0));
        assertNaN(Gamma.regularizedGammaQ(0.0, 1.0));
        assertNaN(Gamma.regularizedGammaQ(1.0, 0.0));
    }
}