import static org.junit.Assert.*;
import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        double result = Gamma.logGamma(5.0);
        assertEquals(3.1780538303479458, result, 0.00001);
    }

    @Test
    public void testRegularizedGammaP() {
        try {
            double result = Gamma.regularizedGammaP(2, 3);
            assertEquals(0.8008517265285442, result, 0.00001);
        } catch (MathException e) {
            fail("MathException thrown");
        }
    }

    @Test
    public void testRegularizedGammaQ() {
        try {
            double result = Gamma.regularizedGammaQ(2, 3);
            assertEquals(0.19914827347145588, result, 0.00001);
        } catch (MathException e) {
            fail("MathException thrown");
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