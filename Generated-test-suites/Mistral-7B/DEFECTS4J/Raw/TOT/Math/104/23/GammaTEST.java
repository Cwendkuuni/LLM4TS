import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        // Test for positive values
        assertEquals(0.0, Gamma.logGamma(0.0), 1e-12);
        assertEquals(0.0, Gamma.logGamma(1.0), 1e-12);
        assertEquals(1.202059956617294, Gamma.logGamma(2.0), 1e-12);

        // Test for large values
        assertEquals(1.702712527636526, Gamma.logGamma(100.0), 1e-12);

        // Test for negative values (NaN)
        assertEquals(Double.NaN, Gamma.logGamma(-1.0), 1e-12);
        assertEquals(Double.NaN, Gamma.logGamma(0.0), 1e-12);

        // Test for zero value (NaN)
        assertEquals(Double.NaN, Gamma.logGamma(0.0), 1e-12);
    }

    @Test
    public void testRegularizedGammaP() {
        // Test for positive values
        assertEquals(0.0, Gamma.regularizedGammaP(0.0, 0.0), 1e-12);
        assertEquals(1.0, Gamma.regularizedGammaP(1.0, 1.0), 1e-12);
        assertEquals(0.88888888888888889045, Gamma.regularizedGammaP(2.0, 1.5), 1e-12);

        // Test for large values
        assertEquals(0.99999999999999999482, Gamma.regularizedGammaP(100.0, 99.0), 1e-12);

        // Test for negative values (NaN)
        try {
            Gamma.regularizedGammaP(-1.0, 1.0);
        } catch (MathException e) {
            // Expected
        }

        try {
            Gamma.regularizedGammaP(0.0, 0.0);
        } catch (MathException e) {
            // Expected
        }

        try {
            Gamma.regularizedGammaP(0.0, 1.0);
        } catch (MathException e) {
            // Expected
        }
    }

    @Test
    public void testRegularizedGammaQ() {
        // Test for positive values
        assertEquals(0.0, Gamma.regularizedGammaQ(0.0, 0.0), 1e-12);
        assertEquals(1.0, Gamma.regularizedGammaQ(1.0, 1.0), 1e-12);
        assertEquals(0.11111111111111110955, Gamma.regularizedGammaQ(2.0, 1.5), 1e-12);

        // Test for large values
        assertEquals(0.99999999999999999482, Gamma.regularizedGammaQ(100.0, 99.0), 1e-12);

        // Test for negative values (NaN)
        try {
            Gamma.regularizedGammaQ(-1.0, 1.0);
        } catch (MathException e) {
            // Expected
        }

        try {
            Gamma.regularizedGammaQ(0.0, 0.0);
        } catch (MathException e) {
            // Expected
        }

        try {
            Gamma.regularizedGammaQ(0.0, 1.0);
        } catch (MathException e) {
            // Expected
        }
    }
}