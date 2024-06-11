import org.junit.Test;
import static org.junit.Assert.*;

public class GammaTest {

    @Test
    public void testLogGamma() {
        // Typical use case
        assertEquals(Math.log(Math.abs(Gamma.logGamma(5))), Gamma.logGamma(-5), 0.0001);

        // Edge case
        assertEquals(Double.NEGATIVE_INFINITY, Gamma.logGamma(0), 0.0001);

        // Error scenario
        assertEquals(Double.NaN, Gamma.logGamma(Double.NaN), 0.0001);
    }

    @Test
    public void testRegularizedGammaP() {
        // Typical use case
        assertEquals(0.6321205588285577, Gamma.regularizedGammaP(1, 0.5), 0.0001);

        // Edge case
        assertEquals(Double.NaN, Gamma.regularizedGammaP(0, 1), 0.0001);

        // Error scenario
        try {
            Gamma.regularizedGammaP(Double.NaN, Double.NaN);
            fail();
        } catch (MathException e) {
            assertEquals("Maximum number of iterations (2147483647) exceeded", e.getMessage());
        }
    }

    @Test
    public void testRegularizedGammaQ() {
        // Typical use case
        assertEquals(0.36787944117144233, Gamma.regularizedGammaQ(1, 0.5), 0.0001);

        // Edge case
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(0, 1), 0.0001);

        // Error scenario
        try {
            Gamma.regularizedGammaQ(Double.NaN, Double.NaN);
            fail();
        } catch (MathException e) {
            assertEquals("Maximum number of iterations (2147483647) exceeded", e.getMessage());
        }
    }
}