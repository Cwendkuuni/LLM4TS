import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.MathException;

public class GammaTest {

    @Test
    public void testLogGamma() {
        assertEquals(Math.log(2), Gamma.logGamma(3), 0.0001); // typical case
        assertEquals(Double.NaN, Gamma.logGamma(0), 0.0001); // edge case
    }

    @Test
    public void testRegularizedGammaP() {
        try {
            assertEquals(0.715, Gamma.regularizedGammaP(1, 2), 0.001); // typical case
            assertEquals(Double.NaN, Gamma.regularizedGammaP(0, 2), 0.001); // edge case
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testRegularizedGammaQ() {
        try {
            assertEquals(0.345, Gamma.regularizedGammaQ(1, 2), 0.001); // typical case
            assertEquals(Double.NaN, Gamma.regularizedGammaQ(0, 2), 0.001); // edge case
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaPException() throws MathException {
        Gamma.regularizedGammaP(0, 0);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaQException() throws MathException {
        Gamma.regularizedGammaQ(0, 0);
    }

}