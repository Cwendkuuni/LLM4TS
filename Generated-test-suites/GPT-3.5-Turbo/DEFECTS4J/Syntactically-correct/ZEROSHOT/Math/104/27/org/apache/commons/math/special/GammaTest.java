import static org.junit.Assert.*;
import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGammaNaN() {
        double result = Gamma.logGamma(Double.NaN);
        assertTrue(Double.isNaN(result));
    }
    
    @Test
    public void testLogGammaNegative() {
        double result = Gamma.logGamma(-1.0);
        assertTrue(Double.isNaN(result));
    }
    
    @Test
    public void testLogGammaZero() {
        double result = Gamma.logGamma(0.0);
        assertTrue(Double.isNaN(result));
    }
    
    @Test
    public void testLogGammaPositive() {
        double result = Gamma.logGamma(5.0);
        assertEquals(3.1780538303479458, result, 0.000001);
    }
    
    @Test
    public void testRegularizedGammaPNegativeA() {
        try {
            double result = Gamma.regularizedGammaP(-1.0, 2.0);
            fail("Expected MathException to be thrown");
        } catch (MathException e) {
            assertTrue(true);
        }  
    }
    
    @Test
    public void testRegularizedGammaPZeroA() {
        double result = Gamma.regularizedGammaP(0.0, 2.0);
        assertEquals(0.9196986029295159, result, 0.000001);
    }
    
    @Test
    public void testRegularizedGammaPPositiveA() {
        double result = Gamma.regularizedGammaP(3.0, 2.0);
        assertEquals(0.3233235838169366, result, 0.000001);
    }
    
    @Test
    public void testRegularizedGammaPInvalidX() {
       double result = Gamma.regularizedGammaP(2.0, -1.0);
       assertTrue(Double.isNaN(result));
    }
    
    @Test
    public void testRegularizedGammaQNegativeA() {
        try {
            double result = Gamma.regularizedGammaQ(-1.0, 2.0);
            fail("Expected MathException to be thrown");
        } catch (MathException e) {
            assertTrue(true);
        }  
    }
    
    @Test
    public void testRegularizedGammaQZeroA() {
        double result = Gamma.regularizedGammaQ(0.0, 2.0);
        assertEquals(0.08030139707048413, result, 0.000001);
    }
    
    @Test
    public void testRegularizedGammaQPositiveA() {
        double result = Gamma.regularizedGammaQ(3.0, 2.0);
        assertEquals(0.6766764161830634, result, 0.000001);
    }
    
    @Test
    public void testRegularizedGammaQInvalidX() {
       double result = Gamma.regularizedGammaQ(2.0, -1.0);
       assertTrue(Double.isNaN(result));
    }
}