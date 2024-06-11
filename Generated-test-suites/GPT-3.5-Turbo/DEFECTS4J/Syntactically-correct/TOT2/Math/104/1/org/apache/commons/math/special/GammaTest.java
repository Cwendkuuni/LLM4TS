import static org.junit.Assert.*;

import org.apache.commons.math.MathException;
import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        assertEquals(0.0, Gamma.logGamma(1.0), 0.0001);
        assertEquals(Double.NaN, Gamma.logGamma(-1.0), 0.0001);
        assertEquals(0.5723649, Gamma.logGamma(2.5), 0.0001);
    }

    @Test
    public void testRegularizedGammaP() {
        try {
            assertEquals(0.154885, Gamma.regularizedGammaP(2.0, 1.0), 0.0001);
            assertEquals(1.0, Gamma.regularizedGammaP(2.0, 0.0), 0.0001);
            assertEquals(Double.NaN, Gamma.regularizedGammaP(-2.0, 1.0), 0.0001);
        } catch (MathException e) {
            fail("MathException thrown unexpectedly.");
        }
    }

    @Test
    public void testRegularizedGammaQ() {
        try {
            assertEquals(0.845114, Gamma.regularizedGammaQ(2.0, 1.0), 0.0001);
            assertEquals(0.0, Gamma.regularizedGammaQ(2.0, 0.0), 0.0001);
            assertEquals(Double.NaN, Gamma.regularizedGammaQ(-2.0, 1.0), 0.0001);
        } catch (MathException e) {
            fail("MathException thrown unexpectedly.");
        }
    }

}