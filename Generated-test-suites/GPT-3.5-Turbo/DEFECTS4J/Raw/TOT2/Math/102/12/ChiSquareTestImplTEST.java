import org.junit.Test;
import static org.junit.Assert.*;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquare() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0, 4.0};
        long[] observed = {1, 2, 3, 4};
        assertEquals(0.0, test.chiSquare(expected, observed), 0.0001); // typical use case

        expected = new double[]{1.0, 2.0};
        observed = new long[]{1, 2, 3}; // mismatched lengths
        try {
            test.chiSquare(expected, observed);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testChiSquareTest() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0, 4.0};
        long[] observed = {1, 2, 3, 4};
        try {
            test.chiSquareTest(expected, observed);
        } catch (MathException e) {
            fail("Unexpected MathException");
        }

        expected = new double[]{1.0, 2.0};
        observed = new long[]{1, 2, 3}; // mismatched lengths
        try {
            test.chiSquareTest(expected, observed);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testChiSquareDataSetsComparison() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {3, 2, 1};
        assertEquals(3.0, test.chiSquareDataSetsComparison(observed1, observed2), 0.0001); // typical use case

        observed1 = new long[]{1, 2};
        observed2 = new long[]{3, 2, 1}; // mismatched lengths
        try {
            test.chiSquareDataSetsComparison(observed1, observed2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testChiSquareTestDataSetsComparison() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {3, 2, 1};
        try {
            test.chiSquareTestDataSetsComparison(observed1, observed2);
        } catch (MathException e) {
            fail("Unexpected MathException");
        }

        observed1 = new long[]{1, 2};
        observed2 = new long[]{3, 2, 1}; // mismatched lengths
        try {
            test.chiSquareTestDataSetsComparison(observed1, observed2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testChiSquareTestImplConstructor() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        assertNotNull(test);
    }
}