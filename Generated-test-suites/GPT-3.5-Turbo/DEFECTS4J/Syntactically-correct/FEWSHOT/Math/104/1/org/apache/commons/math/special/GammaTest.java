import org.junit.Test;
import static org.junit.Assert.*;

public class GammaTest {
    @Test
    public void testLogGamma() {
        assertEquals(Double.NaN, Gamma.logGamma(Double.NaN), 0.000001);
        assertEquals(Double.NaN, Gamma.logGamma(0.0), 0.000001);
        assertEquals(Double.POSITIVE_INFINITY, Gamma.logGamma(Double.POSITIVE_INFINITY), 0.000001);
        assertEquals(0.5723649429247001, Gamma.logGamma(2.0), 0.000001);
        assertEquals(1.7847299162293293, Gamma.logGamma(3.0), 0.000001);
    }

    @Test
    public void testRegularizedGammaP() {
        assertEquals(Double.NaN, Gamma.regularizedGammaP(Double.NaN, 1.0), 0.000001);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(1.0, Double.NaN), 0.000001);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(-1.0, 1.0), 0.000001);
        assertEquals(0.36787944117144233, Gamma.regularizedGammaP(1.0, 1.0), 0.000001);
        assertEquals(0.18126924692201818, Gamma.regularizedGammaP(2.0, 1.0), 0.000001);
    }

    @Test
    public void testRegularizedGammaQ() {
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(Double.NaN, 1.0), 0.000001);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(1.0, Double.NaN), 0.000001);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(-1.0, 1.0), 0.000001);
        assertEquals(0.6321205588285577, Gamma.regularizedGammaQ(1.0, 1.0), 0.000001);
        assertEquals(0.8187307530779818, Gamma.regularizedGammaQ(2.0, 1.0), 0.000001);
    }
}