import org.apache.commons.math3.SpecialFunctions; // Assuming you are using Apache Commons Math version 3
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GammaTest {

    @Test
    public void testLogGamma() {
        assertEquals(0.0, SpecialFunctions.logGamma(0.0), 1e-9); // Test for edge case: logGamma(0)
        assertEquals(Math.log(1.0), SpecialFunctions.logGamma(1.0), 1e-9); // Test for logGamma(1)
        assertEquals(Math.log(2.0 * Math.PI), SpecialFunctions.logGamma(0.5), 1e-9); // Test for logGamma(0.5)

        try {
            SpecialFunctions.logGamma(-1.0); // Test for exception: logGamma(-1)
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        try {
            SpecialFunctions.logGamma(Double.NaN); // Test for exception: logGamma(NaN)
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testRegularizedGammaP() {
        assertEquals(0.0, SpecialFunctions.regularizedGammaP(0.0, 0.0), 1e-9); // Test for edge case: P(0, 0)
        assertEquals(0.5, SpecialFunctions.regularizedGammaP(0.5, 0.5), 1e-9); // Test for P(0.5, 0.5)
        assertEquals(1.0, SpecialFunctions.regularizedGammaP(1.0, 1.0), 1e-9); // Test for P(1, 1)

        try {
            SpecialFunctions.regularizedGammaP(-1.0, 2.0); // Test for exception: P(-1, 2)
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        try {
            SpecialFunctions.regularizedGammaP(Double.NaN, Double.NaN); // Test for exception: P(NaN, NaN)
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        try {
            SpecialFunctions.regularizedGammaP(1.0, 0.0); // Test for exception: P(1, 0)
        } catch (MathException e) {
            // Expected exception
        }
    }

    @Test
    public void testRegularizedGammaQ() {
        assertEquals(1.0, SpecialFunctions.regularizedGammaQ(0.0, 0.0), 1e-9); // Test for edge case: Q(0, 0)
        assertEquals(0.5, SpecialFunctions.regularizedGammaQ(0.5, 0.5), 1e-9); // Test for Q(0.5, 0.5)
        assertEquals(1.0 - Math.E, SpecialFunctions.regularizedGammaQ(1.0, 1.0), 1e-9); // Test for Q(1, 1)

        try {
            SpecialFunctions.regularizedGammaQ(-1.0, 2.0); // Test for exception: Q(-1, 2)
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        try {
            SpecialFunctions.regularizedGammaQ(Double.NaN, Double.NaN); // Test for exception: Q(NaN, NaN)
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        try {
            SpecialFunctions.regularizedGammaQ(1.0, 0.0); // Test for exception: Q(1, 0)
        } catch (MathException e) {
            // Expected exception
        }
    }

    // Add more test cases if needed

}