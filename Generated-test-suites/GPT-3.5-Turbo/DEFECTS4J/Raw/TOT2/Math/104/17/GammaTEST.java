import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class GammaTest {

    @Test
    public void testLogGamma() {
        assertEquals(Math.log(120), Gamma.logGamma(6), 0.0001);
        assertEquals(Math.log(5040), Gamma.logGamma(8), 0.0001);
        assertEquals(Math.log(362880), Gamma.logGamma(10), 0.0001);
    }

    @Test
    public void testRegularizedGammaP() {
        try {
            assertEquals(0.6321205588285577, Gamma.regularizedGammaP(0.5, 1.2), 0.0001);
            assertEquals(0.18126924692201815, Gamma.regularizedGammaP(0.9, 0.4), 0.0001);
            assertEquals(0.0000010030014096938826, Gamma.regularizedGammaP(1.3, 0.001), 0.0000001);
        } catch (Exception e) {
            fail("Exception thrown when testing regularizedGammaP: " + e.getMessage());
        }
    }

    @Test
    public void testRegularizedGammaQ() {
        try {
            assertEquals(0.367879441171, Gamma.regularizedGammaQ(0.5, 1.2), 0.0001);
            assertEquals(0.036787944117, Gamma.regularizedGammaQ(0.9, 0.4), 0.0001);
            assertEquals(0.9999989969985903, Gamma.regularizedGammaQ(1.3, 0.001), 0.0000001);
        } catch (Exception e) {
            fail("Exception thrown when testing regularizedGammaQ: " + e.getMessage());
        }
    }
}