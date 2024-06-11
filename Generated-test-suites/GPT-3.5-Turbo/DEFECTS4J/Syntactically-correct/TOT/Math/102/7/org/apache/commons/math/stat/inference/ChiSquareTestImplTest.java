import org.junit.Test;

import static org.junit.Assert.*;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquare_expectedLengthLessThanTwo_throwsIllegalArgumentException() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {};
        long[] observed = {1, 2, 3};
        try {
            chiSquareTest.chiSquare(expected, observed);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("observed, expected array lengths incorrect", e.getMessage());
        }
    }

    @Test
    public void testChiSquare_expectedLengthDifferentThanObservedLength_throwsIllegalArgumentException() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1, 2, 3};
        long[] observed = {1, 2};
        try {
            chiSquareTest.chiSquare(expected, observed);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("observed, expected array lengths incorrect", e.getMessage());
        }
    }

    @Test
    public void testChiSquare_expectedContainsNegative_throwsIllegalArgumentException() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1, -2, 3};
        long[] observed = {1, 2, 3};
        try {
            chiSquareTest.chiSquare(expected, observed);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("observed counts must be non-negative and expected counts must be positive", e.getMessage());
        }
    }

    @Test
    public void testChiSquare_expectedContainsZero_throwsIllegalArgumentException() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1, 0, 3};
        long[] observed = {1, 2, 3};
        try {
            chiSquareTest.chiSquare(expected, observed);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("observed counts must be non-negative and expected counts must be positive", e.getMessage());
        }
    }

    @Test
    public void testChiSquare_validInput_returnsChiSquare() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1, 2, 3};
        long[] observed = {1, 2, 3};
        double chiSquare = chiSquareTest.chiSquare(expected, observed);
        assertEquals(0, chiSquare, 0);
    }

    // More test cases for other methods...

}