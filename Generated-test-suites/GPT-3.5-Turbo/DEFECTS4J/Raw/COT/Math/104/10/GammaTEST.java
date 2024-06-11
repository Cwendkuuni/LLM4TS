import org.junit.Test;
import static org.junit.Assert.*;

public class GammaTest {

    @Test
    public void testLogGamma() {
        assertEquals(1.0000000000000007, Gamma.logGamma(3), 0);
        assertEquals(1.791759469228055, Gamma.logGamma(5), 0);
        assertEquals(Double.NaN, Gamma.logGamma(-2), 0);
        assertEquals(Double.NaN, Gamma.logGamma(0), 0);
    }

    @Test
    public void testRegularizedGammaP() throws MathException {
        assertEquals(0.6321205588285577, Gamma.regularizedGammaP(2, 1), 0);
        assertEquals(0.9999999999999787, Gamma.regularizedGammaP(3, 5), 0);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(-2, 4), 0);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(0, 10), 0);
    }

    @Test
    public void testRegularizedGammaQ() throws MathException {
        assertEquals(0.36787944117144233, Gamma.regularizedGammaQ(2, 1), 0);
        assertEquals(2.1249682935276543e-13, Gamma.regularizedGammaQ(3, 5), 0);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(-2, 4), 0);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(0, 10), 0);
    }

}