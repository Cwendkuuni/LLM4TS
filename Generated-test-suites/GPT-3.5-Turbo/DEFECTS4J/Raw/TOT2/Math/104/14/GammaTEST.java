import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class GammaTest {

    @Test
    public void testLogGamma() {
        double result = Gamma.logGamma(5.0);
        assertEquals(3.1780538300302308, result, 0.0000001);
    }

    @Test
    public void testRegularizedGammaP() {
        try {
            double result = Gamma.regularizedGammaP(2.0, 3.0);
            assertEquals(0.893023250084207, result, 0.0000001);
        } catch (Exception e) {
            fail("Exception thrown when it shouldn't have.");
        }
    }

    @Test
    public void testRegularizedGammaQ() {
        try {
            double result = Gamma.regularizedGammaQ(2.0, 3.0);
            assertEquals(0.106976749915793, result, 0.0000001);
        } catch (Exception e) {
            fail("Exception thrown when it shouldn't have.");
        }
    }

}