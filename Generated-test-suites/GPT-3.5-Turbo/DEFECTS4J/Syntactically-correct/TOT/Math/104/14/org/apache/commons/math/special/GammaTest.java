import org.junit.Test;
import static org.junit.Assert.*;

public class GammaTest {

    @Test
    public void testLogGamma() {
        // Typical case
        double result1 = Gamma.logGamma(5.5);
        assertEquals(3.1780538303479458, result1, 0.000000001);
        
        // Edge case - minimum valid input
        double result2 = Gamma.logGamma(Double.MIN_VALUE);
        assertEquals(Double.NaN, result2, 0.0);
        
        // Error scenario - negative input
        double result3 = Gamma.logGamma(-2.5);
        assertEquals(Double.NaN, result3, 0.0);
    }

    @Test
    public void testRegularizedGammaP() {
        // Typical case
        double result1 = Gamma.regularizedGammaP(3, 2.5);
        assertEquals(0.3233235838169366, result1, 0.000000001);
        
        // Edge case - minimum valid input
        double result2 = Gamma.regularizedGammaP(Double.MIN_VALUE, 0.0);
        assertEquals(Double.NaN, result2, 0.0);
        
        // Error scenario - negative input
        try {
            double result3 = Gamma.regularizedGammaP(-1, 2.5);
            fail("Expected MathException");
        } catch (MathException e) {
            assertEquals("The algorithm fails to converge", e.getMessage());
        }
    }
    
    @Test
    public void testRegularizedGammaQ() {
        // Typical case
        double result1 = Gamma.regularizedGammaQ(3, 2.5);
        assertEquals(0.6766764161830633, result1, 0.000000001);
        
        // Edge case - minimum valid input
        double result2 = Gamma.regularizedGammaQ(Double.MIN_VALUE, 0.0);
        assertEquals(Double.NaN, result2, 0.0);
        
        // Error scenario - negative input
        try {
            double result3 = Gamma.regularizedGammaQ(-1, 2.5);
            fail("Expected MathException");
        } catch (MathException e) {
            assertEquals("The algorithm fails to converge", e.getMessage());
        }
    }

}