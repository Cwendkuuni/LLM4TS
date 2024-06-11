import static org.junit.Assert.*;
import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        assertEquals(0.0, Gamma.logGamma(1), 1e-15);
        assertEquals(Double.NaN, Gamma.logGamma(0), 0.0);
    }

    @Test
    public void testRegularizedGammaP() {
        assertEquals(1.0, Gamma.regularizedGammaP(1, 0), 1e-15);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(0, 1), 0.0);
        assertEquals(0.5, Gamma.regularizedGammaP(0.5, 0.5), 1e-15);
    }

    @Test
    public void testRegularizedGammaQ() {
        assertEquals(0.0, Gamma.regularizedGammaQ(1, 0), 1e-15);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(0, 1), 0.0);
        assertEquals(0.5, Gamma.regularizedGammaQ(0.5, 0.5), 1e-15);
    }

}