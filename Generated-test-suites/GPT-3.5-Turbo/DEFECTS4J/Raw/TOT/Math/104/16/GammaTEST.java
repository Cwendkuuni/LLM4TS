import org.junit.Test;
import static org.junit.Assert.*;

public class GammaTest {

    @Test
    public void testLogGamma() {
        // Typical use case
        assertEquals(0, Double.compare(0.5723649429247001, Gamma.logGamma(4.2)));
        
        // Edge case
        assertEquals(Double.POSITIVE_INFINITY, Gamma.logGamma(Double.POSITIVE_INFINITY), 0.0);
        
        // Error scenario
        assertEquals(Double.NaN, Gamma.logGamma(Double.NaN), 0.0);
    }
    
    @Test
    public void testRegularizedGammaP() throws MathException {
        // Typical use case
        assertEquals(0, Double.compare(0.18126924692201817, Gamma.regularizedGammaP(0.5, 1.2)));
        
        // Edge case
        assertEquals(1.0, Gamma.regularizedGammaP(1.0, 0.0), 0.0);
        
        // Error scenario
        try {
            Gamma.regularizedGammaP(-1.0, 2.0);
            fail("Expected MathException to be thrown");
        } catch (MathException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testRegularizedGammaQ() throws MathException {
        // Typical use case
        assertEquals(0, Double.compare(0.8187307530779818, Gamma.regularizedGammaQ(0.5, 1.2)));
        
        // Edge case
        assertEquals(0.0, Gamma.regularizedGammaQ(1.0, 0.0), 0.0);
        
        // Error scenario
        try {
            Gamma.regularizedGammaQ(-1.0, 2.0);
            fail("Expected MathException to be thrown");
        } catch (MathException e) {
            // Exception expected
        }
    }

}