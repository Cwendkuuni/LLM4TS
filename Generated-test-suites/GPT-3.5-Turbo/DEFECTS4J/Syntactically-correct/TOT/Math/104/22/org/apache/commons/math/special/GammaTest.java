import org.junit.Test;
import static org.junit.Assert.*;

public class GammaTest {

    // Test logGamma method

    @Test
    public void testLogGammaWithPositiveInput() {
        double result = Gamma.logGamma(5);
        assertEquals(3.1780538303479453, result, 0.000001);
    }

    @Test
    public void testLogGammaWithZeroInput() {
        double result = Gamma.logGamma(0);
        assertEquals(Double.NaN, result, 0.000001);
    }

    @Test
    public void testLogGammaWithNegativeInput() {
        double result = Gamma.logGamma(-2);
        assertEquals(Double.NaN, result, 0.000001);
    }

    // Test regularizedGammaP method

    @Test
    public void testRegularizedGammaPWithValidInputs() throws MathException {
        double result = Gamma.regularizedGammaP(2, 5);
        assertEquals(0.9866142981514303, result, 0.000001);
    }

    @Test
    public void testRegularizedGammaPWithZeroInput() throws MathException {
        double result = Gamma.regularizedGammaP(2, 0);
        assertEquals(0.0, result, 0.000001);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaPWithNegativeA() throws MathException {
        Gamma.regularizedGammaP(-2, 5);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaPWithNegativeX() throws MathException {
        Gamma.regularizedGammaP(2, -5);
    }

    // Test regularizedGammaQ method

    @Test
    public void testRegularizedGammaQWithValidInputs() throws MathException {
        double result = Gamma.regularizedGammaQ(2, 5);
        assertEquals(0.013385701848569722, result, 0.000001);
    }

    @Test
    public void testRegularizedGammaQWithZeroInput() throws MathException {
        double result = Gamma.regularizedGammaQ(2, 0);
        assertEquals(1.0, result, 0.000001);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaQWithNegativeA() throws MathException {
        Gamma.regularizedGammaQ(-2, 5);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaQWithNegativeX() throws MathException {
        Gamma.regularizedGammaQ(2, -5);
    }

}