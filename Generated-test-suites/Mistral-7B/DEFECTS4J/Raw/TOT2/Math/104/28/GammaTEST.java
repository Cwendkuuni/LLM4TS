import org.apache.commons.math3.special.Gamma;
import org.junit.Assert;
import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        // Test for positive numbers
        assertEquals(0.0, Gamma.logGamma(0.0), 0.0);
        assertEquals(0.0, Gamma.logGamma(1.0), 0.0);
        assertEquals(0.5176248461545553366, Gamma.logGamma(2.0), 1e-10);

        // Test for large numbers
        assertEquals(2.035664826855, Gamma.logGamma(5.0), 1e-10);
        assertEquals(7.657566832277, Gamma.logGamma(10.0), 1e-10);

        // Test for negative numbers and complex numbers
        try {
            Gamma.logGamma(-1.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test for non-numeric values
        try {
            Gamma.logGamma('a');
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testRegularizedGammaP_ValidInput() {
        double[] aValues = { 0.5, 1.0, 2.0, 3.0 };
        double[] xValues = { 0.1, 1.0, 2.0, 3.0 };

        for (double a : aValues) {
            for (double x : xValues) {
                double result = Gamma.regularizedGammaP(a, x);
                double expected = 0.0; // To be filled based on test cases from literature

                // Add test cases based on literature and other sources
                if (a == 0.5 && x == 0.1) {
                    expected = 0.5;
                }
                if (a == 1.0 && x == 1.0) {
                    expected = 0.5;
                }
                if (a == 2.0 && x == 2.0) {
                    expected = 1.0 / Math.sqrt(Math.PI);
                }
                if (a == 3.0 && x == 3.0) {
                    expected = Math.sqrt(Math.PI) / 2.0;
                }

                Assert.assertEquals(expected, result, 1e-10);
            }
        }
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaP_InvalidInput() {
        // Test for invalid input combinations
        Gamma.regularizedGammaP(-1.0, 0.1);
        Gamma.regularizedGammaP(0.5, -0.1);
        Gamma.regularizedGammaP(0.0, 0.1);
        Gamma.regularizedGammaP(1.0, -0.1);
    }

    @Test
    public void testRegularizedGammaQ_ValidInput() {
        double[] aValues = { 0.5, 1.0, 2.0, 3.0 };
        double[] xValues = { 0.1, 1.0, 2.0, 3.0 };

        for (double a : aValues) {
            for (double x : xValues) {
                double result = Gamma.regularizedGammaQ(a, x);
                double expected = 0.0; // To be filled based on test cases from literature

                // Add test cases based on literature and other sources
                if (a == 0.5 && x == 0.1) {
                    expected = 0.5;
                }
                if (a == 1.0 && x == 1.0) {
                    expected = 1.0 - 0.5;
                }
                if (a == 2.0 && x == 2.0) {
                    expected = 1.0 - Math.sqrt(Math.PI) / 2.0;
                }
                if (a == 3.0 && x == 3.0) {
                    expected = Math.sqrt(Math.PI) / 2.0 - 1.0;
                }

                Assert.assertEquals(expected, result, 1e-10);
            }
        }
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaQ_InvalidInput() {
        // Test for invalid input combinations
        Gamma.regularizedGammaQ(-1.0, 0.1);
        Gamma.regularizedGammaQ(0.5, -0.1);
        Gamma.regularizedGammaQ(0.0, 0.1);
        Gamma.regularizedGammaQ(1.0, -0.1);
    }
}