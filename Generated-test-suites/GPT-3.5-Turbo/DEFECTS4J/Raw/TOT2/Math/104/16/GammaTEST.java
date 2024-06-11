import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GammaTest {

    @Test
    public void testLogGamma() {
        assertEquals(Double.NaN, Gamma.logGamma(Double.NaN), 0.0001);
        assertEquals(Double.NaN, Gamma.logGamma(0.0), 0.0001);
        assertEquals(363.73937555556397, Gamma.logGamma(10.0), 0.0001);
    }

    @Test
    public void testRegularizedGammaP() {
        assertEquals(0.0, Gamma.regularizedGammaP(1.0, 0.0), 0.0001);
        assertEquals(1.0, Gamma.regularizedGammaP(1.0, 1.0), 0.0001);
    }

    @Test
    public void testRegularizedGammaQ() {
        assertEquals(1.0, Gamma.regularizedGammaQ(1.0, 0.0), 0.0001);
        assertEquals(0.0, Gamma.regularizedGammaQ(1.0, 1.0), 0.0001);
    }

}