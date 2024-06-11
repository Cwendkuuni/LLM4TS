package org.apache.commons.math.special;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNaN;

import org.apache.commons.math3.special.Gamma;
import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        // Test with valid arguments
        assertEquals(0.0, Gamma.logGamma(0.0), 1e-10); // gamma(0) = 0
        assertEquals(0.57721566490153286, Gamma.logGamma(1.0), 1e-10); // ln(gamma(1)) = 0.57721566490153286
        assertEquals(1.2020569031595942, Gamma.logGamma(2.0), 1e-10); // ln(gamma(2)) = 1.2020569031595942

        // Test with negative arguments
        assertNaN(Gamma.logGamma(-1.0));
        assertNaN(Gamma.logGamma(0.0));

        // Test with integers
        for (int i = 1; i < 10; i++) {
            assertEquals(Math.log(fact(i)) + Constants.LOG_SQRT_2_PI, Gamma.logGamma(i + 1.0), 1e-10);
        }
    }

    private static double fact(int n) {
        double result = 1.0;
        for (int i = 1; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    @Test
    public void testRegularizedGammaP() {
        // Test with valid arguments
        assertEquals(0.7112645969352755, Gamma.regularizedGammaP(0.5, 0.5), 1e-10); // P(0.5, 0.5) = 0.7112645969352755
        assertEquals(0.23434483108438328, Gamma.regularizedGammaP(1.5, 1.5), 1e-10); // P(1.5, 1.5) = 0.23434483108438328
        assertEquals(1.0, Gamma.regularizedGammaP(2.0, 2.0), 1e-10); // P(2.0, 2.0) = 1.0

        // Test with negative arguments
        assertNaN(Gamma.regularizedGammaP(-1.0, 0.5));
        assertNaN(Gamma.regularizedGammaP(0.0, 0.5));

        // Test with large arguments
        assertEquals(0.9753785744594985, Gamma.regularizedGammaP(100.0, 100.0), 1e-10); // P(100, 100) = 0.9753785744594985

        // Test with small arguments
        assertEquals(0.7678595513733795, Gamma.regularizedGammaP(1.2, 1.0), 1e-10); // P(1.2, 1.0) = 0.7678595513733795
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaP_ConvergenceFailure() {
        Gamma.regularizedGammaP(0.5, 0.5, 1e-15, 1000);
    }

    @Test
    public void testRegularizedGammaQ() {
        // Test with valid arguments
        assertEquals(0.28862333615384595, Gamma.regularizedGammaQ(0.5, 0.5), 1e-10); // Q(0.5, 0.5) = 0.28862333615384595
        assertEquals(0.7652940908683371, Gamma.regularizedGammaQ(1.5, 1.5), 1e-10); // Q(1.5, 1.5) = 0.7652940908683371
        assertEquals(0.0, Gamma.regularizedGammaQ(2.0, 2.0), 1e-10); // Q(2.0, 2.0) = 0.0

        // Test with negative arguments
        assertNaN(Gamma.regularizedGammaQ(-1.0, 0.5));
        assertNaN(Gamma.regularizedGammaQ(0.0, 0.5));

        // Test with large arguments
        assertEquals(0.02464855674488771, Gamma.regularizedGammaQ(100.0, 100.0), 1e-10); // Q(100, 100) = 0.02464855674488771

        // Test with small arguments
        assertEquals(0.2321285500110325, Gamma.regularizedGammaQ(1.2, 1.0), 1e-10); // Q(1.2, 1.0) = 0.2321285500110325
    }
}