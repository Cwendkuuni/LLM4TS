import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.*;

public class GammaTest {

    @Test
    public void testLogGamma() {
        double result = Gamma.logGamma(1.0);
        double expected = 0.0;
        assertEquals(expected, result, 1e-9);

        result = Gamma.logGamma(0.5);
        expected = -0.5 * Math.log(2.0) - 0.5 * Math.log(Math.PI) - 0.1202056782;
        assertEquals(expected, result, 1e-9);

        result = Gamma.logGamma(-1.0);
        assertTrue(Double.isNaN(result));

        result = Gamma.logGamma(0.0);
        assertTrue(Double.isNaN(result));
    }

    @Test
    public void testRegularizedGammaP() throws MathException {
        double result = Gamma.regularizedGammaP(1.0, 1.0);
        double expected = 1.0;
        assertEquals(expected, result, 1e-9);

        result = Gamma.regularizedGammaP(1.0, 2.0);
        expected = 0.5;
        assertEquals(expected, result, 1e-9);

        result = Gamma.regularizedGammaP(1.0, 0.5);
        expected = 0.8862269254;
        assertEquals(expected, result, 1e-9);

        result = Gamma.regularizedGammaP(1.0, 0.0);
        expected = 1.0;
        assertEquals(expected, result, 1e-9);

        result = Gamma.regularizedGammaP(1.0, -1.0);
        assertTrue(Double.isNaN(result));

        result = Gamma.regularizedGammaP(-1.0, 1.0);
        assertTrue(Double.isNaN(result));

        result = Gamma.regularizedGammaP(0.0, 1.0);
        assertTrue(Double.isNaN(result));

        try {
            result = Gamma.regularizedGammaP(1.5, 1.0);
            fail();
        } catch (MathException e) {
            // expected
        }

        try {
            result = Gamma.regularizedGammaP(1.0, 3.0);
            fail();
        } catch (MathException e) {
            // expected
        }

        try {
            result = Gamma.regularizedGammaP(1.0, Double.POSITIVE_INFINITY);
            fail();
        } catch (MathException e) {
            // expected
        }
    }

    @Test
    public void testRegularizedGammaQ() throws MathException {
        double result = Gamma.regularizedGammaQ(1.0, 1.0);
        double expected = 0.0;
        assertEquals(expected, result, 1e-9);

        result = Gamma.regularizedGammaQ(1.0, 2.0);
        expected = 1.0 - 0.5;
        assertEquals(expected, result, 1e-9);

        result = Gamma.regularizedGammaQ(1.0, 0.5);
        expected = 1.0 - 0.8862269254;
        assertEquals(expected, result, 1e-9);

        result = Gamma.regularizedGammaQ(1.0, 0.0);
        expected = 1.0;
        assertEquals(expected, result, 1e-9);

        result = Gamma.regularizedGammaQ(-1.0, 1.0);
        assertTrue(Double.isNaN(result));

        result = Gamma.regularizedGammaQ(0.0, 1.0);
        assertTrue(Double.isNaN(result));

        try {
            result = Gamma.regularizedGammaQ(1.5, 1.0);
            fail();
        } catch (MathException e) {
            // expected
        }

        try {
            result = Gamma.regularizedGammaQ(1.0, 3.0);
            fail();
        } catch (MathException e) {
            // expected
        }

        try {
            result = Gamma.regularizedGammaQ(1.0, Double.POSITIVE_INFINITY);
            fail();
        } catch (MathException e) {
            // expected
        }
    }
}