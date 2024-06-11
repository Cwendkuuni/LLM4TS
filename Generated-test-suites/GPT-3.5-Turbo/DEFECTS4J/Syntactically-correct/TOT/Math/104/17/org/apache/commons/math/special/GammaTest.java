import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GammaTest {

    @Test
    public void testLogGammaWithValidInput() {
        double x = 5.0;
        double expected = 3.1780538303479458;
        double result = Gamma.logGamma(x);
        assertEquals(expected, result, 0.000000001);
    }

    @Test
    public void testLogGammaWithZeroInput() {
        double x = 0.0;
        double expected = Double.NaN;
        double result = Gamma.logGamma(x);
        assertEquals(expected, result, 0.0);
    }

    @Test
    public void testLogGammaWithInvalidInput() {
        double x = -2.5;
        double expected = Double.NaN;
        double result = Gamma.logGamma(x);
        assertEquals(expected, result, 0.0);
    }

    @Test
    public void testRegularizedGammaPWithValidInput() {
        double a = 2.0;
        double x = 1.5;
        double expected = 0.7295128074893786;
        double result = Gamma.regularizedGammaP(a, x);
        assertEquals(expected, result, 0.000000001);
    }

    @Test
    public void testRegularizedGammaPWithZeroInput() {
        double a = 2.0;
        double x = 0.0;
        double expected = 0.0;
        double result = Gamma.regularizedGammaP(a, x);
        assertEquals(expected, result, 0.0);
    }

    @Test
    public void testRegularizedGammaPWithInvalidInput() {
        double a = 0.0;
        double x = 1.5;
        double expected = Double.NaN;
        double result = Gamma.regularizedGammaP(a, x);
        assertEquals(expected, result, 0.0);
    }

    @Test
    public void testRegularizedGammaQWithValidInput() {
        double a = 2.0;
        double x = 1.5;
        double expected = 0.27048719251062143;
        double result = Gamma.regularizedGammaQ(a, x);
        assertEquals(expected, result, 0.000000001);
    }

    @Test
    public void testRegularizedGammaQWithZeroInput() {
        double a = 2.0;
        double x = 0.0;
        double expected = 1.0;
        double result = Gamma.regularizedGammaQ(a, x);
        assertEquals(expected, result, 0.0);
    }

    @Test
    public void testRegularizedGammaQWithInvalidInput() {
        double a = 0.0;
        double x = 1.5;
        double expected = Double.NaN;
        double result = Gamma.regularizedGammaQ(a, x);
        assertEquals(expected, result, 0.0);
    }
}