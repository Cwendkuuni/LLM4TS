import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class GammaTest {
    
    @Test
    public void testLogGamma_ValidInput_ReturnsCorrectResult() {
        double x = 2.0;
        double expected = 0.6931471805599453;
        double actual = Gamma.logGamma(x);
        assertEquals(expected, actual, 0.000001);
    }
    
    @Test
    public void testLogGamma_ZeroInput_ReturnsNaN() {
        double x = 0.0;
        double expected = Double.NaN;
        double actual = Gamma.logGamma(x);
        assertEquals(expected, actual, 0.000001);
    }
    
    @Test
    public void testLogGamma_NegativeInput_ReturnsNaN() {
        double x = -2.0;
        double expected = Double.NaN;
        double actual = Gamma.logGamma(x);
        assertEquals(expected, actual, 0.000001);
    }
    
    @Test
    public void testRegularizedGammaP_ValidInput_ReturnsCorrectResult() throws MathException {
        double a = 2.0;
        double x = 0.75;
        double expected = 0.225347;
        double actual = Gamma.regularizedGammaP(a, x);
        assertEquals(expected, actual, 0.000001);
    }
    
    @Test
    public void testRegularizedGammaP_InvalidInput_ReturnsNaN() throws MathException {
        double a = -2.0;
        double x = 0.75;
        double expected = Double.NaN;
        double actual = Gamma.regularizedGammaP(a, x);
        assertEquals(expected, actual, 0.000001);
    }
    
    @Test
    public void testRegularizedGammaQ_ValidInput_ReturnsCorrectResult() throws MathException {
        double a = 2.0;
        double x = 0.75;
        double expected = 0.774652;
        double actual = Gamma.regularizedGammaQ(a, x);
        assertEquals(expected, actual, 0.000001);
    }
    
    @Test
    public void testRegularizedGammaQ_InvalidInput_ReturnsNaN() throws MathException {
        double a = -2.0;
        double x = 0.75;
        double expected = Double.NaN;
        double actual = Gamma.regularizedGammaQ(a, x);
        assertEquals(expected, actual, 0.000001);
    }
}