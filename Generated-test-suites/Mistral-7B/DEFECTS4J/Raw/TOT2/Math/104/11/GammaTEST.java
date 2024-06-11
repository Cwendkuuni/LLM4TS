import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.*;

public class GammaTest {

    @Test
    public void testLogGamma() {
        // Test with valid inputs
        assertEquals(0.0, Gamma.logGamma(0.0), 1e-10);
        assertEquals(Math.log(1.0), Gamma.logGamma(1.0), 1e-10);
        assertEquals(Math.log(2.0) + Math.log(Math.PI), Gamma.logGamma(1.5), 1e-10);

        // Test with negative inputs
        try {
            Gamma.logGamma(-1.0);
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected
        }

        // Test with non-numeric inputs
        try {
            Gamma.logGamma("not a number");
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected
        }

        // Test with large inputs
        assertEquals(Gamma.logGamma(1000.0), Math.log(1000.0) + Math.log(Math.PI), 1e-10);

        // Test with very small inputs
        assertEquals(Gamma.logGamma(0.000001), Math.log(0.000001) + Math.log(Math.PI), 1e-10);
    }

    // Test other methods (regularizedGammaP, regularizedGammaQ) similarly
}