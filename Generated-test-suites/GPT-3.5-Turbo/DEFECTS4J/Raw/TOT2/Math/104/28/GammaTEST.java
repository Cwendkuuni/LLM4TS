import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GammaTest {

    @Test
    public void testLogGamma() {
        double result = Gamma.logGamma(5.0);
        assertEquals(3.1780538303479458, result, 1e-10);
    }

    @Test
    public void testRegularizedGammaP() {
        double result = Gamma.regularizedGammaP(2.0, 3.0);
        assertEquals(0.593994150290162, result, 1e-10);
    }

    @Test
    public void testRegularizedGammaQ() {
        double result = Gamma.regularizedGammaQ(2.0, 3.0);
        assertEquals(0.40600584970983805, result, 1e-10);
    }

    @Test
    public void testLogGammaWithNegativeInput() {
        double result = Gamma.logGamma(-5.0);
        assertEquals(Double.NaN, result, 0.0);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaPWithNegativeA() throws MathException {
        Gamma.regularizedGammaP(-2.0, 3.0);
    }

    @Test
    public void testRegularizedGammaQWithZeroX() throws MathException {
        double result = Gamma.regularizedGammaQ(2.0, 0.0);
        assertEquals(1.0, result, 1e-10);
    }

}