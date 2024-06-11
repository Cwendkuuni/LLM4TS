import org.junit.Test;
import static org.junit.Assert.*;

public class GammaTest {
    
    @Test
    public void testLogGamma() {
        // Typical use case
        double result1 = Gamma.logGamma(5.0);
        assertEquals(3.1780538303479458, result1, 0.0001);
        
        // Edge case
        double result2 = Gamma.logGamma(1.0);
        assertEquals(0.0, result2, 0.0001);
        
        // Error scenario
        double result3 = Gamma.logGamma(-2.0);
        assertEquals(Double.NaN, result3, 0.0001);
    }
    
    @Test
    public void testRegularizedGammaP() {
        // Typical use case
        double result1 = Gamma.regularizedGammaP(2.0, 3.0);
        assertEquals(0.3233235838161529, result1, 0.0001);
        
        // Edge case
        double result2 = Gamma.regularizedGammaP(1.0, 0.0);
        assertEquals(0.0, result2, 0.0001);
        
        // Error scenario
        try {
            double result3 = Gamma.regularizedGammaP(-2.0, 3.0);
            fail("Expected MathException");
        } catch (MathException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testRegularizedGammaQ() {
        // Typical use case
        double result1 = Gamma.regularizedGammaQ(2.0, 3.0);
        assertEquals(0.676676416183847, result1, 0.0001);
        
        // Edge case
        double result2 = Gamma.regularizedGammaQ(1.0, 0.0);
        assertEquals(1.0, result2, 0.0001);
        
        // Error scenario
        try {
            double result3 = Gamma.regularizedGammaQ(-2.0, 3.0);
            fail("Expected MathException");
        } catch (MathException e) {
            // Exception expected
        }
    }
}