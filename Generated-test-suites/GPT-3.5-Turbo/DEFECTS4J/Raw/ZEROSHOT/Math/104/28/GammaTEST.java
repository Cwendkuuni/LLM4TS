import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class GammaTest {

    @BeforeClass
    public static void setUpClass() {
        //
    }

    @Test
    public void testLogGamma() {
        double result = Gamma.logGamma(5.0);
        double expected = 3.1780538303479458;
        assertEquals(expected, result, 0.000000001);

        result = Gamma.logGamma(0.5);
        expected = 0.5723649429247001;
        assertEquals(expected, result, 0.000000001);

        result = Gamma.logGamma(Double.POSITIVE_INFINITY);
        expected = Double.POSITIVE_INFINITY;
        assertEquals(expected, result, 0.0);

        result = Gamma.logGamma(Double.NaN);
        expected = Double.NaN;
        assertEquals(expected, result, 0.0);

        result = Gamma.logGamma(-1.0);
        expected = Double.NaN;
        assertEquals(expected, result, 0.0);
    }

    @Test
    public void testRegularizedGammaP() throws MathException {
        double result = Gamma.regularizedGammaP(5.0, 2.0);
        double expected = 0.9875941310103374;
        assertEquals(expected, result, 0.000000001);

        result = Gamma.regularizedGammaP(0.5, 1.0);
        expected = 0.6826894921370859;
        assertEquals(expected, result, 0.000000001);

        result = Gamma.regularizedGammaP(10.0, Double.POSITIVE_INFINITY);
        expected = 1.0;
        assertEquals(expected, result, 0.0);

        result = Gamma.regularizedGammaP(Double.NaN, 1.0);
        expected = Double.NaN;
        assertEquals(expected, result, 0.0);
    }

    @Test
    public void testRegularizedGammaQ() throws MathException {
        double result = Gamma.regularizedGammaQ(5.0, 2.0);
        double expected = 0.012405868989662575;
        assertEquals(expected, result, 0.000000001);

        result = Gamma.regularizedGammaQ(0.5, 1.0);
        expected = 0.31731050786291415;
        assertEquals(expected, result, 0.000000001);

        result = Gamma.regularizedGammaQ(10.0, Double.POSITIVE_INFINITY);
        expected = 0.0;
        assertEquals(expected, result, 0.0);

        result = Gamma.regularizedGammaQ(Double.NaN, 1.0);
        expected = Double.NaN;
        assertEquals(expected, result, 0.0);
    }

}