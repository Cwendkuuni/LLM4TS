import static org.junit.Assert.*;
import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        double result = Gamma.logGamma(5.0);
        assertEquals(3.178053830347945, result, 1e-10);
    }

    @Test
    public void testRegularizedGammaP() {
        try {
            double result = Gamma.regularizedGammaP(2.0, 3.0);
            assertEquals(0.8008517265285442, result, 1e-10);
        } catch (MathException e) {
            fail("Exception thrown in regularizedGammaP");
        }
    }

    @Test
    public void testRegularizedGammaQ() {
        try {
            double result = Gamma.regularizedGammaQ(2.0, 3.0);
            assertEquals(0.19914827347145583, result, 1e-10);
        } catch (MathException e) {
            fail("Exception thrown in regularizedGammaQ");
        }
    }

}