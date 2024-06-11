import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        double result = Gamma.logGamma(5.0);
        assertEquals(3.1780538303479458, result, 0.0001);
    }

    @Test
    public void testRegularizedGammaP() throws MathException {
        double result = Gamma.regularizedGammaP(2.0, 3.0);
        assertEquals(0.5939941502901619, result, 0.0001);
    }

    @Test
    public void testRegularizedGammaPException() {
        try {
            double result = Gamma.regularizedGammaP(-2.0, 3.0);
            fail("MathException not thrown");
        } catch (MathException e) {
            // Success
        }
    }

    @Test
    public void testRegularizedGammaQ() throws MathException {
        double result = Gamma.regularizedGammaQ(2.0, 3.0);
        assertEquals(0.40600584970983815, result, 0.0001);
    }

    @Test
    public void testRegularizedGammaQException() {
        try {
            double result = Gamma.regularizedGammaQ(-2.0, 3.0);
            fail("MathException not thrown");
        } catch (MathException e) {
            // Success
        }
    }

}