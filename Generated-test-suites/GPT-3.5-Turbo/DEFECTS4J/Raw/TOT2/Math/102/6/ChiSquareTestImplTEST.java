import static org.junit.Assert.*;
import org.junit.Test;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquare() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {2.0, 3.0, 4.0};
        long[] observed = {2, 3, 4};
        assertEquals(0.0, chiSquareTest.chiSquare(expected, observed), 0.0001);
    }

    @Test
    public void testChiSquareTest() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {2.0, 3.0, 4.0};
        long[] observed = {2, 3, 4};
        try {
            double pValue = chiSquareTest.chiSquareTest(expected, observed);
            assertTrue(pValue >= 0 && pValue <= 1);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testChiSquareTestWithAlpha() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {2.0, 3.0, 4.0};
        long[] observed = {2, 3, 4};
        double alpha = 0.05;
        try {
            boolean result = chiSquareTest.chiSquareTest(expected, observed, alpha);
            assertTrue(result);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testChiSquareWithDataSetsComparison() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {2, 3, 4};
        long[] observed2 = {1, 5, 3};
        assertEquals(0.83333, chiSquareTest.chiSquareDataSetsComparison(observed1, observed2), 0.0001);
    }

    @Test
    public void testChiSquareTestDataSetsComparison() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {2, 3, 4};
        long[] observed2 = {1, 5, 3};
        try {
            double pValue = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2);
            assertTrue(pValue >= 0 && pValue <= 1);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithAlpha() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {2, 3, 4};
        long[] observed2 = {1, 5, 3};
        double alpha = 0.05;
        try {
            boolean result = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
            assertTrue(result);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
}