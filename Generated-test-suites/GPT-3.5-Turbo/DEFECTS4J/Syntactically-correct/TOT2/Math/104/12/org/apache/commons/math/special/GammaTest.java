import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.MathException;

public class GammaTest {

    @Test
    public void testLogGamma() {
        double result = Gamma.logGamma(5);
        assertEquals(3.178053830, result, 0.000001);
    }

    @Test
    public void testRegularizedGammaP() {
        try {
            double result = Gamma.regularizedGammaP(3, 2);
            assertEquals(0.800851189, result, 0.000001);
        } catch (MathException e) {
            fail("MathException thrown");
        }
    }

    @Test
    public void testRegularizedGammaQ() {
        try {
            double result = Gamma.regularizedGammaQ(3, 2);
            assertEquals(0.199148811, result, 0.000001);
        } catch (MathException e) {
            fail("MathException thrown");
        }
    }

    @Test
    public void testLogGammaEdgeCases() {
        // Test edge case x is NaN
        assertTrue(Double.isNaN(Gamma.logGamma(Double.NaN)));

        // Test edge case x is negative
        assertTrue(Double.isNaN(Gamma.logGamma(-1)));
    }

    @Test
    public void testRegularizedGammaPEnd() {
        // Test for large a and x values
        try {
            double result = Gamma.regularizedGammaP(100, 10000);
            assertEquals(1.0, result, 0.000001);
        } catch (MathException e) {
            fail("MathException thrown");
        }
    }

    @Test
    public void testRegularizedGammaQEnd() {
        // Test for large a and x values
        try {
            double result = Gamma.regularizedGammaQ(100, 10000);
            assertEquals(0.0, result, 0.000001);
        } catch (MathException e) {
            fail("MathException thrown");
        }
    }
}