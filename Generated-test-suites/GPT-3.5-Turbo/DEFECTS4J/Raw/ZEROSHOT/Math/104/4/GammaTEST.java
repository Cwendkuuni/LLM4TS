import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class GammaTest {

    @Test
    public void testLogGamma() {
        assertEquals(Double.NaN, Gamma.logGamma(Double.NaN), 0.0);
        assertEquals(Double.NaN, Gamma.logGamma(0.0), 0.0);
        assertEquals(Double.NaN, Gamma.logGamma(-1.0), 0.0);
        assertEquals(-0.6931471805599453, Gamma.logGamma(0.5), 1e-9);
        assertEquals(0.0, Gamma.logGamma(1.0), 0.0);
        assertEquals(0.6931471805599453, Gamma.logGamma(1.5), 1e-9);
        assertEquals(2.1594842493533728, Gamma.logGamma(3.25), 1e-9);
        assertEquals(11.604095563491174, Gamma.logGamma(22.75), 1e-9);
    }

    @Test
    public void testRegularizedGammaP() {
        try {
            Gamma.regularizedGammaP(Double.NaN, 1.0);
            fail("Expected MathException");
        } catch (MathException e) {
            assertTrue(true);
        }
        try {
            Gamma.regularizedGammaP(1.0, Double.NaN);
            fail("Expected MathException");
        } catch (MathException e) {
            assertTrue(true);
        }
        assertEquals(Double.NaN, Gamma.regularizedGammaP(0.0, 1.0), 0.0);
        assertEquals(1.0, Gamma.regularizedGammaP(1.0, 0.0), 0.0);
        assertEquals(0.6321205588285577, Gamma.regularizedGammaP(0.5, 1.0), 1e-9);
        assertEquals(3.674935552215257E-4, Gamma.regularizedGammaP(1.5, 1.0), 1e-9);
        assertEquals(0.1490026469258439, Gamma.regularizedGammaP(2.5, 1.0), 1e-9);
        assertEquals(0.5040670619068473, Gamma.regularizedGammaP(3.5, 1.0), 1e-9);
        assertEquals(0.5340251237647363, Gamma.regularizedGammaP(3.5, 5.0), 1e-9);
        assertEquals(0.8271450583529214, Gamma.regularizedGammaP(3.5, 10.0), 1e-9);
        assertEquals(0.9340079449406524, Gamma.regularizedGammaP(3.5, 20.0), 1e-9);
    }

    @Test
    public void testRegularizedGammaQ() {
        try {
            Gamma.regularizedGammaQ(Double.NaN, 1.0);
            fail("Expected MathException");
        } catch (MathException e) {
            assertTrue(true);
        }
        try {
            Gamma.regularizedGammaQ(1.0, Double.NaN);
            fail("Expected MathException");
        } catch (MathException e) {
            assertTrue(true);
        }
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(0.0, 1.0), 0.0);
        assertEquals(0.0, Gamma.regularizedGammaQ(1.0, 0.0), 0.0);
        assertEquals(0.36787944117144233, Gamma.regularizedGammaQ(0.5, 1.0), 1e-9);
        assertEquals(0.9996325064447786, Gamma.regularizedGammaQ(1.5, 1.0), 1e-9);
        assertEquals(0.8509973530741562, Gamma.regularizedGammaQ(2.5, 1.0), 1e-9);
        assertEquals(0.4959329380931527, Gamma.regularizedGammaQ(3.5, 1.0), 1e-9);
        assertEquals(0.46597487623526365, Gamma.regularizedGammaQ(3.5, 5.0), 1e-9);
        assertEquals(0.1728549416470786, Gamma.regularizedGammaQ(3.5, 10.0), 1e-9);
        assertEquals(0.06599205505934761, Gamma.regularizedGammaQ(3.5, 20.0), 1e-9);
    }

}