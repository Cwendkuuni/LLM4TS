import org.junit.Test;
import static org.junit.Assert.*;

public class GammaTest {

    @Test
    public void testLogGamma() {
        assertEquals(Double.NaN, Gamma.logGamma(-1), 0.001);
        assertEquals(Double.NaN, Gamma.logGamma(0), 0.001);
        assertEquals(0.0, Gamma.logGamma(1), 0.001);
        assertEquals(0.693, Gamma.logGamma(2), 0.001);
    }

    @Test
    public void testRegularizedGammaP() {
        assertEquals(1.0, Gamma.regularizedGammaP(2, 0), 0.001);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(-1, 4), 0.001);
        assertEquals(0.0, Gamma.regularizedGammaP(1, 0), 0.001);
    }

    @Test
    public void testRegularizedGammaQ() {
        assertEquals(1.0, Gamma.regularizedGammaQ(1, 0), 0.001);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(-1, 4), 0.001);
        assertEquals(0.0, Gamma.regularizedGammaQ(2, 0), 0.001);
    }

}