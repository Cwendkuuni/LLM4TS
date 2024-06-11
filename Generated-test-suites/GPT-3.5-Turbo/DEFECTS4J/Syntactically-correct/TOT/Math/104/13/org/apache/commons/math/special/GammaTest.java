import static org.junit.Assert.*;
import org.junit.Test;

public class GammaTest {

    // Test cases for logGamma method
    @Test
    public void testLogGamma() {
        // Typical use case
        double result = Gamma.logGamma(5);
        double expected = 3.1780538303479458;
        assertEquals(expected, result, 0.0001);
        
        // Edge case: x = 0
        result = Gamma.logGamma(0);
        expected = Double.NaN;
        assertEquals(expected, result, 0.0001);
        
        // Edge case: x = 1
        result = Gamma.logGamma(1);
        expected = 0.0;
        assertEquals(expected, result, 0.0001);
        
        // Error scenario: x < 0
        result = Gamma.logGamma(-1);
        expected = Double.NaN;
        assertEquals(expected, result, 0.0001);
    }

    // Test cases for regularizedGammaP method
    @Test
    public void testRegularizedGammaP() throws MathException {
        // Typical use case
        double result = Gamma.regularizedGammaP(3, 2);
        double expected = 0.5939941502901619;
        assertEquals(expected, result, 0.0001);
        
        // Edge case: a = 0
        result = Gamma.regularizedGammaP(0, 2);
        expected = Double.NaN;
        assertEquals(expected, result, 0.0001);
        
        // Edge case: x = 0
        result = Gamma.regularizedGammaP(3, 0);
        expected = 0.0;
        assertEquals(expected, result, 0.0001);
        
        // Error scenario: a <= 0
        try {
            result = Gamma.regularizedGammaP(-1, 2);
            fail("Expected MathException");
        } catch (MathException e) {
            // success
        }
    }
    
    // Test cases for regularizedGammaQ method
    @Test
    public void testRegularizedGammaQ() throws MathException {
        // Typical use case
        double result = Gamma.regularizedGammaQ(3, 2);
        double expected = 0.4060058497098381;
        assertEquals(expected, result, 0.0001);
        
        // Edge case: a = 0
        result = Gamma.regularizedGammaQ(0, 2);
        expected = Double.NaN;
        assertEquals(expected, result, 0.0001);
        
        // Edge case: x = 0
        result = Gamma.regularizedGammaQ(3, 0);
        expected = 1.0;
        assertEquals(expected, result, 0.0001);
        
        // Error scenario: a <= 0
        try {
            result = Gamma.regularizedGammaQ(-1, 2);
            fail("Expected MathException");
        } catch (MathException e) {
            // success
        }
    }

}