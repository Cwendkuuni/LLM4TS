import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GammaTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testLogGamma() {
        double x = 5.0;
        double result = Gamma.logGamma(x);
        assertEquals(3.1780538303479458, result, 0.0000000001);
    }

    @Test
    public void testLogGamma_NaN() {
        double x = Double.NaN;
        double result = Gamma.logGamma(x);
        assertTrue(Double.isNaN(result));
    }

    @Test
    public void testLogGamma_Negative() {
        double x = -5.0;
        double result = Gamma.logGamma(x);
        assertTrue(Double.isNaN(result));
    }

    @Test
    public void testRegularizedGammaP() throws MathException {
        double a = 2.0;
        double x = 1.0;
        double result = Gamma.regularizedGammaP(a, x);
        assertEquals(0.26424111765711537, result, 0.0000000001);
    }

    @Test
    public void testRegularizedGammaP_NaN() throws MathException {
        double a = Double.NaN;
        double x = 1.0;
        double result = Gamma.regularizedGammaP(a, x);
        assertTrue(Double.isNaN(result));
    }

    @Test
    public void testRegularizedGammaP_NegativeA() throws MathException {
        double a = -2.0;
        double x = 1.0;
        double result = Gamma.regularizedGammaP(a, x);
        assertTrue(Double.isNaN(result));
    }

    @Test
    public void testRegularizedGammaP_NegativeX() throws MathException {
        double a = 2.0;
        double x = -1.0;
        double result = Gamma.regularizedGammaP(a, x);
        assertTrue(Double.isNaN(result));
    }

    @Test
    public void testRegularizedGammaQ() throws MathException {
        double a = 2.0;
        double x = 1.0;
        double result = Gamma.regularizedGammaQ(a, x);
        assertEquals(0.7357588823428846, result, 0.0000000001);
    }

    @Test
    public void testRegularizedGammaQ_NaN() throws MathException {
        double a = Double.NaN;
        double x = 1.0;
        double result = Gamma.regularizedGammaQ(a, x);
        assertTrue(Double.isNaN(result));
    }

    @Test
    public void testRegularizedGammaQ_NegativeA() throws MathException {
        double a = -2.0;
        double x = 1.0;
        double result = Gamma.regularizedGammaQ(a, x);
        assertTrue(Double.isNaN(result));
    }

    @Test
    public void testRegularizedGammaQ_NegativeX() throws MathException {
        double a = 2.0;
        double x = -1.0;
        double result = Gamma.regularizedGammaQ(a, x);
        assertTrue(Double.isNaN(result));
    }
}