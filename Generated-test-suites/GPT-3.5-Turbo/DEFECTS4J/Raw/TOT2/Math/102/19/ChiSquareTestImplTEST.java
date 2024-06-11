import static org.junit.Assert.*;
import org.junit.Test;

public class ChiSquareTestImplTest {

    // Testing the constructor ChiSquareTestImpl()
    @Test
    public void testConstructor() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        assertNotNull(chiSquareTest);
    }

    // Test for chiSquare() method with typical use case
    @Test
    public void testChiSquareTypical() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2, 3, 4};
        double result = chiSquareTest.chiSquare(expected, observed);
        assertEquals(0.25, result, 0.001);
    }

    // Test for chiSquare() method with edge case of empty arrays
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareEmptyArrays() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {};
        long[] observed = {};
        chiSquareTest.chiSquare(expected, observed);
    }

    // Test for chiSquareTest() method with typical use case
    @Test
    public void testChiSquareTestTypical() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2, 3, 4};
        double pValue = chiSquareTest.chiSquareTest(expected, observed);
        assertEquals(0.518, pValue, 0.001);
    }

    // Test for chiSquareTest() method with edge case of zero expected counts
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestZeroExpectedCounts() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {0.0, 0.0, 0.0};
        long[] observed = {2, 3, 4};
        chiSquareTest.chiSquareTest(expected, observed);
    }

    // Test for chiSquareTest() method with exception handling
    @Test(expected = MathException.class)
    public void testChiSquareTestException() throws MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2, 3, 4};
        chiSquareTest.setDistribution(new ChiSquaredDistributionImpl(-1.0));
        chiSquareTest.chiSquareTest(expected, observed);
    }

    // Test for chiSquareTestDataSetsComparison() method with typical use case
    @Test
    public void testChiSquareDataSetsComparisonTypical() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {2, 3, 4};
        long[] observed2 = {1, 2, 3};
        double result = chiSquareTest.chiSquareDataSetsComparison(observed1, observed2);
        assertEquals(0.0666, result, 0.001);
    }

    // Test for chiSquareTestDataSetsComparison() method with edge case of zero counts
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetsComparisonZeroCounts() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {0, 0, 0};
        long[] observed2 = {1, 2, 3};
        chiSquareTest.chiSquareDataSetsComparison(observed1, observed2);
    }

    // Test for chiSquareTestDataSetsComparison() method with exception handling
    @Test(expected = MathException.class)
    public void testChiSquareDataSetsComparisonException() throws MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {2, 3, 4};
        long[] observed2 = {1, 2, 3};
        chiSquareTest.setDistribution(new ChiSquaredDistributionImpl(-1.0));
        chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    // Test for chiSquareTest() method with typical use case
    @Test
    public void testChiSquareTestDataSetsComparisonTypical() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {2, 3, 4};
        long[] observed2 = {1, 2, 3};
        double pValue = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2);
        assertEquals(0.234, pValue, 0.001);
    }

    // Test for chiSquareTestDataSetsComparison() method with edge case of negative alpha
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonNegativeAlpha() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {2, 3, 4};
        long[] observed2 = {1, 2, 3};
        chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, -0.1);
    }

    // Test for chiSquareTestDataSetsComparison() method with exception handling
    @Test(expected = MathException.class)
    public void testChiSquareTestDataSetsComparisonException() throws MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {2, 3, 4};
        long[] observed2 = {1, 2, 3};
        chiSquareTest.setDistribution(new ChiSquaredDistributionImpl(-1.0));
        chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
    }

}