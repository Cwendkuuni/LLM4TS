import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.MathException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.special.Gamma;

public class GammaTest {
    
    // Test logGamma method
    @Test
    public void testLogGamma() {
        // Typical use case
        double result1 = Gamma.logGamma(5.5);
        assertEquals(4.787491, result1, 0.000001);
        
        // Edge case
        double result2 = Gamma.logGamma(1);
        assertEquals(0.0, result2, 0.000001);
        
        // Error scenario
        double result3 = Gamma.logGamma(-5.5);
        assertTrue(Double.isNaN(result3));
    }
    
    // Test regularizedGammaP method
    @Test
    public void testRegularizedGammaP() throws MathException {
        // Typical use case
        double result1 = Gamma.regularizedGammaP(3, 5);
        assertEquals(0.800851, result1, 0.000001);
        
        // Edge case
        double result2 = Gamma.regularizedGammaP(1, 0);
        assertEquals(0.0, result2, 0.000001);
        
        // Error scenario
        try {
            double result3 = Gamma.regularizedGammaP(-3, 5);
            fail("Expected MathException");
        } catch (MathException e) {
            // Exception is expected
        }
    }
    
    // Test regularizedGammaQ method
    @Test
    public void testRegularizedGammaQ() throws MathException {
        // Typical use case
        double result1 = Gamma.regularizedGammaQ(3, 5);
        assertEquals(0.199149, result1, 0.000001);
        
        // Edge case
        double result2 = Gamma.regularizedGammaQ(1, 0);
        assertEquals(1.0, result2, 0.000001);
        
        // Error scenario
        try {
            double result3 = Gamma.regularizedGammaQ(-3, 5);
            fail("Expected MathException");
        } catch (MathException e) {
            // Exception is expected
        }
    }
    
}