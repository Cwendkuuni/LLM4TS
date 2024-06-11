import org.junit.Test;
import org.apache.commons.math.special.Gamma;
import org.apache.commons.math.MathException;
import org.apache.commons.math.MaxIterationsExceededException;
import static org.junit.Assert.*;

public class GammaTest {

    @Test
    public void testLogGamma() {
        assertEquals(-76.18009172947146, Gamma.logGamma(0.1), 0.0001);
        assertEquals(0.6931471805599453, Gamma.logGamma(1), 0.0001);
        assertEquals(2.890371757896165, Gamma.logGamma(4.5), 0.0001);
        assertEquals(162797.71727522786, Gamma.logGamma(170), 0.0001);
    }

    @Test
    public void testRegularizedGammaP() throws MathException {
        assertEquals(0, Gamma.regularizedGammaP(0.1, 0), 0.0001);
        assertEquals(0.8521437889662115, Gamma.regularizedGammaP(0.5, 0.5), 0.0001);
        assertEquals(1, Gamma.regularizedGammaP(10, 15), 0.0001);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testRegularizedGammaPMaxIterationsExceededException() throws MathException {
        Gamma.regularizedGammaP(10, -15);
    }

    @Test
    public void testRegularizedGammaQ() throws MathException {
        assertEquals(1, Gamma.regularizedGammaQ(0.1, 0), 0.0001);
        assertEquals(0.14785621103378853, Gamma.regularizedGammaQ(0.5, 0.5), 0.0001);
        assertEquals(0, Gamma.regularizedGammaQ(10, 15), 0.0001);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testRegularizedGammaQMaxIterationsExceededException() throws MathException {
        Gamma.regularizedGammaQ(10, -15);
    }
}