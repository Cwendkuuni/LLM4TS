import static org.junit.Assert.*;
import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        assertEquals(0.0, Gamma.logGamma(1.0), 1e-10);
        assertEquals(Double.NaN, Gamma.logGamma(0.0), 0.0);
        assertEquals(0.0, Gamma.logGamma(1.0), 1e-10);
    }

    @Test
    public void testRegularizedGammaP() {
        assertEquals(1.0, Gamma.regularizedGammaP(1.0, 0.0), 0.0);
        assertEquals(0.0, Gamma.regularizedGammaP(1.0, Double.POSITIVE_INFINITY), 0.0);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(0.0, 0.0), 0.0);
        assertEquals(2.0 / 3.0, Gamma.regularizedGammaP(2.0, 1.0), 1e-10);
    }

    @Test
    public void testRegularizedGammaQ() {
        assertEquals(1.0, Gamma.regularizedGammaQ(1.0, 0.0), 0.0);
        assertEquals(1.0, Gamma.regularizedGammaQ(1.0, Double.POSITIVE_INFINITY), 0.0);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(0.0, 0.0), 0.0);
        assertEquals(1.0 / 3.0, Gamma.regularizedGammaQ(2.0, 1.0), 1e-10);
    }
}