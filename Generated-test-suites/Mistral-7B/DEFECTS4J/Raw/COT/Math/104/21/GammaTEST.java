import org.apache.commons.math3.SpecialFunctions; // Assuming you are using Apache Commons Math 3.x
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNaN;

public class GammaTest {

    @Test
    public void testLogGamma() {
        // Test with valid inputs
        assertEquals(Math.log(Gamma.EULER_MASCHERONI), Gamma.logGamma(0.5), 1e-9);
        assertEquals(Math.log(1.4), Gamma.logGamma(1.4), 1e-9);
        assertEquals(Math.log(100.0), Gamma.logGamma(100.0), 1e-9);

        // Test with edge cases
        assertEquals(-Math.log(2.0), Gamma.logGamma(0.0), 1e-9);
        assertNaN(Gamma.logGamma(Double.NaN));
        assertNaN(Gamma.logGamma(-1.0));

        // Test with large input
        assertEquals(Math.log(factDouble(1000.0)), Gamma.logGamma(1000.0), 1e-15);
    }

    private double factDouble(double n) {
        double result = 1.0;
        for (int i = 2; i <= n; i++)
            result *= i;
        return result;
    }

    @Test
    public void testRegularizedGammaP() {
        // Test with valid inputs
        assertEquals(0.66537233226351, Gamma.regularizedGammaP(0.5, 1.0), 1e-8);
        assertEquals(0.73165944434703, Gamma.regularizedGammaP(1.6, 2.0), 1e-8);

        // Test with edge cases
        assertEquals(1.0, Gamma.regularizedGammaP(1.0, 1.0), 1e-8);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(-1.0, 2.0), 1e-8);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(0.5, 0.0), 1e-8);

        // Test with large input
        assertEquals(0.36268648285877, Gamma.regularizedGammaP(10.0, 50.0), 1e-8);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaP_MaxIterationsExceeded() {
        // Test MaxIterationsExceededException
        Gamma.regularizedGammaP(1.5, 1.5, 1e-22, 1);
    }

    @Test
    public void testRegularizedGammaQ() {
        // Test with valid inputs
        assertEquals(0.33506366296784, Gamma.regularizedGammaQ(0.5, 1.0), 1e-8);
        assertEquals(0.26840690964329, Gamma.regularizedGammaQ(1.6, 2.0), 1e-8);

        // Test with edge cases
        assertEquals(0.5, Gamma.regularizedGammaQ(1.0, 1.0), 1e-8);
        assertEquals(1.0, Gamma.regularizedGammaQ(-1.0, 2.0), 1e-8);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(0.5, 0.0), 1e-8);

        // Test with large input
        assertEquals(0.63745399361836, Gamma.regularizedGammaQ(10.0, 50.0), 1e-8);
    }
}