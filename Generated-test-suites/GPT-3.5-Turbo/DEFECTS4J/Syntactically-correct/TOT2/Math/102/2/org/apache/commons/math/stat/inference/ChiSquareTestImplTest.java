import org.junit.Test;
import static org.junit.Assert.*;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquare() {
        // typical use case
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0};
        long[] observed = {3, 4};
        double result = chiSquareTest.chiSquare(expected, observed);
        assertEquals(0.7222222222222221, result, 0.0001);
        
        // edge case: empty arrays
        double[] expectedEmpty = {};
        long[] observedEmpty = {};
        try {
            chiSquareTest.chiSquare(expectedEmpty, observedEmpty);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("observed, expected array lengths incorrect", e.getMessage());
        }
        
        // error scenario: negative values in arrays
        double[] expectedNeg = {1.0, -2.0};
        long[] observedNeg = {-3, 4};
        try {
            chiSquareTest.chiSquare(expectedNeg, observedNeg);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("observed counts must be non-negative and expected counts must be postive", e.getMessage());
        }
    }

    @Test
    public void testChiSquareTest() {
        // typical use case
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0};
        long[] observed = {3, 4};
        double result = chiSquareTest.chiSquareTest(expected, observed);
        assertEquals(0.0032114323004858917, result, 0.0001);

        // edge case: empty arrays
        double[] expectedEmpty = {};
        long[] observedEmpty = {};
        try {
            chiSquareTest.chiSquareTest(expectedEmpty, observedEmpty);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("observed, expected array lengths incorrect", e.getMessage());
        }
        
        // error scenario: negative values in arrays
        double[] expectedNeg = {1.0, -2.0};
        long[] observedNeg = {-3, 4};
        try {
            chiSquareTest.chiSquareTest(expectedNeg, observedNeg);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("observed counts must be non-negative and expected counts must be postive", e.getMessage());
        }
    }

    // Additional test cases for other methods can be added here

}