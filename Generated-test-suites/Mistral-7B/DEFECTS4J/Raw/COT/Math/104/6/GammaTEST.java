import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNaN;

import org.apache.commons.math3.special.Gamma;
import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        // Test valid input with expected results
        assertEquals(0.0, Gamma.logGamma(0.0), 0.0);
        assertEquals(0.0, Gamma.logGamma(1.0), 0.0);
        assertEquals(Math.log(1.072632679489661923), Gamma.logGamma(2.0), 1e-12);
        assertEquals(Math.log(13.08913329236118523), Gamma.logGamma(3.0), 1e-12);
        assertEquals(Math.log(1031.926111661726206), Gamma.logGamma(5.0), 1e-12);

        // Test edge cases and corner cases
        assertEquals(Double.POSITIVE_INFINITY, Gamma.logGamma(1.0 / 0.0), 0.0);
        assertEquals(Double.NEGATIVE_INFINITY, Gamma.logGamma(-1.0), 0.0);
        assertEquals(Double.NEGATIVE_INFINITY, Gamma.logGamma(0.0), 0.0);
        assertEquals(Double.NaN, Gamma.logGamma(1.0 / 2.0), 0.0);
    }

    @Test(expected = MathException.class)
    public void testLogGammaException() {
        Gamma.logGamma(-1.5);
    }

    @Test
    public void testRegularizedGammaP() {
        // Test valid input with expected results
        assertEquals(1.0 / Math.sqrt(Math.PI), Gamma.regularizedGammaP(0.5, 0.5), 1e-12);
        assertEquals(0.66419675825170762, Gamma.regularizedGammaP(2.5, 2.0), 1e-12);
        assertEquals(0.9999999999999999987, Gamma.regularizedGammaP(20.0, 20.0), 1e-12);

        // Test edge cases and corner cases
        assertEquals(1.0, Gamma.regularizedGammaP(1.0, 1.0), 1e-12);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(0.0, 1.0), 0.0);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(1.0, 0.0), 0.0);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(1.0, -1.0), 0.0);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(-1.0, 1.0), 0.0);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(1.0, Double.POSITIVE_INFINITY), 0.0);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(1.0, Double.NEGATIVE_INFINITY), 0.0);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaPException() {
        Gamma.regularizedGammaP(-1.5, 1.0);
    }

    @Test
    public void testRegularizedGammaQ() {
        // Test valid input with expected results
        assertEquals(1.0 - 1.0 / Math.sqrt(Math.PI), Gamma.regularizedGammaQ(0.5, 0.5), 1e-12);
        assertEquals(1.0 - 0.66419675825170762, Gamma.regularizedGammaQ(2.5, 2.0), 1e-12);
        assertEquals(1.0 - 0.9999999999999999987, Gamma.regularizedGammaQ(20.0, 20.0), 1e-12);

        // Test edge cases and corner cases
        assertEquals(0.0, Gamma.regularizedGammaQ(1.0, 1.0), 1e-12);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(0.0, 1.0), 0.0);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(1.0, 0.0), 0.0);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(1.0, -1.0), 0.0);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(-1.0, 1.0), 0.0);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(1.0, Double.POSITIVE_INFINITY), 0.0);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(1.0, Double.NEGATIVE_INFINITY), 0.0);
    }
}