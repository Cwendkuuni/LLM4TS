import org.junit.Test;
import static org.junit.Assert.*;

public class GammaTest {

    @Test
    public void testLogGamma() {
        double result = Gamma.logGamma(1.0);
        assertEquals(0.0, result, 0.00001);
        
        result = Gamma.logGamma(2.0);
        assertEquals(0.69315, result, 0.00001);
        
        result = Gamma.logGamma(5.0);
        assertEquals(3.17805, result, 0.00001);
        
        result = Gamma.logGamma(10.0);
        assertEquals(12.80183, result, 0.00001);
    }
    
    @Test
    public void testRegularizedGammaP() throws MathException {
        double result = Gamma.regularizedGammaP(1.0, 1.0);
        assertEquals(0.63212, result, 0.00001);
        
        result = Gamma.regularizedGammaP(2.0, 2.0);
        assertEquals(0.59399, result, 0.00001);
        
        result = Gamma.regularizedGammaP(5.0, 3.0);
        assertEquals(0.05256, result, 0.00001);
        
        result = Gamma.regularizedGammaP(10.0, 10.0);
        assertEquals(0.54207, result, 0.00001);
    }
    
    @Test
    public void testRegularizedGammaQ() throws MathException {
        double result = Gamma.regularizedGammaQ(1.0, 1.0);
        assertEquals(0.36788, result, 0.00001);
        
        result = Gamma.regularizedGammaQ(2.0, 2.0);
        assertEquals(0.40601, result, 0.00001);
        
        result = Gamma.regularizedGammaQ(5.0, 3.0);
        assertEquals(0.94744, result, 0.00001);
        
        result = Gamma.regularizedGammaQ(10.0, 10.0);
        assertEquals(0.45793, result, 0.00001);
    }

}