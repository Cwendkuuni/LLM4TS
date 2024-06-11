import static org.junit.Assert.*;
import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        double result = Gamma.logGamma(5.5);
        assertEquals(7.89782845, result, 0.000001);
    }

    @Test
    public void testRegularizedGammaP() {
        try {
            double result = Gamma.regularizedGammaP(1.0, 2.0);
            assertEquals(0.8646647, result, 0.000001);
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testRegularizedGammaQ() {
        try {
            double result = Gamma.regularizedGammaQ(1.0, 2.0);
            assertEquals(0.1353353, result, 0.000001);
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }

}