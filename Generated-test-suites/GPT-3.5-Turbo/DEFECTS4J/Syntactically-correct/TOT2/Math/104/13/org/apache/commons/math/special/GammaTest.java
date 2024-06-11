import org.junit.Test;
import static org.junit.Assert.*;

public class GammaTest {

    @Test
    public void testLogGamma() {
        assertEquals(Gamma.logGamma(5), 3.713478, 0.000001);
        assertEquals(Gamma.logGamma(0), Double.NaN, 0.0);
    }
    
    @Test
    public void testRegularizedGammaP() {
        try {
            assertEquals(Gamma.regularizedGammaP(5, 10), 0.999969, 0.000001);
        } catch (MathException e) {
            fail("MathException occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testRegularizedGammaQ() {
        try {
            assertEquals(Gamma.regularizedGammaQ(5, 10), 0.000031, 0.000001);
        } catch (MathException e) {
            fail("MathException occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testEdgeCases() {
        // Test edge cases here
    }
    
    @Test
    public void testExceptions() {
        // Test exception handling scenarios here
    }

}