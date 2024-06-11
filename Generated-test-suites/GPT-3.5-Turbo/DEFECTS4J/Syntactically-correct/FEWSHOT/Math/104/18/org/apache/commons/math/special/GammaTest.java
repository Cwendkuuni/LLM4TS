import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class GammaTest {
    @Test
    public void testLogGamma() {
        assertEquals(Double.NaN, Gamma.logGamma(Double.NaN), 0);
        assertEquals(Double.NaN, Gamma.logGamma(0), 0);
        assertEquals(0.5723649429247001, Gamma.logGamma(0.5), 0.0001);
        assertEquals(0.0, Gamma.logGamma(1), 0);
        assertEquals(0.0, Gamma.logGamma(2), 0);
        assertEquals(0.6931471805599453, Gamma.logGamma(3.5), 0.0001);
    }

    @Test
    public void testRegularizedGammaP() {
        assertEquals(Double.NaN, Gamma.regularizedGammaP(Double.NaN, 1), 0);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(1, Double.NaN), 0);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(0, 1), 0);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(1, 0), 0);
        
        assertEquals(1.0, Gamma.regularizedGammaP(1, 0), 0);
        assertEquals(0.0, Gamma.regularizedGammaP(1, Double.POSITIVE_INFINITY), 0);
        assertEquals(0.0, Gamma.regularizedGammaP(1, Double.NEGATIVE_INFINITY), 0);
        assertEquals(0.9899030744511208, Gamma.regularizedGammaP(1, 0.5), 0.0001);
        assertEquals(0.9999546000702375, Gamma.regularizedGammaP(1, 1), 0.0001);
    }

    @Test
    public void testRegularizedGammaQ() {
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(Double.NaN, 1), 0);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(1, Double.NaN), 0);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(0, 1), 0);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(1, 0), 0);
        
        assertEquals(0.0, Gamma.regularizedGammaQ(1, 0), 0);
        assertEquals(1.0, Gamma.regularizedGammaQ(1, Double.POSITIVE_INFINITY), 0);
        assertEquals(1.0, Gamma.regularizedGammaQ(1, Double.NEGATIVE_INFINITY), 0);
        assertEquals(0.010096925548879183, Gamma.regularizedGammaQ(1, 0.5), 0.0001);
        assertEquals(4.539993096548187E-5, Gamma.regularizedGammaQ(1, 1), 0.0001);
    }
}