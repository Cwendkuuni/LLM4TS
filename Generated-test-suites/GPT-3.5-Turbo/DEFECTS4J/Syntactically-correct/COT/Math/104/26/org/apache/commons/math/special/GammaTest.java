
import org.junit.Test;
import static org.junit.Assert.*;

public class GammaTest {
    
    @Test
    public void testLogGamma() {
        assertEquals(Gamma.logGamma(1.0), 0.0, 0.00001);
        assertEquals(Gamma.logGamma(2.0), 0.0, 0.00001);
        assertEquals(Gamma.logGamma(5.0), Math.log(24), 0.00001);
        assertEquals(Gamma.logGamma(10.0), Math.log(362880), 0.00001);
        assertEquals(Gamma.logGamma(0.5), Math.log(Math.sqrt(Math.PI)), 0.00001);
        // potential edge case: negative input
        assertEquals(Gamma.logGamma(-1.0), Double.NaN, 0.00001);
    }
    
    @Test
    public void testRegularizedGammaP() throws MathException {
        assertEquals(Gamma.regularizedGammaP(1.0, 1.0), 0.63212, 0.00001);
        assertEquals(Gamma.regularizedGammaP(2.0, 3.0), 0.59399, 0.00001);
        assertEquals(Gamma.regularizedGammaP(5.0, 10.0), 0.97562, 0.00001);
        assertEquals(Gamma.regularizedGammaP(0.5, 0.5), Double.NaN, 0.00001);
        // potential edge case: a = 0
        assertEquals(Gamma.regularizedGammaP(0.0, 1.0), Double.NaN, 0.00001);
        // potential edge case: x = 0
        assertEquals(Gamma.regularizedGammaP(1.0, 0.0), 0.0, 0.00001);
    }
    
    @Test
    public void testRegularizedGammaQ() throws MathException {
        assertEquals(Gamma.regularizedGammaQ(1.0, 1.0), 0.36787, 0.00001);
        assertEquals(Gamma.regularizedGammaQ(2.0, 3.0), 0.40600, 0.00001);
        assertEquals(Gamma.regularizedGammaQ(5.0, 10.0), 0.02438, 0.00001);
        assertEquals(Gamma.regularizedGammaQ(0.5, 0.5), Double.NaN, 0.00001);
        // potential edge case: a < 1
        assertEquals(Gamma.regularizedGammaQ(0.75, 2.0), 0.63856, 0.00001);
        // potential edge case: x < a
        assertEquals(Gamma.regularizedGammaQ(2.0, 1.0), 0.26300, 0.00001);
        // potential edge case: x = 0
        assertEquals(Gamma.regularizedGammaQ(1.0, 0.0), 1.0, 0.00001);
    }
}
