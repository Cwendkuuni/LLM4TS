import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GammaTest {

    @Test
    public void testLogGamma() {
        double result = Gamma.logGamma(5);
        assertEquals(3.1780538303479458, result, 0.0000001);
    }

    @Test
    public void testRegularizedGammaP() {
        double result = Gamma.regularizedGammaP(2, 3);
        assertEquals(0.8008517265285442, result, 0.0000001);
    }

    @Test
    public void testRegularizedGammaQ() {
        double result = Gamma.regularizedGammaQ(2, 3);
        assertEquals(0.19914827347145582, result, 0.0000001);
    }

    @Test
    public void testRegularizedGammaP_edgeCase() {
        double result = Gamma.regularizedGammaP(1, 0);
        assertEquals(0.0, result, 0.0);
    }

    @Test
    public void testRegularizedGammaQ_edgeCase() {
        double result = Gamma.regularizedGammaQ(1, 0);
        assertEquals(1.0, result, 0.0);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaP_exception() throws MathException {
        Gamma.regularizedGammaP(-1, 2);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaQ_exception() throws MathException {
        Gamma.regularizedGammaQ(-1, 2);
    }
}