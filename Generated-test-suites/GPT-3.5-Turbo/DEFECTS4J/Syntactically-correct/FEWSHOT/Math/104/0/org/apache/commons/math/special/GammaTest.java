import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.MathException;
import org.junit.Test;

public class GammaTest {
    @Test
    public void testLogGamma() {
        double result = Gamma.logGamma(1.0);
        assertEquals(0.0, result, 0.000001);
        
        result = Gamma.logGamma(2.0);
        assertEquals(0.0, result, 0.000001);
        
        result = Gamma.logGamma(3.0);
        assertEquals(0.6931471805599453, result, 0.000001);
        
        result = Gamma.logGamma(4.0);
        assertEquals(1.791759469228055, result, 0.000001);
        
        // Add more test cases here
    }
    
    @Test
    public void testRegularizedGammaP() throws MathException {
        double result = Gamma.regularizedGammaP(1.0, 0.0);
        assertEquals(0.0, result, 0.000001);
        
        result = Gamma.regularizedGammaP(2.0, 1.0);
        assertEquals(0.5939941502901615, result, 0.000001);
        
        result = Gamma.regularizedGammaP(3.0, 2.0);
        assertEquals(0.32332358381693654, result, 0.000001);
        
        result = Gamma.regularizedGammaP(4.0, 3.0);
        assertEquals(0.4842783259199185, result, 0.000001);
        
        // Add more test cases here
    }
    
    @Test
    public void testRegularizedGammaQ() throws MathException {
        double result = Gamma.regularizedGammaQ(1.0, 0.0);
        assertEquals(1.0, result, 0.000001);
        
        result = Gamma.regularizedGammaQ(2.0, 1.0);
        assertEquals(0.40600584970983846, result, 0.000001);
        
        result = Gamma.regularizedGammaQ(3.0, 2.0);
        assertEquals(0.6766764161830635, result, 0.000001);
        
        result = Gamma.regularizedGammaQ(4.0, 3.0);
        assertEquals(0.5157216740800815, result, 0.000001);
        
        // Add more test cases here
    }
}