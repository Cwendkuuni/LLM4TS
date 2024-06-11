import org.junit.Test;
import static org.junit.Assert.*;

public class GammaTest {

    @Test
    public void testLogGamma() {
        // Typical use case
        assertEquals(1.791759469228055, Gamma.logGamma(5), 0.0001);
        
        // Edge cases
        assertEquals(Double.NaN, Gamma.logGamma(Double.NaN), 0.0001);
        assertEquals(Double.NaN, Gamma.logGamma(0), 0.0001);
        assertEquals(Double.NEGATIVE_INFINITY, Gamma.logGamma(Double.NEGATIVE_INFINITY), 0.0001);
        assertEquals(Double.NEGATIVE_INFINITY, Gamma.logGamma(Double.POSITIVE_INFINITY), 0.0001);
    }

    @Test
    public void testRegularizedGammaP() {
        // Typical use case
        try {
            assertEquals(0.6321205588285577, Gamma.regularizedGammaP(1, 1), 0.0001);
        } catch (MathException e) {
            e.printStackTrace();
        }

        // Edge cases
        try {
            assertEquals(Double.NaN, Gamma.regularizedGammaP(Double.NaN, 1), 0.0001);
            assertEquals(Double.NaN, Gamma.regularizedGammaP(1, Double.NaN), 0.0001);
            assertEquals(Double.NaN, Gamma.regularizedGammaP(Double.NaN, Double.NaN), 0.0001);
            assertEquals(Double.NaN, Gamma.regularizedGammaP(0, 1), 0.0001);
            assertEquals(Double.NaN, Gamma.regularizedGammaP(1, -1), 0.0001);
            assertEquals(1.0, Gamma.regularizedGammaP(0, 0), 0.0001);
        } catch (MathException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRegularizedGammaQ() {
        // Typical use case
        try {
            assertEquals(0.36787944117144233, Gamma.regularizedGammaQ(1, 1), 0.0001);
        } catch (MathException e) {
            e.printStackTrace();
        }

        // Edge cases
        try {
            assertEquals(Double.NaN, Gamma.regularizedGammaQ(Double.NaN, 1), 0.0001);
            assertEquals(Double.NaN, Gamma.regularizedGammaQ(1, Double.NaN), 0.0001);
            assertEquals(Double.NaN, Gamma.regularizedGammaQ(Double.NaN, Double.NaN), 0.0001);
            assertEquals(Double.NaN, Gamma.regularizedGammaQ(0, 1), 0.0001);
            assertEquals(Double.NaN, Gamma.regularizedGammaQ(1, -1), 0.0001);
            assertEquals(0.0, Gamma.regularizedGammaQ(0, 0), 0.0001);
        } catch (MathException e) {
            e.printStackTrace();
        }
    }
}