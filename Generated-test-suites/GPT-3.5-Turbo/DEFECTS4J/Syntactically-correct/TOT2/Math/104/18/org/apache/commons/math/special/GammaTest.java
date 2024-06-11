import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.MathException;

public class GammaTest {

    @Test
    public void testLogGamma() {
        double result = Gamma.logGamma(5.5);
        assertEquals(4.6983, result, 0.0001);
        
        result = Gamma.logGamma(0.5);
        assertEquals(0.5723, result, 0.0001);
        
        result = Gamma.logGamma(-1.0);
        assertTrue(Double.isNaN(result));
    }

    @Test
    public void testRegularizedGammaP() {
        try {
            double result = Gamma.regularizedGammaP(0.5, 1.5);
            assertEquals(0.0227, result, 0.0001);
            
            result = Gamma.regularizedGammaP(1.5, 0.5);
            assertEquals(0.7332, result, 0.0001);
            
            result = Gamma.regularizedGammaP(-1.0, 2.0);
            assertTrue(Double.isNaN(result));
        } catch (MathException e) {
            fail("Exception thrown when it shouldn't have.");
        }
    }

    @Test
    public void testRegularizedGammaQ() {
        try {
            double result = Gamma.regularizedGammaQ(0.5, 1.5);
            assertEquals(0.9773, result, 0.0001);
            
            result = Gamma.regularizedGammaQ(1.5, 0.5);
            assertEquals(0.2668, result, 0.0001);
            
            result = Gamma.regularizedGammaQ(-1.0, 2.0);
            assertTrue(Double.isNaN(result));
        } catch (MathException e) {
            fail("Exception thrown when it shouldn't have.");
        }
    }
}