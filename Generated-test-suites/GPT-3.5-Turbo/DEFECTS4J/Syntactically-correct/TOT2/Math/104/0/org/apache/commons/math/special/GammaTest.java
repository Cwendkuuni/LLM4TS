import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.special.Gamma;
import org.apache.commons.math.MathException;

public class GammaTest {

    @Test
    public void testLogGamma() {
        assertEquals(Math.log(24), Gamma.logGamma(5), 0.001);
    }

    @Test
    public void testRegularizedGammaP() {
        try {
            assertEquals(0.366, Gamma.regularizedGammaP(2, 2), 0.001);
        } catch (MathException e) {
            fail("MathException thrown");
        }
    }

    @Test
    public void testRegularizedGammaQ() {
        try {
            assertEquals(0.634, Gamma.regularizedGammaQ(2, 2), 0.001);
        } catch (MathException e) {
            fail("MathException thrown");
        }
    }
    
    @Test
    public void testLogGammaNaN() {
        assertTrue(Double.isNaN(Gamma.logGamma(Double.NaN)));
    }

    @Test
    public void testLogGammaZero() {
        assertTrue(Double.isNaN(Gamma.logGamma(0)));
    }

    @Test
    public void testRegularizedGammaPNegative() {
        try {
            assertEquals(Double.NaN, Gamma.regularizedGammaP(-1, 1), 0.001);
        } catch (MathException e) {
            fail("MathException thrown");
        }
    }

    @Test
    public void testRegularizedGammaQNegative() {
        try {
            assertEquals(Double.NaN, Gamma.regularizedGammaQ(-1, 1), 0.001);
        } catch (MathException e) {
            fail("MathException thrown");
        }
    }

    @Test
    public void testRegularizedGammaPMAXIterations() {
        try {
            assertEquals(Double.NaN, Gamma.regularizedGammaP(5, 1, 0.001, 1), 0.001);
            fail("Failed to throw MaxIterationsExceededException");
        } catch (MathException e) {
            assertTrue(e instanceof MaxIterationsExceededException);
        }
    }

}