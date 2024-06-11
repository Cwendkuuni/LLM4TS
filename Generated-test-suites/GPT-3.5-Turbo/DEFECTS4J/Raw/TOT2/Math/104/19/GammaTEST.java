import static org.junit.Assert.*;
import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        assertEquals(Double.NaN, Gamma.logGamma(Double.NaN), 0.0001);
        assertEquals(Double.NaN, Gamma.logGamma(0.0), 0.0001);
        assertEquals(0.0, Gamma.logGamma(1.0), 0.0001);
        // Add more test cases here
    }

    @Test
    public void testRegularizedGammaP() {
        try {
            assertEquals(Double.NaN, Gamma.regularizedGammaP(Double.NaN, 2.0), 0.0001);
            assertEquals(Double.NaN, Gamma.regularizedGammaP(1.0, Double.NaN), 0.0001);
            assertEquals(Double.NaN, Gamma.regularizedGammaP(-1.0, 2.0), 0.0001);
            // Add more test cases here
        } catch (MathException e) {
            fail("MathException thrown");
        }
    }

    @Test
    public void testRegularizedGammaQ() {
        try {
            assertEquals(Double.NaN, Gamma.regularizedGammaQ(Double.NaN, 2.0), 0.0001);
            assertEquals(Double.NaN, Gamma.regularizedGammaQ(1.0, Double.NaN), 0.0001);
            assertEquals(Double.NaN, Gamma.regularizedGammaQ(-1.0, 2.0), 0.0001);
            // Add more test cases here
        } catch (MathException e) {
            fail("MathException thrown");
        }
    }

}