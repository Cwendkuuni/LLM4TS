import org.junit.Test;
import static org.junit.Assert.*;

public class GammaTest {

    @Test
    public void testLogGamma() {
        // Typical use case
        assertEquals(Math.log(1), Gamma.logGamma(1), 0.0001);
        
        // Edge cases
        assertEquals(Double.NaN, Gamma.logGamma(0), 0.0001);
        assertEquals(Double.NaN, Gamma.logGamma(-1), 0.0001);
        
        // Error scenarios
        assertEquals(Double.NaN, Gamma.logGamma(Double.NaN), 0.0001);
    }

    @Test
    public void testRegularizedGammaP() throws MathException {
        // Typical use case
        assertEquals(1.0, Gamma.regularizedGammaP(2, 5), 0.0001);
        
        // Edge cases
        assertEquals(1.0, Gamma.regularizedGammaP(1, 0), 0.0001);
        assertEquals(0.0, Gamma.regularizedGammaP(0.5, Double.POSITIVE_INFINITY), 0.0001);
        
        // Error scenarios
        try {
            Gamma.regularizedGammaP(-1, 5);
            fail("Expected MathException to be thrown");
        } catch (MathException e) {
            // Expected
        }
        assertEquals(Double.NaN, Gamma.regularizedGammaP(Double.NaN, 5), 0.0001);
    }

    @Test
    public void testRegularizedGammaQ() throws MathException {
        // Typical use case
        assertEquals(0.0, Gamma.regularizedGammaQ(2, 5), 0.0001);
        
        // Edge cases
        assertEquals(0.0, Gamma.regularizedGammaQ(1, 0), 0.0001);
        assertEquals(1.0, Gamma.regularizedGammaQ(0.5, Double.POSITIVE_INFINITY), 0.0001);
        
        // Error scenarios
        try {
            Gamma.regularizedGammaQ(-1, 5);
            fail("Expected MathException to be thrown");
        } catch (MathException e) {
            // Expected
        }
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(Double.NaN, 5), 0.0001);
    }
}