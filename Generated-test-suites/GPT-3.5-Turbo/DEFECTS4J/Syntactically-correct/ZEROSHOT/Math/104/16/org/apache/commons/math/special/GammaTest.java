import static org.junit.Assert.*;
import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        assertEquals(0.832, Gamma.logGamma(2), 0.001);
        assertEquals(4.787, Gamma.logGamma(7.5), 0.001);
        assertEquals(Double.NaN, Gamma.logGamma(-5), 0.001);
        assertEquals(Double.NaN, Gamma.logGamma(0), 0.001);
    }
    
    @Test
    public void testRegularizedGammaP() {
        assertEquals(0.75, Gamma.regularizedGammaP(2, 1), 0.001);
        assertEquals(0.120, Gamma.regularizedGammaP(7, 3), 0.001);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(-5, 4), 0.001);
        assertEquals(0, Gamma.regularizedGammaP(0, 2), 0.001);
    }
    
    @Test
    public void testRegularizedGammaQ() {
        assertEquals(0.25, Gamma.regularizedGammaQ(2, 1), 0.001);
        assertEquals(0.879, Gamma.regularizedGammaQ(7, 3), 0.001);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(-5, 4), 0.001);
        assertEquals(1, Gamma.regularizedGammaQ(0, 2), 0.001);
    }
}