import org.junit.Test;
import org.apache.commons.math.special.Gamma;

public class GammaTest {

    @Test
    public void testLogGammaNaN() {
        double result = Gamma.logGamma(Double.NaN);
        assertTrue(Double.isNaN(result));
    }

    @Test
    public void testLogGammaNegative() {
        double result = Gamma.logGamma(-1.0);
        assertTrue(Double.isNaN(result));
    }

    @Test
    public void testLogGammaZero() {
        double result = Gamma.logGamma(0.0);
        assertTrue(Double.isNaN(result));
    }

    @Test
    public void testLogGammaPositive() {
        double result = Gamma.logGamma(1.0);
        assertEquals(0.0, result, 0.000001);
    }

    @Test
    public void testRegularizedGammaPNegativeA() {
        double result = Gamma.regularizedGammaP(-1.0, 1.0);
        assertTrue(Double.isNaN(result));
    }

    @Test
    public void testRegularizedGammaPNegativeX() {
        double result = Gamma.regularizedGammaP(1.0, -1.0);
        assertTrue(Double.isNaN(result));
    }

    @Test
    public void testRegularizedGammaPZero() {
        double result = Gamma.regularizedGammaP(1.0, 0.0);
        assertEquals(0.0, result, 0.000001);
    }

    @Test
    public void testRegularizedGammaPPositive() {
        double result = Gamma.regularizedGammaP(1.0, 1.0);
        assertEquals(0.6321205588285577, result, 0.000001);
    }

    @Test
    public void testRegularizedGammaQNegativeA() {
        double result = Gamma.regularizedGammaQ(-1.0, 1.0);
        assertTrue(Double.isNaN(result));
    }

    @Test
    public void testRegularizedGammaQNegativeX() {
        double result = Gamma.regularizedGammaQ(1.0, -1.0);
        assertTrue(Double.isNaN(result));
    }

    @Test
    public void testRegularizedGammaQZero() {
        double result = Gamma.regularizedGammaQ(1.0, 0.0);
        assertEquals(1.0, result, 0.000001);
    }

    @Test
    public void testRegularizedGammaQPositive() {
        double result = Gamma.regularizedGammaQ(1.0, 1.0);
        assertEquals(0.36787944117144233, result, 0.000001);
    }

}