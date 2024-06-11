import static org.junit.Assert.*;
import org.junit.Test;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquare_basicCase() {
        // Test basic case for chiSquare method
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {1L, 2L, 3L};
        double result = test.chiSquare(expected, observed);
        assertEquals(0.0, result, 0.001);
    }

    @Test
    public void testChiSquare_edgeCase() {
        // Test edge case for chiSquare method
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0};
        long[] observed = {1L, 2L, 3L}; // Different length array
        try {
            test.chiSquare(expected, observed);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testChiSquareTest_basicCase() {
        // Test basic case for chiSquareTest method
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {1L, 2L, 3L};
        try {
            double result = test.chiSquareTest(expected, observed);
            assertNotNull(result);
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

    // More test cases for other methods can be added here

}