import static org.junit.Assert.*;
import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        double result = Gamma.logGamma(5.0);
        assertEquals(3.1780538303479458, result, 1e-10);
    }

    @Test
    public void testRegularizedGammaP() {
        try {
            double result = Gamma.regularizedGammaP(2.0, 3.0);
            assertEquals(0.5948260211450953, result, 1e-10);
        } catch (MathException e) {
            fail("MathException should not be thrown for valid input");
        }
    }

    @Test
    public void testRegularizedGammaP_Exception() {
        try {
            double result = Gamma.regularizedGammaP(0.0, 3.0);
            fail("MathException should be thrown for invalid input");
        } catch (MathException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testRegularizedGammaQ() {
        try {
            double result = Gamma.regularizedGammaQ(2.0, 3.0);
            assertEquals(0.4051739788549047, result, 1e-10);
        } catch (MathException e) {
            fail("MathException should not be thrown for valid input");
        }
    }

    @Test
    public void testRegularizedGammaQ_Exception() {
        try {
            double result = Gamma.regularizedGammaQ(0.0, 3.0);
            fail("MathException should be thrown for invalid input");
        } catch (MathException e) {
            assertTrue(true);
        }
    }
}