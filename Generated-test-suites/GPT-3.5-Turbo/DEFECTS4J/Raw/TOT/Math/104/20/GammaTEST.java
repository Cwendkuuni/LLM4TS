import org.junit.*;
import static org.junit.Assert.*;

public class GammaTest {

    @Test
    public void testLogGamma() {
        // Typical use case
        assertEquals(Math.log(24), Gamma.logGamma(5), 0.0001);
        // Edge case
        assertEquals(Double.NaN, Gamma.logGamma(0), 0.0001);
        // Error scenario
        assertEquals(Double.NaN, Gamma.logGamma(Double.NaN), 0.0001);
    }

    @Test
    public void testRegularizedGammaP() {
        // Typical use case
        assertEquals(0.6321, Gamma.regularizedGammaP(2, 1), 0.0001);
        // Edge case
        assertEquals(0, Gamma.regularizedGammaP(0, 5), 0.0001);
        // Error scenario
        try {
            Gamma.regularizedGammaP(2, -1);
            fail("MathException not thrown");
        } catch (MathException e) {
            assertTrue(e instanceof MaxIterationsExceededException);
        }
    }

    @Test
    public void testRegularizedGammaQ() {
        // Typical use case
        assertEquals(0.3679, Gamma.regularizedGammaQ(2, 1), 0.0001);
        // Edge case
        assertEquals(1, Gamma.regularizedGammaQ(0, 5), 0.0001);
        // Error scenario
        try {
            Gamma.regularizedGammaQ(2, -1);
            fail("MathException not thrown");
        } catch (MathException e) {
            assertTrue(e instanceof MaxIterationsExceededException);
        }
    }

}