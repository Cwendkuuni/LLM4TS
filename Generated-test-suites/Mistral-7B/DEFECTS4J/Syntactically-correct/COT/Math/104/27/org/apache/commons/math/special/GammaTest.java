package org.apache.commons.math.special;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNaN;

import org.apache.commons.math3.special.Gamma;
import org.junit.Test;

/**
 * JUnit test cases for the Gamma class.
 */
public class GammaTest {
    // 

    /**
     * Test method for {@link Gamma#logGamma(double)}.
     */
    @Test
    public void testLogGamma() {
        // Basic test
        double result = Gamma.logGamma(1.5);
        double expected = 0.4058228651039982;
        assertEquals(expected, result, 1e-10);

        // Test with negative argument
        result = Gamma.logGamma(-1.0);
        assertNaN(result);

        // Test with zero argument
        result = Gamma.logGamma(0.0);
        assertNaN(result);

        // Test with large argument
        result = Gamma.logGamma(1000.0);
        expected = 1151.798017621142;
        assertEquals(expected, result, 1e-10);
    }

    /**
     * Test method for {@link Gamma#regularizedGammaP(double, double)}.
     */
    @Test
    public void testRegularizedGammaP() {
        // Basic test
        double result = Gamma.regularizedGammaP(1.5, 1.5);
        double expected = 0.886226925452758;
        assertEquals(expected, result, 1e-10);

        // Test with negative a
        result = Gamma.regularizedGammaP(-1.0, 1.0);
        assertNaN(result);

        // Test with negative x
        result = Gamma.regularizedGammaP(1.5, -1.0);
        assertNaN(result);

        // Test with x = a
        result = Gamma.regularizedGammaP(1.5, 1.5);
        expected = 0.886226925452758;
        assertEquals(expected, result, 1e-10);

        // Test with x > a
        result = Gamma.regularizedGammaP(1.5, 2.5);
        expected = 0.422784335013251;
        assertEquals(expected, result, 1e-10);

        // Test with a < 1 and x > a
        result = Gamma.regularizedGammaP(0.5, 1.5);
        expected = 0.292893218813452;
        assertEquals(expected, result, 1e-10);

        // Test with large a and x = 0
        result = Gamma.regularizedGammaP(1000.0, 0.0);
        expected = 1.0;
        assertEquals(expected, result, 1e-10);
    }

    /**
     * Test method for {@link Gamma#regularizedGammaQ(double, double)}.
     */
    @Test
    public void testRegularizedGammaQ() {
        // Basic test
        double result = Gamma.regularizedGammaQ(1.5, 1.5);
        double expected = 0.113773074547242;
        assertEquals(expected, result, 1e-10);

        // Test with negative a
        result = Gamma.regularizedGammaQ(-1.0, 1.0);
        assertNaN(result);

        // Test with negative x
        result = Gamma.regularizedGammaQ(1.5, -1.0);
        assertNaN(result);

        // Test with x = a
        result = Gamma.regularizedGammaQ(1.5, 1.5);
        expected = 0.113773074547242;
        assertEquals(expected, result, 1e-10);

        // Test with x < a
        result = Gamma.regularizedGammaQ(1.5, 0.5);
        expected = 0.886226925452758;
        assertEquals(expected, result, 1e-10);

        // Test with a < 1 and x > a
        result = Gamma.regularizedGammaQ(0.5, 1.5);
        expected = 0.886226925452758;
        assertEquals(expected, result, 1e-10);
    }

    // 
}