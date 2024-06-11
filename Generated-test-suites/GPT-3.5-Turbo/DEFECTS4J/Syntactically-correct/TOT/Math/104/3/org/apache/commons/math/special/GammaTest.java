
import org.junit.Test;
import static org.junit.Assert.*;

public class GammaTest {

    // Testing logGamma method
    
    @Test
    public void testLogGamma_positiveValue() {
        double result = Gamma.logGamma(5);
        assertEquals(3.1780538303479458, result, 0.00000001);
    }

    @Test
    public void testLogGamma_zeroValue() {
        double result = Gamma.logGamma(0);
        assertEquals(Double.NaN, result, 0.0);
    }

    @Test
    public void testLogGamma_negativeValue() {
        double result = Gamma.logGamma(-1);
        assertEquals(Double.NaN, result, 0.0);
    }

    // Testing regularizedGammaP method
    
    @Test
    public void testRegularizedGammaP_positiveAandX() throws MathException {
        double result = Gamma.regularizedGammaP(2.5, 1.5);
        assertEquals(0.32332358381632563, result, 0.00000001);
    }

    @Test
    public void testRegularizedGammaP_negativeA() throws MathException {
        double result = Gamma.regularizedGammaP(-1.5, 2.5);
        assertEquals(Double.NaN, result, 0.0);
    }

    @Test
    public void testRegularizedGammaP_negativeX() throws MathException {
        double result = Gamma.regularizedGammaP(1.5, -2.5);
        assertEquals(Double.NaN, result, 0.0);
    }

    // Testing regularizedGammaQ method
    
    @Test
    public void testRegularizedGammaQ_positiveAandX() throws MathException {
        double result = Gamma.regularizedGammaQ(2.5, 1.5);
        assertEquals(0.6766764161836744, result, 0.00000001);
    }

    @Test
    public void testRegularizedGammaQ_negativeA() throws MathException {
        double result = Gamma.regularizedGammaQ(-1.5, 2.5);
        assertEquals(Double.NaN, result, 0.0);
    }

    @Test
    public void testRegularizedGammaQ_negativeX() throws MathException {
        double result = Gamma.regularizedGammaQ(1.5, -2.5);
        assertEquals(Double.NaN, result, 0.0);
    }

}

