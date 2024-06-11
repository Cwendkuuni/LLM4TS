import static org.junit.Assert.*;
import org.junit.Test;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquare() {
        // Typical use case
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {10.0, 20.0, 30.0};
        long[] observed = {5, 15, 35};
        double result = test.chiSquare(expected, observed);
        assertEquals(5.0, result, 0.0001);
        
        // Edge case: empty arrays
        expected = new double[0];
        observed = new long[0];
        try {
            test.chiSquare(expected, observed);
            fail("Exception not thrown for empty arrays");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testChiSquareTest() {
        // Typical use case
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {10.0, 20.0, 30.0};
        long[] observed = {5, 15, 35};
        try {
            double result = test.chiSquareTest(expected, observed);
            assertEquals(0.95, result, 0.0001);
        } catch (MathException e) {
            fail("MathException thrown unexpectedly");
        }
        
        // Edge case: negative alpha
        try {
            test.chiSquareTest(expected, observed, -0.1);
            fail("Exception not thrown for negative alpha");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    // Additional test cases for other methods can be added here

}