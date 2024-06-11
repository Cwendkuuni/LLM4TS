import org.junit.Test;
import static org.junit.Assert.*;

public class GammaTest {

    // Test cases for logGamma
    @Test
    public void testLogGamma() {
        // Typical use case
        double result1 = Gamma.logGamma(5.0);
        assertEquals(3.1780538303479458, result1, 0.0001);
        
        // Edge case: x = 0
        double result2 = Gamma.logGamma(0.0);
        assertEquals(Double.NaN, result2, 0.0001);
        
        // Error scenario: NaN input
        double result3 = Gamma.logGamma(Double.NaN);
        assertEquals(Double.NaN, result3, 0.0001);
    }

    // Test cases for regularizedGammaP
    @Test
    public void testRegularizedGammaP() throws MathException {
        // Typical use case
        double result1 = Gamma.regularizedGammaP(2.0, 3.0);
        assertEquals(0.3233235838169366, result1, 0.0001);
        
        // Edge case: x = 0
        double result2 = Gamma.regularizedGammaP(2.0, 0.0);
        assertEquals(0.0, result2, 0.0001);
        
        // Error scenario: NaN input
        double result3 = Gamma.regularizedGammaP(Double.NaN, 3.0);
        assertEquals(Double.NaN, result3, 0.0001);
    }

    // Test cases for regularizedGammaQ
    @Test
    public void testRegularizedGammaQ() throws MathException {
        // Typical use case
        double result1 = Gamma.regularizedGammaQ(2.0, 3.0);
        assertEquals(0.6766764161830634, result1, 0.0001);
        
        // Edge case: x = 0
        double result2 = Gamma.regularizedGammaQ(2.0, 0.0);
        assertEquals(1.0, result2, 0.0001);
        
        // Error scenario: NaN input
        double result3 = Gamma.regularizedGammaQ(Double.NaN, 3.0);
        assertEquals(Double.NaN, result3, 0.0001);
    }
}