import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.MathException;
import org.apache.commons.math.MaxIterationsExceededException;

public class GammaTest {

    @Test
    public void testLogGamma() {
        assertEquals(0.0, Gamma.logGamma(1), 0.0001);
    }

    @Test
    public void testRegularizedGammaP() {
        try {
            assertEquals(0.5, Gamma.regularizedGammaP(1, 0.5), 0.0001);
        } catch (MathException e) {
            fail("Exception thrown in regularizedGammaP test");
        }
    }

    @Test
    public void testRegularizedGammaQ() {
        try {
            assertEquals(0.5, Gamma.regularizedGammaQ(1, 0.5), 0.0001);
        } catch (MathException e) {
            fail("Exception thrown in regularizedGammaQ test");
        }
    }

    @Test
    public void testInvalidLogGamma() {
        assertTrue(Double.isNaN(Gamma.logGamma(-1)));
    }

    @Test
    public void testInvalidRegularizedGammaP() {
        try {
            assertTrue(Double.isNaN(Gamma.regularizedGammaP(-1, 0.5)));
        } catch (MathException e) {
            // Expected exception, do nothing
        }
    }

    @Test
    public void testInvalidRegularizedGammaQ() {
        try {
            assertTrue(Double.isNaN(Gamma.regularizedGammaQ(-1, 0.5)));
        } catch (MathException e) {
            // Expected exception, do nothing
        }
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() throws MathException {
        Gamma.regularizedGammaP(1, 0.5, 10e-9, 5);
    }
}