import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.MathException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.special.Gamma;

public class GammaTest {

    @Test
    public void testLogGamma() {
        double expected = -1.9635100260214235;
        double result = Gamma.logGamma(0.1);
        assertEquals(expected, result, 1e-9);
    }

    @Test
    public void testRegularizedGammaP() throws MathException {
        double expected = 0.6321237181963725;
        double result = Gamma.regularizedGammaP(1.0, 1.0);
        assertEquals(expected, result, 1e-9);
    }

    @Test
    public void testRegularizedGammaPWithEpsilonAndMaxIterations() throws MathException {
        double expected = 0.9084218055563272;
        double result = Gamma.regularizedGammaP(0.5, 0.3, 1e-6, 100);
        assertEquals(expected, result, 1e-9);
    }

    @Test
    public void testRegularizedGammaQ() throws MathException {
        double expected = 0.367876318020563;
        double result = Gamma.regularizedGammaQ(1.0, 1.0);
        assertEquals(expected, result, 1e-9);
    }

    @Test
    public void testRegularizedGammaQWithEpsilonAndMaxIterations() throws MathException {
        double expected = 0.3052574903904398;
        double result = Gamma.regularizedGammaQ(0.5, 0.3, 1e-6, 100);
        assertEquals(expected, result, 1e-9);
    }
}