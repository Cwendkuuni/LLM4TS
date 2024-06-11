import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        assertEquals(Double.NaN, Gamma.logGamma(Double.NaN), 0.0);
        assertEquals(Double.NaN, Gamma.logGamma(0.0), 0.0);
        assertEquals(-0.6931471805599453, Gamma.logGamma(1.0), 1e-15);
        assertEquals(0.0, Gamma.logGamma(2.0), 1e-15);
        assertEquals(0.6931471805599453, Gamma.logGamma(3.0), 1e-15);
        assertEquals(1.791759469228055, Gamma.logGamma(4.0), 1e-15);
    }

    @Test
    public void testRegularizedGammaP() {
        assertEquals(Double.NaN, Gamma.regularizedGammaP(Double.NaN, 1.0), 0.0);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(1.0, Double.NaN), 0.0);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(Double.NaN, Double.NaN), 0.0);
        assertEquals(0.0, Gamma.regularizedGammaP(0.0, 1.0), 0.0);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(1.0, 0.0), 0.0);
        assertEquals(0.6321205588285577, Gamma.regularizedGammaP(1.0, 1.0), 1e-15);
        assertEquals(0.8646647167633873, Gamma.regularizedGammaP(2.0, 2.0), 1e-15);
        assertEquals(0.45058404864769147, Gamma.regularizedGammaP(2.0, 1.0), 1e-15);
    }

    @Test
    public void testRegularizedGammaQ() {
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(Double.NaN, 1.0), 0.0);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(1.0, Double.NaN), 0.0);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(Double.NaN, Double.NaN), 0.0);
        assertEquals(1.0, Gamma.regularizedGammaQ(0.0, 1.0), 0.0);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(1.0, 0.0), 0.0);
        assertEquals(0.36787944117144233, Gamma.regularizedGammaQ(1.0, 1.0), 1e-15);
        assertEquals(0.1353352832366127, Gamma.regularizedGammaQ(2.0, 2.0), 1e-15);
        assertEquals(0.5494159513523085, Gamma.regularizedGammaQ(2.0, 1.0), 1e-15);
    }
}