import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GammaTest {

    @Test
    public void testLogGamma() {
        assertEquals(Double.NaN, Gamma.logGamma(Double.NaN), 0);
        assertEquals(Double.NaN, Gamma.logGamma(0), 0);
        assertEquals(Double.POSITIVE_INFINITY, Gamma.logGamma(-1), 0);
    }

    @Test
    public void testRegularizedGammaP() {
        assertEquals(Double.NaN, Gamma.regularizedGammaP(Double.NaN, 1), 0);
        assertEquals(0.0, Gamma.regularizedGammaP(0, 1), 0);
        assertEquals(1.0, Gamma.regularizedGammaP(1, 0), 0);
    }

    @Test
    public void testRegularizedGammaQ() {
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(Double.NaN, 1), 0);
        assertEquals(1.0, Gamma.regularizedGammaQ(0, 1), 0);
        assertEquals(0.0, Gamma.regularizedGammaQ(1, 0), 0);
    }
}