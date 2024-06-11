import static org.junit.Assert.*;
import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        assertEquals(Double.NEGATIVE_INFINITY, Gamma.logGamma(0.0), 0.0001);
        assertEquals(0.0, Gamma.logGamma(1.0), 0.0001);
        assertEquals(0.6931, Gamma.logGamma(2.0), 0.0001);
        assertEquals(1.7917, Gamma.logGamma(4.0), 0.0001);
    }

    @Test
    public void testRegularizedGammaP() {
        try {
            assertEquals(0.0, Gamma.regularizedGammaP(0.0, 1.0), 0.0001);
            assertEquals(0.5, Gamma.regularizedGammaP(0.5, 1.0), 0.0001);
            assertEquals(1.0, Gamma.regularizedGammaP(1.0, 1.0), 0.0001);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testRegularizedGammaQ() {
        try {
            assertEquals(1.0, Gamma.regularizedGammaQ(0.0, 1.0), 0.0001);
            assertEquals(0.5, Gamma.regularizedGammaQ(0.5, 1.0), 0.0001);
            assertEquals(0.0, Gamma.regularizedGammaQ(1.0, 1.0), 0.0001);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

}