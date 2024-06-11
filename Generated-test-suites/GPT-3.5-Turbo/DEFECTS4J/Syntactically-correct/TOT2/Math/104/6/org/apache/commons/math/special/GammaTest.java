import org.junit.Test;
import static org.junit.Assert.*;

public class GammaTest {
    
    // Test logGamma method with positive input
    @Test
    public void testLogGammaPositive() {
        double result = Gamma.logGamma(5);
        assertEquals(3.1780538300006177, result, 0.0001);
    }
    
    // Test logGamma method with negative input
    @Test
    public void testLogGammaNegative() {
        double result = Gamma.logGamma(-5);
        assertTrue(Double.isNaN(result));
    }
    
    // Test regularizedGammaP method with valid inputs
    @Test
    public void testRegularizedGammaPValid() {
        double result = Gamma.regularizedGammaP(2, 3);
        assertEquals(0.5939941502901618, result, 0.0001);
    }
    
    // Test regularizedGammaP method with negative input
    @Test
    public void testRegularizedGammaPNegative() {
        try {
            Gamma.regularizedGammaP(-2, 3);
            fail("Expected MathException");
        } catch (MathException e) {
            assertTrue(true);
        }
    }
    
    // Test regularizedGammaQ method with valid inputs
    @Test
    public void testRegularizedGammaQValid() {
        double result = Gamma.regularizedGammaQ(2, 3);
        assertEquals(0.4060058497098382, result, 0.0001);
    }
    
    // Test regularizedGammaQ method with negative input
    @Test
    public void testRegularizedGammaQNegative() {
        try {
            Gamma.regularizedGammaQ(-2, 3);
            fail("Expected MathException");
        } catch (MathException e) {
            assertTrue(true);
        }
    }
}