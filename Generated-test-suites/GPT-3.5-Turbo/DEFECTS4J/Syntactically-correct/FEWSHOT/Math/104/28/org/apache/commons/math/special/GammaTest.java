import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.apache.commons.math.MathException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        assertEquals(Double.NaN, Gamma.logGamma(Double.NaN), 0.0);
        assertEquals(Double.NaN, Gamma.logGamma(0.0), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, Gamma.logGamma(-1.0), 0.0);

        assertEquals(0.0, Gamma.logGamma(1.0), 0.0);
        assertEquals(0.0, Gamma.logGamma(2.0), 0.0);
        assertEquals(0.0, Gamma.logGamma(3.0), 0.0);

        assertEquals(0.6931471805599453, Gamma.logGamma(0.5), 1e-10);
        assertEquals(1.791759469228055, Gamma.logGamma(4.0), 1e-10);
        assertEquals(9.903487552536126, Gamma.logGamma(10.0), 1e-10);
    }

    @Test
    public void testRegularizedGammaP() {
        try {
            Gamma.regularizedGammaP(Double.NaN, 1.0);
            fail("MathException not thrown when a is NaN");
        } catch (MathException e) {
            // Expected exception
        }

        try {
            Gamma.regularizedGammaP(1.0, Double.NaN);
            fail("MathException not thrown when x is NaN");
        } catch (MathException e) {
            // Expected exception
        }

        try {
            Gamma.regularizedGammaP(0.0, 1.0);
            fail("MathException not thrown when a is 0");
        } catch (MathException e) {
            // Expected exception
        }

        try {
            Gamma.regularizedGammaP(-1.0, 1.0);
            fail("MathException not thrown when a is negative");
        } catch (MathException e) {
            // Expected exception
        }

        try {
            Gamma.regularizedGammaP(1.0, -1.0);
            fail("MathException not thrown when x is negative");
        } catch (MathException e) {
            // Expected exception
        }

        assertEquals(1.0, Gamma.regularizedGammaP(1.0, 0.0), 0.0);
        assertEquals(0.0, Gamma.regularizedGammaP(1.0, Double.POSITIVE_INFINITY), 0.0);
        assertEquals(0.3296799539643607, Gamma.regularizedGammaP(2.0, 1.0), 1e-10);
        assertEquals(0.1353352832366127, Gamma.regularizedGammaP(3.0, 1.0), 1e-10);
        assertEquals(0.26424111765711533, Gamma.regularizedGammaP(2.0, 2.0), 1e-10);
        assertEquals(0.18126924692201814, Gamma.regularizedGammaP(3.0, 2.0), 1e-10);
    }

    @Test
    public void testRegularizedGammaQ() {
        try {
            Gamma.regularizedGammaQ(Double.NaN, 1.0);
            fail("MathException not thrown when a is NaN");
        } catch (MathException e) {
            // Expected exception
        }

        try {
            Gamma.regularizedGammaQ(1.0, Double.NaN);
            fail("MathException not thrown when x is NaN");
        } catch (MathException e) {
            // Expected exception
        }

        try {
            Gamma.regularizedGammaQ(0.0, 1.0);
            fail("MathException not thrown when a is 0");
        } catch (MathException e) {
            // Expected exception
        }

        try {
            Gamma.regularizedGammaQ(-1.0, 1.0);
            fail("MathException not thrown when a is negative");
        } catch (MathException e) {
            // Expected exception
        }

        try {
            Gamma.regularizedGammaQ(1.0, -1.0);
            fail("MathException not thrown when x is negative");
        } catch (MathException e) {
            // Expected exception
        }

        assertEquals(0.0, Gamma.regularizedGammaQ(1.0, 0.0), 0.0);
        assertEquals(1.0, Gamma.regularizedGammaQ(1.0, Double.POSITIVE_INFINITY), 0.0);
        assertEquals(0.6703200460356394, Gamma.regularizedGammaQ(2.0, 1.0), 1e-10);
        assertEquals(0.8646647167633873, Gamma.regularizedGammaQ(3.0, 1.0), 1e-10);
        assertEquals(0.7357588823428846, Gamma.regularizedGammaQ(2.0, 2.0), 1e-10);
        assertEquals(0.8187307530779819, Gamma.regularizedGammaQ(3.0, 2.0), 1e-10);
    }

}