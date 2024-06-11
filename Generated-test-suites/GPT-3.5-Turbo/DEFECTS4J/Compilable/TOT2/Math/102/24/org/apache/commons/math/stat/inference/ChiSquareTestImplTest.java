package org.apache.commons.math.stat.inference;
import static org.junit.Assert.*;
import org.junit.Test;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquare_basic() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1.5, 2.5, 3.5};
        long[] observed = {1, 2, 3};
        double result = chiSquareTest.chiSquare(expected, observed);
        assertEquals(0.42857142857142855, result, 0.0001);
    }
    
    @Test
    public void testChiSquare_invalidLength() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1.5, 2.5};
        long[] observed = {1, 2, 3};
        try {
            chiSquareTest.chiSquare(expected, observed);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
    }
    
    @Test
    public void testChiSquare_invalidCounts() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {-1, 2.5, 3.5};
        long[] observed = {1, 2, 3};
        try {
            chiSquareTest.chiSquare(expected, observed);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
    }

    // Add more test cases as needed for other methods
    
}