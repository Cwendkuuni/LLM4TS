import org.junit.Test;
import static org.junit.Assert.*;

public class GammaTest {

    // Test cases for logGamma method

    @Test
    public void testLogGamma_typicalCase() {
        double result = Gamma.logGamma(5.0);
        double expected = 3.1780538303479458;
        assertEquals(expected, result, 0.000001);
    }

    @Test
    public void testLogGamma_zeroInput() {
        double result = Gamma.logGamma(0.0);
        double expected = Double.NEGATIVE_INFINITY;
        assertEquals(expected, result, 0.000001);
    }

    @Test
    public void testLogGamma_negativeInput() {
        double result = Gamma.logGamma(-2.5);
        assertTrue(Double.isNaN(result));
    }

    // Test cases for regularizedGammaP method

    @Test
    public void testRegularizedGammaP_typicalCase() throws MathException {
        double result = Gamma.regularizedGammaP(3.0, 2.0);
        double expected = 0.2706705679511232;
        assertEquals(expected, result, 0.000001);
    }

    @Test
    public void testRegularizedGammaP_zeroInput() throws MathException {
        double result = Gamma.regularizedGammaP(3.0, 0.0);
        double expected = 0.0;
        assertEquals(expected, result, 0.000001);
    }

    @Test
    public void testRegularizedGammaP_negativeInput() throws MathException {
        double result = Gamma.regularizedGammaP(-2.0, 1.0);
        assertTrue(Double.isNaN(result));
    }

    // Test cases for regularizedGammaQ method

    @Test
    public void testRegularizedGammaQ_typicalCase() throws MathException {
        double result = Gamma.regularizedGammaQ(2.0, 3.0);
        double expected = 0.32332358381693654;
        assertEquals(expected, result, 0.000001);
    }

    @Test
    public void testRegularizedGammaQ_zeroInput() throws MathException {
        double result = Gamma.regularizedGammaQ(3.0, 0.0);
        double expected = 1.0;
        assertEquals(expected, result, 0.000001);
    }

    @Test
    public void testRegularizedGammaQ_negativeInput() throws MathException {
        double result = Gamma.regularizedGammaQ(-2.0, 1.0);
        assertTrue(Double.isNaN(result));
    }

}