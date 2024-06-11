import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.special.Gamma;
import org.apache.commons.math.MathException;
import org.apache.commons.math.MaxIterationsExceededException;

public class GammaTest {

    @Test
    public void testLogGamma() {
        assertEquals(0.0, Gamma.logGamma(1), 1e-5);
        assertEquals(Double.NaN, Gamma.logGamma(0), 0.0);
    }

    @Test
    public void testRegularizedGammaP() {
        try {
            assertEquals(0.0, Gamma.regularizedGammaP(1, 0), 1e-5);
            assertEquals(0.5, Gamma.regularizedGammaP(0.5, 0.5), 1e-5);
        } catch (MathException e) {
            fail("MathException thrown in regularizedGammaP test");
        }
    }

    @Test
    public void testRegularizedGammaQ() {
        try {
            assertEquals(1.0, Gamma.regularizedGammaQ(1, 0), 1e-5);
            assertEquals(0.5, Gamma.regularizedGammaQ(0.5, 0.5), 1e-5);
        } catch (MathException e) {
            fail("MathException thrown in regularizedGammaQ test");
        }
    }

    @Test
    public void testRegularizedGammaP_EdgeCases() {
        try {
            assertEquals(Double.NaN, Gamma.regularizedGammaP(0, 1), 0.0);
        } catch (MathException e) {
            fail("MathException thrown in regularizedGammaP edge case test");
        }
    }

    @Test
    public void testRegularizedGammaQ_EdgeCases() {
        try {
            assertEquals(Double.NaN, Gamma.regularizedGammaQ(1, -1), 1e-5);
        } catch (MathException e) {
            fail("MathException thrown in regularizedGammaQ edge case test");
        }
    }
}