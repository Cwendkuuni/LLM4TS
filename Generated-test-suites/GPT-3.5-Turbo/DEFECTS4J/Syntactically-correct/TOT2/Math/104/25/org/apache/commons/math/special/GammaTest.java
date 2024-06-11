import static org.junit.Assert.*;
import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        assertEquals(Double.NaN, Gamma.logGamma(Double.NaN), 0.0001);
        assertEquals(Double.NaN, Gamma.logGamma(0.0), 0.0001);
        assertEquals(0.5723649429247001, Gamma.logGamma(1.5), 0.0001);
    }

    @Test
    public void testRegularizedGammaP() {
        assertThrows(MathException.class, () -> Gamma.regularizedGammaP(-1, 0), "Invalid input");
        assertEquals(0.0, Gamma.regularizedGammaP(0.5, 0.0), 0.0001);
        assertEquals(0.6839454201577721, Gamma.regularizedGammaP(1.5, 0.5), 0.0001);
    }

    @Test
    public void testRegularizedGammaQ() {
        assertThrows(MathException.class, () -> Gamma.regularizedGammaQ(-1, 0), "Invalid input");
        assertEquals(1.0, Gamma.regularizedGammaQ(0.5, 0.0), 0.0001);
        assertEquals(0.31605457984222786, Gamma.regularizedGammaQ(1.5, 0.5), 0.0001);
    }
}