import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class GammaTest {

    @Test
    public void testLogGamma() {
        double result = Gamma.logGamma(1.0);
        assertEquals(0.0, result, 0.000001);

        result = Gamma.logGamma(2.0);
        assertEquals(0.0, result, 0.000001);

        result = Gamma.logGamma(3.0);
        assertEquals(Math.log(2.0), result, 0.000001);

        result = Gamma.logGamma(4.0);
        assertEquals(Math.log(6.0), result, 0.000001);
    }

    @Test
    public void testRegularizedGammaP() throws MathException {
        double result = Gamma.regularizedGammaP(1.0, 1.0);
        assertEquals(0.6321205588285577, result, 0.000001);

        result = Gamma.regularizedGammaP(2.0, 2.0);
        assertEquals(0.5939941502901611, result, 0.000001);

        result = Gamma.regularizedGammaP(3.0, 3.0);
        assertEquals(0.5648793473919816, result, 0.000001);

        result = Gamma.regularizedGammaP(4.0, 4.0);
        assertEquals(0.5488506949471233, result, 0.000001);
    }

    @Test
    public void testRegularizedGammaQ() throws MathException {
        double result = Gamma.regularizedGammaQ(1.0, 1.0);
        assertEquals(0.36787944117144233, result, 0.000001);

        result = Gamma.regularizedGammaQ(2.0, 2.0);
        assertEquals(0.4060058497098389, result, 0.000001);

        result = Gamma.regularizedGammaQ(3.0, 3.0);
        assertEquals(0.4351206526080184, result, 0.000001);

        result = Gamma.regularizedGammaQ(4.0, 4.0);
        assertEquals(0.4511493050528767, result, 0.000001);
    }

}