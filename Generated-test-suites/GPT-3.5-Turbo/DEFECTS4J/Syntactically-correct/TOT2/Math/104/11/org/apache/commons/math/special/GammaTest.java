import static org.junit.Assert.*;
import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        assertEquals(0.0, Gamma.logGamma(1.0), 1e-15);
    }

    @Test
    public void testRegularizedGammaP() {
        assertEquals(0.0, Gamma.regularizedGammaP(0.5, 0.0), 1e-15);
    }

    @Test
    public void testRegularizedGammaQ() {
        assertEquals(1.0, Gamma.regularizedGammaQ(0.5, 0.0), 1e-15);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaPWithException() throws MathException {
        Gamma.regularizedGammaP(-1.0, 1.0);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaQWithException() throws MathException {
        Gamma.regularizedGammaQ(0.5, -1.0);
    }
}