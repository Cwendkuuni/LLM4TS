import org.junit.Test;
import static org.junit.Assert.*;

public class GammaTest {

    @Test
    public void testLogGamma() {
        double result = Gamma.logGamma(5.0);
        assertEquals(3.1780538303479458, result, 0.000001);
    }

    @Test
    public void testRegularizedGammaP() {
        double result = Gamma.regularizedGammaP(1.0, 0.5);
        assertEquals(0.5, result, 0.000001);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaPException() throws MathException {
        Gamma.regularizedGammaP(0.0, 0.5);
    }

    @Test
    public void testRegularizedGammaQ() {
        double result = Gamma.regularizedGammaQ(1.0, 0.5);
        assertEquals(0.5, result, 0.000001);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaQException() throws MathException {
        Gamma.regularizedGammaQ(0.0, 0.5);
    }
}