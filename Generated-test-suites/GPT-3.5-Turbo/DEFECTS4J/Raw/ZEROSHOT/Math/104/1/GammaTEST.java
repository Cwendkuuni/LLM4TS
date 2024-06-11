import org.junit.Test;

import static org.junit.Assert.*;

public class GammaTest {

    @Test
    public void testLogGamma() {
        double x = 1.0;
        double expected = 0.0;
        double result = Gamma.logGamma(x);
        assertEquals(expected, result, 0.0001);

        x = 2.0;
        expected = 0.6931;
        result = Gamma.logGamma(x);
        assertEquals(expected, result, 0.0001);

        x = 5.0;
        expected = 3.1780;
        result = Gamma.logGamma(x);
        assertEquals(expected, result, 0.0001);
    }

    @Test
    public void testRegularizedGammaP() {
        double a = 1.0;
        double x = 1.0;
        double expected = 0.6321;
        double result = Gamma.regularizedGammaP(a, x);
        assertEquals(expected, result, 0.0001);

        a = 2.0;
        x = 2.0;
        expected = 0.3052;
        result = Gamma.regularizedGammaP(a, x);
        assertEquals(expected, result, 0.0001);

        a = 1.5;
        x = 0.5;
        expected = 0.0404;
        result = Gamma.regularizedGammaP(a, x);
        assertEquals(expected, result, 0.0001);
    }

    @Test
    public void testRegularizedGammaQ() {
        double a = 1.0;
        double x = 1.0;
        double expected = 0.3679;
        double result = Gamma.regularizedGammaQ(a, x);
        assertEquals(expected, result, 0.0001);

        a = 2.0;
        x = 2.0;
        expected = 0.6948;
        result = Gamma.regularizedGammaQ(a, x);
        assertEquals(expected, result, 0.0001);

        a = 1.5;
        x = 0.5;
        expected = 0.9596;
        result = Gamma.regularizedGammaQ(a, x);
        assertEquals(expected, result, 0.0001);
    }
}