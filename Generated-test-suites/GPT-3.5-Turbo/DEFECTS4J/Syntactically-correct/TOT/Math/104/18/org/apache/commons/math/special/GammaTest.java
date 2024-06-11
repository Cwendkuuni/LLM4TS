import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        // Typical use case
        double result1 = Gamma.logGamma(5);
        assertEquals(3.1780538303479458, result1, 0.000000000000001);
        
        // Edge case
        double result2 = Gamma.logGamma(0.5);
        assertEquals(0.5723649429247001, result2, 0.000000000000001);
        
        // Error scenario
        double result3 = Gamma.logGamma(-3);
        assertEquals(Double.NaN, result3, 0.0);
    }

    @Test
    public void testRegularizedGammaP() {
        // Typical use case
        double result1 = Gamma.regularizedGammaP(2, 4);
        assertEquals(0.9764339154105833, result1, 0.000000000000001);
        
        // Edge case
        double result2 = Gamma.regularizedGammaP(0.5, 0);
        assertEquals(0.0, result2, 0.0);
        
        // Error scenario
        double result3 = Gamma.regularizedGammaP(-2, 1);
        assertEquals(Double.NaN, result3, 0.0);
    }
    
    @Test
    public void testRegularizedGammaQ() {
        // Typical use case
        double result1 = Gamma.regularizedGammaQ(3, 5);
        assertEquals(0.09974750720320295, result1, 0.000000000000001);
        
        // Edge case
        double result2 = Gamma.regularizedGammaQ(1, 0);
        assertEquals(1.0, result2, 0.0);
        
        // Error scenario
        double result3 = Gamma.regularizedGammaQ(-5, 3);
        assertEquals(Double.NaN, result3, 0.0);
    }
}