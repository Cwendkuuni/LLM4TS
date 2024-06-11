
import org.junit.Test;
import static org.junit.Assert.*;

public class GammaTest {

    @Test
    public void testLogGamma() {
        assertEquals(Double.NaN, Gamma.logGamma(Double.NaN), 0.0001);
        assertEquals(Double.NaN, Gamma.logGamma(-1), 0.0001);
        assertEquals(Double.NaN, Gamma.logGamma(0), 0.0001);
        assertEquals(0.5723649429247001, Gamma.logGamma(1), 0.0001);
        assertEquals(0.0, Gamma.logGamma(2), 0.0001);
        assertEquals(0.6931471805599453, Gamma.logGamma(3), 0.0001);
    }

    @Test
    public void testRegularizedGammaP() throws MathException {
        assertEquals(Double.NaN, Gamma.regularizedGammaP(Double.NaN, 2), 0.0001);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(2, Double.NaN), 0.0001);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(0, 2), 0.0001);
        assertEquals(1.0, Gamma.regularizedGammaP(2, 0), 0.0001);
        assertEquals(0.9200172524228639, Gamma.regularizedGammaP(2, 1), 0.0001);
        assertEquals(0.42102443824070833, Gamma.regularizedGammaP(2, 2), 0.0001);
    }

    @Test
    public void testRegularizedGammaQ() throws MathException {
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(Double.NaN, 2), 0.0001);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(2, Double.NaN), 0.0001);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(0, 2), 0.0001);
        assertEquals(0.0, Gamma.regularizedGammaQ(2, 0), 0.0001);
        assertEquals(0.07998274757713613, Gamma.regularizedGammaQ(2, 1), 0.0001);
        assertEquals(0.5789755617592917, Gamma.regularizedGammaQ(2, 2), 0.0001);
    }

}