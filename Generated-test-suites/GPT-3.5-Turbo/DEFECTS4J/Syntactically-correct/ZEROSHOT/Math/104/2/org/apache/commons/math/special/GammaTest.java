import org.junit.Test;
import static org.junit.Assert.*;

public class GammaTest {

    @Test
    public void testLogGamma() {
        assertEquals(Double.NaN, Gamma.logGamma(Double.NaN), 0.0001);
        assertEquals(Double.NaN, Gamma.logGamma(-1.0), 0.0001);
        assertEquals(0.5723649429247001, Gamma.logGamma(2.0), 0.0001);
        assertEquals(0.0, Gamma.logGamma(3.0), 0.0001);
        assertEquals(0.0, Gamma.logGamma(4.0), 0.0001);
        assertEquals(0.0, Gamma.logGamma(5.0), 0.0001);
        assertEquals(1.200973602347074, Gamma.logGamma(6.0), 0.0001);
    }

    @Test
    public void testRegularizedGammaP() throws MathException {
        assertEquals(Double.NaN, Gamma.regularizedGammaP(Double.NaN, 1.0), 0.0001);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(-1.0, 1.0), 0.0001);
        assertEquals(0.3843793136630712, Gamma.regularizedGammaP(2.0, 1.0), 0.0001);
        assertEquals(0.593994150290161, Gamma.regularizedGammaP(3.0, 1.0), 0.0001);
        assertEquals(0.7, Gamma.regularizedGammaP(4.0, 1.0), 0.0001);
        assertEquals(0.7698600397200763, Gamma.regularizedGammaP(5.0, 1.0), 0.0001);
        assertEquals(1.0, Gamma.regularizedGammaP(6.0, 1.0), 0.0001);
    }

    @Test
    public void testRegularizedGammaQ() throws MathException {
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(Double.NaN, 1.0), 0.0001);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(-1.0, 1.0), 0.0001);
        assertEquals(0.6156206863369288, Gamma.regularizedGammaQ(2.0, 1.0), 0.0001);
        assertEquals(0.406005849709839, Gamma.regularizedGammaQ(3.0, 1.0), 0.0001);
        assertEquals(0.3, Gamma.regularizedGammaQ(4.0, 1.0), 0.0001);
        assertEquals(0.2301399602799236, Gamma.regularizedGammaQ(5.0, 1.0), 0.0001);
        assertEquals(0.0, Gamma.regularizedGammaQ(6.0, 1.0), 0.0001);
    }
}