import static org.junit.Assert.*;
import org.apache.commons.math3.special.Gamma;
import org.junit.Test;

public class GammaTest {

    // Test logGamma method
    @Test
    public void testLogGamma() {
        double result = Gamma.logGamma(1.5);
        double expected = Math.log(Math.PI) + (Math.log(1.5) - 0.5 * Math.log(2.0)) - 0.57721566490153286061;
        assertEquals(expected, result, 1.0e-6);

        try {
            result = Gamma.logGamma(0.0);
            fail("Expected MathException for logGamma(0.0)");
        } catch (MathException e) {
            // Expected exception
        }

        try {
            result = Gamma.logGamma(-0.5);
            fail("Expected MathException for logGamma(-0.5)");
        } catch (MathException e) {
            // Expected exception
        }
    }

    // Test regularizedGammaP method
    @Test
    public void testRegularizedGammaP() {
        double result = Gamma.regularizedGammaP(2.0, 1.5);
        double expected = Math.sqrt(Math.PI) / (2.0 * Math.sin(Math.PI / 4.0)) * Math.pow(2.0, 1.5 - 1.0) * Math.exp(-1.5);
        assertEquals(expected, result, 1.0e-6);

        try {
            result = Gamma.regularizedGammaP(-1.0, 2.0);
            fail("Expected MathException for regularizedGammaP(-1.0, 2.0)");
        } catch (MathException e) {
            // Expected exception
        }

        try {
            result = Gamma.regularizedGammaP(1.0, 0.0);
            fail("Expected MathException for regularizedGammaP(1.0, 0.0)");
        } catch (MathException e) {
            // Expected exception
        }

        try {
            result = Gamma.regularizedGammaP(0.5, -1.0);
            fail("Expected MathException for regularizedGammaP(0.5, -1.0)");
        } catch (MathException e) {
            // Expected exception
        }
    }

    // Test regularizedGammaQ method
    @Test
    public void testRegularizedGammaQ() {
        double result = Gamma.regularizedGammaQ(2.0, 1.5);
        double expected = 1.0 - result;
        assertEquals(expected, result, 1.0e-6);

        try {
            result = Gamma.regularizedGammaQ(-1.0, 2.0);
            fail("Expected MathException for regularizedGammaQ(-1.0, 2.0)");
        } catch (MathException e) {
            // Expected exception
        }

        try {
            result = Gamma.regularizedGammaQ(1.0, 0.0);
            fail("Expected MathException for regularizedGammaQ(1.0, 0.0)");
        } catch (MathException e) {
            // Expected exception
        }

        try {
            result = Gamma.regularizedGammaQ(0.5, -1.0);
            fail("Expected MathException for regularizedGammaQ(0.5, -1.0)");
        } catch (MathException e) {
            // Expected exception
        }
    }
}