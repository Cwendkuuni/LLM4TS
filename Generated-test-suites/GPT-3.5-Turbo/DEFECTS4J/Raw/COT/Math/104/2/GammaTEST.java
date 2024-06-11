import org.junit.Test;
import static org.junit.Assert.*;

public class GammaTest {
    
    // Test logGamma method
    @Test
    public void testLogGamma() {
        // Test for positive value
        assertEquals(1.784, Gamma.logGamma(4.7), 0.001);
        
        // Test for zero
        assertEquals(Double.NaN, Gamma.logGamma(0), 0.001);
        
        // Test for negative value
        assertEquals(Double.NaN, Gamma.logGamma(-4.7), 0.001);
        
        // Test for NaN
        assertEquals(Double.NaN, Gamma.logGamma(Double.NaN), 0.001);
    }
    
    // Test regularizedGammaP method
    @Test
    public void testRegularizedGammaP() {
        // Test for positive values of a and x
        assertEquals(0.686, Gamma.regularizedGammaP(3.5, 5.7), 0.001);
        
        // Test for zero x
        assertEquals(0.0, Gamma.regularizedGammaP(3.5, 0.0), 0.001);
        
        // Test for zero a
        assertEquals(Double.NaN, Gamma.regularizedGammaP(0.0, 5.7), 0.001);
        
        // Test for negative a
        assertEquals(Double.NaN, Gamma.regularizedGammaP(-2.5, 5.7), 0.001);
        
        // Test for NaN
        assertEquals(Double.NaN, Gamma.regularizedGammaP(Double.NaN, 5.7), 0.001);
    }
    
    // Test regularizedGammaQ method
    @Test
    public void testRegularizedGammaQ() {
        // Test for positive values of a and x
        assertEquals(0.314, Gamma.regularizedGammaQ(3.5, 5.7), 0.001);
        
        // Test for zero x
        assertEquals(1.0, Gamma.regularizedGammaQ(3.5, 0.0), 0.001);
        
        // Test for zero a
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(0.0, 5.7), 0.001);
        
        // Test for negative a
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(-2.5, 5.7), 0.001);
        
        // Test for NaN
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(Double.NaN, 5.7), 0.001);
    }
}