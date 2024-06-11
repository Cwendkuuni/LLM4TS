import org.junit.Test;
import static org.junit.Assert.*;

public class GammaTest {

    @Test
    public void testLogGamma() {
        assertEquals(Double.NaN, Gamma.logGamma(Double.NaN), 0.0001);
        assertEquals(Double.NaN, Gamma.logGamma(0.0), 0.0001);
        assertEquals(0.5723649429247001, Gamma.logGamma(0.5), 0.0001);
        assertEquals(9.189385332046727, Gamma.logGamma(5.0), 0.0001);
        assertEquals(13.815510557964274, Gamma.logGamma(10.0), 0.0001);
    }

    @Test
    public void testRegularizedGammaP() throws MathException {
        assertEquals(Double.NaN, Gamma.regularizedGammaP(Double.NaN, 5.0), 0.0001);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(5.0, Double.NaN), 0.0001);
        assertEquals(0.0, Gamma.regularizedGammaP(0.0, 5.0), 0.0001);
        assertEquals(1.0, Gamma.regularizedGammaP(5.0, 0.0), 0.0001);
        assertEquals(0.6583990408889639, Gamma.regularizedGammaP(0.5, 5.0), 0.0001);
        assertEquals(0.6321215865082171, Gamma.regularizedGammaP(5.0, 10.0), 0.0001);
    }

    @Test
    public void testRegularizedGammaQ() throws MathException {
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(Double.NaN, 5.0), 0.0001);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(5.0, Double.NaN), 0.0001);
        assertEquals(1.0, Gamma.regularizedGammaQ(0.0, 5.0), 0.0001);
        assertEquals(0.0, Gamma.regularizedGammaQ(5.0, 0.0), 0.0001);
        assertEquals(0.34160095911103613, Gamma.regularizedGammaQ(0.5, 5.0), 0.0001);
        assertEquals(0.3678784134917829, Gamma.regularizedGammaQ(5.0, 10.0), 0.0001);
    }
}