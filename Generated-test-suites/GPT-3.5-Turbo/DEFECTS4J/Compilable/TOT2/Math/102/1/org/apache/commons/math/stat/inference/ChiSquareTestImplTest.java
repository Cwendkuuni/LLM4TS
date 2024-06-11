package org.apache.commons.math.stat.inference;
import static org.junit.Assert.*;
import org.junit.Test;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquare_basic() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {10, 20, 30};
        long[] observed = {12, 18, 28};
        double result = chiSquareTest.chiSquare(expected, observed);
        assertEquals(0.38333333333333336, result, 0.0001);
    }

    @Test
    public void testChiSquare_lengthMismatch() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {10, 20, 30};
        long[] observed = {12, 18};
        try {
            chiSquareTest.chiSquare(expected, observed);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("observed, expected array lengths incorrect", e.getMessage());
        }
    }

    @Test
    public void testChiSquare_negativeValues() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {-10, 20, 30};
        long[] observed = {12, 18, 28};
        try {
            chiSquareTest.chiSquare(expected, observed);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("observed counts must be non-negative and expected counts must be positive", e.getMessage());
        }
    }

    // More test cases for other methods can be added here

}