
import org.junit.Test;
import org.apache.commons.math.MathException;
import static org.junit.Assert.assertEquals;

public class GammaTest {

    @Test
    public void testLogGamma() {
        // Test cases for valid inputs
        assertEquals(Math.log(1), Gamma.logGamma(1), 0.0001);
        assertEquals(Math.log(2), Gamma.logGamma(2), 0.0001);
        assertEquals(Math.log(5), Gamma.logGamma(5), 0.0001);

        // Test cases for invalid inputs
        assertEquals(Double.NaN, Gamma.logGamma(Double.NaN), 0.0001);
        assertEquals(Double.NaN, Gamma.logGamma(0), 0.0001);
        assertEquals(Double.NaN, Gamma.logGamma(-1), 0.0001);
    }

    @Test
    public void testRegularizedGammaP() throws MathException {
        // Test cases for valid inputs
        assertEquals(0.6321205588285577, Gamma.regularizedGammaP(1, 1), 0.0001);
        assertEquals(0.9999999999978568, Gamma.regularizedGammaP(2, 5), 0.0001);
        assertEquals(0.44049328506521825, Gamma.regularizedGammaP(3, 2), 0.0001);

        // Test case for invalid inputs
        assertEquals(Double.NaN, Gamma.regularizedGammaP(Double.NaN, 1), 0.0001);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(1, Double.NaN), 0.0001);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(-1, 1), 0.0001);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(1, -1), 0.0001);
    }

    @Test
    public void testRegularizedGammaQ() throws MathException {
        // Test cases for valid inputs
        assertEquals(0.36787944117144233, Gamma.regularizedGammaQ(1, 1), 0.0001);
        assertEquals(2.1432896191051734E-12, Gamma.regularizedGammaQ(2, 5), 0.0001);
        assertEquals(0.5595067149347817, Gamma.regularizedGammaQ(3, 2), 0.0001);

        // Test case for invalid inputs
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(Double.NaN, 1), 0.0001);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(1, Double.NaN), 0.0001);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(-1, 1), 0.0001);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(1, -1), 0.0001);
    }
}
