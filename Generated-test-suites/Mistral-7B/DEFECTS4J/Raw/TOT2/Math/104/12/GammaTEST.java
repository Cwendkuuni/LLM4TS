
package org.junit.Test;
import static org.junit.Assert.*;
import org.junit.*;
import org.apache.commons.math.special.Gamma;

public class GammaTest {

    @BeforeClass
    public static void beforeClass() {
        // Initialize any static variables or load data if needed
    }

    @AfterClass
    public static void afterClass() {
        // Clean up any resources used during testing
    }

    @Before
    public void before() {
        // Initialize any test-specific variables or setups
    }

    @After
    public void after() {
        // Clean up any test-specific variables or resources
    }

    @Test
    public void testLogGamma() {
        // Test with valid input
        double result = Gamma.logGamma(1.5);
        double expected = 0.40582973600827583;
        assertEquals(expected, result, 1e-6);

        // Test with edge cases
        result = Gamma.logGamma(0.5); // small positive value
        double expectedEdge = -Math.log(Math.PI) + 0.5 * Math.log(2.0) + 0.5 * Math.log(0.5) - 0.072880828325;
        assertEquals(expectedEdge, result, 1e-6);

        result = Gamma.logGamma(2.0); // integer value
        expected = 1.1821259661112512 + Math.log(Math.PI);
        assertEquals(expected, result, 1e-6);

        // Test with negative input
        assertThrows(MathException.class, () -> Gamma.logGamma(-1.0));

        // Test with zero input
        assertThrows(MathException.class, () -> Gamma.logGamma(0.0));

        // Test with large input
        result = Gamma.logGamma(100.0);
        expected = 116.58268433325652 + Math.log(Math.PI) - 7.682063643248476;
        assertEquals(expected, result, 1e-6);
    }

    @Test
    public void testRegularizedGammaP() {
        // Test with valid input
        double result = Gamma.regularizedGammaP(1.5, 1.5);
        double expected = 1.0369346480591129;
        assertEquals(expected, result, 1e-6);

        // Test with edge cases
        result = Gamma.regularizedGammaP(2.0, 2.0); // same value as argument
        assertEquals(1.0, result);

        result = Gamma.regularizedGammaP(0.5, 1.0); // x > a
        expected = 1.265502992801398;
        assertEquals(expected, result, 1e-6);

        result = Gamma.regularizedGammaP(1.5, 0.0); // x < a
        expected = 0.817244943336214;
        assertEquals(expected, result, 1e-6);

        // Test with negative input
        assertThrows(MathException.class, () -> Gamma.regularizedGammaP(-1.0, 1.0));

        // Test with zero input
        assertEquals(0.5, Gamma.regularizedGammaP(0.0, 1.0));

        // Test with large input
        result = Gamma.regularizedGammaP(10.0, 10.0);
        expected = 0.999999999989416;
        assertEquals(expected, result, 1e-6);
    }

    @Test
    public void testRegularizedGammaQ() {
        // Test with valid input
        double result = Gamma.regularizedGammaQ(1.5, 1.5);
        double expected = 0.9630653512693871;
        assertEquals(expected, result, 1e-6);

        // Test with edge cases
        result = Gamma.regularizedGammaQ(2.0, 2.0); // same value as argument
        assertEquals(0.0, result);

        result = Gamma.regularizedGammaQ(0.5, 1.0); // x > a
        expected = 1.265502992801398;
        assertEquals(expected, result, 1e-6);

        result = Gamma.regularizedGammaQ(1.5, 0.0); // x < a
        expected = 0.817244943336214;
        assertEquals(expected, result, 1e-6);

        // Test with negative input
        assertThrows(MathException.class, () -> Gamma.regularizedGammaQ(-1.0, 1.0));

        // Test with zero input
        assertEquals(1.0, Gamma.regularizedGammaQ(0.0, 1.0));

        // Test with large input
        result = Gamma.regularizedGammaQ(10.0, 10.0);
        expected = 0.0000000011921251;
        assertEquals(expected, result, 1e-6);
    }

    // Add other tests here
}