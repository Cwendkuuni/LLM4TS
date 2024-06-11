import static org.junit.Assert.*;
import org.junit.Test;

public class GammaTest {
  
    @Test
    public void testLogGamma() {
        // Typical use case
        assertEquals(0.0, Gamma.logGamma(1), 0.0001);
        
        // Edge case
        assertEquals(Double.NaN, Gamma.logGamma(0), 0.0001);
        
        // Error scenario
        assertEquals(Double.NaN, Gamma.logGamma(-1), 0.0001);
    }

    @Test
    public void testRegularizedGammaP() {
        // Typical use case
        assertEquals(0.84375, Gamma.regularizedGammaP(2, 4), 0.0001);
        
        // Edge case
        assertEquals(Double.NaN, Gamma.regularizedGammaP(-1, 2), 0.0001);
        
        // Error scenario
        try {
            Gamma.regularizedGammaP(1, -2);
            fail("Expected MathException");
        } catch (MathException e) {
            // Test passed
        }
    }

    @Test
    public void testRegularizedGammaQ() {
        // Typical use case
        assertEquals(0.5838, Gamma.regularizedGammaQ(2, 4), 0.0001);
        
        // Edge case
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(-1, 2), 0.0001);
        
        // Error scenario
        try {
            Gamma.regularizedGammaQ(1, -2);
            fail("Expected MathException");
        } catch (MathException e) {
            // Test passed
        }
    }
  
}