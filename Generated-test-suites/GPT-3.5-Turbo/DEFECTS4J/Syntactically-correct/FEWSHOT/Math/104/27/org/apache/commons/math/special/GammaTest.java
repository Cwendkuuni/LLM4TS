import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class GammaTest {
    @Test
    public void testLogGamma() {
        assertEquals(Double.NaN, Gamma.logGamma(Double.NaN), 0.0001);
        assertEquals(Double.NaN, Gamma.logGamma(0.0), 0.0001);
        assertEquals(0.0, Gamma.logGamma(1.0), 0.0001);
        assertEquals(0.6931471805599453, Gamma.logGamma(2.0), 0.0001);
        assertEquals(1.791759469228055, Gamma.logGamma(3.0), 0.0001);
        assertEquals(14.102205602829993, Gamma.logGamma(10.0), 0.0001);
    }

    @Test
    public void testRegularizedGammaP() throws MathException {
        assertEquals(Double.NaN, Gamma.regularizedGammaP(Double.NaN, 0.5), 0.0001);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(0.0, Double.NaN), 0.0001);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(Double.NaN, Double.NaN), 0.0001);
        assertEquals(0.0, Gamma.regularizedGammaP(1.0, 0.0), 0.0001);
        assertEquals(1.0, Gamma.regularizedGammaP(1.0, Double.POSITIVE_INFINITY), 0.0001);
        assertEquals(0.059979074437227484, Gamma.regularizedGammaP(2.0, 0.5), 0.0001);
        assertEquals(0.9970113303937629, Gamma.regularizedGammaP(2.0, 10.0), 0.0001);
    }

    @Test
    public void testRegularizedGammaQ() throws MathException {
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(Double.NaN, 0.5), 0.0001);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(0.0, Double.NaN), 0.0001);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(Double.NaN, Double.NaN), 0.0001);
        assertEquals(1.0, Gamma.regularizedGammaQ(1.0, 0.0), 0.0001);
        assertEquals(0.0, Gamma.regularizedGammaQ(1.0, Double.POSITIVE_INFINITY), 0.0001);
        assertEquals(0.9400209255627725, Gamma.regularizedGammaQ(2.0, 0.5), 0.0001);
        assertEquals(0.0029886696062371295, Gamma.regularizedGammaQ(2.0, 10.0), 0.0001);
    }
}